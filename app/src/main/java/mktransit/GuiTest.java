package mktransit;

import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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
        StackPane leftPane = new StackPane(); // ใช้ StackPane เพราะจัดให้อยู่ตรงกลางได้ง่าย
        leftPane.setPrefWidth(500);

        // สร้าง Box ที่อยากให้อยู่กลาง
        Rectangle map = new Rectangle(650, 650, Color.LIGHTGRAY); // อันนี้ไม่เต็มขนาดฝั่งซ้าย
        map.widthProperty().bind(Bindings.min(leftPane.widthProperty().multiply(0.9), leftPane.heightProperty().multiply(0.9)));
        map.heightProperty().bind(Bindings.min(leftPane.widthProperty().multiply(0.9), leftPane.heightProperty().multiply(0.9)));
        leftPane.getChildren().add(map); // เอา rectangle ใส่ใน leftPane

        // ฝั่งขวา
        VBox rightPane = new VBox(10);
        rightPane.setPrefWidth(200);
        // rightPane.setStyle("-fx-background-color: lightgreen;");//BG Color

        Label label1 = new Label("Start Station: ");
        Label label2 = new Label("End Station: ");
        rightPane.getChildren().addAll(label1, label2);

        // บังคับให้ขยาย
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        // ใส่ทุกอย่างใน root
        root.getChildren().addAll(leftPane, rightPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();

    }
}
