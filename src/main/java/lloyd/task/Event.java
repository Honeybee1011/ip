package lloyd.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents an event that occurs during a specified period.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a", Locale.ENGLISH);

    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        if (to.isBefore(from)) {
            throw new IllegalArgumentException(
                    "Event end date and time cannot be before its start");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Returns when the event starts.
     *
     * @return event start date and time
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns when the event ends.
     *
     * @return event end date and time
     */
    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from.format(DISPLAY_FORMAT)
                + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }
}
