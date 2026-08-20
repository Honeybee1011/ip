import java.util.Scanner;

/**
 * Starts the Lloyd chatbot application and responds to commands entered by the user.
 */
public class Lloyd {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        String banner = " _      _                 _\n"
                + "| |    | |               | |\n"
                + "| |    | | ___  _   _  __| |\n"
                + "| |    | |/ _ \\| | | |/ _` |\n"
                + "| |____| | (_) | |_| | (_| |\n"
                + "|______|_|\\___/ \\__, |\\__,_|\n"
                + "                 __/ |       \n"
                + "                |___/        ";
        System.out.println(line);
        System.out.println(banner);
        System.out.println(" Hello! I'm Lloyd.");
        System.out.println(" What can I do for you?");
        System.out.println(line);
        System.out.println();
        Scanner scanner = new Scanner(System.in);

        String[] toDoList = new String[100];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if ("bye".equals(command)) {
                break;
            }
            if ("list".equals(command)) {
                System.out.println(line);
                for (int i = 0; i < taskCount; i++) {
                    if (toDoList[i] == null) {
                        break;
                    }
                    System.out.printf(
                            "%d. %s%n", i + 1, toDoList[i]
                    );
                }
                System.out.println(line);
            } else {
                System.out.println(line);
                System.out.println("added: " + command);
                toDoList[taskCount] = command;
                taskCount++;
                System.out.println(line);
                System.out.println();
            }
        }
        System.out.println(line);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}
