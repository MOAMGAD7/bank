package com.banking;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangePasswordController {

    @FXML private TextField newPasswordField;
    @FXML private TextField confirmPasswordField;
    @FXML private Label errorLabel;

    @FXML
    protected void handleChangePassword(ActionEvent event) throws IOException {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        UserSession session = UserSession.getInstance();
        String username = session.getUsername();

        // التحقق من أن كلمات المرور متطابقة
        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        if (newPassword.isEmpty()) {
            errorLabel.setText("Please enter a new password");
            return;
        }

        // تحديث كلمة المرور في قاعدة البيانات (افتراضي)
        if (database_BankSystem.updatePassword(username, newPassword)) {
            // إذا تم تغيير كلمة المرور بنجاح، نغير حالة إعادة التعيين
            session.setPasswordReset(false); // السطر 40: إعادة تعيين الحالة بعد التغيير

            // تحميل صفحة Login
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/maged/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // تحميل خلفية الصورة
            Image backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
            ImageView backgroundView = new ImageView(backgroundImage);
            
            // الحصول على أبعاد الشاشة
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();
            
            // ضبط الخلفية لتناسب الشاشة
            backgroundView.setFitWidth(screenWidth);
            backgroundView.setFitHeight(screenHeight);
            backgroundView.setPreserveRatio(false);
            backgroundView.setEffect(new GaussianBlur(20));
            
            // إنشاء طبقة زرقاء شفافة
            Region blueOverlay = new Region();
            blueOverlay.setBackground(new Background(new BackgroundFill(
                    Color.rgb(0, 120, 255, 0.2),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
            blueOverlay.setEffect(new GaussianBlur(20));
            blueOverlay.setPrefSize(screenWidth, screenHeight);
            
            // تجميع الخلفية والطبقة الشفافة وواجهة المستخدم
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(backgroundView, blueOverlay, root);
            
            // إنشاء المشهد
            Scene scene = new Scene(stackPane);
            scene.getStylesheets().clear();

            
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } else {
            errorLabel.setText("Error updating password");
        }
    }

    @FXML
    protected void switchToLogin(ActionEvent event) throws IOException {
        UserSession session = UserSession.getInstance();
        session.setPasswordReset(false); // إعادة تعيين الحالة إذا رجع للـ Login

        Parent root = FXMLLoader.load(getClass().getResource("/com/example/maged/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Load background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
        ImageView backgroundView = new ImageView(backgroundImage);
        
        // Get screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();
        
        // Fit background to screen
        backgroundView.setFitWidth(screenWidth);
        backgroundView.setFitHeight(screenHeight);
        backgroundView.setPreserveRatio(false);
        backgroundView.setEffect(new GaussianBlur(20));
        
        // Create a transparent blue overlay
        Region blueOverlay = new Region();
        blueOverlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 120, 255, 0.2),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        blueOverlay.setEffect(new GaussianBlur(20));
        blueOverlay.setPrefSize(screenWidth, screenHeight);
        
        // Stack background, overlay, and FXML UI
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundView, blueOverlay, root);
        
        // Create the scene
        Scene scene = new Scene(stackPane);
        scene.getStylesheets().clear();

        
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

}