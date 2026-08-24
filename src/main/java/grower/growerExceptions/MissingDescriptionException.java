package grower.growerExceptions;

/**
 * Error representing a missing required description.
 * Eg: for any Tasks
 */

public class MissingDescriptionException extends GrowerException {
    public MissingDescriptionException(String message) {
        super(message);
    }
}