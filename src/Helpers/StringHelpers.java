package Helpers;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelpers {
    private static final Pattern dateRegex = Pattern
            .compile("^(0[1-9]|1[0-2])\\/(0[1-9]|[1-2][0-9]|3[0-1])\\/([1-2][0-9]{3})$");

    static final String allChars = "0123456789abcdefghijklmnopqrstuvwxyz";
    static final int idLength = 12;
    static SecureRandom random = new SecureRandom();

    /**
     * Checks to see if a string is title case. Title case -> first letter of each word is capitalized
     **/
    public static boolean isTitleCase(String input) {
        if (input == null || input.equals(""))
            return false;

        String[] tokens = input.split(" ");

        for (String tok : tokens) {
            if (!Character.isUpperCase(tok.charAt(0)))
                return false;
        }

        return true;
    }

    /**
     * Capitalizes the first letter of each word in the string and returns it
     **/
    public static String makeTitleCase(String input) {
        if (input == null || input.equals(""))
            return "";

        // Already Title case
        if (isTitleCase(input))
            return input;

        StringBuilder sb = new StringBuilder();
        String[] tokens = input.split(" ");

        for (String tok : tokens)
            sb.append(capitalize(tok)).append(" ");

        return sb.toString().trim();
    }

    /**
     * NOTE: Only Capitalizes the first Letter of the first word.
     * If first letter isn't alphabetic then return input.
     * Capitalize the first letter of a SINGLE word
     **/
    public static String capitalize(String input) {
        if (input == null || input.equals(""))
            return input;

        char firstLetter = input.charAt(0);
        boolean firstCharIsAlphabetic = Character.isAlphabetic(firstLetter);

        if (firstCharIsAlphabetic) {
            if (input.length() == 1)
                return "" + Character.toUpperCase(input.charAt(0));

            return Character.toUpperCase(input.charAt(0)) + input.substring(1);
        }

        return input;
    }

    /**
     * Returns true if the date is in the correct format.
     * NOTE: This does not mean the date is a valid date.
     * Valid years: 1000 - 2999, Valid Months: 01 - 12, Valid Days: 01 - 31
     **/
    public static boolean isValidDateFormat(String date) {
        if (date == null || date.equals(""))
            return false;

        Matcher matcher = dateRegex.matcher(date);
        return matcher.find();
    }

    /**
     * Generates a ransom string to use for Id for users
     * Id has length 12 and can contain letters a - z
     * and digits 0 - 9
     **/
    public static String generateRandomId() {
        StringBuilder sb = new StringBuilder();
        int numbPossibleChars = allChars.length();

        for (int i = 0; i < idLength; i++)
            sb.append(allChars.charAt(random.nextInt(numbPossibleChars)));

        return sb.toString();
    }

    /**
     * Checks to see that the input is made up of only
     * letters of the alphabet or a spaces
     **/
    public static boolean isAllLetters(String input) {
        String[] name = input.split(" ");
        boolean isAllChars;

        for (String partialName : name) {
            isAllChars = partialName.chars().allMatch(Character::isLetter);

            if (!isAllChars)
                return false;
        }

        return true;
    }

    /**
     * Returns true if the input is null or an empty string ("")
     **/
    public static boolean isNullOrEmptyString(String input) {
        return input == null || input.equals("");
    }
}
