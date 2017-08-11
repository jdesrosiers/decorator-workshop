package books;

import java.util.List;
import java.util.logging.Logger;

public class BookServiceLogger implements BookService {
    private String parentClass;
    private BookService parent;
    private Logger logger;

    public BookServiceLogger(BookService parent, Logger logger) {
        this.parentClass = parent.getClass().toString();
        this.parent = parent;
        this.logger = logger;
    }

    @Override
    public List<Integer> getAll() throws BookstoreException {
        logger.info(String.format("%s.getAll() called", parentClass));
        return parent.getAll();
    }

    @Override
    public Book get(int id) throws BookstoreException {
        logger.info(String.format("%s.get(%s) called", parentClass, id));
        return parent.get(id);
    }

    @Override
    public void delete(int id) throws BookstoreException {
        logger.info(String.format("%s.delete(%s) called", parentClass, id));
        parent.delete(id);
    }

    @Override
    public void save(Book book) throws BookstoreException {
        logger.info(String.format("%s.save(%s) called", parentClass, book));
        parent.save(book);
    }
}
