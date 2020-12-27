package Test;

import org.junit.jupiter.api.Test;
import Models.Author;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    Author gatsbyAuthor = new Author("f. scott fitzgerald", "09/24/1996");
    Author cleanCodeAuthor = new Author("Robert C. Martin", "12/05/1952");

    @Test
    void testGettersAndSetters() {
        assertEquals("F. Scott Fitzgerald", gatsbyAuthor.getName());

        String expected = "Name: F. Scott Fitzgerald, Birth Date: 09/24/1996, Books Written: 0";
        assertEquals(expected, gatsbyAuthor.toString());
        assertEquals("09/24/1996", gatsbyAuthor.getBirthDate());

        gatsbyAuthor.setBirthDate("10/11/1111");
        gatsbyAuthor.setBirthDate("01/40/1900");
        assertEquals("10/11/1111", gatsbyAuthor.getBirthDate());

        gatsbyAuthor.setName(null);
        gatsbyAuthor.setName("");
        assertEquals("F. Scott Fitzgerald", gatsbyAuthor.getName());

        assertEquals("[]", gatsbyAuthor.getBooksWritten());

        assertNotEquals(cleanCodeAuthor, gatsbyAuthor);
    }

    @Test
    void testAuthorAddAndRemove() {
        gatsbyAuthor.addBookWritten("the great gatsby");
        assertEquals("[The Great Gatsby]", gatsbyAuthor.getBooksWritten());

        gatsbyAuthor.addBookWritten("tender is the night");
        assertEquals("[Tender Is The Night, The Great Gatsby]", gatsbyAuthor.getBooksWritten());

        assertEquals(2, gatsbyAuthor.getNumbOfBooksWritten());

        gatsbyAuthor.removeBookWritten("the great gatsby");
        assertEquals("[Tender Is The Night]", gatsbyAuthor.getBooksWritten());
    }
}