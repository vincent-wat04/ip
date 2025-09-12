# Vince GUI Application

## Overview
Vince now has a JavaFX-based GUI that provides a chat-like interface for task management.

## Features
- **Chat Interface**: User-friendly chat-style interface with text input and response display
- **Task Management**: All existing CLI commands work through the GUI:
  - `todo <description>` - Add a todo task
  - `deadline <description> /by <date>` - Add a deadline task
  - `event <description> /from <date> /to <date>` - Add an event task
  - `list` - Show all tasks
  - `find <keyword>` - Search tasks by keyword
  - `mark <index>` - Mark task as done
  - `unmark <index>` - Mark task as not done
  - `delete <index>` - Delete a task
  - `on <date>` - Show tasks on a specific date
  - `bye` - Exit the application

## Running the GUI

### Using Gradle (Recommended)
```bash
./gradlew run
```

### Using Java directly
```bash
java -cp src/main/java vince.Launcher
```

## GUI Components
- **Scrollable Chat Area**: Shows conversation history between user and Vince
- **Text Input Field**: Enter commands at the bottom
- **Send Button**: Click to send commands
- **User/Duke Avatars**: Visual indicators for user vs. bot messages

## Technical Details
- Built with JavaFX 17.0.7
- Uses the existing command pattern and task management logic
- Maintains data persistence through the existing Storage system
- Follows SE-EDU JavaFX tutorial structure with Launcher class

## Notes
- The GUI maintains all functionality from the CLI version
- Data is automatically saved to `./data/vince.txt`
- The `bye` command closes the application
- Images are loaded from `/images/` resources (with fallback for missing images)
