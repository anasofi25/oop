package sample.demo2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class InitialPage {
    @FXML
    private Integer userID;
    @FXML
    private Integer bookingid;
    public void initData(int userID ) {
        this.userID=userID;

    }
    @FXML
    private AnchorPane init;
    @FXML
    public static void changeScene(int userId) throws IOException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("initialpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        InitialPage controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void getData_1() throws IOException {
        Controller.changeScene(userID);
    }
    @FXML
    protected void getData() throws IOException, SQLException {
        int bookingid=JavaPostgreSql.createBookingForUser(userID);
        PageController.changeScene(userID, bookingid);
    }
    @FXML
    protected void getData_2() throws IOException {
        Account.changeScene(userID);
    }
    @FXML
    protected void getData_3() throws IOException, SQLException {
        MyBookingsController.changeScene(userID);

    }

    @FXML
    private ImageView page;
    Image myImage = new Image(getClass().getResourceAsStream("page.jpeg"));
    public void displayImage() {
        page.setImage(myImage);
    }


}
