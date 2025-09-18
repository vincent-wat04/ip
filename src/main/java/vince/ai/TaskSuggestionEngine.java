package vince.ai;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import vince.task.Task;
import vince.task.Deadline;
import vince.task.Event;
import vince.task.Priority;

/**
 * AI-powered task suggestion and productivity enhancement engine.
 * Provides intelligent recommendations based on task patterns and user behavior.
 */
public class TaskSuggestionEngine {
    
    /**
     * Generates intelligent suggestions based on current task list.
     * 
     * @param tasks the current list of tasks
     * @return list of helpful suggestions
     */
    public static List<String> generateSuggestions(List<Task> tasks) {
        List<String> suggestions = new ArrayList<>();
        
        if (tasks.isEmpty()) {
            suggestions.add("💡 Start your productivity journey! Add your first task with 'todo <description>'");
            suggestions.add("💡 Set a deadline: 'deadline Submit report /by tomorrow 5pm'");
            suggestions.add("💡 Plan an event: 'event Team meeting /from today 2pm /to today 3pm'");
            return suggestions;
        }
        
        // Analyze task patterns
        long completedTasks = tasks.stream().filter(Task::isDone).count();
        long totalTasks = tasks.size();
        double completionRate = (double) completedTasks / totalTasks;
        
        long highPriorityTasks = tasks.stream()
                .filter(task -> task.getPriority() == Priority.HIGH)
                .filter(task -> !task.isDone())
                .count();
        
        long overdueTasks = tasks.stream()
                .filter(task -> task instanceof Deadline)
                .filter(task -> !task.isDone())
                .map(task -> (Deadline) task)
                .filter(deadline -> deadline.getBy().isBefore(LocalDateTime.now()))
                .count();
        
        // Generate contextual suggestions
        if (overdueTasks > 0) {
            suggestions.add(String.format("⚠️  You have %d overdue task%s! Consider reviewing your priorities.", 
                overdueTasks, overdueTasks > 1 ? "s" : ""));
        }
        
        if (highPriorityTasks > 0) {
            suggestions.add(String.format("🔴 Focus on your %d high-priority task%s first!", 
                highPriorityTasks, highPriorityTasks > 1 ? "s" : ""));
        }
        
        if (completionRate < 0.3) {
            suggestions.add("📈 Break down large tasks into smaller, manageable steps");
            suggestions.add("🎯 Try focusing on completing 2-3 tasks today");
        } else if (completionRate > 0.8) {
            suggestions.add("🎉 Great progress! You're doing amazing!");
            suggestions.add("✨ Consider adding some stretch goals");
        }
        
        // Time-based suggestions
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(12, 0))) {
            suggestions.add("🌅 Good morning! Plan your day with 'schedule today'");
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            suggestions.add("☀️  Check your afternoon schedule with 'schedule today'");
        } else {
            suggestions.add("🌙 Review today's accomplishments and plan for tomorrow");
        }
        
        // Task organization suggestions
        if (totalTasks > 10) {
            suggestions.add("🗂️  Use 'find <keyword>' to quickly locate specific tasks");
            suggestions.add("📅 Organize your week with schedule views");
        }
        
        return suggestions.stream()
                .limit(3) // Limit to top 3 suggestions to avoid overwhelming
                .collect(Collectors.toList());
    }
    
    /**
     * Suggests optimal times for scheduling tasks based on existing schedule.
     * 
     * @param tasks existing tasks
     * @return list of time slot suggestions
     */
    public static List<String> suggestTimeSlots(List<Task> tasks) {
        List<String> suggestions = new ArrayList<>();
        
        // Find busy time slots
        List<LocalTime> busyTimes = tasks.stream()
                .filter(task -> task instanceof Event || task instanceof Deadline)
                .map(task -> {
                    if (task instanceof Event) {
                        return ((Event) task).getFrom().toLocalTime();
                    } else {
                        return ((Deadline) task).getBy().toLocalTime();
                    }
                })
                .collect(Collectors.toList());
        
        // Suggest optimal time slots
        if (busyTimes.stream().noneMatch(time -> time.isBefore(LocalTime.of(9, 0)))) {
            suggestions.add("🌅 Early morning (8:00-9:00) looks free - great for focused work!");
        }
        
        if (busyTimes.stream().noneMatch(time -> 
                time.isAfter(LocalTime.of(10, 0)) && time.isBefore(LocalTime.of(12, 0)))) {
            suggestions.add("☀️  Late morning (10:00-12:00) is available - perfect for important tasks!");
        }
        
        if (busyTimes.stream().noneMatch(time -> 
                time.isAfter(LocalTime.of(14, 0)) && time.isBefore(LocalTime.of(16, 0)))) {
            suggestions.add("🕐 Afternoon (2:00-4:00 PM) is open - ideal for meetings or collaborative work!");
        }
        
        return suggestions;
    }
    
    /**
     * Analyzes task description and suggests improvements.
     * 
     * @param description the task description
     * @return list of improvement suggestions
     */
    public static List<String> suggestTaskImprovements(String description) {
        List<String> suggestions = new ArrayList<>();
        
        if (description.length() < 10) {
            suggestions.add("💭 Consider adding more details to make the task clearer");
        }
        
        if (!description.toLowerCase().contains("by") && 
            !description.toLowerCase().contains("deadline") &&
            !description.toLowerCase().contains("due")) {
            suggestions.add("📅 Consider setting a deadline: 'deadline " + description + " /by <date>'");
        }
        
        if (description.toLowerCase().contains("meeting") || 
            description.toLowerCase().contains("call") ||
            description.toLowerCase().contains("appointment")) {
            suggestions.add("📞 This looks like an event! Try: 'event " + description + " /from <start> /to <end>'");
        }
        
        // Check for vague terms
        String lower = description.toLowerCase();
        if (containsAny(lower, "later", "someday", "eventually", "maybe")) {
            suggestions.add("🎯 Try to be more specific about when you'll do this task");
        }
        
        return suggestions;
    }
    
    private static boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
