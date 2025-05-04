package mktransit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GuiTest extends Application {

    private double scale = 1.5;
    private final double minScale = 1.65;
    private final double maxScale = 7;

    @Override
    public void start(Stage stage) {

        JsonReader reader = new JsonReader();
        reader.loadJsonData(); // แค่โหลด

        Map<String, Station> stationMap = reader.getStationMap(); // ดึงข้อมูล Station
        PathFinder pathFinder = new PathFinder(stationMap);
        // โหลดสถานีมาจาก JsonReader
        List<Station> stationList = new ArrayList<>(reader.getStationMap().values());
        StationUtil stationUtil = new StationUtil(stationList);
        HBox root = new HBox();

        Scene scene = new Scene(root, 1530, 790);

        // ---------- LEFT ----------
        StackPane leftPane = new StackPane();
        leftPane.setPrefWidth(150);

        // Map image
        Image image = new Image("https://www.bts.co.th/assets/images/yellow-map.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.fitWidthProperty().bind(leftPane.widthProperty().multiply(0.5)); // ปรับขนาดเริ่มต้นอิงขนาด pane
        imageView.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0, 5); -fx-background-radius: 10;");

        // Group ที่ใช้ scale/translate
        Group zoomGroup = new Group(imageView);

        // ตั้งค่าขนาดเริ่มต้นของ zoomGroup ให้เท่ากับ minScale
        zoomGroup.setScaleX(minScale);
        zoomGroup.setScaleY(minScale);
        scale = minScale; // กำหนดค่า scale เริ่มต้นให้เท่ากับ minScale

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
        VBox rightPane = new VBox(20);
        rightPane.setPrefWidth(750);
        rightPane.setStyle("-fx-padding: 50 100 20 100; -fx-alignment: center;"); // Top Right Bottom Left ,Padding
                                                                                  // และจัดให้อยู่ตรงกลาง

        // Logo
        Image logoImage = new Image(
                "https://th.m.wikipedia.org/wiki/%E0%B9%84%E0%B8%9F%E0%B8%A5%E0%B9%8C:BTS-Logo_Gold.svg"); // URL
                                                                                                           // ของโลโก้
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(100); // กำหนดขนาดโลโก้
        logoView.setPreserveRatio(true);

        // Group for Project Name and TextFields
        VBox contentBox = new VBox(15); // ระยะห่างระหว่างองค์ประกอบในกรอบ
        contentBox.setStyle(
                "-fx-border-width: 2; -fx-padding: 0 0 25 0 ; -fx-background-color: #f9f9f9;-fx-alignment: center; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0, 5); -fx-background-radius: 10;"); // กำหนดกรอบและพื้นหลัง

        StackPane bgName = new StackPane();
        bgName.setStyle("-fx-background-color: #003366; -fx-padding: 10;"); // กำหนดกรอบและพื้นหลัง

        // Project Name
        Label projectName = new Label("MK Transit");
        projectName.setStyle("-fx-text-fill: white; -fx-font-size: 50px; -fx-font-weight: bold;");

        // TextField1
        Label inputLabel1 = new Label("Enter Start Station ID:");
        inputLabel1.setStyle("-fx-text-fill: #003366;-fx-font-weight: bold;-fx-font-size: 13px;");
        TextField textField1 = new TextField();
        textField1.setPromptText("Ex. N24");
        textField1.setMaxWidth(60);

        // Label สำหรับแสดงชื่อสถานี
        Label stationName1 = new Label();
        stationName1.setStyle("-fx-text-fill: #003366; -fx-font-size: 13px; -fx-font-style: italic;");

        Circle circleStation1 = new Circle(6);

        // จัด TextField และ Label ในแนวนอน
        HBox textField1Box = new HBox(10); // ระยะห่างระหว่าง TextField และ Label
        textField1Box.setStyle("-fx-alignment: center;"); // จัดให้อยู่ชิดซ้าย
        textField1Box.getChildren().addAll(textField1);

        // เพิ่ม Listener ให้ TextField1
        textField1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                stationName1.setText("");
                textField1.setStyle(""); // ล้างชื่อสถานีหากไม่มีการป้อนข้อมูล
                // textField1Box.getChildren().addAll(textField1, circleStation1, stationName1);
                textField1Box.getChildren().remove(stationName1);
                return;
            }

            String stationId1 = newValue.toUpperCase();
            Station someStation1 = stationMap.get(stationId1); // ดึงข้อมูลสถานีจาก map

            if (!textField1Box.getChildren().contains(stationName1)) {
                textField1Box.getChildren().add(1, stationName1);
            }

            if (someStation1 == null) {
                stationName1.setText("Station not found"); // แสดงข้อความเมื่อไม่พบสถานี
                textField1.setStyle("-fx-border-color: red;"); // เปลี่ยนสีขอบ TextField เป็นสีแดง
                textField1Box.getChildren().remove(circleStation1);
            } else {
                stationName1.setText(someStation1.getName()); // แสดงชื่อสถานี
                textField1.setStyle(""); // ล้างสีขอบ TextField

                if (!textField1Box.getChildren().contains(circleStation1)) {
                    textField1Box.getChildren().add(1, circleStation1); // เพิ่มวงกลมกลับมาในตำแหน่งที่ 2
                }
                
                if (someStation1.getId().equals("CEN")) {
                    // ใช้ LinearGradient สำหรับสีเขียวอ่อนและเขียวเข้ม
                    circleStation1.setFill(new LinearGradient(
                            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.web("#84c469")), // สีเขียวอ่อน
                            new Stop(1, Color.web("#328674")) // สีเขียวเข้ม
                    ));
                } else {
                    // สีปกติสำหรับสถานีอื่น
                    circleStation1.setFill(Color.web(someStation1.getColor()));
                }
            }
        });

        // TextField2
        Label inputLabel2 = new Label("Enter End Station ID:");
        inputLabel2.setStyle("-fx-text-fill: #003366;-fx-font-weight: bold;-fx-font-size: 13px;");
        TextField textField2 = new TextField();
        textField2.setPromptText("Ex. N24");
        textField2.setMaxWidth(60);

        // Label สำหรับแสดงชื่อสถานี
        Label stationName2 = new Label();
        stationName2.setStyle("-fx-text-fill: #003366; -fx-font-size: 13px; -fx-font-style: italic;");

        Circle circleStation2 = new Circle(6);

        // จัด TextField และ Label ในแนวนอน
        HBox textField2Box = new HBox(10); // ระยะห่างระหว่าง TextField และ Label
        textField2Box.setStyle("-fx-alignment: center;"); // จัดให้อยู่ชิดซ้าย
        textField2Box.getChildren().addAll(textField2);

        // เพิ่ม Listener ให้ TextField1
        textField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                stationName2.setText("");
                textField2.setStyle(""); // ล้างชื่อสถานีหากไม่มีการป้อนข้อมูล
                return;
            }

            String stationId2 = newValue.toUpperCase();
            Station someStation2 = stationMap.get(stationId2); // ดึงข้อมูลสถานีจาก map

            if (!textField2Box.getChildren().contains(stationName2)) {
                textField2Box.getChildren().add(1, stationName2);
            }

            if (someStation2 == null) {
                stationName2.setText("Station not found"); // แสดงข้อความเมื่อไม่พบสถานี
                textField2.setStyle("-fx-border-color: red;"); // เปลี่ยนสีขอบ TextField เป็นสีแดง
                textField2Box.getChildren().remove(circleStation2);
            } else {
                stationName2.setText(someStation2.getName()); // แสดงชื่อสถานี
                textField2.setStyle(""); // ล้างสีขอบ TextField

                if (!textField2Box.getChildren().contains(circleStation2)) {
                    textField2Box.getChildren().add(1, circleStation2); // เพิ่มวงกลมกลับมาในตำแหน่งที่ 2
                }
                if (someStation2.getId().equals("CEN")) {
                    // ใช้ LinearGradient สำหรับสีเขียวอ่อนและเขียวเข้ม
                    circleStation2.setFill(new LinearGradient(
                            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.web("#84c469")), // สีเขียวอ่อน
                            new Stop(1, Color.web("#328674")) // สีเขียวเข้ม
                    ));
                } else {
                    // สีปกติสำหรับสถานีอื่น
                    circleStation2.setFill(Color.web(someStation2.getColor()));
                }
            }
        });

        bgName.getChildren().addAll(projectName);

        // วงกลม 3 อัน
        VBox circleBox = new VBox(4); // ระยะห่างระหว่างวงกลม
        circleBox.setStyle("-fx-alignment: center;"); // จัดให้อยู่ตรงกลาง

        Circle circle1 = new Circle(5); // วงกลมขนาดรัศมี 10
        circle1.setStyle("-fx-fill: #003366;"); // สีแดง

        Circle circle2 = new Circle(5); // วงกลมขนาดรัศมี 10
        circle2.setStyle("-fx-fill: #003366;"); // สีเขียว

        Circle circle3 = new Circle(5); // วงกลมขนาดรัศมี 10
        circle3.setStyle("-fx-fill: #003366;"); // สีน้ำเงิน

        // เพิ่มวงกลมเข้าไปใน HBox
        circleBox.getChildren().addAll(circle1, circle2, circle3);

        // Button
        Button submitButton = new Button("Submit");
        submitButton.setStyle(
                "-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; -fx-border-radius: 5; -fx-background-radius: 5;");

        Button clearButton = new Button("Clear");
        clearButton.setStyle(
                "-fx-background-color:rgb(196, 0, 0); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Add action for buttons
        submitButton.setOnAction(event -> {
            String startId = textField1.getText().trim(); // ดึงค่า Start Station ID
            String endId = textField2.getText().trim();   // ดึงค่า End Station ID
        
            if (startId.isEmpty() || endId.isEmpty()) {
                System.out.println("Karuna Krak Hai Krop Tuan!"); // แสดงข้อความเมื่อไม่มีการป้อนข้อมูล
                return;
            }
        
            PathResult result = pathFinder.findShortestPath(startId, endId);
        
            if (result.getFullPath().isEmpty()) {
                System.out.println(" Mai Pop Sen Tang " + startId + " ไปยัง " + endId);
            } else {
                System.out.println(" Jur Sen Tang");
                System.out.println("เส้นทางเดินทั้งหมด:");
        
                for (String stationId : result.getFullPath()) {
                    Station station = stationMap.get(stationId);
                    System.out.println("- " + station.getName() + " (" + station.getId() + ")");
                }
        
                List<String> importantSteps = result.getImportantSteps();
                for (int i = 0; i < importantSteps.size(); i++) {
                    String currentId = importantSteps.get(i);
        
                    if (i == importantSteps.size() - 1 && i > 0 && currentId.equals(importantSteps.get(i - 1))) {
                        continue;
                    }
                    Station station = stationMap.get(currentId);
                    if (i == 0) {
                        System.out.print("\n จุดสำคัญ (Important Steps):\n");
                        System.out.print(stationUtil.IDtoName(startId) + "(" + startId + ")" + " -> ");
                        System.out.print(stationUtil.IDtoName(currentId) + "(" + station.getId() + ")");
                    } else {
                        System.out.print(" -> " + stationUtil.IDtoName(currentId) + "(" + station.getId() + ")");
                    }
                }
                System.out.println();
        
                System.out.println("\n เวลารวมทั้งหมด: " + result.getTotalTime() + " นาที");
            }
        });

        clearButton.setOnAction(event -> {
            textField1.clear();
            textField2.clear();
            textField1Box.getChildren().remove(circleStation1);
            textField2Box.getChildren().remove(circleStation2);
            textField1Box.getChildren().remove(stationName1);
            textField2Box.getChildren().remove(stationName2);
        });

        // Add buttons to an HBox
        HBox buttonBox = new HBox(10); // ระยะห่างระหว่างปุ่ม
        buttonBox.setStyle("-fx-alignment: center;"); // จัดให้อยู่ตรงกลาง
        buttonBox.getChildren().addAll(submitButton, clearButton);

        // Add elements to the contentBox
        contentBox.getChildren().addAll(bgName, inputLabel1, textField1Box, circleBox, inputLabel2,
                textField2Box, buttonBox);

        // Add elements to the rightPane
        rightPane.getChildren().addAll(logoView, contentBox);

        // ---------- Layout ----------
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        root.getChildren().addAll(leftPane, rightPane);

        stage.setTitle("MK Transit");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}