package lloyd.gui;

import java.io.IOException;
import java.nio.file.Path;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lloyd.Lloyd;

/** Displays and coordinates Lloyd's JavaFX user interface. */
public class Main extends Application {
    private static final double WINDOW_WIDTH = 400.0;
    private static final double WINDOW_HEIGHT = 600.0;
    private static final double INPUT_HEIGHT = 55.0;

    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox dialogContainer = new VBox();
    private final TextField userInput = new TextField();
    private final Button sendButton = new Button("Send");

    private Lloyd lloyd;

    /** Builds and displays the main Lloyd window. */
    @Override
    public void start(Stage stage) {
        configureConversationArea();
        configureInputArea();

        AnchorPane mainLayout = createMainLayout();
        Scene scene = new Scene(mainLayout, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(scene);
        stage.setTitle("Lloyd");
        stage.setResizable(false);
        stage.show();

        try {
            lloyd = new Lloyd(Path.of("data", "lloyd.txt"));
            dialogContainer.getChildren().add(
                    DialogBox.createLloydDialog(lloyd.getGreeting()));
        } catch (IOException e) {
            dialogContainer.getChildren().add(DialogBox.createLloydDialog(
                    "I could not load the task file. Check that data/lloyd.txt"
                            + " contains valid task data and can be read."));
            disableInput();
        }
    }

    /** Configures the scrolling list that contains conversation messages. */
    private void configureConversationArea() {
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.setPadding(new Insets(8.0));
        dialogContainer.setSpacing(4.0);

        scrollPane.setContent(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Configures command submission from the text field and button. */
    private void configureInputArea() {
        userInput.setPromptText("Enter a command...");
        userInput.setOnAction(event -> handleUserInput());
        sendButton.setOnAction(event -> handleUserInput());
    }

    /**
     * Creates the anchored layout used by the application window.
     *
     * @return Configured main layout.
     */
    private AnchorPane createMainLayout() {
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, INPUT_HEIGHT);

        AnchorPane.setLeftAnchor(userInput, 0.0);
        AnchorPane.setRightAnchor(userInput, 70.0);
        AnchorPane.setBottomAnchor(userInput, 0.0);
        userInput.setPrefHeight(INPUT_HEIGHT);

        AnchorPane.setRightAnchor(sendButton, 0.0);
        AnchorPane.setBottomAnchor(sendButton, 0.0);
        sendButton.setPrefSize(70.0, INPUT_HEIGHT);
        return mainLayout;
    }

    /** Sends one nonblank command to Lloyd and displays both sides of the exchange. */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String response = lloyd.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.createUserDialog(input),
                DialogBox.createLloydDialog(response));
        userInput.clear();

        if (lloyd.isExitRequested()) {
            disableInput();
        }
    }

    /** Prevents more commands from being entered after exit or startup failure. */
    private void disableInput() {
        userInput.setDisable(true);
        sendButton.setDisable(true);
    }
}
