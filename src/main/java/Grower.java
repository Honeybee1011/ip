import java.util.Scanner;
import commands.Command;


public class Grower {
    public static void main(String[] args) {
        System.out.println("Goodday to you, I am Grow-er \n" +
                "your accountability partner! \n" +
                "i'm here to support your growth! \n" +
                "What can I do for you today \n");

        Scanner scanner = new Scanner(System.in);
        boolean continueRun = true;

        while (continueRun) {
            String userInput = scanner.nextLine();

            Command command = Parser.parse(userInput);

            if (command != null) {
                continueRun = command.execute();
            } else {
                System.out.println("Sorry! Didn't get that, this bot ain't so smart.");
            }
        }

        scanner.close();
    }
}
