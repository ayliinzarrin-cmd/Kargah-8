# Personal Library System - Kargah 8

This is a Java Swing project for a personal library management system.

## Features

- Load book metadata from `data/Book_List.txt`
- Show all books in a graphical user interface
- View and edit book metadata
- Read book text with simple pagination
- Edit and save full book text
- Count text file lines
- Separate packages for model, service, UI, and file utilities

## Project Structure

```text
project-root/
├── data/
│   ├── Book_List.txt
│   └── books_text/
├── src/main/java/library/
│   ├── Main.java
│   ├── model/Book.java
│   ├── service/LibraryService.java
│   ├── ui/MainFrame.java
│   └── util/FileManager.java
└── README.md
```

## How to Run

From the project root:

```bash
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out library.Main
```

On Windows PowerShell:

```powershell
mkdir out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src/main/java/*.java).FullName
java -cp out library.Main
```

## Team Members

Member 1: `FULL_NAME_1` - `STUDENT_ID_1` - `GITHUB_USERNAME_1`

Member 2: `FULL_NAME_2` - `STUDENT_ID_2` - `GITHUB_USERNAME_2`
