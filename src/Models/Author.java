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

    public Author(String name, String birthDate) {
        this.name = name;
        this.birthDate = (StringHelpers.isValidDateFormat(birthDate)) ? birthDate : "Unknown";
        this.bookSet = new HashSet<>();
        this.booksWritten = 0;
    }

    /**
     * Returns the name of the author.
     **/
    public String getName() {
        return this.name;
    }

    /**
     * Returns the number of known books the author has written.
     **/
    public int getNumbOfBooksWritten() {
        return this.booksWritten;
    }

    /**
     * Update the name of the author
     **/
    public void setName(String name) {
        if (name == null || name.equals(""))
            return;

        this.name = StringHelpers.makeTitleCase(name);
    }

    /**
     * Returns the birth date of the author
     **/
    public String getBirthDate() {
        return this.birthDate;
    }

    /**
     * Update the author's birth date
     **/
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
     * Removes a SINGLE book title from author's bookSet.
     **/
    public void removeBookWritten(String title) {
        if (title == null || title.equals(""))
            return;

        title = StringHelpers.makeTitleCase(title);

        if (this.bookSet.contains(title)) {
            this.bookSet.remove(title);
            this.booksWritten--;
        }
    }

    /**
     * Adds a SINGLE book to author's bookSet.
     **/
    public void addBookWritten(String title) {
        if (title == null || title.equals(""))
            return;

        title = (StringHelpers.isTitleCase(title)) ?
                title : StringHelpers.makeTitleCase(title);

        if (this.bookSet.contains(title))
            return;

        this.bookSet.add(title);
        this.booksWritten++;
    }

    public String toString() {
        return "Name: " + name + ", Birth Date: " + birthDate + ", Books Written: " + booksWritten;
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
