package sample.demo2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class CartController {

    @FXML
    AnchorPane cart_1;

    @FXML
    private ImageView cart;
    @FXML
    private Label bookingIdLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label hotelNameLabel;
    @FXML
    private Label hotelPriceLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label endDateLabel;
    public Integer selectedBookingId;

    Image myImage = new Image(getClass().getResourceAsStream("cart.png"));

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void initData1(ObservableList<Booking> bookings, int userID){
        this.userID=userID;
    }


    public void initData(ObservableList<Booking> bookings, int userID) throws SQLException {

        ObservableList<Booking> bookingData = JavaPostgreSql.getBookingDetails(userID);

        if (!bookingData.isEmpty()) {
            Booking currentbooking = bookingData.get(0);

            selectedBookingId=currentbooking.getBookingId();
           // bookingIdLabel.setText(currentbooking.getBookingId().toString());
            cityLabel.setText(currentbooking.getCity());
            countryLabel.setText(currentbooking.getCountry());
            startDateLabel.setText(currentbooking.getStartingDate().toString());
            endDateLabel.setText(currentbooking.getEndDate().toString());
            hotelNameLabel.setText(currentbooking.getHotelName());
            hotelPriceLabel.setText(currentbooking.calculateTotalPrice().toString());

        } else {
            System.out.println("Booking not found");
        }

    }
    private int userID;
    @FXML
    public void changeScene(ObservableList<Booking> bookings, int userID) throws IOException {
        System.out.println("Current User ID: ");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("cart.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // scene

        // Set the userID in the new controller
        CartController controller = fxmlLoader.getController();
        controller.initData1(bookings, userID);


        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void switchTo() throws IOException{
        Controller.changeScene(userID);
    }
  /*  private void switchToFlightScene(ObservableList<Flights> flights) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("flight.fxml"));
        Parent cartRoot = loader.load();
        Stage primaryStage = (Stage) cart.getScene().getWindow();

        // Get the controller from the loader
        FlightController cartController = loader.getController();
        cartController.initData1(flights,selectedBookingId);

        SceneController.switchScene(cartRoot, primaryStage);
    }*/

   /* @FXML
    protected void sw(ActionEvent actionEvent) throws IOException, SQLException {
        ObservableList<Flights> selectedflight=JavaPostgreSql.getFlightsFromDatabase(selectedBookingId);
        switchToFlightScene(selectedflight);
    }*/



}
