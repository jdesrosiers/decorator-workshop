package books;

import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;

import java.util.List;

public class BookServiceCache implements BookService {
    private BookService service;
    private CacheAccess<Integer, Book> cache;

    public BookServiceCache(BookService service, CacheAccess<Integer, Book> cache) {
        this.service = service;
        this.cache = cache;
    }

    @Override
    public List<Integer> getAll() throws BookstoreException {
        return service.getAll();
    }

    @Override
    public Book get(int id) throws BookstoreException {
        Book book = cache.get(id);

        if (book == null) {
            book = service.get(id);
            try {
                cache.put(id, book);
            } catch (CacheException e) {
                // TODO: Log cache failure
            }
        }

        return book;
    }

    @Override
    public void delete(int id) throws BookstoreException {
        service.delete(id);
        try {
            cache.remove(id);
        } catch (CacheException e) {
            // TODO: Log cache failure
        }
    }

    @Override
    public void save(Book book) throws BookstoreException {
        service.save(book);
        try {
            cache.put(book.getId(), book);
        } catch (CacheException e) {
            // TODO: Log cache failure
        }
    }
}
