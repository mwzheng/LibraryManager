package Models;

import Helpers.StringHelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Author {
    private String name;
    private String birthDate;
    private final HashSet<String> bookSet;
    private int booksWritten;

    public Author(String name) {
        this.name = StringHelpers.makeTitleCase(name);
        this.birthDate = null;
        this.booksWritten = 0;
        bookSet = new HashSet<>();
    }

    public Author(String name, String birthDate) {
        this(name);

        if (StringHelpers.isValidDateFormat(birthDate))
            this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public int getNumbOfBooksWritten() {
        return booksWritten;
    }

    public void setName(String name) {
        if (name == null || name.equals(""))
            return;

        this.name = StringHelpers.makeTitleCase(name);
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        if (!StringHelpers.isValidDateFormat(birthDate))
            return;

        this.birthDate = birthDate;
    }

    /**
     * Returns a string of book titles (separated by commas) written by the author
     **/
    public String getBooksWritten() {
        ArrayList<String> books = new ArrayList<>(bookSet);
        Collections.sort(books);
        return books.toString();
    }

    /**
     * Removes a SINGLE book title from author's bookSet
     **/
    public void removeBookWritten(String title) {
        title = StringHelpers.makeTitleCase(title);

        if (bookSet.contains(title)) {
            bookSet.remove(title);
            this.booksWritten--;
        }
    }

    /**
     * Adds a SINGLE book to author's bookSet
     **/
    public void addBookWritten(String title) {
        if (title == null || title.equals(""))
            return;

        title = StringHelpers.makeTitleCase(title);
        bookSet.add(title);
        this.booksWritten++;
    }

    public String toString() {
        return "Name: " + name + ", Birth Date: " + ((birthDate == null) ? "Unknown" : birthDate) + ", Books Written: " + booksWritten;
    }

    /**
     * Two others are considered equal if they have the same name and birth date
     **/
    public boolean equals(Object obj) {
        if (!(obj instanceof Author))
            return false;

        Author otherAuthor = (Author) obj;

        return name.equals(otherAuthor.getName()) && birthDate.equals(otherAuthor.getBirthDate());
    }
}
