package Test;

import Models.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    Book calcTextBook = new Book("Calculus Textbook",
            "Robert C. Martin, Franklin D. Demana, Bet K. Waits, Daniel Kennedy", "Mathematics, Educational", 2);
    Book gatsby = new Book("The Great Gatsby", "F. Scott Fitzgeral", "Historical Fiction, American, Romance", 10);
    Book cleanCode = new Book("Clean Code", "Robert C. Martin", "Educational", 2);
    Book giver = new Book("The Giver", "Lowis Lowry", "Science Fiction, Dystopian Fiction", 3);
    Book redFern = new Book("Where the Red Fern Grows", "Wilson Rawls", "Adventure, Fiction", 6);
    Book mountain = new Book("My Side of the Mountain", "Jean Craighed George", "Adventure, Fiction", 4);
    Book cat = new Book("The Cat in the Hat", "Doctor Suess", "Children, Fiction, Picture", 10);
    Book eggs = new Book("Green Eggs and Ham", "Doctor Suess", "Picture, Children, Fiction", 3);
    Book lorax = new Book("The Lorax", "Doctor Suess", "Children, Fiction, Picture", 5);
    Book frog = new Book("Frog and Toad are Friends", "Arnold Lobel", "Fiction, Picture, Children", 4);
    Book wimp = new Book("Diary of the Wimpy Kid");

    @Test
    void bookTestGettersAndSetters() {
        assertEquals(2, calcTextBook.getTotalCopies());
        assertEquals("Calculus Textbook", calcTextBook.getTitle());

        calcTextBook.addBookCopies(-1);
        assertEquals(2, calcTextBook.getTotalCopies());

        calcTextBook.addBookCopies(5);
        assertEquals(7, calcTextBook.getTotalCopies());

        assertEquals("[Bet K. Waits, Daniel Kennedy, Franklin D. Demana, Robert C. Martin]", calcTextBook.getAuthors());
        assertEquals("[Educational, Mathematics]", calcTextBook.getGenres());

        assertEquals(1, wimp.getCopiesAvailable());
        assertEquals("Title: Diary Of The Wimpy Kid, Author(s): Unknown, Genre(s): Unknown, Total Copies: 1", wimp.toString());
    }

    @Test
    void bookTestToStringAndEquals() {
        String toStringOutput = "Title: Calculus Textbook, Author(s): [Bet K. Waits, Daniel Kennedy, Franklin D. Demana, Robert C. Martin]," +
                " Genre(s): [Educational, Mathematics], Total Copies: 2";

        assertEquals(toStringOutput, calcTextBook.toString());

        toStringOutput = "Title: Frog And Toad Are Friends, Author(s): [Arnold Lobel], Genre(s): [Children, Fiction, Picture], Total Copies: 4";
        assertEquals(toStringOutput, frog.toString());

        toStringOutput = "Title: The Cat In The Hat, Author(s): [Doctor Suess], Genre(s): [Children, Fiction, Picture], Total Copies: 10";
        assertEquals(toStringOutput, cat.toString());

        assertEquals(calcTextBook, calcTextBook);

        assertNotEquals(gatsby, cleanCode);
    }

    @Test
    void BookTestAddingAndRemoving() {
        calcTextBook.addBookCopies(-1);
        assertEquals(2, calcTextBook.getTotalCopies());

        calcTextBook.addBookCopies(5);
        assertEquals(7, calcTextBook.getTotalCopies());

        wimp.addGenre(null);
        wimp.addAuthor("");
        wimp.addGenre("children");
        wimp.addGenre("Children");
        assertEquals("[Children]", wimp.getGenres());

        wimp.addAuthor("jeff kinney");
        wimp.addAuthor("");
        wimp.addAuthor(null);
        wimp.addAuthor("Jeff Kinney");
        assertEquals("[Jeff Kinney]", wimp.getAuthors());
    }
}