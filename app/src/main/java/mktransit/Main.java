package mktransit;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        JsonReader reader = new JsonReader();
        reader.loadJsonData(); // แค่โหลด

        List<Line> lines = reader.getLines(); // ดึงข้อมูลมาใช้

        VBox contentBox = new VBox(10);
        contentBox.setStyle("-fx-padding: 20; -fx-alignment: top-left;");

        if (lines.isEmpty()) {
            contentBox.getChildren().add(new Label("No data loaded."));
        } else {
            for (Line line : lines) {
                Label lineLabel = new Label("🚈 Line: " + line.getName() + " (" + line.getColor() + ")");
                contentBox.getChildren().add(lineLabel);

                for (Station station : line.getStations()) {
                    Label stationLabel = new Label("  🏙 " + station.getName() + " (ID: " + station.getId() + ")");
                    contentBox.getChildren().add(stationLabel);

                    for (Connection conn : station.getConnections()) {
                        Label connLabel = new Label("    ↳ To: " + conn.getTo() + " (" + conn.getTime() + " min)");
                        contentBox.getChildren().add(connLabel);
                    }
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 800, 700);
        stage.setTitle("Bangkok Transit Viewer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
