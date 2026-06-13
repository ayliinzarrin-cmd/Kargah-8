package library.ui;

import library.model.Book;
import library.service.LibraryService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private LibraryService service;

    private JPanel booksPanel;
    private JPanel menuPanel;
    private JPanel readerPanel;
    private Book selectedBook;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField publisherField;
    private JTextField yearField;
    private JTextArea pageArea;
    private List<String> pages;
    private int currentPage;
    private JLabel pageLabel;
    private JTextField searchField;
    private JPanel listPanel;
    private List<Book> allBooks;

    public MainFrame() {
        service = new LibraryService();
        pages = new ArrayList<>();
        currentPage = 0;

        setTitle("Personal Library System");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createBooksPanel();
        setVisible(true);
    }

    private void createBooksPanel() {
        booksPanel = new JPanel(new BorderLayout(10, 10));
        booksPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("Personal Library", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(new JLabel("Search:"));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterBooks();
            }
        });
        searchPanel.add(searchField);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            filterBooks();
        });
        searchPanel.add(clearButton);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        allBooks = service.getAllBooks();
        displayBooks(allBooks);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            service = new LibraryService();
            allBooks = service.getAllBooks();
            displayBooks(allBooks);
            searchField.setText(""); });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(header, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.CENTER);

        booksPanel.add(northPanel, BorderLayout.NORTH);
        booksPanel.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        booksPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(booksPanel);
        revalidate();
        repaint();
    }

    private void showBookMenu() {
        if (selectedBook == null) {
            return;
        }

        menuPanel = new JPanel(new BorderLayout(10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("Book Information", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        titleField = new JTextField(selectedBook.getTitle());
        authorField = new JTextField(selectedBook.getAuthor());
        publisherField = new JTextField(selectedBook.getPublisher());
        yearField = new JTextField(String.valueOf(selectedBook.getPublicationYear()));

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);

        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);

        formPanel.add(new JLabel("Publisher:"));
        formPanel.add(publisherField);

        formPanel.add(new JLabel("Publication Year:"));
        formPanel.add(yearField);

        formPanel.add(new JLabel("Text File:"));
        formPanel.add(new JLabel(selectedBook.getTextFilePath()));

        formPanel.add(new JLabel("Total Lines:"));
        formPanel.add(new JLabel(String.valueOf(service.countLines(selectedBook))));

        JButton saveButton = new JButton("Save Metadata");
        JButton readButton = new JButton("Read Book");
        JButton editButton = new JButton("Edit Book");
        JButton backButton = new JButton("Back");

        saveButton.addActionListener(e -> saveMetadata());
        readButton.addActionListener(e -> openBook(false));
        editButton.addActionListener(e -> openBook(true));
        backButton.addActionListener(e -> createBooksPanel());

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(readButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(backButton);

        menuPanel.add(header, BorderLayout.NORTH);
        menuPanel.add(formPanel, BorderLayout.CENTER);
        menuPanel.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(menuPanel);
        revalidate();
        repaint();
    }

    private void saveMetadata() {
        if (selectedBook == null) {
            return;
        }

        Integer year = readYearFromField();

        if (year == null) {
            JOptionPane.showMessageDialog(this,
                    "Publication year must be a valid number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean edited = service.editBookMetadata(
                selectedBook.getId(),
                titleField.getText(),
                authorField.getText(),
                publisherField.getText(),
                year
        );

        if (edited) {
            JOptionPane.showMessageDialog(this,
                    "Book information saved successfully.",
                    "Saved",
                    JOptionPane.INFORMATION_MESSAGE);
            showBookMenu();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Book was not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer readYearFromField() {
        try {
            return Integer.parseInt(yearField.getText().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void openBook(boolean editable) {
        if (selectedBook == null) {
            return;
        }

        pages = service.getBookPages(selectedBook, 1);
        currentPage = 0;

        readerPanel = new JPanel(new BorderLayout(10, 10));
        readerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(selectedBook.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        pageArea = new JTextArea();
        pageArea.setFont(new Font("Serif", Font.PLAIN, 38));
        pageArea.setEditable(editable);
        pageArea.setLineWrap(true);
        pageArea.setWrapStyleWord(true);

        if (!pages.isEmpty()) {
            pageArea.setText(pages.get(currentPage));
        }

        JButton previousButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JButton backButton = new JButton("Back");

        pageLabel = new JLabel("", SwingConstants.CENTER);

        previousButton.addActionListener(e -> goPreviousPage(editable));
        nextButton.addActionListener(e -> goNextPage(editable));
        backButton.addActionListener(e -> showBookMenu());

        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        navigationPanel.add(previousButton);
        navigationPanel.add(pageLabel);
        navigationPanel.add(nextButton);

        if (editable) {
            JButton applyButton = new JButton("Apply");
            applyButton.addActionListener(e -> applyBookChanges());
            navigationPanel.add(applyButton);
        }

        navigationPanel.add(backButton);

        updatePageLabel();

        readerPanel.add(titleLabel, BorderLayout.NORTH);
        readerPanel.add(new JScrollPane(pageArea), BorderLayout.CENTER);
        readerPanel.add(navigationPanel, BorderLayout.SOUTH);

        setContentPane(readerPanel);
        revalidate();
        repaint();
    }

    private void goPreviousPage(boolean editable) {
        if (currentPage <= 0) {
            return;
        }

        if (editable) {
            saveCurrentPageInMemory();
        }

        currentPage--;
        pageArea.setText(pages.get(currentPage));
        pageArea.setCaretPosition(0);
        updatePageLabel();
    }

    private void goNextPage(boolean editable) {
        if (currentPage >= pages.size() - 1) {
            return;
        }

        if (editable) {
            saveCurrentPageInMemory();
        }

        currentPage++;
        pageArea.setText(pages.get(currentPage));
        pageArea.setCaretPosition(0);
        updatePageLabel();
    }

    private void saveCurrentPageInMemory() {
        if (pages != null && !pages.isEmpty()) {
            pages.set(currentPage, pageArea.getText());
        }
    }

    private void applyBookChanges() {
        if (selectedBook == null) {
            return;
        }

        saveCurrentPageInMemory();

        StringBuilder fullText = new StringBuilder();

        for (int i = 0; i < pages.size(); i++) {
            fullText.append(pages.get(i));

            if (i < pages.size() - 1 && !pages.get(i).endsWith(System.lineSeparator())) {
                fullText.append(System.lineSeparator());
            }
        }

        try {
            boolean edited = service.editBookContent(selectedBook.getId(), fullText.toString());

            if (edited) {
                JOptionPane.showMessageDialog(this,
                        "Book content saved successfully.",
                        "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
                openBook(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Book was not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not save book content:\n" + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePageLabel() {
        int totalPages = pages == null ? 0 : pages.size();
        pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
    }

    private void displayBooks(List<Book> books) {
        listPanel.removeAll();

        if (books.isEmpty()) {
            JLabel emptyLabel = new JLabel( searchField.getText().isEmpty() ?
                    "No books found. Check data/Book_List.txt" :
                    "No matching books found.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
        } else {
            for (Book book : books) {
                JButton button = new JButton(book.toString());
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

                button.addActionListener(e -> {
                    selectedBook = book;
                    showBookMenu();
                });

                listPanel.add(button);
                listPanel.add(Box.createVerticalStrut(8));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void filterBooks() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            displayBooks(allBooks);
            return;
        }

        List<Book> filtered = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.getTitle().toLowerCase().contains(searchText)) {
                filtered.add(book);
            }
        }

        displayBooks(filtered);
    }
}
