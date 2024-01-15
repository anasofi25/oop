/*
package sample.demo2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class FlightController {

    @FXML
    private AnchorPane flight;

    @FXML
    private Label airlineLabel;

    @FXML
    private Label departureAirportLabel;

    @FXML
    private Label arrivalAirportLabel;

    @FXML
    private Label departureDateTimeLabel;

    @FXML
    private Label arrivalDateTimeLabel;

    @FXML
    private Label priceLabel;

    private int userID;

    public static void changeScene(ObservableList<Flights> flights, int userID) throws SQLException, IOException {
        System.out.println("Current User ID: ");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("flights.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        FlightController controller = fxmlLoader.getController();
        controller.initData(flights, userID);


        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    public void initData(ObservableList<Flights> flights, int userID) throws SQLException {
        this.userID = userID;

    }
    public void initData1(ObservableList<Flights> flights, int bookingId) throws SQLException {

        ObservableList<Flights> bookingData = JavaPostgreSql.getFlightsFromDatabase(bookingId);

        if (!bookingData.isEmpty()) {
            Flights currentbooking = bookingData.get(0);

            //bookingIdLabel.setText(currentbooking.getBookingId().toString());
            airlineLabel.setText(currentbooking.getAirline());
            departureAirportLabel.setText(currentbooking.getDepartureAirport());
            arrivalAirportLabel.setText(currentbooking.getArrivalAirport());
            arrivalDateTimeLabel.setText(currentbooking.getArrivalDateTime().toString());
            departureDateTimeLabel.setText(currentbooking.getDepartureDateTime().toString());
            priceLabel.setText(currentbooking.getPrice().toString());

        } else {
            System.out.println("Flights not found");
        }

    }

    @FXML
    private ImageView flight1;
    Image myImage = new Image(getClass().getResourceAsStream("flight.jpg"));
    public void displayImage() {
        flight1.setImage(myImage);
    }

    @FXML
    public void switchToScene(ActionEvent actionEvent) throws IOException{
        new SceneController(flight, "login.fxml");
    }

}
*/
