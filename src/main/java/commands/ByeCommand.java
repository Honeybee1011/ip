package commands;

public class ByeCommand extends Command {
    @Override
    public boolean execute() {
        System.out.println("Bye! seeya again.");
        return false;
    }
}