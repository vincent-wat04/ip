# Vince AI Assistant User Guide

Welcome to **Vince AI Assistant** - your intelligent task management companion! 🤖✨

![Vince AI Assistant](https://github.com/vincent-wat04/ip/)

Vince is an AI-enhanced chatbot designed to help you manage your tasks efficiently with smart features like natural language understanding, automatic priority detection, and intelligent scheduling. Whether you're planning your day or organizing complex projects, Vince has got you covered!

---

## 🚀 Quick Start

### Running Vince

**GUI Version (Recommended):**
```bash
./gradlew run
```

**Command Line Version:**
```bash
java -cp src/main/java vince.Vince
```

---

## 📝 Core Features

### 1. Adding Tasks

#### Todo Tasks
Add simple tasks without specific deadlines.

**Format:** `todo <description>`

**Example:**
```
todo Buy groceries
```

**Expected Output:**
```
Got it. I've added this task:
🔴 [T] [ ] Buy groceries
Now you have 1 tasks in the list.
```

*Note: Vince automatically detects priority based on keywords!*

#### Deadline Tasks
Add tasks with specific deadlines.

**Format:** `deadline <description> /by <date/time>`

**Example:**
```
deadline Submit report /by 15/12/2024 1700
deadline Finish assignment /by tomorrow 5pm
```

**Expected Output:**
```
Got it. I've added this task:
🟡 [D] [ ] Submit report (by: Dec 15 2024, 17:00)
Now you have 2 tasks in the list.
```

#### Event Tasks
Add scheduled events with start and end times.

**Format:** `event <description> /from <start> /to <end>`

**Example:**
```
event Team meeting /from 15/12/2024 1000 /to 15/12/2024 1100
event Conference /from today 2pm /to today 5pm
```

**Expected Output:**
```
Got it. I've added this task:
🔴 [E] [ ] Team meeting (from: Dec 15 2024, 10:00 to: Dec 15 2024, 11:00)
Now you have 3 tasks in the list.
```

---

## 📋 Managing Tasks

### 2. Viewing Tasks

#### List All Tasks
Display all your tasks with their status and priority.

**Format:** `list`

**Example:**
```
list
```

**Expected Output:**
```
Here are the tasks in your list:
1. 🔴 [T] [ ] Buy groceries
2. 🟡 [D] [ ] Submit report (by: Dec 15 2024, 17:00)
3. 🔴 [E] [ ] Team meeting (from: Dec 15 2024, 10:00 to: Dec 15 2024, 11:00)
```

**Priority Indicators:**
- 🔴 **HIGH**: Urgent, important, critical tasks
- 🟡 **MEDIUM**: Normal priority tasks
- 🟢 **LOW**: Low priority, leisure tasks
- ⚪ **NONE**: No specific priority

### 3. Updating Tasks

#### Mark as Done
Mark completed tasks.

**Format:** `mark <task_number>`

**Example:**
```
mark 1
```

**Expected Output:**
```
Nice! I've marked this task as done:
🔴 [T] [X] Buy groceries
```

#### Mark as Not Done
Unmark previously completed tasks.

**Format:** `unmark <task_number>`

**Example:**
```
unmark 1
```

**Expected Output:**
```
OK, I've marked this task as not done yet:
🔴 [T] [ ] Buy groceries
```

#### Delete Tasks
Remove tasks permanently.

**Format:** `delete <task_number>`

**Example:**
```
delete 2
```

**Expected Output:**
```
Noted. I've removed this task:
🟡 [D] [ ] Submit report (by: Dec 15 2024, 17:00)
Now you have 2 tasks in the list.
```

---

## 🔍 Smart Search & Scheduling

### 4. Find Tasks
Search for tasks using keywords.

**Format:** `find <keyword>`

**Example:**
```
find meeting
```

**Expected Output:**
```
Here are the matching tasks in your list:
3. 🔴 [E] [ ] Team meeting (from: Dec 15 2024, 10:00 to: Dec 15 2024, 11:00)
```

### 5. View Tasks by Date
See all tasks scheduled for a specific date.

**Format:** `on <date>`

**Example:**
```
on 15/12/2024
on today
on tomorrow
```

**Expected Output:**
```
Tasks on Dec 15 2024:
2. 🟡 [D] [ ] Submit report (by: Dec 15 2024, 17:00)
3. 🔴 [E] [ ] Team meeting (from: Dec 15 2024, 10:00 to: Dec 15 2024, 11:00)
```

### 6. Schedule View
Get a timeline view of your day with organized time slots.

**Format:** `schedule <date>`

**Example:**
```
schedule today
schedule 15/12/2024
```

**Expected Output:**
```
📅 Schedule for Dec 15 2024:

📅 Daily Schedule:

🌅 All Day:
  • 🔴 [T] [ ] Buy groceries

🕐 10:00:
  • 🔴 [E] [ ] Team meeting (from: Dec 15 2024, 10:00 to: Dec 15 2024, 11:00) (until 11:00)

🕐 17:00:
  • 🟡 [D] [ ] Submit report (by: Dec 15 2024, 17:00)
```

---

## 🧠 AI-Enhanced Features

### 7. Natural Language Understanding
Vince understands human-friendly date and time expressions!

**Supported Formats:**
- **Relative dates:** `today`, `tomorrow`, `yesterday`, `next week`
- **Specific days:** `next monday`, `this friday`
- **Time periods:** `in 3 days`, `in 2 weeks`
- **Natural times:** `3pm`, `2:30am`, `1400`, `9:15pm`

**Examples:**
```
deadline Submit report /by tomorrow 5pm
event Lunch meeting /from today 12:30pm /to today 1:30pm
schedule next friday
on this weekend
```

### 8. Automatic Priority Detection
Vince automatically suggests task priorities based on content analysis.

**High Priority Keywords:**
`urgent`, `asap`, `emergency`, `critical`, `important`, `meeting`, `interview`, `exam`

**Medium Priority Keywords:**
`soon`, `report`, `project`, `assignment`, `call`, `email`, `review`

**Low Priority Keywords:**
`someday`, `maybe`, `consider`, `leisure`, `hobby`, `when free`

**Example:**
```
todo urgent presentation preparation
→ Automatically assigned 🔴 HIGH priority

todo maybe read a book someday
→ Automatically assigned 🟢 LOW priority
```

### 9. Smart Error Handling
Vince helps you when you make mistakes!

**Typo Correction:**
```
Input: lst
Output: Did you mean 'list'?
        Try: 'list'
```

**Format Suggestions:**
```
Input: deadline submit report
Output: Deadline tasks need a '/by' clause to specify the deadline!
        
        Suggestions:
        - deadline <description> /by <date>
        - Example: deadline Submit report /by 15/12/2024 1700
```

### 10. AI-Powered Help
Get contextual help and productivity suggestions.

**Format:** `help`

**Example Output:**
```
🤖 Vince AI Assistant - Available Commands:

📝 Task Management:
  • todo <description> - Add a simple task
  • deadline <description> /by <date> - Add a task with deadline
  • event <description> /from <start> /to <end> - Add a scheduled event

🧠 AI Suggestions for You:
  🔴 Focus on your 3 high-priority tasks first!
  🌅 Good morning! Plan your day with 'schedule today'
  📈 Break down large tasks into smaller, manageable steps
```

---

## 💡 Pro Tips

### Date Format Examples
```
✅ Supported formats:
- 15/12/2024
- 2024-12-15
- today, tomorrow, yesterday
- next monday, this friday
- in 3 days, in 2 weeks

✅ Time format examples:
- 1700 (24-hour)
- 5pm, 5:30pm (12-hour with AM/PM)
- 17:00, 17:30 (24-hour with colon)
```

### Productivity Workflows

**Daily Planning:**
1. `schedule today` - Check your day
2. `todo` - Add new tasks as they come up
3. `mark <number>` - Complete tasks as you go

**Weekly Review:**
1. `list` - See all pending tasks
2. `find overdue` - Check missed deadlines
3. `schedule next week` - Plan ahead

**Project Management:**
1. Use descriptive task names
2. Set realistic deadlines
3. Break large tasks into smaller ones
4. Use priority keywords for automatic sorting

---

## 🖥️ GUI Features

### Window Management
- **Resizable Window:** Drag corners or edges to resize
- **Responsive Design:** Content automatically adapts to window size
- **Minimum Size:** 350×400 pixels
- **Maximum Size:** 800×1000 pixels

### Chat Interface
- **Scrollable History:** All conversations are preserved
- **User/Bot Distinction:** Clear visual separation
- **Message Wrapping:** Long messages wrap appropriately
- **Real-time Response:** Instant feedback for all commands

---

## 🚪 Exit
To close Vince, simply type:

**Format:** `bye`

**Expected Output:**
```
Bye. Hope to see you again soon!
```

---

## 🔧 Troubleshooting

### Common Issues

**Q: My tasks aren't saving between sessions**
A: Check if the `data` directory exists in your project folder. Vince automatically creates it, but ensure you have write permissions.

**Q: Date parsing isn't working**
A: Make sure to use supported date formats. When in doubt, use `DD/MM/YYYY` or `YYYY-MM-DD` formats.

**Q: GUI window is too small/large**
A: Drag the window corners to resize. The window size is constrained between 350×400 and 800×1000 pixels.

**Q: Command not recognized**
A: Check for typos. Vince will suggest corrections for common mistakes. Type `help` for a full command list.

### Error Messages
- `Command cannot be empty!` → Type a valid command
- `Invalid command format` → Check the command syntax
- `Task number out of range` → Use `list` to see valid task numbers
- `Failed to save/load data` → Check file permissions in the project directory

---

## 📚 Command Reference

| Command | Format | Example |
|---------|--------|---------|
| Add Todo | `todo <description>` | `todo Buy milk` |
| Add Deadline | `deadline <desc> /by <date>` | `deadline Report /by tomorrow 5pm` |
| Add Event | `event <desc> /from <start> /to <end>` | `event Meeting /from today 2pm /to today 3pm` |
| List Tasks | `list` | `list` |
| Mark Done | `mark <number>` | `mark 1` |
| Mark Undone | `unmark <number>` | `unmark 2` |
| Delete Task | `delete <number>` | `delete 3` |
| Find Tasks | `find <keyword>` | `find meeting` |
| Tasks on Date | `on <date>` | `on today` |
| Schedule View | `schedule <date>` | `schedule tomorrow` |
| Help | `help` | `help` |
| Exit | `bye` | `bye` |

---

## 🎯 About Vince

Vince AI Assistant is designed to be your intelligent productivity companion. With advanced natural language processing, automatic priority detection, and smart scheduling features, Vince helps you stay organized and focused on what matters most.

**Key Strengths:**
- 🧠 **AI-Enhanced:** Smart error correction and contextual suggestions
- 🗣️ **Natural Language:** Human-friendly date and time input
- 🎯 **Priority-Aware:** Automatic task prioritization
- 📅 **Schedule-Focused:** Visual timeline and calendar features
- 🔄 **Adaptive:** Responsive design that works on any screen size

---

*Happy task managing with Vince! 🎉*

---

**Need more help?** Type `help` in Vince for contextual assistance and AI-powered productivity suggestions!
