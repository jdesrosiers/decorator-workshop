import books.Book;
import books.BookService;
import books.BookServiceFactory;
import books.BookstoreException;

import java.util.ArrayList;
import java.util.List;

public class Bookstore {
    public static void main(String[] args) throws BookstoreException {
        displayGreeting();

        BookServiceFactory factory = new BookServiceFactory();
        BookService service = factory.getBookService();

        handleMainMenu(service);
    }

    public static void handleMainMenu(BookService service) throws BookstoreException {
        while (true) {
            List<Book> books = new ArrayList<>();
            for (int id : service.getAll()) {
                books.add(service.get(id));
            }

            displayBookshelf(books);
            displayMainMenu();

            String option = System.console().readLine();
            if (option.equals("q")) {
                break;
            } else if (option.equals("c")) {
                System.out.println("TODO: CREATE BOOK");
            } else {
                handleBookMenu(Integer.valueOf(option), service);
            }
        }
    }

    public static void handleBookMenu(int bookId, BookService service) throws BookstoreException {
        while (true) {
            Book book = service.get(bookId);
            displayBook(book);
            displayBookMenu();

            String option = System.console().readLine();
            if (option.equals("l")) {
                break;
            } else if (option.equals("u")) {
                System.out.println("TODO: UPDATE BOOK");
            } else if (option.equals("d")) {
                service.delete(bookId);
                break;
            }
        }
    }

    public static void displayGreeting() {
        System.out.println("Welcome to the bookstore");
    }

    public static void displayBookshelf(List<Book> books) {
        System.out.println("");
        System.out.println("-- BOOKSHELF --");
        for (Book book : books) {
            System.out.println(String.format("%d) %s, by %s", book.getId(), book.getTitle(), book.getAuthors().get(0)));
        }
    }

    public static void displayBook(Book book) {
        System.out.println("");
        System.out.println(String.format("Title: %s", book.getTitle()));
        System.out.println(String.format("Subtitle: %s", book.getSubtitle()));
        System.out.println(String.format("Author(s): %s", String.join(", ", book.getAuthors())));
        System.out.println(String.format("Publish Date: %d", Integer.valueOf(book.getPublishDate().toString())));
    }

    public static void displayMainMenu() {
        System.out.println("");
        System.out.println("MENU");
        System.out.println(" id) Get a book's details by id");
        System.out.println("  c) Add a book");
        System.out.println("  q) Quit");
        System.out.println("");
        System.out.print("Select an option: ");
    }

    public static void displayBookMenu() {
        System.out.println("");
        System.out.println(" u) Modify book");
        System.out.println(" d) Remove book");
        System.out.println(" l) Back to bookshelf");
        System.out.println("");
        System.out.print("Select an option: ");
    }
}
