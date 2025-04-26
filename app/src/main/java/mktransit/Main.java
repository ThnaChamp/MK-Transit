package mktransit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        JsonReader reader = new JsonReader();
        List<Line> lines = reader.loadJsonData(); // return List<Line> แทน

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        for (Line line : lines) {
            Label lineLabel = new Label("Line: " + line.getName() + " (" + line.getColor() + ")");
            root.getChildren().add(lineLabel);

            for (Station station : line.getStations()) {
                Label stationLabel = new Label("  Station: " + station.getName() + " (ID: " + station.getId() + ")");
                root.getChildren().add(stationLabel);

                for (Connection conn : station.getConnections()) {
                    Label connLabel = new Label("    → To: " + conn.getTo() + " (" + conn.getTime() + " min)");
                    root.getChildren().add(connLabel);
                }
            }
        }

        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("JSON Viewer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

