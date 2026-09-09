package lloyd.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Displays one user or Lloyd message in the conversation. */
public class DialogBox extends HBox {
    private static final double MESSAGE_WIDTH = 280.0;

    private static final String USER_MESSAGE_STYLE =
            "-fx-background-color: #d8ecff; -fx-background-radius: 8;";
    private static final String LLOYD_MESSAGE_STYLE =
            "-fx-background-color: #eeeeee; -fx-background-radius: 8;";
    private static final String SPEAKER_STYLE =
            "-fx-background-color: #303846; -fx-background-radius: 20;"
                    + " -fx-text-fill: white; -fx-font-weight: bold;";

    /**
     * Creates a dialog box for a message and its speaker.
     *
     * @param text Message to display.
     * @param speaker Short name shown beside the message.
     * @param isUserMessage Whether the message belongs to the user.
     */
    private DialogBox(String text, String speaker, boolean isUserMessage) {
        Label message = new Label(text);
        message.setWrapText(true);
        message.setMaxWidth(MESSAGE_WIDTH);
        message.setPadding(new Insets(10));
        message.setStyle(isUserMessage ? USER_MESSAGE_STYLE : LLOYD_MESSAGE_STYLE);

        Label speakerLabel = new Label(speaker);
        speakerLabel.setAlignment(Pos.CENTER);
        speakerLabel.setMinSize(52.0, 40.0);
        speakerLabel.setStyle(SPEAKER_STYLE);

        setAlignment(isUserMessage ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        setSpacing(8.0);
        setPadding(new Insets(4.0));

        if (isUserMessage) {
            getChildren().addAll(message, speakerLabel);
        } else {
            getChildren().addAll(speakerLabel, message);
        }
    }

    /**
     * Creates a dialog box for text entered by the user.
     *
     * @param text User's message.
     * @return Dialog box aligned to the right.
     */
    public static DialogBox createUserDialog(String text) {
        return new DialogBox(text, "YOU", true);
    }

    /**
     * Creates a dialog box for a response from Lloyd.
     *
     * @param text Lloyd's response.
     * @return Dialog box aligned to the left.
     */
    public static DialogBox createLloydDialog(String text) {
        return new DialogBox(text, "L", false);
    }
}
