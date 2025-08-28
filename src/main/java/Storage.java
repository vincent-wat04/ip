import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static Path resolveDataFile() {
        // Support running from project root or from text-ui-test directory
        Path base = Paths.get(".");
        if (Files.isDirectory(Paths.get("text-ui-test"))) {
            base = Paths.get("..");
        }
        Path dataDir = base.resolve(Paths.get("data"));
        return dataDir.resolve("vince.txt");
    }

    public static ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        try {
            Path dataFile = resolveDataFile();
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
                    // Expected formats:
                    // T | 1 | description
                    // D | 0 | description | by
                    // E | 1 | description | from | to
                    String[] parts = line.split("\\|", -1);
                    if (parts.length < 3) {
                        // corrupted line, skip
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
                                continue; // corrupted
                            }
                            String by = parts[3].trim();
                            task = new Deadline(description, by);
                            break;
                        case "E":
                            if (parts.length < 5) {
                                continue; // corrupted
                            }
                            String from = parts[3].trim();
                            String to = parts[4].trim();
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
                    // Skip corrupted line gracefully
                    continue;
                }
            }
        } catch (IOException ioException) {
            throw new VinceException("Failed to load data from disk!" );
        }
        return tasks;
    }

    public static void save(ArrayList<Task> tasks) {
        try {
            Path dataFile = resolveDataFile();
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
                    line = String.format("D | %d | %s | %s", task.isDone() ? 1 : 0, task.getDescription(), d.getBy());
                } else if (task instanceof Event) {
                    Event e = (Event) task;
                    line = String.format("E | %d | %s | %s | %s", task.isDone() ? 1 : 0, task.getDescription(), e.getFrom(), e.getTo());
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
