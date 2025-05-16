package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Library {
    private String name;
    private int totalBooks;
    private List<Author> authors;

    public static class Author {
        private String name;
        private List<String> books;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getBooks() {
            return books;
        }

        public void setBooks(List<String> books) {
            this.books = books;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    @JsonProperty("total_books")
    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
