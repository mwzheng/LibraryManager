package Helpers;

public class StringHelpers {

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
}
