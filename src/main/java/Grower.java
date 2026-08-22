import java.util.Scanner;
import commands.Command;


public class Grower {
    public static void main(String[] args) {
        System.out.println("Goodday to you, I am Grow-er \n" +
                "your accountability partner! \n" +
                "i'm here to support your growth! \n" +
                "What can I do for you today \n");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String userInput = scanner.nextLine();

            Command command = Parser.parse(userInput);

            if (command != null) {
                command.execute();
            } else if (userInput.equals("bye")) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("Sorry! Didn't get that, this bot ain't so smart.");
            }
        }

        scanner.close();
    }
}
