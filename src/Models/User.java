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
    private String password;
    private String name;
    private String id;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
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
     * Returns true if the user hasn't reached their checkout limit
     **/
    public boolean canCheckOutMoreBooks() {
        return booksCheckedOut.size() < checkOutLimit;
    }

    /**
     * Returns true if the password passed in is the user's password
     **/
    public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Returns true if new password is successfully set
     **/
    private boolean setPassword(String password) {
        if (StringHelpers.isNullOrEmptyString(password) || !isValidPassword(password))
            return false;

        this.password = password;
        return true;
    }

    /**
     * A password is valid if it's between 6 - 20 characters
     **/
    private boolean isValidPassword(String password) {
        return password.length() < 6 || password.length() > 20;
    }

    /**
     * Update the user's password to newPassword if the
     * oldPassword matches current password.
     **/
    public boolean changePassWord(String oldPassword, String newPassword) {
        if (isCorrectPassword(oldPassword))
            return setPassword(newPassword);

        return false;
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

    public boolean hasBookCheckedOut(String title) {
        return booksCheckedOut.contains(title);
    }

    /**
     * Returns string in the format:
     * Id: id, Name: name, Checkout Limit: limit, Books Checked Out: [books]
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