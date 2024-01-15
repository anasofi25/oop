package sample.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
   private AnchorPane scene1;
    @FXML
    private ImageView icon;
    @FXML
    private ImageView img;
    private static int userID=-1;

    public void switchToScene(ActionEvent actionEvent) throws IOException {
        new SceneController(scene1,"Register.fxml");
    }
    Image myImage = new Image(getClass().getResourceAsStream("bee_2.jpg"));
    public void displayImage() {
        icon.setImage(myImage);
    }

    Image myImage1 = new Image(getClass().getResourceAsStream("img1.jpg"));
    public void displayImage1() {
        img.setImage(myImage1);
    }

    public void switchToSceneL(ActionEvent actionEvent) throws IOException {
        new SceneController(scene1,"login.fxml");
    }
    public void initData(int userID) {
        this.userID = userID;
        System.out.println("Current User ID: " + userID);
    }

    @FXML
    public static void changeScene(int userId) throws IOException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        HelloController controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void getData6() throws IOException {
        RegisterController.changeScene(userID);

    }


}