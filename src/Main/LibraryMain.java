package Main;

import Models.LibraryManager;

import java.io.FileNotFoundException;

public class LibraryMain {
    private static final LibraryManager libManager = LibraryManager.getInstance();

    public static void main(String[] args) throws FileNotFoundException {
        // Populate libManager with data of book and authors
        libManager.startUpManager();
        libManager.startLibrary();
        System.out.println("Shutting down library system.");
    }
}
