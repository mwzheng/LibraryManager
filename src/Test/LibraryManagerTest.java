package Test;

import Models.LibraryManager;
import Models.User;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NOTE: must run all test at once since libManager is built as each test runs in order
 **/
class LibraryManagerTest {
    LibraryManager libManger = LibraryManager.getInstance();

    @Before
    public void setUp() {
        String title, author, genre, copies, birthDate, name;
        String[] info;

        String[] bookData = {
                "the cat in the hat - dr. suess - picture, children, fiction - 5",
                "my side of the mountain - jean craighed george - adventure, fiction - 3",
                "the great gatsby - F. Scott Fitzgerald - Historical Fiction, American, Romance - 4",
                "Where the Red Fern Grows - Wilson rawls - adventure, fiction - 2",
                "Frog and Toad are Friends - Arnold Lobel - Fiction, Picture, Children - 4",
        };

        String[] authorData = {
                "F. Scott Fitzgerald - 09/24/1996",
                "Robert C. Martin - 12/05/1952",
                "Dr. Suess - 03/02/1904"
        };

        for (String aBook : bookData) {
            info = aBook.split(" - ");
            title = info[0];
            author = info[1];
            genre = info[2];
            copies = info[3];
            libManger.addBook(title, author, genre, Integer.parseInt(copies));
        }

        for (String anAuthor : authorData) {
            info = anAuthor.split(" - ");
            name = info[0];
            birthDate = info[1];
            libManger.addAuthor(name, birthDate);
        }
    }

    @Test
    void testBasicLibManagerSearches() {
        setUp();

        String children = libManger.findBooksByGenre("children");
        assertEquals("Frog And Toad Are Friends, The Cat In The Hat", children);

        String fiction = "Frog And Toad Are Friends, My Side Of The Mountain, The Cat In The Hat, Where The Red Fern Grows";
        assertEquals(fiction, libManger.findBooksByGenre("fiction"));

        String suess = "Name: Dr. Suess, Birth Date: 03/02/1904, Books Written: [The Cat In The Hat]";
        assertEquals(suess, libManger.getAuthorByName("dr. suess"));

        String notFound = "Sorry invalid search for sam\n";
        assertEquals(notFound, libManger.getAuthorByName("sam"));

        String fern = "Title: Where The Red Fern Grows, Author(s): [Wilson Rawls], Genre(s): [Adventure, Fiction], Total Copies: 2";
        assertEquals(fern, libManger.getBookByTitle("where the red fern grows"));

        String allBookTitles = "Frog And Toad Are Friends, My Side Of The Mountain, The Cat In The Hat, The Great Gatsby, Where The Red Fern Grows";
        assertEquals(allBookTitles, libManger.getAllBookTitles());

        String authors = "Arnold Lobel, Dr. Suess, F. Scott Fitzgerald, Jean Craighed George, Robert C. Martin, Wilson Rawls";
        assertEquals(authors, libManger.getAllAuthorNames());
    }

    @Test
    void testLibManagerBasics() {
        libManger.addBook("the great gatsby", "f. scott fitzgerald", "historical fiction, american, romance", 10);

        String expected = "Title: The Great Gatsby, Author(s): [F. Scott Fitzgerald], Genre(s): [American, Historical Fiction, Romance], Total Copies: 14";
        assertEquals(expected, libManger.getBookByTitle("the great gatsby"));

        libManger.addBook("the lorax", "dr. suess", "picture, children", 3);

        String books = "Frog And Toad Are Friends, My Side Of The Mountain, The Cat In The Hat, The Great Gatsby, The Lorax, Where The Red Fern Grows";
        assertEquals(books, libManger.getAllBookTitles());

        libManger.addBook("diary of the wimpy kid", "jeff kinney", "life, Fiction", 9);
        String authors = "Arnold Lobel, Dr. Suess, F. Scott Fitzgerald, Jean Craighed George, Jeff Kinney, Robert C. Martin, Wilson Rawls";
        assertEquals(authors, libManger.getAllAuthorNames());

        String jeff = "Name: Jeff Kinney, Birth Date: Unknown, Books Written: [Diary Of The Wimpy Kid]";
        assertEquals(jeff, libManger.getAuthorByName("jeff kinney"));

        assertEquals(7, libManger.getUniqueAuthorCount());
        assertEquals(0, libManger.getTotalUsers());
        assertEquals(7, libManger.getUniqueBookCount());
    }

    @Test
    void testLibManagerRemoveMethods() {
        User admin = new User("sam", "password");

        libManger.removeBook(admin, "diary of the wimpy kid");

        String books = "Frog And Toad Are Friends, My Side Of The Mountain, The Cat In The Hat, The Great Gatsby, The Lorax, Where The Red Fern Grows";
        assertEquals(books, libManger.getAllBookTitles());

        libManger.removeBook(admin, "random book");
        assertEquals(books, libManger.getAllBookTitles());

        libManger.removeAuthor(admin, "jeff kinney");
        String authors = "Arnold Lobel, Dr. Suess, F. Scott Fitzgerald, Jean Craighed George, Robert C. Martin, Wilson Rawls";
        assertEquals(authors, libManger.getAllAuthorNames());

        libManger.removeAuthor(admin, "random");
        assertEquals(authors, libManger.getAllAuthorNames());
    }
}