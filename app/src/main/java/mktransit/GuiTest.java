package mktransit;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GuiTest extends Application {

    private double scale = 1.0;
    private final double minScale = 1.5;
    private final double maxScale = 7;

    @Override
    public void start(Stage stage) {
        HBox root = new HBox();

        // ---------- LEFT ----------
        StackPane leftPane = new StackPane();
        leftPane.setPrefWidth(300);

        // Map image
        Image image = new Image("https://www.bts.co.th/assets/images/yellow-map.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.fitWidthProperty().bind(leftPane.widthProperty().multiply(0.6)); // ปรับขนาดเริ่มต้นอิงขนาด pane

        // Group ที่ใช้ scale/translate
        Group zoomGroup = new Group(imageView);

        // Clip กำหนดขอบไม่ให้ภาพหลุด
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(leftPane.widthProperty());
        clip.heightProperty().bind(leftPane.heightProperty());
        leftPane.setClip(clip);

        final double[] mouseAnchorX = new double[1];
        final double[] mouseAnchorY = new double[1];
        final double[] translateAnchorX = new double[1];
        final double[] translateAnchorY = new double[1];

        // Handle dragging
        zoomGroup.setOnMousePressed(event -> {
            mouseAnchorX[0] = event.getSceneX();
            mouseAnchorY[0] = event.getSceneY();
            translateAnchorX[0] = zoomGroup.getTranslateX();
            translateAnchorY[0] = zoomGroup.getTranslateY();
        });

        zoomGroup.setOnMouseDragged(event -> {
            if (scale > 5.0) {
                double deltaX = event.getSceneX() - mouseAnchorX[0];
                double deltaY = event.getSceneY() - mouseAnchorY[0];
                zoomGroup.setTranslateX(translateAnchorX[0] + deltaX);
                zoomGroup.setTranslateY(translateAnchorY[0] + deltaY);
            }
        });

        // ซูมเฉพาะเมื่อเมาส์อยู่บนรูป
        imageView.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1.1;
            double deltaY = event.getDeltaY();

            double oldScale = scale;
            if (deltaY < 0) {
                // ซูมออก: fix กลับศูนย์กลาง
                scale /= zoomFactor;
            } else {
                // ซูมเข้า: ซูมตามเมาส์
                scale *= zoomFactor;
            }

            scale = Math.max(minScale, Math.min(scale, maxScale));
            double factor = scale / oldScale;

            zoomGroup.setScaleX(scale);
            zoomGroup.setScaleY(scale);

            if (deltaY < 0) {
                // ซูมออก: reset กลับศูนย์กลาง
                zoomGroup.setTranslateX(0);
                zoomGroup.setTranslateY(0);
            } else {
                // ซูมเข้า: zoom ตามเมาส์
                Bounds bounds = zoomGroup.localToScene(zoomGroup.getBoundsInLocal());
                double dx = event.getSceneX() - (bounds.getMinX() + bounds.getWidth() / 2);
                double dy = event.getSceneY() - (bounds.getMinY() + bounds.getHeight() / 2);

                zoomGroup.setTranslateX(zoomGroup.getTranslateX() - (factor - 1) * dx);
                zoomGroup.setTranslateY(zoomGroup.getTranslateY() - (factor - 1) * dy);
            }

            event.consume();
        });

        leftPane.getChildren().add(zoomGroup);

        // ---------- RIGHT ----------
        VBox rightPane = new VBox(10);
        rightPane.setPrefWidth(700);
        rightPane.setStyle("-fx-padding: 10;");
        rightPane.getChildren().addAll(
                new Label("Start Station: "),
                new Label("End Station: "));

        // ---------- Layout ----------
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        root.getChildren().addAll(leftPane, rightPane);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Zoomable Map");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
