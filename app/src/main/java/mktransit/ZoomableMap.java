package mktransit;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ZoomableMap extends Application {

    private static final double MAP_WIDTH = 1000;
    private static final double MAP_HEIGHT = 1000;

    private double scale = 1.0;

    @Override
    public void start(Stage stage) {
        // Load image
        Image image = new Image("https://www.bts.co.th/assets/images/yellow-map.jpg"); // เปลี่ยนเป็น path ของคุณ
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        // Layer ปุ่ม
        Pane buttonLayer = new Pane();
        buttonLayer.setPickOnBounds(false);

        // ตัวอย่างปุ่ม
        Button siam = new Button("Siam");
        double siamX = 0.5 * MAP_WIDTH;
        double siamY = 0.3 * MAP_HEIGHT;
        siam.setLayoutX(siamX - 20);
        siam.setLayoutY(siamY - 10);
        buttonLayer.getChildren().add(siam);

        // Group รวมภาพกับปุ่มไว้ด้วยกัน
        Group zoomGroup = new Group();
        zoomGroup.getChildren().addAll(imageView, buttonLayer);

        // ใส่ใน StackPane
        StackPane root = new StackPane(zoomGroup);
        Scene scene = new Scene(root, 800, 600);

        // Scroll Zoom
        scene.addEventFilter(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            double zoomFactor = 1.05;

            if (delta < 0) {
                scale /= zoomFactor;
            } else {
                scale *= zoomFactor;
            }

            zoomGroup.setScaleX(scale);
            zoomGroup.setScaleY(scale);
            event.consume();
        });

        stage.setScene(scene);
        stage.setTitle("Zoomable Map");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
