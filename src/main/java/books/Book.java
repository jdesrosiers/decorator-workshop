package books;

import java.time.Year;
import java.util.Collections;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String subTitle;
    private List<String> authors;
    private Year publishDate;

    public Book(int id, String title, String subTitle, List<String> authors, Year publishDate) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.authors = authors;
        this.publishDate = publishDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subTitle;
    }

    public List<String> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public Year getPublishDate() {
        return publishDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", authors=" + authors +
                ", publishDate=" + publishDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (!title.equals(book.title)) return false;
        if (subTitle != null ? !subTitle.equals(book.subTitle) : book.subTitle != null) return false;
        if (!authors.equals(book.authors)) return false;
        return publishDate.equals(book.publishDate);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + (subTitle != null ? subTitle.hashCode() : 0);
        result = 31 * result + authors.hashCode();
        result = 31 * result + publishDate.hashCode();
        return result;
    }
}
