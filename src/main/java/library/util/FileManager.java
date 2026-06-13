package library.util;

import library.model.Book;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static final String BOOKS_TEXT_DIR = "data/books_text";
    public static final String BOOK_LIST_CSV = "data/Book_List.txt";

    public static List<Book> loadBooksFromCSV() {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOK_LIST_CSV);

        if (!file.exists()) {
            createEmptyBookListFile();
            return books;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            int id = 1;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                List<String> parts = splitCsvLine(line);

                if (parts.size() < 5) {
                    System.out.println("Invalid row ignored: " + line);
                    continue;
                }

                String title = parts.get(0).trim();
                String author = parts.get(1).trim();
                String publisher = parts.get(2).trim();
                int year = parseYear(parts.get(3));
                String path = fixBookPath(parts.get(4).trim());

                books.add(new Book(id, title, path, author, publisher, year));
                id++;
            }

        } catch (IOException e) {
            System.out.println("Could not load books: " + e.getMessage());
        }

        return books;
    }

    public static void saveBooksToCSV(List<Book> books) {
        File file = new File(BOOK_LIST_CSV);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write("title,author,publisher,year,file");
            writer.newLine();

            for (Book book : books) {
                writer.write(toCsvValue(book.getTitle()));
                writer.write(",");
                writer.write(toCsvValue(book.getAuthor()));
                writer.write(",");
                writer.write(toCsvValue(book.getPublisher()));
                writer.write(",");
                writer.write(String.valueOf(book.getPublicationYear()));
                writer.write(",");
                writer.write(toCsvValue(book.getTextFilePath()));
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Could not save books: " + e.getMessage());
        }
    }

    public static List<String> readBookPages(String filePath, int linesPerPage) {
        List<String> pages = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            pages.add("There is no text file: " + filePath);
            return pages;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder pageBuilder = new StringBuilder();
            int nonEmptyLineCount = 0;

            String line;
            while ((line = br.readLine()) != null) {
                pageBuilder.append(line).append("\n");

                if (!line.trim().isEmpty()) {
                    nonEmptyLineCount++;
                }

                if (nonEmptyLineCount == linesPerPage) {
                    String pageContent = pageBuilder.toString();
                    pageContent = pageContent.replaceAll("^\\n+", "");
                    pageContent = pageContent.replaceAll("\\n+$", "");
                    pages.add(pageContent);

                    pageBuilder.setLength(0);
                    nonEmptyLineCount = 0;
                }
            }

            if (pageBuilder.length() > 0) {
                String pageContent = pageBuilder.toString();
                pageContent = pageContent.replaceAll("^\\n+", "");
                pageContent = pageContent.replaceAll("\\n+$", "");
                pages.add(pageContent);
            }

            if (pages.isEmpty()) {
                pages.add("");
            }

        } catch (IOException e) {
            pages.clear();
            pages.add("Error reading file: " + e.getMessage());
        }

        return pages;
    }

    public static int countLines(String filePath) {
        return countLines(filePath, 59);
    }

    public static int countLines(String filePath, int maxCharsPerLine) {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }

        int totalLines = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                int charCount = line.length();
                int linesForThisLine = (int) Math.ceil((double) charCount / maxCharsPerLine);
                totalLines += linesForThisLine;
            }

        } catch (IOException e) {
            System.out.println("Could not count lines: " + e.getMessage());
            return 0;
        }

        return totalLines;
    }

    public static void writeBookText(String filePath, String content) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            if (content != null) {
                writer.write(content);
            }
        }
    }

    public static String readFullText(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            return "";
        }

        StringBuilder text = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line).append(System.lineSeparator());
            }

        } catch (IOException e) {
            return "";
        }

        return text.toString();
    }

    private static void createEmptyBookListFile() {
        File file = new File(BOOK_LIST_CSV);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write("title,author,publisher,year,file");
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Could not create Book_List.txt: " + e.getMessage());
        }
    }

    private static int parseYear(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String fixBookPath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }

        path = path.replace("\\", "/");

        if (new File(path).exists()) {
            return path;
        }

        if (path.startsWith(BOOKS_TEXT_DIR + "/")) {
            return path;
        }

        if (path.contains("/")) {
            return path;
        }

        return BOOKS_TEXT_DIR + "/" + path;
    }

    private static String toCsvValue(String value) {
        if (value == null) {
            return "";
        }

        value = value.replace("\"", "\"\"");
        boolean needsQuote = value.contains(",") || value.contains("\n") || value.contains("\"");

        if (needsQuote) {
            return "\"" + value + "\"";
        }

        return value;
    }

    private static List<String> splitCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuote = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (insideQuote && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    insideQuote = !insideQuote;
                }
            } else if (ch == ',' && !insideQuote) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        result.add(current.toString());
        return result;
    }
}
