package com.banking;

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
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the payment page where users can make different types of payments
 */
public class PaymentPage {

    @FXML
    public Button BkToPy;
    @FXML
    public Button BkToPy2;
    @FXML
    public Label TpLb;
    @FXML
    public ImageView CatIm;
    @FXML
    public Label DpBxLb;
    @FXML
    public ComboBox<String> PyCmbx;
    @FXML
    public Label TxLb;
    @FXML
    public TextField TxFld;
    @FXML
    public Label ShLb;
    @FXML
    public String PyCmbxSt,TxFldSt,ShLbSt,AddTxFlSt;
    @FXML
    public TextField AddTxFl;
    @FXML
    public Label EGPlb;
    @FXML
    public Label LbLb;
    @FXML
    public Button PayBtn;
    @FXML
    public Label TxLb1;
    @FXML
    public ComboBox<String> cardSelectBox;
    @FXML
    public Label cardSelectLabel;

    @FXML
    private Label categoryLabel;

    private UserSession userSession = UserSession.getInstance();
    private database_BankSystem.Card selectedCard;

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        loadUserCards();
        PayBtn.setDisable(true); // Disable pay button until card is selected
        
        // Add listener for card selection
        cardSelectBox.setOnAction(event -> {
            String selected = cardSelectBox.getValue();
            if (selected != null) {
                // Find the selected card
                List<database_BankSystem.Card> userCards = database_BankSystem.getUserCards(userSession.getUsername());
                for (database_BankSystem.Card card : userCards) {
                    if ((card.getCardType() + " - " + card.getAmount() + " EGP").equals(selected)) {
                        selectedCard = card;
                        PayBtn.setDisable(false);
                        break;
                    }
                }
            }
        });
    }

    private void loadUserCards() {
        List<database_BankSystem.Card> userCards = database_BankSystem.getUserCards(userSession.getUsername());
        cardSelectBox.getItems().clear();
        for (database_BankSystem.Card card : userCards) {
            cardSelectBox.getItems().add(card.getCardType() + " - " + card.getAmount() + " EGP");
        }
    }

    /**
     * Sets the payment category in the UI
     *
     * @param category The payment category (Bills, Mobile, etc.)
     */
    public void setPaymentCategory(String category) {
        TpLb.setText(category);
        if(category.equals("Bills")){
            CatIm.setImage(new Image(getClass().getResourceAsStream("/bill2.png")));
            DpBxLb.setText("Bill Type:");
            AddTxFl.setVisible(false);
            TxLb1.setVisible(false);
            // Update combobox with bill-specific items
            PyCmbx.getItems().clear();
            PyCmbx.getItems().addAll(
                    "Electricity",
                    "Water",
                    "Gas",
                    "Internet",
                    "Phone",
                    "Television"
            );
            TxLb.setText("Customer ID:");
            EGPlb.setVisible(false);
            LbLb.setText("Bills Amount:");
        } else if(category.equals("Mobile Top-Up")) {
            LbLb.setVisible(true);
            ShLb.setVisible(true);
            CatIm.setImage(new Image(getClass().getResourceAsStream("/iphone2.png")));
            DpBxLb.setText("Network:");

            // Update combobox with mobile-specific items
            PyCmbx.getItems().clear();
            PyCmbx.getItems().addAll(
                    "Vodafone",
                    "Orange",
                    "Etisalat",
                    "We"
            );
            TxLb.setText("Top-Up Amount:");
            EGPlb.setVisible(true);
            TxLb1.setText("Mobile Number:");
        }else if(category.equals("Credit Card")){
            CatIm.setImage(new Image(getClass().getResourceAsStream("/credit-card2.png")));
            DpBxLb.setText("Choose Card:");
            AddTxFl.setVisible(false);
            TxLb1.setVisible(false);
            // Update combobox with bill-specific items
            PyCmbx.getItems().clear();
            PyCmbx.getItems().addAll(
                    "Card1",
                    "Card2",
                    "Card3",
                    "Card4"
            );
            TxLb.setText("Amount:");
            EGPlb.setVisible(true);
        }
        else  if(category.equals("Government Service")){
            LbLb.setVisible(true);
            ShLb.setVisible(true);
            CatIm.setImage(new Image(getClass().getResourceAsStream("/government2.png")));
            DpBxLb.setText("Choose Service:");

            // Update combobox with mobile-specific items
            PyCmbx.getItems().clear();
            PyCmbx.getItems().addAll(
                    "Taxes",
                    "Licensing",
                    "Fines"
            );
            TxLb.setText("Service Amount:");
            EGPlb.setVisible(true);
            TxLb1.setText("Service Number:");
        }
        else if(category.equals("Donation")){
            CatIm.setImage(new Image(getClass().getResourceAsStream("/heart2.png")));
            DpBxLb.setText("Choose Charity:");
            AddTxFl.setVisible(false);
            TxLb1.setVisible(false);
            // Update combobox with bill-specific items
            PyCmbx.getItems().clear();
            PyCmbx.getItems().addAll(
                    "57357 Hospital",
                    "Masr Elkheir",
                    "Resala",
                    "Elzakah box",
                    "Tahia Masr",
                    "Handsa Helwan"
            );
            TxLb.setText("Donation Amount:");
            EGPlb.setVisible(true);
        }
        else if(category.equals("Education Payments")){
            LbLb.setVisible(true);
            ShLb.setVisible(true);
            CatIm.setImage(new Image(getClass().getResourceAsStream("/graduation2.png")));
            DpBxLb.setText("Choose Facility:");

            // Update combobox with mobile-specific items
            PyCmbx.getItems().clear();
            PyCmbx.getItems().addAll(
                    "Ain-Shams University",
                    "Cairo University",
                    "Helwan University",
                    "El-Mansora University"
            );
            TxLb.setText("Amount:");
            EGPlb.setVisible(true);
            TxLb1.setText("Student ID:");
        }
        else if(category.equals("Insurance Payments")){
            LbLb.setVisible(true);
            ShLb.setVisible(true);
            CatIm.setImage(new Image(getClass().getResourceAsStream("/insurance2.png")));
            DpBxLb.setText("Insurance Providers:");

            // Update combobox with mobile-specific items
            PyCmbx.getItems().clear();
            PyCmbx.getItems().addAll(
                    "Allianz",
                    "AXA",
                    "MetLife",
                    "Misr Insurance Company",
                    "Misr Life Insurance",
                    "GIG Egypt"
            );
            TxLb.setText("Insurance Amount:");
            EGPlb.setVisible(true);
            TxLb1.setText("Policy Number:");
        }
        else if(category.equals("Other Payments")){
            LbLb.setVisible(true);
            ShLb.setVisible(true);
            CatIm.setImage(new Image(getClass().getResourceAsStream("/coin2.png")));
            DpBxLb.setText("Payment Category:");

            // Update combobox with mobile-specific items
            PyCmbx.getItems().clear();
            PyCmbx.getItems().addAll(
                    "Loan Payments",
                    "Subscription Services",
                    "Club or Membership Fees",
                    "Rent Payments",
                    "E-commerce / Marketplace Settlements",
                    "Business Payments\n"
            );
            TxLb.setText("Payment Amount:");
            EGPlb.setVisible(true);
            TxLb1.setText("Payee Name:");
        }
    }

    public void Clicking7(ActionEvent actionEvent) throws IOException {
        // Load the Payment.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/Payment.fxml"));
        Parent paymentPage = fxmlLoader.load();

        // Load background image
        Image backgroundImage;
        try {
            backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
            if (backgroundImage.isError()) {
                System.err.println("Error loading background image: " + backgroundImage.getException().getMessage());
                // Use a fallback color if image fails to load
                backgroundImage = null;
            }
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
            backgroundImage = null;
        }

        // Get screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Create stack pane for layering content
        StackPane stackPane = new StackPane();

        // Add background and overlay if image loaded successfully
        if (backgroundImage != null) {
            ImageView backgroundView = new ImageView(backgroundImage);
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

            // Add background and overlay to stack
            stackPane.getChildren().addAll(backgroundView, blueOverlay);
        } else {
            // Create a fallback blue gradient background if image failed to load
            Region fallbackBackground = new Region();
            fallbackBackground.setBackground(new Background(new BackgroundFill(
                    Color.rgb(10, 60, 120, 1.0),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
            fallbackBackground.setPrefSize(screenWidth, screenHeight);
            stackPane.getChildren().add(fallbackBackground);
        }

        // Add the UI content on top
        stackPane.getChildren().add(paymentPage);

        // Create the scene
        Scene scene = new Scene(stackPane, 1200, 700);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void Writing3(ActionEvent actionEvent) {
        if(TpLb.getText().equals("Bills")){
            TxFld.setOnAction(e -> {
                String input = TxFld.getText();
                TxFldSt=input;
                ShLb.setText("1000.00 EGP");
                ShLbSt="1000.00";
            });
        }
        else if(TpLb.getText().equals("Mobile Top-Up")){
            TxFld.setOnAction(e -> {
                String input = TxFld.getText();
                TxFldSt=input;
                double n=Double.parseDouble(TxFldSt);
                n+=(n*3.0/100.0);
                ShLb.setText(String.valueOf(n)+" EGP");
                ShLbSt=String.valueOf(n);
            });
        }
        else if(TpLb.getText().equals("Credit Card")){
            TxFld.setOnAction(e -> {
                String input = TxFld.getText();
                TxFldSt=input;
                double n=Double.parseDouble(TxFldSt);
                n+=(n*3.0/100.0);
                ShLb.setText(String.valueOf(n)+" EGP");
                ShLbSt=String.valueOf(n)+" EGP";
            });
        }
        else if(TpLb.getText().equals("Government Service")){
            TxFld.setOnAction(e -> {
                String input = TxFld.getText();
                TxFldSt=input;
                double n=Double.parseDouble(TxFldSt);
                n+=(n*3.0/100.0);
                ShLb.setText(String.valueOf(n)+" EGP");
                ShLbSt=String.valueOf(n)+" EGP";
            });
        }
        else if(TpLb.getText().equals("Donation")){
            TxFld.setOnAction(e -> {
                String input = TxFld.getText();
                TxFldSt=input;
                double n=Double.parseDouble(TxFldSt);
                n+=(n*3.0/100.0);
                ShLb.setText(String.valueOf(n)+" EGP");
                ShLbSt=String.valueOf(n)+" EGP";
            });
        }
        else if(TpLb.getText().equals("Education Payments")){
            TxFld.setOnAction(e -> {
                String input = TxFld.getText();
                TxFldSt=input;
                double n=Double.parseDouble(TxFldSt);
                n+=(n*3.0/100.0);
                ShLb.setText(String.valueOf(n)+" EGP");
                ShLbSt=String.valueOf(n)+" EGP";
            });
        }
        else if(TpLb.getText().equals("Insurance Payments")){
            TxFld.setOnAction(e -> {
                String input = TxFld.getText();
                TxFldSt=input;
                double n=Double.parseDouble(TxFldSt);
                n+=(n*3.0/100.0);
                ShLb.setText(String.valueOf(n)+" EGP");
                ShLbSt=String.valueOf(n)+" EGP";
            });
        }
        else if(TpLb.getText().equals("Other Payments")){
            TxFld.setOnAction(e -> {
                String input = TxFld.getText();
                TxFldSt=input;
                double n=Double.parseDouble(TxFldSt);
                n+=(n*3.0/100.0);
                ShLb.setText(String.valueOf(n)+" EGP");
                ShLbSt=String.valueOf(n)+" EGP";
            });
        }
    }

    public void Choosing(ActionEvent actionEvent) {
        PyCmbx.setOnAction(e -> {
            String input = PyCmbx.getSelectionModel().getSelectedItem();
            PyCmbxSt=input;
        });
    }

    @FXML
    public void Paied(ActionEvent actionEvent) {
        if (selectedCard == null) {
            showAlert("Please select a card first!");
            return;
        }

        double amount = Double.parseDouble(TxFld.getText());
        if (amount > selectedCard.getAmount()) {
            showAlert("Insufficient funds in selected card!");
            return;
        }

        // Update card amount
        database_BankSystem.updateCardAmount(userSession.getUsername(), selectedCard.getId(), selectedCard.getAmount() - amount);

        // Continue with existing payment logic
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/TransferConfirm.fxml"));
        try {
            Parent root = fxmlLoader.load();
            TransferConfirm transferConfirm = fxmlLoader.getController();
            
            if(TpLb.getText().equals("Bills")){
                transferConfirm.LabelText(PyCmbxSt,TxFldSt,ShLbSt,"Bills");
            }
            else if(TpLb.getText().equals("Mobile Top-Up")){
                transferConfirm.LabelText(PyCmbxSt,AddTxFlSt,ShLbSt,"Mobile Top-Up");
            }
            else if(TpLb.getText().equals("Credit Card")){
                transferConfirm.LabelText(PyCmbxSt,TxFldSt,ShLbSt,"Credit Card");
            }
            else if(TpLb.getText().equals("Government Service")){
                transferConfirm.LabelText(PyCmbxSt,TxFldSt,ShLbSt,"Government Service");
            }
            else if(TpLb.getText().equals("Donation")){
                transferConfirm.LabelText(PyCmbxSt,TxFldSt,ShLbSt,"Donation");
            }
            else if(TpLb.getText().equals("Education Payments")){
                transferConfirm.LabelText(PyCmbxSt,TxFldSt,ShLbSt,"Education Payments");
            }
            else if(TpLb.getText().equals("Insurance Payments")){
                transferConfirm.LabelText(PyCmbxSt,TxFldSt,ShLbSt,"Insurance Payments");
            }
            else if(TpLb.getText().equals("Other Payments")){
                transferConfirm.LabelText(PyCmbxSt,TxFldSt,ShLbSt,"Other Payments");
            }

            Stage stage = new Stage();
            stage.setTitle("Transfer Confirmation");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            // Reload cards after payment
            loadUserCards();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Writing4(ActionEvent actionEvent) {
        AddTxFl.setOnAction(e -> {
            String input = AddTxFl.getText();
            AddTxFlSt=input;
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
