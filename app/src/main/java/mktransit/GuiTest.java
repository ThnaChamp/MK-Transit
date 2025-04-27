package mktransit;

import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class GuiTest extends Application {

    @Override
    public void start(Stage stage) {
        JsonReader reader = new JsonReader();
        reader.loadJsonData(); // แค่โหลด

        List<Line> lines = reader.getLines(); // ดึงข้อมูล Line
        Map<String, Station> stationMap = reader.getStationMap(); // ดึงข้อมูล Station

        // Station SomeStation = stationMap.get("N8");

        // VBox contentBox = new VBox(10);
        // contentBox.setStyle("-fx-padding: 20; -fx-alignment: top-left;");

        // // Show N8
        // System.out.println(SomeStation.getName());
        // Label A = new Label("Station N8: " + SomeStation.getName());
        // contentBox.getChildren().add(A);

        HBox root = new HBox();

        // ฝั่งซ้าย
        Pane leftPane = new Pane();
        // leftPane.setStyle("-fx-background-color: lightblue;");
        leftPane.setPrefWidth(400);

        Rectangle fakeMap = new Rectangle(400, 400, Color.LIGHTBLUE); // จำลองแผนที่ด้วยสี่เหลี่ยมสีฟ้า
        leftPane.getChildren().add(fakeMap);
        
        // ฝั่งขวา
        VBox rightPane = new VBox();
        rightPane.setStyle("-fx-background-color: lightgreen;");
        rightPane.setPrefWidth(200);
        
        // ข้างในขวา
        Label topPart = new Label("Top part");
        topPart.setStyle("-fx-background-color: lightcoral; -fx-alignment: center;");
        Label bottomPart = new Label("Bottom part");
        bottomPart.setStyle("-fx-background-color: lightyellow; -fx-alignment: center;");
        
        // บังคับให้มันขยาย
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        
        VBox.setVgrow(topPart, Priority.ALWAYS);
        VBox.setVgrow(bottomPart, Priority.ALWAYS);
        
        // ใส่เนื้อหา
        rightPane.getChildren().addAll(topPart, bottomPart);
        root.getChildren().addAll(leftPane, rightPane);
        
        // scene
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

    }
}
