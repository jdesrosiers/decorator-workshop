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
    @SuppressWarnings("unchecked")
    public void setUp() {
        cache = mock(CacheAccess.class);
        parent = mock(BookService.class);
        service = new BookServiceCache(parent, cache);
    }

    @Test
    public void getBookShouldRetrieveABookFromTheParentOnACacheMissThenCacheIt() throws BookstoreException {
        when(cache.get(1)).thenReturn(null);
        when(parent.get(1)).thenReturn(bookFixture);
        Book book = service.get(1);

        verify(parent).get(1);
        verify(cache).put(1, bookFixture);
        assertThat(book, equalTo(bookFixture));
    }

    @Test
    public void getBookShouldNotCallTheParentForABookThatHasBeenCached() throws BookstoreException {
        when(cache.get(1)).thenReturn(bookFixture);
        Book book = service.get(1);

        verify(parent, never()).get(1);
        assertThat(book, equalTo(bookFixture));
    }

    @Test
    public void getAllBooksShouldRetrieveBooksFromTheParent() throws BookstoreException {
        when(parent.getAll()).thenReturn(Arrays.asList(1, 2));
        List<Integer> book = service.getAll();

        assertThat(book.size(), equalTo(2));
        assertThat(book.get(0), equalTo(1));
        assertThat(book.get(1), equalTo(2));
    }

    @Test
    public void deleteBookShouldRemoveABookFromTheParentAndTheCache() throws BookstoreException {
        service.delete(1);

        verify(parent).delete(1);
        verify(cache).remove(1);
    }

    @Test
    public void saveBookShouldSaveTheBookOnTheParentWithTheGivenBookAndUpdateTheCache() throws BookstoreException {
        Book newBook = new Book(1, "Foo", "Bar", Arrays.asList("Me"), Year.of(2017));
        service.save(newBook);
        verify(parent).save(newBook);
        verify(cache).put(1, newBook);
    }
}