package books;

import java.util.List;
import java.util.logging.Logger;

public class BookServiceProfiler implements BookService {
    private String parentClass;
    private BookService parent;
    private Logger logger;

    public BookServiceProfiler(BookService parent, Logger logger) {
        this.parentClass = parent.getClass().toString();
        this.parent = parent;
        this.logger = logger;
    }

    @Override
    public List<Integer> getAll() throws BookstoreException {
        long start = System.nanoTime();
        List<Integer> result = parent.getAll();
        long end = System.nanoTime();

        long executionTime = (end - start) / 1000000;
        logger.info(String.format("%s.getAll() took %d milliseconds", parentClass, executionTime));

        return result;
    }

    @Override
    public Book get(int id) throws BookstoreException {
        long start = System.nanoTime();
        Book result = parent.get(id);
        long end = System.nanoTime();

        long executionTime = (end - start) / 1000000;
        logger.info(String.format("%s.get(%s) took %d milliseconds", parentClass, id, executionTime));

        return result;
    }

    @Override
    public void delete(int id) throws BookstoreException {
        long start = System.nanoTime();
        parent.delete(id);
        long end = System.nanoTime();

        long executionTime = (end - start) / 1000000;
        logger.info(String.format("%s.delete(%s) took %d milliseconds", parentClass, id, executionTime));
    }

    @Override
    public void save(Book book) throws BookstoreException {
        long start = System.nanoTime();
        parent.save(book);
        long end = System.nanoTime();

        long executionTime = (end - start) / 1000000;
        logger.info(String.format("%s.save(%s) took %d milliseconds", parentClass, book, executionTime));
    }
}
