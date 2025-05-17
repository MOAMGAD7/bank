package com.banking;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink forgetPasswordLink; // رابط "Forget Password"

    private int failedAttempts = 0; // عدد المحاولات الخاطئة
    private final int MAX_ATTEMPTS = 5; // الحد الأقصى للمحاولات
    private boolean isLocked = false; // حالة القفل
    private Timeline lockTimeline; // عداد 30 ثانية

    @FXML
    public void initialize() {
        if (usernameField == null) {
            System.out.println("❌ usernameField is null - FXML binding issue");
        }
        if (passwordField == null) {
            System.out.println("❌ passwordField is null - FXML binding issue");
        }
        if (errorLabel == null) {
            System.out.println("❌ errorLabel is null - FXML binding issue");
        }
        if (loginButton == null) {
            System.out.println("❌ loginButton is null - FXML binding issue");
        }
        if (forgetPasswordLink == null) {
            System.out.println("❌ forgetPasswordLink is null - FXML binding issue");
        } else {
            forgetPasswordLink.setVisible(false); // إخفاء الرابط في البداية
        }
    }

    @FXML
    protected void handleLogin(ActionEvent event) throws IOException {
        // إذا كان الحساب مقفول، نمنع تسجيل الدخول
        if (isLocked) {
            errorLabel.setText("Too many failed attempts. Please wait.");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        // التحقق من إن الحقول مش فاضية
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }

        // التحقق من بيانات تسجيل الدخول باستخدام قاعدة البيانات
        if (database_BankSystem.loginUser(username, password)) {
            // تحديث تاريخ آخر تسجيل دخول
            database_BankSystem.updateLastLogin(username);

            // تخزين username في UserSession
            UserSession session = UserSession.getInstance();
            session.setUsername(username);

            // رسالة نجاح تسجيل الدخول
            errorLabel.setText("Login successful! Redirecting to Dashboard...");

            // الانتقال إلى صفحة الـ Home2
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/maged/Home2.fxml"));
            Image backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
            ImageView backgroundView = new ImageView(backgroundImage);

            // الحصول على حجم الشاشة
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();

            // إعداد الخلفية
            backgroundView.setFitWidth(screenWidth);
            backgroundView.setFitHeight(screenHeight);
            backgroundView.setPreserveRatio(false);
            backgroundView.setEffect(new GaussianBlur(20));

            // عمل طبقة شفافة زرقاء
            Region blueOverlay = new Region();
            blueOverlay.setBackground(new Background(new BackgroundFill(
                    Color.rgb(0, 120, 255, 0.2),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
            blueOverlay.setEffect(new GaussianBlur(20));
            blueOverlay.setPrefSize(screenWidth, screenHeight);

            // وضع كل العناصر في StackPane
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(backgroundView, blueOverlay, root);

            // إنشاء المشهد
            Scene scene = new Scene(stackPane);

            scene.getStylesheets().clear();

            // الحصول على الـ stage الحالي
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SIGN UP");
            stage.setWidth(1124);
            stage.setHeight(700);
            stage.centerOnScreen();
            stage.show();
            // إعادة تعيين عدد المحاولات بعد تسجيل دخول ناجح
            failedAttempts = 0;
            forgetPasswordLink.setVisible(false);
        } else {
            failedAttempts++;
            errorLabel.setText("Invalid username or password. Attempt " + failedAttempts + " of " + MAX_ATTEMPTS);

            // التحقق إذا وصلنا للحد الأقصى للمحاولات
            if (failedAttempts >= MAX_ATTEMPTS) {
                isLocked = true;
                loginButton.setDisable(true);
                forgetPasswordLink.setVisible(true);

                // عدد الثواني
                final int[] secondsLeft = {30};

                // تحديث الرسالة كل ثانية
                lockTimeline = new Timeline(
                        new KeyFrame(Duration.seconds(1), e -> {
                            secondsLeft[0]--;
                            if (secondsLeft[0] > 0) {
                                errorLabel.setText("Too many failed attempts. Try again in " + secondsLeft[0] + " seconds.");
                            } else {
                                isLocked = false;
                                loginButton.setDisable(false);
                                failedAttempts = 0;
                                errorLabel.setText("You can try again now.");
                                lockTimeline.stop(); // إيقاف المؤقت
                            }
                        })
                );

                lockTimeline.setCycleCount(30); // 30 مرة (ثانية)
                lockTimeline.play();

                // أول رسالة تظهر مباشرة
                errorLabel.setText("Too many failed attempts. Try again in 30 seconds.");
            }
        }
    }

            @FXML
    protected void handleForgetPassword(ActionEvent event) throws IOException {
        // تحميل صفحة Forget Password
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/maged/ForgetPassword.fxml"));

        // تحميل خلفية الصورة
        Image backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
        ImageView backgroundView = new ImageView(backgroundImage);

        // الحصول على حجم الشاشة
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // إعداد الخلفية
        backgroundView.setFitWidth(screenWidth);
        backgroundView.setFitHeight(screenHeight);
        backgroundView.setPreserveRatio(false);
        backgroundView.setEffect(new GaussianBlur(20));

        // عمل طبقة شفافة زرقاء
        Region blueOverlay = new Region();
        blueOverlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 120, 255, 0.2),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        blueOverlay.setEffect(new GaussianBlur(20));
        blueOverlay.setPrefSize(screenWidth, screenHeight);

        // وضع كل العناصر في StackPane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundView, blueOverlay, root);

        // إنشاء المشهد
        Scene scene = new Scene(stackPane);
        UserSession session = UserSession.getInstance();
        scene.getStylesheets().clear();

        // الحصول على الـ stage الحالي
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Forget Password");
        stage.setWidth(1550);
        stage.setHeight(840);
        stage.centerOnScreen();
        stage.show();
    }


    @FXML
    protected void switchToSignup(ActionEvent event) throws IOException {
        UserSession session = UserSession.getInstance();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/maged/signup.fxml"));
        Image backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
        ImageView backgroundView = new ImageView(backgroundImage);

        // الحصول على حجم الشاشة
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // إعداد الخلفية
        backgroundView.setFitWidth(screenWidth);
        backgroundView.setFitHeight(screenHeight);
        backgroundView.setPreserveRatio(false);
        backgroundView.setEffect(new GaussianBlur(20));

        // عمل طبقة شفافة زرقاء
        Region blueOverlay = new Region();
        blueOverlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 120, 255, 0.2),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        blueOverlay.setEffect(new GaussianBlur(20));
        blueOverlay.setPrefSize(screenWidth, screenHeight);

        // وضع كل العناصر في StackPane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundView, blueOverlay, root);

        // إنشاء المشهد
        Scene scene = new Scene(stackPane);

        scene.getStylesheets().clear();

        // الحصول على الـ stage الحالي
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("SIGN UP");
        stage.setWidth(1550);
        stage.setHeight(840);
        stage.centerOnScreen();
        stage.show();
    }
}
