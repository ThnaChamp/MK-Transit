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
    private final double minScale = 0.5;
    private final double maxScale = 2.5;

    @Override
    public void start(Stage stage) {
        HBox root = new HBox();

        // ---------- LEFT (Map) ----------
        StackPane leftPane = new StackPane();
        leftPane.setPrefWidth(450);

        // Map image
        Image image = new Image("https://www.bts.co.th/assets/images/yellow-map.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(400); // ขนาดเริ่มต้น

        // Group that will be zoomed
        Group zoomGroup = new Group(imageView);

        // Clip เพื่อไม่ให้ภาพหลุดขอบ
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(leftPane.widthProperty());
        clip.heightProperty().bind(leftPane.heightProperty());
        leftPane.setClip(clip);

        // ซูมเมื่อ scroll
        leftPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double zoomFactor = 1.1;
            double deltaY = event.getDeltaY();

            double oldScale = scale;
            if (deltaY < 0) {
                scale /= zoomFactor;
            } else {
                scale *= zoomFactor;
            }

            scale = Math.max(minScale, Math.min(scale, maxScale));
            double factor = scale / oldScale;

            // หาตำแหน่งที่เมาส์ชี้อยู่
            Bounds bounds = zoomGroup.localToScene(zoomGroup.getBoundsInLocal());
            double dx = event.getSceneX() - (bounds.getMinX() + bounds.getWidth() / 2);
            double dy = event.getSceneY() - (bounds.getMinY() + bounds.getHeight() / 2);

            zoomGroup.setScaleX(scale);
            zoomGroup.setScaleY(scale);
            zoomGroup.setTranslateX(zoomGroup.getTranslateX() - (factor - 1) * dx);
            zoomGroup.setTranslateY(zoomGroup.getTranslateY() - (factor - 1) * dy);

            event.consume();
        });

        leftPane.getChildren().add(zoomGroup);

        // ---------- RIGHT ----------
        VBox rightPane = new VBox(10);
        rightPane.setPrefWidth(400);
        rightPane.setStyle("-fx-padding: 10;");
        rightPane.getChildren().addAll(
            new Label("Start Station: "),
            new Label("End Station: ")
        );

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
