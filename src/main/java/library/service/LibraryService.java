package library.service;

import library.model.Book;
import library.util.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryService {
    private List<Book> books;

    public LibraryService() {
        books = FileManager.loadBooksFromCSV();
        FileManager.saveBooksToCSV(books);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public Optional<Book> findBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return Optional.of(book);
            }
        }

        return Optional.empty();
    }

    public void saveBooks() {
        FileManager.saveBooksToCSV(books);
    }

    public boolean editBookMetadata(int bookId, String newTitle, String newAuthor,
                                    String newPublisher, Integer newYear) {

        Optional<Book> foundBook = findBookById(bookId);

        if (!foundBook.isPresent()) {
            return false;
        }

        Book book = foundBook.get();

        if (hasText(newTitle)) {
            book.setTitle(newTitle);
        }

        if (hasText(newAuthor)) {
            book.setAuthor(newAuthor);
        }

        if (hasText(newPublisher)) {
            book.setPublisher(newPublisher);
        }

        if (newYear != null && newYear > 0) {
            book.setPublicationYear(newYear);
        }

        saveBooks();
        return true;
    }

    public List<String> getBookPages(Book book, int linesPerPage) {
        if (book == null) {
            List<String> result = new ArrayList<>();
            result.add("No book selected.");
            return result;
        }

        return FileManager.readBookPages(book.getTextFilePath(), linesPerPage);
    }

    public String readFullText(int bookId) {
        Optional<Book> foundBook = findBookById(bookId);

        if (!foundBook.isPresent()) {
            return "";
        }

        return FileManager.readFullText(foundBook.get().getTextFilePath());
    }

    public boolean editBookContent(int bookId, String newContent) throws IOException {
        Optional<Book> foundBook = findBookById(bookId);

        if (!foundBook.isPresent()) {
            return false;
        }

        FileManager.writeBookText(foundBook.get().getTextFilePath(), newContent);
        return true;
    }

    public int countLines(Book book) {
        if (book == null) {
            return 0;
        }

        return FileManager.countLines(book.getTextFilePath());
    }

    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
