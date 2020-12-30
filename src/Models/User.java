package Models;

import Helpers.StringHelpers;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class represents a library user. Each user has a unique id on creation, a name,
 * a list of books they've checked out and a cap on # of books they can check out.
 **/
public class User {
    private static final int defaultCheckOutLimit = 5;
    private final ArrayList<String> booksCheckedOut;
    private int checkOutLimit;
    private String id;
    private String name;

    public User(String name) {
        this.name = name;
        id = StringHelpers.generateRandomId();
        checkOutLimit = defaultCheckOutLimit;
        booksCheckedOut = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCheckOutLimit() {
        return checkOutLimit;
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
     * Update the id for the user
     **/
    public void setId(String newId) {
        this.id = newId;
    }

    /**
     * Set a new name for user, if the new name is valid
     **/
    public void setName(String newName) {
        if (StringHelpers.isNullOrEmptyString(newName))
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
     * Check out a single book from the library. Prevents user from checking
     * out two of the same book. or more books than their limit
     **/
    public void checkOutBook(String title) {
        if (StringHelpers.isNullOrEmptyString(title))
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
        if (StringHelpers.isNullOrEmptyString(title))
            return;

        title = StringHelpers.makeTitleCase(title);
        booksCheckedOut.remove(title);
    }

    /**
     * Returns string in the format:
     * Id: id, Name: name, Checkout Limit: limit, Books Checked Out: books
     **/
    public String toString() {
        return "Id: " + id + ", Name: " + name + ", Checkout Limit: " + checkOutLimit +
                ", Books Checked Out: " + getBooksCheckedOut();
    }

    /**
     * Two users are considered equal if they have the same id
     **/
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;

        User otherUser = (User) obj;
        return getId().equals(otherUser.getId());
    }
}
