package Test;

import Models.User;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user1 = new User("Sam");

    @Test
    void testUserGetterAndSetters() {
        assertEquals("Sam", user1.getName());
        assertEquals(5, user1.getCheckOutLimit());

        user1.setCheckOutLimit(10);
        assertEquals(10, user1.getCheckOutLimit());

        user1.setName("samuel");
        user1.setName("-  adfasdf");
        assertEquals("Samuel", user1.getName());

        user1.setCheckOutLimit(-3);
        assertEquals(10, user1.getCheckOutLimit());
    }

    @Test
    void testAddAndRemove() {
        user1.checkOutBook("the cat in the hat");
        user1.checkOutBook("my side of the mountain");
        user1.checkOutBook("my side of the mountain");
        assertEquals("[My Side Of The Mountain, The Cat In The Hat]", user1.getBooksCheckedOut());
        assertEquals(2, user1.getNumbBooksCheckedOut());

        user1.setCheckOutLimit(5);
        assertEquals(5, user1.getCheckOutLimit());

        user1.returnBook("the cat in the hat");
        assertEquals("[My Side Of The Mountain]", user1.getBooksCheckedOut());
    }

    @Test
    void testUniqueUserId() {
        String[] names = {"bob", "henry", "alice", "jay", "luke", "kelly", "jacob", "lizzy"};
        HashSet<String> ids = new HashSet<>();
        boolean added;

        for (String name : names) {
            User newUser = new User(name);
            added = ids.add(newUser.getId());
            assertTrue(added);
        }
    }
}