
package sample.demo2;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class DatesController {
   @FXML
   private AnchorPane tripDetails;
    @FXML
    private TableColumn<Trip, Date> tableStart;

    @FXML
    private TableColumn<Trip,Date> tableEnd;
    private ObservableList<Trip> trips;
    @FXML
    private TableView<Trip> tableView;
    @FXML
    private ImageView icon;
    @FXML
    private ImageView mapid;
    @FXML
    private int userID;
    @FXML
    private int bookingId;

    public void initData(ObservableList<Trip> trips, int userId, int bookingId) {
        this.userID=userId;
        this.bookingId=bookingId;
        tableView.setItems(trips);
    }
    public void initData1(ObservableList<Date> dates, int userId) {
        this.userID=userId;
    }
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    @FXML
    private void initialize() throws SQLException, IOException {

        tableStart.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartingDate()));
        tableEnd.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEndDate()));

       trips=JavaPostgreSql.getTripsFromDatabase();
        ObservableList<Trip> trips1 = JavaPostgreSql.getTripsFromDatabase();
        tableView.setItems(trips);

        tableView.setRowFactory(tv -> {
            TableRow<Trip> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                   Trip selectedTrip = row.getItem();
                    handleHotelSelection(selectedTrip);
                }
            });
            return row;
        });

    }

    @FXML
    private void handleHotelSelection(Trip selectedTrip) {

        int availableDatesId = selectedTrip.getTripId();
        System.out.println(availableDatesId);

        try {
            JavaPostgreSql.storeDatesId(bookingId, availableDatesId);
            System.out.println("Current Dates id:"+ availableDatesId);
        } catch (SQLException e) {
            e.printStackTrace();

        }

        try {
            ObservableList<Hotel> hotelsForDate = JavaPostgreSql.getHotelsForDateFromDatabase(availableDatesId);

            switchToAvailableHotelsScene(hotelsForDate, availableDatesId);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private void switchToAvailableHotelsScene(ObservableList<Hotel> hotels, int availableDatesId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hotel.fxml"));
        Parent datesRoot = loader.load();
        Stage primaryStage = (Stage) tripDetails.getScene().getWindow();

        HotelController hotelController = loader.getController();
        hotelController.initData(hotels, userID, bookingId, availableDatesId);

        SceneController.switchScene(datesRoot, primaryStage);
    }
    Image myImage = new Image(getClass().getResourceAsStream("bee_2.jpg"));
    public void displayImage() {
        icon.setImage(myImage);
    }

    Image myImage1 = new Image(getClass().getResourceAsStream("dates1.jpg"));
    public void displayImage1() {
        mapid.setImage(myImage1);
    }

    @FXML
    public static void changeScene(ObservableList<Date> dates, int userID, int bookingId) throws IOException {
        System.out.println("Current User ID: ");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dates.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // scene

        // Set the userID in the new controller
        DatesController controller = fxmlLoader.getController();
        controller.initData1(dates, userID);
        controller.setBookingId(bookingId);
        //controller.setAvailableDatesId(availableDatesId);

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void getData_2() throws IOException, SQLException {
        PageController.changeScene(userID, bookingId);
    }

}

