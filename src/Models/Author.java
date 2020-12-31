package Models;

import Helpers.StringHelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Class represents an author. An author has a name, birth date, a list of books they've written
 * and a count for the # of books they've written. Name and book titles should be in title case
 * (Ex: The Cat In The Hat vs. the cat in the hat), Birth Date format: MM/DD/YYYY
 **/
public class Author {
    private String name;
    private String birthDate;
    private final HashSet<String> bookSet;

    /**
     * Author constructor. Assumes that name passed in is title cased. (EX: Dr. Suess)
     * Date should be in format: MM/DD/YYYY
     **/
    public Author(String name, String birthDate) {
        this.name = name;
        this.birthDate = (StringHelpers.isValidDateFormat(birthDate)) ? birthDate : "Unknown";
        this.bookSet = new HashSet<>();
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
        return bookSet.size();
    }

    /**
     * Update the name of the author if name is valid (Not null or empty string).
     * Note: Name will be converted to titleCase if it isn't already.
     **/
    public void setName(String name) {
        if (StringHelpers.isNullOrEmptyString(name))
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
     * Update the author's birth date if the date
     * format is valid (MM/DD/YYYY), else does nothing
     **/
    public void setBirthDate(String birthDate) {
        if (!StringHelpers.isValidDateFormat(birthDate))
            return;

        this.birthDate = birthDate;
    }

    /**
     * Returns a string of book titles (separated by commas and sorted)
     * that are written by the author.
     **/
    public String getBooksWritten() {
        ArrayList<String> books = new ArrayList<>(bookSet);
        Collections.sort(books);
        return books.toString();
    }

    /**
     * Removes a SINGLE book title from author's bookSet and decrements
     * the # of books written by author. If the title isn't in the bookSet,
     * then do nothing. If title is null or "" do nothing.
     **/
    public void removeBookWritten(String title) {
        if (StringHelpers.isNullOrEmptyString(name))
            return;

        title = StringHelpers.makeTitleCase(title);

        this.bookSet.remove(title);
    }

    /**
     * If title is new, adds book to author's bookSet and increases
     * author's booksWritten. If title is null or "" do nothing.
     * NOTE: Doesn't add duplicate books
     **/
    public void addBookWritten(String title) {
        if (StringHelpers.isNullOrEmptyString(name))
            return;

        title = StringHelpers.makeTitleCase(title);

        if (this.bookSet.contains(title)) return;

        bookSet.add(title);
    }

    /**
     * String format: Name: name, Birth Date: date, Books Written: books"
     * NOTE: Book titles listed in sorted order and separated by commas.
     **/
    public String toString() {
        return "Name: " + name + ", Birth Date: " + birthDate + ", Books Written: " + getBooksWritten();
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