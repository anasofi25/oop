package sample.demo2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {

    @FXML
    private Button submit_button;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private AnchorPane scene2;
    @FXML
    private ImageView icon;
    @FXML
    private ImageView img2;
    private static int userID=-1;


    Image myImage = new Image(getClass().getResourceAsStream("bee_2.jpg"));


    @FXML
    public static void changeScene(int userId) throws IOException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Controller controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    public void displayImage() {
        icon.setImage(myImage);
    }

    Image myImage1 = new Image(getClass().getResourceAsStream("img2.jpg"));
    public void displayImage1() {
        img2.setImage(myImage);
    }

    public void switchToScene(ActionEvent actionEvent) throws IOException {
        new SceneController(scene2,"hello-view.fxml");
    }

    @FXML
    protected void getData() throws IOException {
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
            int userID = JavaPostgreSql.retrieveUserID(username.getText(), password.getText());

            if (userID != -1) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("initialpage.fxml"));

                Parent root = loader.load();

                InitialPage controller = loader.getController();
                controller.initData(userID);

                Stage stage = (Stage) submit_button.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } else {
                System.out.println("Failed to get user ID.");
            }
        } else {
            System.out.println("Please enter both username and password");
        }
    }



    public void initData(int userID) {
        this.userID = userID;
        System.out.println("Current User ID: " + userID);

    }

}
