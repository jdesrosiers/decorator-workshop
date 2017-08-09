package books;

import java.util.List;

public interface BookService {
    List<Integer> getAll() throws BookstoreException;

    Book get(int id) throws BookstoreException;

    void delete(int id) throws BookstoreException;

    void save(Book book) throws BookstoreException;
}
