package Test;

import org.junit.jupiter.api.Test;
import Helpers.StringHelpers;

import static org.junit.jupiter.api.Assertions.*;

class StringHelpersTest {
    @Test
    void testCapitalize() {
        assertEquals("Hello", StringHelpers.capitalize("hello"));

        String shouldReturnThis = "!should return this original string";
        assertEquals(shouldReturnThis, StringHelpers.capitalize(shouldReturnThis));

        assertNull(StringHelpers.capitalize(null));

        assertEquals("", StringHelpers.capitalize(""));
    }

    @Test
    void isTitleCaseTest() {
        assertFalse(StringHelpers.isTitleCase(null));
        assertFalse(StringHelpers.isTitleCase(""));
        assertFalse(StringHelpers.isTitleCase("hello Not completely Title case"));
        assertFalse(StringHelpers.isTitleCase("nO lONGER tITLE cASE"));
        assertFalse(StringHelpers.isTitleCase("-no"));
        assertTrue(StringHelpers.isTitleCase("This Should Be In Title Case"));
        assertTrue(StringHelpers.isTitleCase("THIS SHOULD STILL BE TITLE CASE"));
    }

    @Test
    void makeTitleCase() {
        assertEquals("", StringHelpers.makeTitleCase(""));
        assertEquals("", StringHelpers.makeTitleCase(null));

        String input = "make this string Title case";
        assertEquals("Make This String Title Case", StringHelpers.makeTitleCase(input));

        input = "-nothing to say";
        assertEquals("-nothing To Say", StringHelpers.makeTitleCase(input));
    }

    @Test
    void testIsValidDateFormat() {
        assertTrue(StringHelpers.isValidDateFormat("10/12/1991"));
        assertTrue(StringHelpers.isValidDateFormat("02/31/1000"));
        assertTrue(StringHelpers.isValidDateFormat("05/12/1300"));
        assertFalse(StringHelpers.isValidDateFormat(""));
        assertFalse(StringHelpers.isValidDateFormat(null));
        assertFalse(StringHelpers.isValidDateFormat("00/10/1000"));
        assertFalse(StringHelpers.isValidDateFormat("14/10/1999"));
        assertFalse(StringHelpers.isValidDateFormat("04/11/3000"));
    }
}