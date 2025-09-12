#!/bin/bash

echo "Starting Vince GUI Application..."
echo "=================================="
echo ""
echo "The GUI will open in a new window."
echo "You can interact with Vince using the chat interface."
echo ""
echo "Available commands:"
echo "  - todo <description>"
echo "  - deadline <description> /by <date>"
echo "  - event <description> /from <date> /to <date>"
echo "  - list"
echo "  - find <keyword>"
echo "  - mark <index>"
echo "  - unmark <index>"
echo "  - delete <index>"
echo "  - on <date>"
echo "  - bye (to exit)"
echo ""
echo "Press Ctrl+C to stop this script and close the GUI."
echo ""

# Run the GUI application
./gradlew run
