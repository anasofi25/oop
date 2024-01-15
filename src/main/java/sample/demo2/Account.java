package sample.demo2;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

    public class Account {
        @FXML
        private Button backButton;

        @FXML
        private Label birthDateLabel;

        @FXML
        private Label fullNameLabel;

        @FXML
        private Label genderLabel;

        @FXML
        private Label passwordLabel;

        @FXML
        private Label usernameLabel;

        private int userID;

        @FXML
        private Button changePasswordButton;

        public void initData(int userID) {
            this.userID = userID;
            System.out.println("Current User ID: " + userID);
            initialize();
        }


        @FXML
        public static void changeScene(int userId) throws IOException {
            System.out.println("Current User ID: " + userId);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("account.fxml"));
            Scene scene = new Scene(fxmlLoader.load()); // scene

            // Set the userID in the new controller
            Account controller = fxmlLoader.getController();
            controller.initData(userId);

            Stage stage = Main.getPrimaryStage();
            stage.hide();
            stage.setScene(scene);
            stage.show();
        }


        @FXML
        protected void getData() throws IOException {
            InitialPage.changeScene(userID);

        }

        public void initialize() {

            ObservableList<Users> userData = JavaPostgreSql.getUsers(userID);

            if (!userData.isEmpty()) {
                Users currentUser = userData.get(0);

                usernameLabel.setText(currentUser.getUsername());
                passwordLabel.setText(currentUser.getPassword());
                fullNameLabel.setText(currentUser.getFullName());
                genderLabel.setText(currentUser.getGender());
                birthDateLabel.setText(currentUser.getBirthDate());
            } else {
                System.out.println("User not found");
            }
        }
        @FXML
        private void openDialog() {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Account.class.getResource("dialogboxpassword.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                DialogBoxPassword controller = fxmlLoader.getController();
                controller.setUserId(userID, this);

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Change Password");
                dialog.getDialogPane().setContent(anchorPane);

                dialog.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void updatePasswordLabel(String newPassword) {
            // Update the password label with the new password
            passwordLabel.setText(newPassword);
        }
        @FXML
        private ImageView account;
        @FXML
        private ImageView accountIcon;

        Image myImage1 = new Image(getClass().getResourceAsStream("account.jpg"));
        public void displayImage1() {
            account.setImage(myImage1);
        }
        Image myImage = new Image(getClass().getResourceAsStream("account1.jpg"));
        public void displayImage() {
            accountIcon.setImage(myImage);
        }


    }


