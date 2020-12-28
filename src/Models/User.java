package Models;

import Helpers.StringHelpers;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class represents a library user. Each user has a unique id on creation,a name,
 * a list of cooks they've checked out and a cap on # of books they can check out.
 **/
public class User {
    private static final int defaultCheckOutLimit = 5;
    private final ArrayList<String> booksCheckedOut;
    private int checkOutLimit;
    private final String id;
    private String name;

    public User(String name) {
        this.name = StringHelpers.makeTitleCase(name);
        id = StringHelpers.generateRandomId();
        this.checkOutLimit = defaultCheckOutLimit;
        booksCheckedOut = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getCheckOutLimit() {
        return this.checkOutLimit;
    }

    public int getNumbBooksCheckedOut() {
        return booksCheckedOut.size();
    }

    /**
     * Set the new checkout limit for the user. New limit can't be negative and it can't be less than
     * the number of books the user currently has checked out
     **/
    public void setCheckOutLimit(int newLimit) {
        if (newLimit > 0 && newLimit > getNumbBooksCheckedOut())
            this.checkOutLimit = newLimit;
    }

    /**
     * Set a new name for user, if the new name is valid
     **/
    public void setName(String newName) {
        if (newName == null || newName.equals(""))
            return;

        newName = StringHelpers.makeTitleCase(newName);

        if (StringHelpers.isAllLetters(newName))
            this.name = StringHelpers.makeTitleCase(newName);
    }

    /**
     * Returns titles of all books checked out by user
     **/
    public String getBooksCheckedOut() {
        ArrayList<String> books = new ArrayList<>(booksCheckedOut);
        Collections.sort(books);
        return books.toString();
    }

    /**
     * Check out a single book from the library
     **/
    public void checkOutBook(String title) {
        if (title == null || title.equals(""))
            return;

        title = StringHelpers.makeTitleCase(title);
        boolean alreadyHasBook = booksCheckedOut.contains(title);

        if (!alreadyHasBook && booksCheckedOut.size() < checkOutLimit)
            booksCheckedOut.add(title);
    }

    /**
     * Return a book with the given title, if the user has
     * the book checked out, else do nothing.
     **/
    public void returnBook(String title) {
        if (title == null || title.equals(""))
            return;

        title = StringHelpers.makeTitleCase(title);
        booksCheckedOut.remove(title);
    }

    public String toString() {
        return "Id: " + id + ", Name: " + name + ", Checkout Limit: " + checkOutLimit +
                ", Books Checked Out: " + getBooksCheckedOut();
    }
}
