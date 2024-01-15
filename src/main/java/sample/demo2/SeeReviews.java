package sample.demo2;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SeeReviews {
    @FXML
    private TableView<Review> reviewsTableView;

    @FXML
    private TableColumn<Review, String> reviewIdColumn;

    @FXML
    private TableColumn<Review, String> userIdColumn;

    @FXML
    private TableColumn<Review, String> reviewTextColumn;

    private int bookingId;

    public void initData(int bookingId) {
        this.bookingId = bookingId;
        initializeReviewsTableView(bookingId);
    }

    private void setupReviewsColumns() {
        reviewIdColumn.setCellValueFactory(cellData -> cellData.getValue().reviewIdProperty().asString());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asString());
        reviewTextColumn.setCellValueFactory(cellData -> cellData.getValue().reviewTextProperty());
    }

    private void initializeReviewsTableView(int bookingId) {
        setupReviewsColumns();
        ObservableList<Review> reviewsData = JavaPostgreSql.getReviewsByBookingId(bookingId);
        reviewsTableView.setItems(reviewsData);
    }




}
