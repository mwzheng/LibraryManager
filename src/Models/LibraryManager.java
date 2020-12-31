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

    public int getUniqueBookCount() {
        return bookMap.size();
    }

    public int getUniqueAuthorCount() {
        return authorMap.size();
    }

    public int getTotalUsers() {
        return userMap.size();
    }

    private boolean bookIsAvailableForCheckOut(String title) {
        title = StringHelpers.makeTitleCase(title);

        if (bookMap.containsKey(title)) {
            Book requestBook = bookMap.get(title);
            return requestBook.getCopiesAvailable() > 0;
        }

        System.out.println("Sorry the book: " + title + " is not in the system.\n");
        return false;
    }

    /**
     * Adds book to user's checked out list if they can check our more book.
     * If successful, then also decrements copy of the book available by 1.
     **/
    public void checkOutBook(User requester, String title) {
        if (requester == null || StringHelpers.isNullOrEmptyString(title))
            return;

        if (!requester.canCheckOutMoreBooks()) {
            System.out.println("Sorry, you have reached your checkout limit.\n" +
                    "Please return a book to check out another.\n");
            return;
        }

        title = StringHelpers.makeTitleCase(title);

        if (!bookIsAvailableForCheckOut(title)) {
            System.out.println("Sorry the book: " + title + " is currently unavailable for checkout.\n");
            return;
        }

        // Check out book
        requester.checkOutBook(title);
        Book bookCheckedOut = bookMap.get(title);
        bookCheckedOut.checkOutBook();
    }

    /**
     * Library user is returning a book with the given title.
     * If the book doesn't exist in system output message and do nothing.
     **/
    public void returnBook(User returner, String title) {
        title = StringHelpers.makeTitleCase(title);

        if (bookMap.containsKey(title)) {
            Book returningBook = bookMap.get(title);
            returningBook.returnBook();
            returner.returnBook(title);
            return;
        }

        System.out.println("The book: " + title + " is not from this library!\n");
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
     * Returns the string representation of book requested if found.
     **/
    public String getBookByTitle(String title) {
        Book requestBook = (Book) getRequestedItem(title, "book");
        return (requestBook == null) ?
                "Sorry invalid search for " + title + "\n" : requestBook.toString();
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

        title = StringHelpers.makeTitleCase(title);
        author = StringHelpers.makeTitleCase(author);
        genre = StringHelpers.makeTitleCase(genre);

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

        // Get author if they exist or make new author
        Author anAuthor = (isExistingAuthor) ? authorMap.get(name) : new Author(name, birthDate);

        if (isExistingAuthor) { // If author exist update birth date if necessary
            boolean isValidNewDate = StringHelpers.isValidDateFormat(birthDate);
            boolean currBDayUnknown = anAuthor.getBirthDate().equals("Unknown");

            if (currBDayUnknown && isValidNewDate) anAuthor.setBirthDate(birthDate);
            return;
        }

        authorMap.put(name, anAuthor);
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

            author.addBookWritten(title);
        }
    }

    /**
     * Returns all titles currently in the library that includes the given genre.
     * Note: Only searches for a single genre at a time. If null or "" passed in for genre,
     * then it returns all the book titles in the system.
     **/
    public String findBooksByGenre(String genre) {
        if (genre == null || genre.equals(""))
            return getAllBookTitles();

        genre = StringHelpers.capitalize(genre);
        Set<String> booksWithGenre = new HashSet<>();

        for (Book aBook : bookMap.values()) {
            if (aBook.hasGenre(genre))
                booksWithGenre.add(aBook.getTitle());
        }

        return getAllKeys(booksWithGenre);
    }

    /**
     * Returns all the author names in the system currently
     **/
    public String getAllAuthorNames() {
        return getAllKeys(authorMap.keySet());
    }

    /**
     * Returns the titles of all books currently in the system in sorted order
     **/
    public String getAllBookTitles() {
        return getAllKeys(bookMap.keySet());
    }

    /**
     * Returns a string representation of all the keys from the set in sorted order.
     **/
    private String getAllKeys(Set<String> set) {
        if (set.size() == 0)
            return "There is no information currently available.";

        List<String> tempList = new ArrayList<>(set);
        Collections.sort(tempList);
        return tempList.toString().replace("[", "").replace("]", "");
    }

    /**
     * Checks if the credentials of a user are valid. Valid if id exist and
     * name and password entered matches the user's name and password in the system
     **/
    public boolean isValidUser(String id, String name, String password) {
        if (id == null || name == null || password == null)
            return false;

        boolean userExist = userMap.containsKey(id);

        if (userExist) {
            User theUser = userMap.get(id);
            boolean hasSameName = theUser.getName().equals(name);
            boolean isCorrectPassword = theUser.isCorrectPassword(password);
            return hasSameName && isCorrectPassword;
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

        if (password.length() < 6 || password.length() > 20) {
            System.out.println("Invalid password. Must be between 6 - 12 characters.\n");
            return false;
        }

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
     * Prints all the input commands valid for the library.
     **/
    public void printSystemCommands() {
        // TODO
    }
}