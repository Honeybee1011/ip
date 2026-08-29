/**
 * Represents an event that occurs during a specified period.
 */

public class Event extends Task{
    private final String from;
    private final String to;

    public Event (String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns when the event starts.
     *
     * @return event start entered by the user
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns when the event ends.
     *
     * @return event end entered by the user
     */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from
                + " to: " + to + ")";
    }
}
