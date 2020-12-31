package Models;

import Helpers.StringHelpers;

import java.util.*;

/**
 * Singleton Class. It manages the entire 'Library' system. It handles adding
 * new books and authors to the system. Allows user to look up books and authors
 * within the system.
 **/
public class LibraryManager {
    private static LibraryManager INSTANCE;
    private final HashMap<String, Book> bookMap;
    private final HashMap<String, Author> authorMap;
    private final HashMap<String, User> userMap;

    private LibraryManager() {
        this.authorMap = new HashMap<>();
        this.bookMap = new HashMap<>();
        this.userMap = new HashMap<>();
    }

    /**
     * If there is an active instance of a LibraryManager return it,
     * else make a new one and return it
     **/
    public static LibraryManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new LibraryManager();

        return INSTANCE;
    }

    /**
     * Returns the string representation of book requested if found.
     **/
    public String getBookByTitle(String title) {
        Book requestBook = (Book) getRequestedItem(title, "book");
        return (requestBook == null) ?
                "Sorry invalid search for " + title + "\n" : requestBook.toString();
    }
    
    public int getUniqueBookCount() {
        return bookMap.size();
    }

    public int getUniqueAuthorCount() {
        return authorMap.size();
    }

    public int getTotalUsers() {
        return userMap.size();
    }

    /**
     * Returns string representation of the author requested if found.
     **/
    public String getAuthorByName(String name) {
        Author requestAuthor = (Author) getRequestedItem(name, "author");
        return (requestAuthor == null) ?
                "Sorry invalid search for " + name + "\n" : requestAuthor.toString();
    }

    /**
     * Based on searchFor param, returns either a(n) book/author with
     * the title/name of the requested itemName. Returns null if not found.
     **/
    private Object getRequestedItem(String itemName, String searchFor) {
        if (searchFor == null)
            return null;

        itemName = StringHelpers.makeTitleCase(itemName);
        return (searchFor.equals("author")) ?
                authorMap.get(itemName) : bookMap.get(itemName);
    }

    /**
     * Add a new book to the library. If the book already exist then increase the
     * total copies for the book. Also adds new authors if the authors of the book
     * aren't in the system already. If author already exist add title to author's
     * list of written books.
     **/
    public void addBook(String title, String author, String genre, int totalCopies) {
        if (StringHelpers.isNullOrEmptyString(title) || StringHelpers.isNullOrEmptyString(author) ||
                StringHelpers.isNullOrEmptyString(genre) || totalCopies < 0) {
            System.out.println("Invalid Book Arguments when adding book to library!\n");
            return;
        }

        // Make sure all necessary params are in title case
        title = StringHelpers.makeTitleCase(title);
        author = StringHelpers.makeTitleCase(author);
        genre = StringHelpers.makeTitleCase(genre);

        // Add book to system
        if (!bookMap.containsKey(title)) { // Book doesn't exist, add to library
            bookMap.put(title, new Book(title, author, genre, totalCopies));
        } else if (bookMap.containsKey(title) && totalCopies > 0) { // Book exist already, add copies
            bookMap.get(title).addBookCopies(totalCopies);
            return;
        }

        // Update author map if necessary
        updateAuthorInfo(author, title);
    }

    /**
     * Adds author if they don't exist, else get the author.
     * Then update author's books written with title.
     **/
    private void updateAuthorInfo(String authors, String title) {
        String[] allAuthors = authors.split(", ");
        boolean isExistingAuthor;

        for (String anAuthor : allAuthors) {
            anAuthor = StringHelpers.makeTitleCase(anAuthor);
            isExistingAuthor = authorMap.containsKey(anAuthor);

            // If author exist get author, else make new author
            Author author = (isExistingAuthor) ?
                    authorMap.get(anAuthor) : new Author(anAuthor, null);

            if (!isExistingAuthor) authorMap.put(anAuthor, author);

            // Update author's books written
            author.addBookWritten(title);
        }
    }

    /**
     * Add an author to the library system if they aren't already in the system.
     * If author exist, birth date is unknown & new birthDate is valid, update it.
     **/
    public void addAuthor(String name, String birthDate) {
        if (StringHelpers.isNullOrEmptyString(name)) {
            System.out.println("Invalid name when trying to add author to system.\n");
            return;
        }

        name = StringHelpers.makeTitleCase(name);
        boolean isExistingAuthor = authorMap.containsKey(name);
        Author anAuthor = (isExistingAuthor) ? authorMap.get(name) : new Author(name, birthDate);

        if (isExistingAuthor) { // Update birth date if necessary
            boolean isValidNewDate = StringHelpers.isValidDateFormat(birthDate);
            boolean currBDayUnknown = anAuthor.getBirthDate().equals("Unknown");

            if (currBDayUnknown && isValidNewDate) anAuthor.setBirthDate(birthDate);
            return;
        }

        authorMap.put(name, anAuthor);
    }

    /**
     * Checks if the credentials of a user are valid. Valid if id exist and
     * name and password entered matches the user's name and password in the system
     **/
    public boolean isValidUser(String id, String name, String password) {
        if (id == null || name == null || password == null)
            return false;

        boolean hasUser = userMap.containsKey(id);

        if (hasUser) {
            User theUser = userMap.get(id);
            boolean isCorrectPassword = theUser.isCorrectPassword(password);
            boolean hasSameName = theUser.getName().equals(name);
            return isCorrectPassword && hasSameName;
        }

        return false;
    }

    /**
     * Creates a new Library User with the give name and password.
     * Each user created will have a unique id.
     **/
    public boolean createNewUser(String name, String password) {
        if (name == null || password == null)
            return false;

        name = StringHelpers.makeTitleCase(name);

        User newUser = new User(name, password);
        String id = newUser.getId();

        // If id is not unique, generate new id;
        while (userMap.containsKey(id)) {
            id = StringHelpers.generateRandomId();
            newUser.setId(id);
        }

        userMap.put(id, newUser);
        return true;
    }

    /**
     * Returns all titles currently in the library that includes the given genre
     **/
    public String findBooksByGenre(String genre) {
        if (genre == null || genre.equals(""))
            return getAllBookTitles();

        genre = StringHelpers.capitalize(genre);
        List<String> booksWithGenre = new ArrayList<>();

        for (Book aBook : bookMap.values()) {
            if (aBook.hasGenre(genre))
                booksWithGenre.add(aBook.getTitle());
        }

        Collections.sort(booksWithGenre);
        return booksWithGenre.toString().replace("[", "").replace("]", "");
    }

    /**
     * Returns all the author names in the system currently
     **/
    public String getAllAuthorNames() {
        StringBuilder sb = new StringBuilder();
        List<String> names = new ArrayList<>(authorMap.keySet());
        Collections.sort(names);

        for (String authorName : names)
            sb.append(authorName).append(", ");

        return sb.substring(0, sb.length() - 2);
    }

    /**
     * Returns the titles of all books currently in the system in sorted order
     **/
    public String getAllBookTitles() {
        if (getUniqueBookCount() == 0)
            return "There are no books currently in the system.";

        List<String> books = new ArrayList<>(bookMap.keySet());
        Collections.sort(books);
        return books.toString().replace("[", "").replace("]", "");
    }

    public void printSystemCommands() {
        // TODO
    }
}
