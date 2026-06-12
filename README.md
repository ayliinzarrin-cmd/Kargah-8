# Personal Library System - Kargah 8

This is a Java Swing project for a personal library management system.

## Features

* Load book metadata from `data/Book_List.txt`
* Show all books in a graphical user interface
* View and edit book metadata
* Read book text with simple pagination
* Edit and save full book text
* Count text file lines
* Use separate packages for model, service, UI, and file utilities

## Project Structure

```text
project-root/
├── data/
│   ├── Book_List.txt
│   └── books_text/
│       ├── 1984.txt
│       ├── Animal_Farm.txt
│       ├── The_Great_Gatsby.txt
│       └── To_Kill_a_Mockingbird.txt
├── src/main/java/library/
│   ├── Main.java
│   ├── model/
│   │   └── Book.java
│   ├── service/
│   │   └── LibraryService.java
│   ├── ui/
│   │   └── MainFrame.java
│   └── util/
│       └── FileManager.java
├── TEAM_WORK_GUIDE.md
├── QUERA_UPLOAD_TEXT.txt
├── run.sh
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
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java -Path src/main/java).FullName
java -cp out library.Main
```

## Team Members

Member 1:

* Full name: آیلین زرین بخش
* Student ID: 40413418
* GitHub username: ayliinzarrin-cmd

Member 2:

* Full name: نورالدین امینی
* Student ID: 40312010
* GitHub username: amininoorodin1384

## GitHub Workflow

The project was managed using GitHub branches and pull requests.

Each member worked on a separate branch and created a pull request. The other team member reviewed and approved the pull request before it was merged into the `main` branch.

Main collaboration steps:

* Separate branches were created for project updates.
* Pull requests were opened for review.
* Each pull request was approved by the other member.
* Approved pull requests were merged into the `main` branch.

## Final Project Notes

This project was implemented as a personal library management system using Java, Swing, file handling, and object-oriented programming.

The application includes:

* Book metadata management
* Reading book text files
* Editing and saving book content
* CSV-based book list storage
* A simple Swing graphical user interface

The final version was reviewed and prepared for Quera submission.
