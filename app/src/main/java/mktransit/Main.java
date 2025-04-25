package mktransit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // สร้าง object ของ JsonReader และโหลดข้อมูล
        JsonReader reader = new JsonReader();
        reader.loadJsonData();

        // สร้าง GUI ที่โชว์ข้อมูลจาก JSON
        Label nameLabel = new Label("Name: " + reader.getName());
        Label versionLabel = new Label("Version: " + reader.getVersion());

        VBox root = new VBox(10, nameLabel, versionLabel);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("JSON Viewer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // เริ่มรัน JavaFX
    }
}
