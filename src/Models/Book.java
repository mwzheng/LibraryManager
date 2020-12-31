package Models;

import Helpers.StringHelpers;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class for a book object. A book object consists of: a title, the author(s),
 * the genres, copies available, total copies.
 **/
public class Book {
    private final String title;
    private int totalCopies;
    private int copiesAvailable;

    // Use hashset so that dupes aren't inserted
    private final HashSet<String> authorsList;
    private final HashSet<String> genreList;

    public Book(String title) {
        this.title = title;
        totalCopies = copiesAvailable = 1;
        authorsList = new HashSet<>();
        genreList = new HashSet<>();
    }

    /**
     * Book Constructor. Title, genres and authors should be passed in using title case
     * (Ex: Author - Dr. Suess, Title - The Cat In The Hat, Genres - Fiction, Children)
     **/
    public Book(String title, String authors, String genre, int totalCopies) {
        this.title = title;
        this.totalCopies = this.copiesAvailable = totalCopies;
        authorsList = new HashSet<>();
        genreList = new HashSet<>();

        if (!StringHelpers.isNullOrEmptyString(authors))
            addToSet(authors, authorsList);

        if (!StringHelpers.isNullOrEmptyString(genre))
            addToSet(genre, genreList);
    }

    /**
     * Helper method for constructor. Split up list by delimiter ','
     * then trim each string and add it to proper collection
     **/
    private void addToSet(String listToAdd, Collection<String> placeToAdd) {
        List<String> tempList;
        String[] tempArray;

        listToAdd = StringHelpers.makeTitleCase(listToAdd);
        tempArray = listToAdd.split(",");
        tempList = Arrays.stream(tempArray).map(String::trim).collect(Collectors.toList());
        placeToAdd.addAll(tempList);
    }

    public String getTitle() {
        return title;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    /**
     * Called when user returns a copy of the book
     **/
    public void returnBook() {
        copiesAvailable++;
    }

    /**
     * Called when a user checks out a copy of the book.
     **/
    public void checkOutBook() {
        copiesAvailable--;
    }

    /**
     * Increases the totalCopies and availableCopies by given amount
     * AddCopies must be greater than 0, else do nothing
     **/
    public void addBookCopies(int addCopies) {
        if (addCopies < 0)
            return;

        setTotalCopies(getTotalCopies() + addCopies);
        setCopiesAvailable(getCopiesAvailable() + addCopies);
    }

    /**
     * Returns true if the book is of the given genre.
     **/
    public boolean hasGenre(String genre) {
        if (genre == null)
            return false;

        genre = (StringHelpers.isTitleCase(genre)) ? genre : StringHelpers.capitalize(genre);
        return genreList.contains(genre);
    }

    /**
     * Adds a SINGLE author to the book's author list if it's new.
     **/
    public void addAuthor(String author) {
        addTo(author, authorsList);
    }

    /**
     * Adds a SINGLE genre to the book's genre list if it's new.
     **/
    public void addGenre(String genre) {
        addTo(genre, genreList);
    }

    /**
     * Adds element (converted to title case if not already) to the given set.
     **/
    private void addTo(String element, HashSet<String> set) {
        if (StringHelpers.isNullOrEmptyString(element))
            return;

        element = StringHelpers.makeTitleCase(element);
        set.add(element);
    }

    /**
     * Removes a SINGLE genre from the book if it's included in the genreList
     **/
    public void removeGenre(String genre) {
        removeFromSet(genre, genreList);
    }

    /**
     * Removes a SINGLE author from the book, if it's in the authorList.
     **/
    public void removeAuthor(String name) {
        removeFromSet(name, authorsList);
    }

    /**
     * If element is null or "", then do nothing, else remove it from the given set.
     * Element is converted to title case before removing from set.
     **/
    private void removeFromSet(String element, HashSet<String> set) {
        if (StringHelpers.isNullOrEmptyString(element))
            return;

        element = StringHelpers.makeTitleCase(element);
        set.remove(element);
    }

    /**
     * Returns a Array string representation of author name(s) in sorted order
     **/
    public String getAuthors() {
        return getSortedList(authorsList);
    }

    /**
     * Returns a Array string representation of genre(s) in sorted order
     **/
    public String getGenres() {
        return getSortedList(genreList);
    }

    /**
     * Given a set, sort it then return string representation of it.
     **/
    private String getSortedList(HashSet<String> set) {
        ArrayList<String> tempList = new ArrayList<>(set);
        Collections.sort(tempList);
        return tempList.toString();
    }

    /**
     * Returns a book object in the following string format:
     * Tile: title, Author(s): authors, Genre(s): genres, Total Copies: copies
     **/
    public String toString() {
        String authors = authorsList.isEmpty() ? "Unknown" : getAuthors();
        String genres = genreList.isEmpty() ? "Unknown" : getGenres();

        return "Title: " + title + ", Author(s): " + authors + ", Genre(s): " +
                genres + ", Total Copies: " + totalCopies;
    }

    /**
     * Two books are considered equal if they share the same title & author name
     **/
    public boolean equals(Object obj) {
        if (!(obj instanceof Book))
            return false;

        Book otherBook = (Book) obj;
        boolean sameTitle = this.title.equals(otherBook.getTitle());
        boolean sameAuthor = this.getAuthors().equals(otherBook.getAuthors());
        return sameTitle && sameAuthor;
    }
}