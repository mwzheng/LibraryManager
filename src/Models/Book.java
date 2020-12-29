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

    public Book(String title, String authors, String genre, int totalCopies) {
        this.title = title;
        this.totalCopies = this.copiesAvailable = totalCopies;
        authorsList = new HashSet<>();
        genreList = new HashSet<>();
        addToSet(authors, authorsList);
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
     * Returns true if the book is of the given genre.
     **/
    public boolean hasGenre(String genre) {
        if (genre == null)
            return false;

        genre = (StringHelpers.isTitleCase(genre)) ? genre : StringHelpers.capitalize(genre);
        return genreList.contains(genre);
    }

    /**
     * Adds a SINGLE genre to the book's genre list
     **/
    public void addGenre(String genre) {
        if (genre == null || genre.equals(""))
            return;

        genre = StringHelpers.capitalize(genre);
        genreList.add(genre);
    }

    /**
     * Removes a SINGLE genre from the book
     **/
    public void removeGenre(String genre) {
        if (genre == null || genre.equals(""))
            return;

        genre = StringHelpers.capitalize(genre);
        genreList.remove(genre);
    }

    /**
     * Adds a SINGLE author to the book's author list
     **/
    public void addAuthor(String author) {
        if (author == null || author.equals(""))
            return;

        author = StringHelpers.makeTitleCase(author);
        authorsList.add(author);
    }

    /**
     * Removes a SINGLE author from the book
     **/
    public void removeAuthor(String name) {
        if (name == null || name.equals(""))
            return;

        name = StringHelpers.makeTitleCase(name);
        authorsList.remove(name);
    }

    /**
     * Increases the totalCopies and availableCopies by given amount
     **/
    public void addBookCopies(int addCopies) {
        if (addCopies < 0)
            return;

        setTotalCopies(getTotalCopies() + addCopies);
        setCopiesAvailable(getCopiesAvailable() + addCopies);
    }

    /**
     * Returns a string representation of author name(s) in sorted order
     **/
    public String getAuthors() {
        ArrayList<String> authors = new ArrayList<>(authorsList);
        Collections.sort(authors);
        return authors.toString();
    }

    /**
     * Returns a string representation of genre(s) in sorted order
     **/
    public String getGenres() {
        ArrayList<String> genres = new ArrayList<>(genreList);
        Collections.sort(genres);
        return genres.toString();
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
     * Two books are considered equal if they share the same title & author
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
