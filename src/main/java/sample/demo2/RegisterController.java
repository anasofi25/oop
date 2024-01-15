package sample.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.io.IOException;
import java.sql.Date;

public class RegisterController {
    public TextField usernameField;
    public TextField passwordField;
    public TextField nameField;
    public ComboBox<String> genderComboBox;
    public DatePicker dobDatePicker;
    @FXML
    private AnchorPane regscene;
    @FXML
    private ImageView reg1;
    @FXML
    private ImageView reg2;
    @FXML
    private ImageView reg3;
    @FXML
    private ImageView reg4;
    @FXML
    private PasswordField confirmPasswordField;
    int userID;

    Image myImage = new Image(getClass().getResourceAsStream("reg_1.jpg"));

    public void displayImage() {
        reg1.setImage(myImage);
    }

    Image myImage1 = new Image(getClass().getResourceAsStream("reg_2.jpg"));

    public void displayImage1() {
        reg2.setImage(myImage);
    }

    Image myImage2 = new Image(getClass().getResourceAsStream("reg_3.jpg"));

    public void displayImage2() {
        reg3.setImage(myImage);
    }

    Image myImage3 = new Image(getClass().getResourceAsStream("reg_4.jpg"));

    public void displayImage3() {
        reg4.setImage(myImage);
    }


    public void switchToScene(ActionEvent actionEvent) throws IOException {
        new SceneController(regscene, "hello-view.fxml");
    }

    @FXML
    private Button regButton;

    @FXML
    private void handleRegistration(ActionEvent actionEvent) throws IOException {
        //switchToNextScene();
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();
        String confirmedPassword = confirmPasswordField.getText();
        String enteredFullName = nameField.getText();
        String enteredGender = genderComboBox.getValue();
        LocalDate enteredBirthDate = dobDatePicker.getValue();

        if (!usernameField.getText().isEmpty() && !enteredPassword.isEmpty()) {

            if (!enteredPassword.equals(confirmedPassword)) {
                System.out.println("Passwords do not match");
            } else {
                if (enteredFullName.isEmpty() || enteredGender == null || enteredBirthDate == null) {
                    System.out.println("Please enter all required information");
                } else {
                    Date sqlBirthDate = Date.valueOf(enteredBirthDate);

                    int userID = JavaPostgreSql.writeToDatabase(enteredUsername, enteredPassword, enteredFullName, enteredGender, sqlBirthDate);
                    System.out.println(userID);

                    if (userID != -1) {
                        System.out.println("User already exists.");
                    } else {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Registration Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Thank you for registering! You can now log in with your username and password.");
                        alert.showAndWait();
                        System.out.println("Registration successful!");
                        //switchToNextScene();
                    }

                }
            }
        } else {
            System.out.println("Please enter both username and password");
        }
    }

        public void initData(int userID) {
            this.userID = userID;
            System.out.println("Current User ID: " + userID);
        }

    @FXML
    public static void changeScene(int userId) throws IOException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Register.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        RegisterController controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void getData5() throws IOException {
        HelloController.changeScene(userID);
    }

    @FXML
    public void getToLogin(ActionEvent actionEvent) throws IOException{
        new SceneController(regscene,"login.fxml");
    }
}
