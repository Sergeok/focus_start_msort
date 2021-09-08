package entity;

import java.util.Scanner;

public class NamedScanner {

    private final String filename;
    private final Scanner scanner;

    public NamedScanner(String filename, Scanner scanner) {
        this.filename = filename;
        this.scanner = scanner;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public String getFilename() {
        return filename;
    }

}
