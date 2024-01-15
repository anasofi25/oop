package sample.demo2;

import javafx.collections.FXCollections;
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

import java.io.IOException;
import java.sql.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;


public class PageController {

    @FXML
    private AnchorPane scene3;

    @FXML
    private TableView<Destinations> tableView;

    @FXML
    private TableColumn<Destinations, Number> tableIdColumn;

    @FXML
    private TableColumn<Destinations, String> tableCountryColumn;

    @FXML
    private TableColumn<Destinations, String> tableCityColumn;

    @FXML
    private TextField destinationTextField;

    @FXML
    private Button destinationSearch;
    @FXML
    private ImageView icon;
    @FXML
    private ImageView mapid;
    @FXML
    private ComboBox<String> sortComboBox;

    private ObservableList<Destinations> originalData;
    private int userID;
    private int bookingId;

    public void switchToScene(ActionEvent actionEvent) throws IOException {
        new SceneController(scene3,"login.fxml");
    }

    public void initData(int userID, int bookingId ) throws SQLException {
        this.userID=userID;
        this.bookingId=bookingId;
        initialize();
    }
    @FXML
    public static void changeScene(int userId, int bookingId) throws IOException, SQLException {
        System.out.println("Current User ID: " + userId+ " Booking id: "+ bookingId);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("page1.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        PageController controller = fxmlLoader.getController();
        controller.initData(userId, bookingId);

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    protected void getData_1() throws IOException {
        InitialPage.changeScene(userID);
    }

    private enum SortingOption {
        NO_SORT,
        ASCENDING,
        DESCENDING
    }

    private SortingOption currentSortingOption = SortingOption.NO_SORT;

    @FXML
    private void initialize() throws SQLException {

        tableIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDestinationId()));
        tableCountryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        tableCityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCity()));

        originalData=JavaPostgreSql.getDestinationsFromDatabase();
        ObservableList<Destinations> data = JavaPostgreSql.getDestinationsFromDatabase();
        tableView.setItems(data);

        destinationSearch.setOnAction(event -> {
            try {
                filterData(destinationTextField.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        destinationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterData(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


        sortComboBox.setValue("-");


        sortComboBox.setOnAction(event -> {
            try {
                updateSorting();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        tableView.setRowFactory(tv -> {
            TableRow<Destinations> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Destinations selectedDestination = row.getItem();
                    handleDestinationSelection(selectedDestination) ;
                }
            });
            return row;
        });

        countryFilterComboBox.setOnAction(event -> {
            try {
                filterByCountry();
            } catch (Exception e) {
                e.printStackTrace();

            }
        });

       ObservableList<String> countries = JavaPostgreSql.getCountriesFromDatabase();
        countryFilterComboBox.setItems(countries);
    }
    private void handleDestinationSelection(Destinations selectedDestination) {

        int destinationId = selectedDestination.getDestinationId();
        try {
            JavaPostgreSql.storeDestinationId(bookingId, destinationId);
            System.out.println("Current Destination id:"+ destinationId);
        } catch (SQLException e) {
            e.printStackTrace();

        }

        try {
            ObservableList<Trip> availableDates = JavaPostgreSql.getTripsForDestinationFromDatabase(destinationId);

            switchToAvailableDatesScene(availableDates);
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }
    private void switchToAvailableDatesScene(ObservableList<Trip> trips) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dates.fxml"));
        Parent datesRoot = loader.load();
        Stage primaryStage = (Stage) scene3.getScene().getWindow();

        DatesController datesController = loader.getController();
        datesController.initData(trips, userID, bookingId);

        SceneController.switchScene(datesRoot, primaryStage);
    }
    private void updateSorting() {
        String selectedOption = sortComboBox.getValue();
        switch (selectedOption) {
            case "-":
                tableView.setItems(originalData);
                currentSortingOption = SortingOption.NO_SORT;
                break;
            case "A-Z":
                tableView.setItems(tableView.getItems().sorted((d1, d2) ->
                        d1.getCountry().compareToIgnoreCase(d2.getCountry())));
                currentSortingOption = SortingOption.ASCENDING;
                break;
            case "Z-A":
                tableView.setItems(tableView.getItems().sorted((d1, d2) ->
                        d2.getCountry().compareToIgnoreCase(d1.getCountry())));
                currentSortingOption = SortingOption.DESCENDING;
                break;
        }
    }
    private void filterData(String searchQuery) throws SQLException {
        ObservableList<Destinations> data = JavaPostgreSql.getFilteredDestinationsFromDatabase(searchQuery);
        tableView.setItems(data);
    }

    Image myImage = new Image(getClass().getResourceAsStream("bee_2.jpg"));
    public void displayImage() {
        icon.setImage(myImage);
    }

    Image myImage1 = new Image(getClass().getResourceAsStream("map.jpg"));
    public void displayImage1() {
        mapid.setImage(myImage1);
    }



    @FXML
    private ComboBox<String> countryFilterComboBox;
    private void filterByCountry() {
        String selectedCountry = countryFilterComboBox.getValue();
        if (selectedCountry != null && !selectedCountry.isEmpty()) {
            try {
                ObservableList<Destinations> filteredData = JavaPostgreSql.getDestinationsByCountry(selectedCountry);
                tableView.setItems(filteredData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

