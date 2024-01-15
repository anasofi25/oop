package sample.demo2;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;


public class HotelController {
        @FXML
        private AnchorPane tripDetails1;
        @FXML
        private TableColumn<Hotel, Integer> tableIdColumn;
        @FXML
        private TableColumn<Hotel,String> hotelName;
        @FXML
        private TableColumn<Hotel,String> hotelLocation;
        @FXML
        private TableColumn<Hotel,String> hotelDescription;
        @FXML
        private TableColumn<Hotel,Integer> hotelPrice;
        @FXML
        private TableColumn<Hotel,Integer> hotelAvb;

        private ObservableList<Hotel> hotels;
        @FXML
        private TableView<Hotel> tableView;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private Integer userID;
    @FXML
    private Integer bookingId;
    @FXML
    private Integer availableDatesId;
    private enum SortingOption {
        NO_SORT,
        ASCENDING,
        DESCENDING
    }

    private HotelController.SortingOption currentSortingOption = HotelController.SortingOption.NO_SORT;

        public void switchToScene(ActionEvent actionEvent) throws IOException {
            new SceneController(tripDetails1,"dates.fxml");
        }

    public void initData(ObservableList<Hotel> hotels, int userId, int bookingId, int availableDatesId) {
            this.userID=userId;
            this.bookingId=bookingId;
            this.availableDatesId=availableDatesId;
        hotelName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        tableView.setItems(hotels);
    }

    public void switchToSceneN(ActionEvent actionEvent) throws IOException {
        new SceneController(tripDetails1,"Flight.fxml");
    }

        @FXML
        private void initialize() throws SQLException, IOException {

           // tableIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHotelId()).asObject());
            hotelName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
           // hotelLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
            hotelDescription.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getDescription()));
            hotelPrice.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getPricePerNight()).asObject());
           // hotelAvb.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getAvailableRooms()).asObject());
            hotels=JavaPostgreSql.getHotelsFromDatabase();
            ObservableList<Hotel> hotels1 = JavaPostgreSql.getHotelsFromDatabase();
            tableView.setItems(hotels);

            sortComboBox.setValue("-");

            sortComboBox.setOnAction(event -> {
                try {
                    updateSorting();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            });

            tableView.setRowFactory(tv -> {
                TableRow<Hotel> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1 && !row.isEmpty()) {
                        Hotel selectedHotel = row.getItem();
                        handleBookingSelection(selectedHotel);
                    }
                });
                return row;
            });

        }
    private void handleBookingSelection(Hotel selectedHotel) {

        int availableHotelId = selectedHotel.getHotelId();

        try {
            JavaPostgreSql.storeHotelId(bookingId, availableHotelId);
            System.out.println("Current Hotel id:" + availableHotelId);

            ObservableList<Booking> finalBooking = JavaPostgreSql.getBookingDetails(userID);

            switchToCartScene(finalBooking);
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    private void switchToCartScene(ObservableList<Booking> bookings) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
        Parent cartRoot = loader.load();
        Stage primaryStage = (Stage) tripDetails1.getScene().getWindow();

        CartController cartController = loader.getController();
        cartController.initData(bookings,userID);

        SceneController.switchScene(cartRoot, primaryStage);
    }



    private void updateSorting() {
        String selectedOption = sortComboBox.getValue();
        switch (selectedOption) {
            case "-":
                tableView.setItems(hotels);
                currentSortingOption = HotelController.SortingOption.NO_SORT;
                break;
            case "A-Z":
                tableView.setItems(tableView.getItems().sorted((d1, d2) ->
                        d1.getName().compareToIgnoreCase(d2.getName())));
                currentSortingOption = HotelController.SortingOption.ASCENDING;
                break;
            case "Z-A":
                tableView.setItems(tableView.getItems().sorted((d1, d2) ->
                        d2.getName().compareToIgnoreCase(d1.getName())));
                currentSortingOption = HotelController.SortingOption.DESCENDING;
                break;
        }
    }
    private void filterData(String searchQuery) throws SQLException {
        ObservableList<Destinations> data = JavaPostgreSql.getFilteredDestinationsFromDatabase(searchQuery);
        tableView.setItems(hotels);
    }

    @FXML
    private ImageView hotel;
    Image myImage = new Image(getClass().getResourceAsStream("hotel.jpg"));
    public void displayImage() {
        hotel.setImage(myImage);
    }


}