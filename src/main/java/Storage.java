import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path dataFile;

    public Storage() {
        this(resolveDefaultDataFile());
    }

    public Storage(Path dataFile) {
        this.dataFile = dataFile;
    }

    private static Path resolveDefaultDataFile() {
        Path base = Paths.get(".");
        if (Files.isDirectory(Paths.get("text-ui-test"))) {
            base = Paths.get("..");
        }
        Path dataDir = base.resolve(Paths.get("data"));
        return dataDir.resolve("vince.txt");
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        try {
            Path dataDir = dataFile.getParent();
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            if (!Files.exists(dataFile)) {
                Files.createFile(dataFile);
                return tasks;
            }
            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                try {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length < 3) {
                        continue;
                    }
                    String type = parts[0].trim();
                    String doneStr = parts[1].trim();
                    String description = parts[2].trim();
                    boolean isDone = doneStr.equals("1");

                    Task task;
                    switch (type) {
                        case "T":
                            task = new Todo(description);
                            break;
                        case "D":
                            if (parts.length < 4) {
                                continue;
                            }
                            LocalDateTime by = LocalDateTime.parse(parts[3].trim(), DATE_TIME_FORMATTER);
                            task = new Deadline(description, by);
                            break;
                        case "E":
                            if (parts.length < 5) {
                                continue;
                            }
                            LocalDateTime from = LocalDateTime.parse(parts[3].trim(), DATE_TIME_FORMATTER);
                            LocalDateTime to = LocalDateTime.parse(parts[4].trim(), DATE_TIME_FORMATTER);
                            task = new Event(description, from, to);
                            break;
                        default:
                            task = new Task(description);
                    }
                    if (isDone) {
                        task.mark();
                    }
                    tasks.add(task);
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (IOException ioException) {
            throw new VinceException("Failed to load data from disk!");
        }
        return tasks;
    }

    public void save(ArrayList<Task> tasks) {
        try {
            Path dataDir = dataFile.getParent();
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            ArrayList<String> lines = new ArrayList<String>();
            for (Task task : tasks) {
                String line;
                if (task instanceof Todo) {
                    line = String.format("T | %d | %s", task.isDone() ? 1 : 0, task.getDescription());
                } else if (task instanceof Deadline) {
                    Deadline d = (Deadline) task;
                    line = String.format("D | %d | %s | %s", task.isDone() ? 1 : 0, task.getDescription(),
                        d.getBy().format(DATE_TIME_FORMATTER));
                } else if (task instanceof Event) {
                    Event e = (Event) task;
                    line = String.format("E | %d | %s | %s | %s", task.isDone() ? 1 : 0, task.getDescription(),
                        e.getFrom().format(DATE_TIME_FORMATTER), e.getTo().format(DATE_TIME_FORMATTER));
                } else {
                    line = String.format("T | %d | %s", task.isDone() ? 1 : 0, task.getDescription());
                }
                lines.add(line);
            }
            Files.write(dataFile, lines, StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            throw new VinceException("Failed to save data to disk!");
        }
    }
}
