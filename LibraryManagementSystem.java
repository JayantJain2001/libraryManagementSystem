import java.io.*;
import java.util.*;

class Book implements Serializable {
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private boolean available;

    public Book(String title, String author, String isbn, String genre) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.available = true;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return isbn;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Genre: " + genre + ", Available: " + available;
    }
}

class Library {
    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(String isbn) {
        for (Iterator<Book> iterator = books.iterator(); iterator.hasNext();) {
            Book book = iterator.next();
            if (book.getISBN().equals(isbn)) {
                iterator.remove();
                return;
            }
        }
    }

    public Book findBookByISBN(String isbn) {
        for (Book book : books) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> findBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equals(author)) {
                result.add(book);
            }
        }
        return result;
    }

    public void borrowBook(String isbn) {
        Book book = findBookByISBN(isbn);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Book not available for borrowing.");
        }
    }

    public void returnBook(String isbn) {
        Book book = findBookByISBN(isbn);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Invalid book or book already returned.");
        }
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            books = (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        // Load data from file if it exists
        library.loadFromFile("library_data.txt");

        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("\nLibrary Management System Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Search Book by ISBN");
            System.out.println("4. Search Books by Author");
            System.out.println("5. Borrow Book");
            System.out.println("6. Return Book");
            System.out.println("7. Display All Books");
            System.out.println("8. Save Data to File");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter Book Details:");
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Author: ");
                    String author = scanner.nextLine();
                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();
                    library.addBook(new Book(title, author, isbn, genre));
                    break;
                case 2:
                    System.out.print("Enter ISBN of the book to remove: ");
                    String isbnToRemove = scanner.nextLine();
                    library.removeBook(isbnToRemove);
                    break;
                case 3:
                    System.out.print("Enter ISBN to search: ");
                    String isbnToSearch = scanner.nextLine();
                    Book foundBook = library.findBookByISBN(isbnToSearch);
                    if (foundBook != null) {
                        System.out.println("Book found: " + foundBook);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter author name to search: ");
                    String authorToSearch = scanner.nextLine();
                    List<Book> booksByAuthor = library.findBooksByAuthor(authorToSearch);
                    if (!booksByAuthor.isEmpty()) {
                        System.out.println("Books by " + authorToSearch + ":");
                        for (Book book : booksByAuthor) {
                            System.out.println(book);
                        }
                    } else {
                        System.out.println("No books found by " + authorToSearch);
                    }
                    break;
                case 5:
                    System.out.print("Enter ISBN to borrow: ");
                    String isbnToBorrow = scanner.nextLine();
                    library.borrowBook(isbnToBorrow);
                    break;
                case 6:
                    System.out.print("Enter ISBN to return: ");
                    String isbnToReturn = scanner.nextLine();
                    library.returnBook(isbnToReturn);
                    break;
                case 7:
                    System.out.println("List of Books:");
                    library.displayBooks();
                    break;
                case 8:
                    library.saveToFile("library_data.txt");
                    System.out.println("Data saved to file.");
                    break;
                case 9:
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);

        scanner.close();
    }
}
