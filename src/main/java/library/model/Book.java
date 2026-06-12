package library.model;

public class Book {
    private int id;
    private String title;
    private String textFilePath;
    private String author;
    private String publisher;
    private int publicationYear;

    public Book(int id, String title, String textFilePath, String author, String publisher, int publicationYear) {
        this.id = id;
        this.title = title;
        this.textFilePath = textFilePath;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null) {
            this.title = title.trim();
        }
    }

    public String getTextFilePath() {
        return textFilePath;
    }

    public void setTextFilePath(String textFilePath) {
        if (textFilePath != null) {
            this.textFilePath = textFilePath.trim();
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author != null) {
            this.author = author.trim();
        }
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        if (publisher != null) {
            this.publisher = publisher.trim();
        }
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        if (publicationYear > 0) {
            this.publicationYear = publicationYear;
        }
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + "  |  " + author + "  |  " + publisher + "  |  " + publicationYear;
    }
}
