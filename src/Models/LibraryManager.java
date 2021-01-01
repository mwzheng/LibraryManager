package Models;

import Helpers.StringHelpers;

import java.io.File;
import java.io.FileNotFoundException;
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
     * Starts to populate the library manager with data from text files
     **/
    public void startUpManager() throws FileNotFoundException {
        System.out.println("Starting up Library system.");
        loadAuthorData();
        loadBookData();
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
            System.out.println("Sorry, you have reached your checkout limit." +
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
        boolean hasBookCheckedOut = returner.hasBookCheckedOut(title);

        if (bookMap.containsKey(title) && hasBookCheckedOut) {
            Book returningBook = bookMap.get(title);
            returningBook.returnBook();
            returner.returnBook(title);
            return;
        } else if (bookMap.containsKey(title) && !hasBookCheckedOut) {
            System.out.println("You didn't check out the book: " + title + "!\n");
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
     * Removes book with given title from the system if it
     * has no copies currently checked out.
     **/
    public void removeBook(User user, String title) {
        title = StringHelpers.makeTitleCase(title);
        Book book = bookMap.get(title);

        if (book == null)
            return;

        boolean noCopiesCheckedOut = book.getCopiesAvailable() == book.getTotalCopies();

        if (noCopiesCheckedOut) {
            bookMap.remove(title);
            return;
        }

        System.out.println("Can't remove book: " + title + " there are copies currently checked out.\n");
    }

    /**
     * Removes author with the given name from the system
     **/
    public void removeAuthor(User user, String name) {
        name = StringHelpers.makeTitleCase(name);
        authorMap.remove(name);
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
        System.out.println(
                "To Search: (search/s), " + "To check user info: (info/i), " + "To Checkout: (checkout/c), " +
                        "To return book: (return/r)" + "To logout: (logout/l), " + "To quit: (quit/q), ");
    }

    /**
     * Load book data into library manager.
     **/
    private void loadBookData() throws FileNotFoundException {
        loadData("books.txt", "Books");
    }

    /**
     * Load author data to library manager.
     **/
    private void loadAuthorData() throws FileNotFoundException {
        loadData("authors.txt", "Authors");
    }

    /**
     * Based on the dataType (Books, or Authors) as you read add book/author
     * Book String format: title - author - genre - copies
     * Author String format: name - birthDate
     **/
    private void loadData(String fileName, String dataType) throws FileNotFoundException {
        File dataFile = new File(fileName);
        Scanner sc = new Scanner(dataFile);
        String[] dataParts;

        while (sc.hasNext()) {
            dataParts = sc.nextLine().split(" - ");

            if (dataType.equals("Books")) {
                addBook(dataParts[0], dataParts[1], dataParts[2], Integer.parseInt(dataParts[3]));
            } else if (dataType.equals("Authors")) {
                addAuthor(dataParts[0], dataParts[1]);
            } else {
                System.out.println("Invalid data type loading into library Manager");
            }
        }
    }

    public void startLibrary() {
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        String userInput;

        User currentUser = getLoginInfo(sc);

        while (run) {
            while (currentUser == null)
                currentUser = getLoginInfo(sc);

            printSystemCommands();
            userInput = sc.nextLine().toLowerCase().strip();

            if (userInput.equals("logout") || userInput.equals("l"))
                currentUser = null;

            if (userInput.equals("q") || userInput.equals("quit"))
                run = false;

            switch (userInput) {
                case "search":
                case "s":
                    searchLibrary(sc);
                    break;
                case "checkout":
                case "c":
                    if (currentUser.canCheckOutMoreBooks()) {
                        System.out.println("What book would you like to check out? (enter the book title)");
                        userInput = sc.nextLine().strip();
                        checkOutBook(currentUser, userInput);
                    } else {
                        System.out.println("You've reached your check out limit please return a book to check out another.");
                    }
                    break;
                case "info":
                case "i":
                    userInfo(sc, currentUser);
                    break;
                case "return":
                case "r":
                    System.out.println("What is the title of the book you would like to return?");
                    userInput = sc.nextLine().toLowerCase().strip();
                    returnBook(currentUser, userInput);
            }
        }
    }

    /**
     * Prompt the user to login or create new user account.
     **/
    public User getLoginInfo(Scanner sc) {
        String name, id = null, password, createUser;
        boolean createNewUser;

        while (true) {
            do {
                System.out.println("Would you like to create a new user? (y/n)");
                createUser = sc.nextLine().toLowerCase();
            } while (!createUser.equals("y") && !createUser.equals("n"));

            createNewUser = createUser.equals("y");

            if (createNewUser)
                System.out.println("Attempting to create new user.");

            System.out.println("Please enter your name.");
            name = sc.nextLine().strip();

            if (!createNewUser) {
                System.out.println("Please enter your id.");
                id = sc.nextLine().strip();
            }

            System.out.println("Please enter your password.");
            password = sc.nextLine().strip();

            boolean isValidPassword = password.length() >= 6 && password.length() <= 20;

            if (createNewUser && isValidPassword) {
                User newUser = new User(name, password);
                System.out.println("Successfully Created new user. Your id is: " + newUser.getId() + "\n");
                userMap.put(newUser.getId(), newUser);
                return null;
            }

            if (isValidUser(id, name, password)) // Correct login info entered
                return userMap.get(id);

            System.out.println("Invalid login information!\n");
        }
    }


    /**
     * Search the library for info based on user input.
     * b - books (t - by title, g - by genre)
     * a - author by name
     **/
    public void searchLibrary(Scanner sc) {
        String userInput;

        do {
            System.out.println("Would you like to search for an author or a book? (a/b)");
            userInput = sc.nextLine().toLowerCase().strip();
        } while (!userInput.equals("a") && !userInput.equals("b"));

        if (userInput.equals("a")) {
            System.out.println("Enter name of author to search for.");
            userInput = sc.nextLine().strip();
            System.out.println(getAuthorByName(userInput));
        } else {
            do {
                System.out.println("Would you like to search for book by title or genre? (t/g)");
                userInput = sc.nextLine().toLowerCase().strip();
            } while (!userInput.equals("t") && !userInput.equals("g"));

            if (userInput.equals("t")) {
                System.out.println("Enter the tile of the book you want to search for.");
                userInput = sc.nextLine().toLowerCase().strip();
                System.out.println(getBookByTitle(userInput));
            } else {
                System.out.println("Enter the genre you're looking for");
                userInput = sc.nextLine().toLowerCase().strip();
                System.out.println(findBooksByGenre(userInput));
            }
        }
    }

    /**
     * Based on user input gives information about the user.
     * i - returns string representation of current user
     * b - returns all books user currently has checked out
     **/
    public void userInfo(Scanner sc, User currentUser) {
        String userInput;

        do {
            System.out.println("What would you like to look up? (i: info, b: books checked out)");
            userInput = sc.nextLine().toLowerCase().strip();
        } while (!userInput.equals("b") && !userInput.equals("i"));

        if (userInput.equals("i")) {
            System.out.println(currentUser);
        } else {
            System.out.println(currentUser.getBooksCheckedOut());
        }
    }
}