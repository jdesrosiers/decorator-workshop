package books;

import org.apache.commons.jcs.access.CacheAccess;
import org.junit.Before;
import org.junit.Test;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class BookServiceCacheTest {
    private CacheAccess<Integer, Book> cache;
    private BookService db;
    private BookService service;

    private Book bookFixture = new Book(
        1,
        "Agile Software Development",
        "Principles, Patterns, and Practices",
        Arrays.asList("Robert C. Martin"),
        Year.of(2003)
    );

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        cache = mock(CacheAccess.class);
        db = mock(BookService.class);
        service = new BookServiceCache(db, cache);
    }

    @Test
    public void getBookShouldRetrieveABookFromTheBookServiceOnACacheMissThenCacheIt() throws BookstoreException {
        when(cache.get(1)).thenReturn(null);
        when(db.get(1)).thenReturn(bookFixture);
        Book book = service.get(1);

        verify(db).get(1);
        verify(cache).put(1, bookFixture);
        assertThat(book, equalTo(bookFixture));
    }

    @Test
    public void getBookShouldNotCallTheBookServiceForABookThatHasBeenCached() throws BookstoreException {
        when(cache.get(1)).thenReturn(bookFixture);
        Book book = service.get(1);

        verify(db, never()).get(1);
        assertThat(book, equalTo(bookFixture));
    }

    @Test
    public void getAllBooksShouldRetrieveBooksFromTheBookService() throws BookstoreException {
        when(db.getAll()).thenReturn(Arrays.asList(1, 2));
        List<Integer> book = service.getAll();

        assertThat(book.size(), equalTo(2));
        assertThat(book.get(0), equalTo(1));
        assertThat(book.get(1), equalTo(2));
    }

    @Test
    public void deleteBookShouldRemoveABookFromTheBookServiceAndTheCache() throws BookstoreException {
        service.delete(1);

        verify(db).delete(1);
        verify(cache).remove(1);
    }

    @Test
    public void saveBookShouldUpdateTheBookInTheBookServiceToMatchTheGivenBookAndUpdateTheCache() throws BookstoreException {
        Book newBook = new Book(1, "Foo", "Bar", Arrays.asList("Me"), Year.of(2017));
        service.save(newBook);
        verify(db).save(newBook);
        verify(cache).put(1, newBook);
    }
}