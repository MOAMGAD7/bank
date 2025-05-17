package com.banking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database tables
        database_BankSystem.createTables();

        // تحميل ملف FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/Home.fxml"));
        Parent rootNode = fxmlLoader.load();

        // تحميل صورة الخلفية
        Image backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
        ImageView backgroundView = new ImageView(backgroundImage);

        // أبعاد الشاشة
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // ضبط صورة الخلفية
        backgroundView.setFitWidth(screenWidth);
        backgroundView.setFitHeight(screenHeight);
        backgroundView.setPreserveRatio(false);
        backgroundView.setEffect(new GaussianBlur(20));

        // طبقة زرقاء شفافة
        Region blueOverlay = new Region();
        blueOverlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 120, 255, 0.2), CornerRadii.EMPTY, Insets.EMPTY)));
        blueOverlay.setEffect(new GaussianBlur(20));
        blueOverlay.setPrefSize(screenWidth, screenHeight);

        // StackPane يحتوي كل العناصر
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundView, blueOverlay, rootNode);

        // إنشاء المشهد
        Scene scene = new Scene(stackPane, 1124, 700);

        // تحميل ملف CSS (تعديل حسب الملف الذي تريد استخدامه)
        scene.getStylesheets().add(getClass().getResource("/com/example/maged/Style.css").toExternalForm());

        // إعدادات النافذة
        primaryStage.setTitle("Bank");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/bank-icon.jpeg")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
