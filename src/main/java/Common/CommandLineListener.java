package Common;

import java.util.Scanner;

public class CommandLineListener {
    private Scanner scanner;

    public CommandLineListener() {
        this.scanner = new Scanner(System.in);
    }

    public String getMsgFromConsole(){
        return scanner.nextLine();
    }

}
