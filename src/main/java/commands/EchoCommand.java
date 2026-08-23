package commands;

public class EchoCommand extends Command {
    private final String textToEcho;

    public EchoCommand(String textToEcho) {
        this.textToEcho = textToEcho;
    }

    @Override
    public boolean execute() {
        System.out.println(this.textToEcho);
        return true;
    }
}