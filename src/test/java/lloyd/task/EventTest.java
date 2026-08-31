package lloyd.task;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class EventTest {
    
    @Test
    public void Event_newEvent_isNotDone() {
        Event event = new Event("Test event", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        assertFalse(event.isDone());
    }

    @Test
    public void Event_invalidEvent_throwsIllegalArgumentException() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.minusHours(1);
        assertThrows(IllegalArgumentException.class, () -> new Event("Invalid event", from, to));
    }


}
