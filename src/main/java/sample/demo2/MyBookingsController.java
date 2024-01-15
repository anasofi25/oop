package sample.demo2;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MyBookingsController {
    @FXML
    private TableView<Booking> bookingsTable;
    @FXML
    private TableColumn<Booking, String> countryColumn;
    @FXML
    private TableColumn<Booking, String> cityColumn;
    @FXML
    private TableColumn<Booking, String> hotelColumn;
    @FXML
    private TableColumn<Booking, String> toColumn;
    @FXML
    private TableColumn<Booking, String> fromColumn;
    @FXML
    private TableColumn<Booking, Integer> priceColumn;
    @FXML
    private Button cancelButton;

    private int userID;
    @FXML
    private TableView<Booking> tableView;

    public void initData(int userID) throws SQLException {
        this.userID = userID;
        System.out.println("Current User " + userID);
        initialize();
    }

    private void initialize() throws SQLException {

        setupColumns();

        ObservableList<Booking> data = JavaPostgreSql.getBookingDetails(userID);
        tableView.setItems(data);
        cancelButton.setOnAction(event -> handleCancelBooking());
    }
        private void setupColumns() {
            countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
            cityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCity()));
            hotelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHotelName()));
            toColumn.setCellValueFactory(cellData-> new SimpleStringProperty(
                    new SimpleDateFormat("yyyy-MM-dd").format(cellData.getValue().getStartingDate())
            ));
            fromColumn.setCellValueFactory(cellData-> new SimpleStringProperty(
                    new SimpleDateFormat("yyyy-MM-dd").format(cellData.getValue().getEndDate())
                    ));
            priceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().calculateTotalPrice()).asObject());
        }

    @FXML
    public static void changeScene(int userId) throws IOException, SQLException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mybookings.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MyBookingsController controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void writeReview() {
        Booking selectedBooking = tableView.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            int bookingId = selectedBooking.getBookingId();
            openReviewDialog(bookingId, userID);
        } else {
            System.out.println("No booking selected!");
        }
    }
    @FXML
    protected void getData() throws IOException{
        InitialPage.changeScene(userID);
    }

    private void openReviewDialog(int bookingId, int userId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogboxrev.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            DialogboxReviews controller = fxmlLoader.getController();
            controller.setBookingId(bookingId);
            controller.setUserId(userId);

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Write Review");
            dialog.getDialogPane().setContent(anchorPane);

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openBookReviewsDialog(int bookId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seeReviews.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();


            SeeReviews controller = fxmlLoader.getController();
            controller.initData(bookId);


            Stage dialogStage = new Stage();
            dialogStage.setTitle("Booking Reviews");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(Main.getPrimaryStage());


            Scene scene = new Scene(anchorPane);
            dialogStage.setScene(scene);


            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private ImageView icon;
    Image myImage = new Image(getClass().getResourceAsStream("pic.jpg"));
    public void displayImage() {
        icon.setImage(myImage);
    }



    @FXML
    private void seeReviewsButtonClicked(ActionEvent event) {
        Booking selectedBooking = tableView.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            int bookId = selectedBooking.getBookingId();
            openBookReviewsDialog(bookId);
        }
    }

    @FXML

    private void handleCancelBooking() {
        Booking selectedBooking = tableView.getSelectionModel().getSelectedItem();

        if (selectedBooking != null) {

            JavaPostgreSql.cancelBooking(userID, selectedBooking.getBookingId());


            tableView.getItems().remove(selectedBooking);

            System.out.println("Booking canceled: " + selectedBooking.getBookingId());
        } else {
            System.out.println("No booking selected to cancel.");
        }
    }
}
