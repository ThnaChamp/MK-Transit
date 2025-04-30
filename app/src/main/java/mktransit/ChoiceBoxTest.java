package mktransit;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChoiceBoxTest extends Application {

    @Override
    public void start(Stage stage) {
        // set title for the stage
        stage.setTitle("Nested Dropdown Example");

        // create a container
        VBox container = new VBox(10); // spacing of 10
        container.setStyle("-fx-padding: 10;"); // add padding

        // create the outer ChoiceBox
        ChoiceBox<String> outerChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));

        // create a ContextMenu for the inner dropdown
        ContextMenu innerDropdown = new ContextMenu();

        // add a listener to the outer ChoiceBox
        outerChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // clear previous items in the inner dropdown
                innerDropdown.getItems().clear();

                // update the inner dropdown based on the selected outer option
                switch (newValue) {
                    case "Option 1":
                        innerDropdown.getItems().addAll(
                                new MenuItem("Inner 1A"),
                                new MenuItem("Inner 1B")
                        );
                        break;
                    case "Option 2":
                        innerDropdown.getItems().addAll(
                                new MenuItem("Inner 2A"),
                                new MenuItem("Inner 2B")
                        );
                        break;
                    case "Option 3":
                        innerDropdown.getItems().addAll(
                                new MenuItem("Inner 3A"),
                                new MenuItem("Inner 3B"),
                                new MenuItem("Inner 3C"),
                                new MenuItem("Inner 3D")
                        );
                        break;
                }

                // Replace the outer ChoiceBox with the inner dropdown
                container.getChildren().clear(); // Clear the container
                container.getChildren().add(outerChoiceBox); // Add the outer ChoiceBox back
                innerDropdown.show(outerChoiceBox, outerChoiceBox.getLayoutX(), outerChoiceBox.getLayoutY() + outerChoiceBox.getHeight());
            }
        });

        // Add the ChoiceBox to the container
        container.getChildren().add(outerChoiceBox);

        // create a scene
        Scene scene = new Scene(container, 400, 200);

        // set the scene
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
