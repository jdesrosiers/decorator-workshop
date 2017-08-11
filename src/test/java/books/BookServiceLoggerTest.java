package books;

import org.junit.Before;
import org.junit.Test;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceLoggerTest {
    private Logger logger;
    private BookService parent;
    private BookService service;

    private Book bookFixture = new Book(
        1,
        "Agile Software Development",
        "Principles, Patterns, and Practices",
        Arrays.asList("Robert C. Martin"),
        Year.of(2003)
    );

    @Before
    public void setUp() {
        parent = mock(BookService.class);
        logger = mock(Logger.class);
        service = new BookServiceLogger(parent, logger);
    }

    @Test
    public void getBookShouldLogTheRequestAndCallTheParent() throws BookstoreException {
        when(parent.get(1)).thenReturn(bookFixture);
        Book book = service.get(1);

        verify(parent).get(1);
        verify(logger).info(anyString());
        assertThat(book, equalTo(bookFixture));
    }

    @Test
    public void getAllBooksShouldLogTheRequestAndCallTheParent() throws BookstoreException {
        when(parent.getAll()).thenReturn(Arrays.asList(1, 2));
        List<Integer> book = service.getAll();

        verify(logger).info(anyString());
        assertThat(book.size(), equalTo(2));
        assertThat(book.get(0), equalTo(1));
        assertThat(book.get(1), equalTo(2));
    }

    @Test
    public void deleteBookShouldLogTheRequestAndCallTheParent() throws BookstoreException {
        service.delete(1);

        verify(logger).info(anyString());
        verify(parent).delete(1);
    }

    @Test
    public void saveBookShouldLogTheRequestAndCallTheParent() throws BookstoreException {
        Book newBook = new Book(1, "Foo", "Bar", Arrays.asList("Me"), Year.of(2017));
        service.save(newBook);

        verify(logger).info(anyString());
        verify(parent).save(newBook);
    }
}

