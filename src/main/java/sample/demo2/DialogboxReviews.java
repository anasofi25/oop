package sample.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DialogboxReviews {
    @FXML
    private Button saveButton;

    @FXML
    private TextArea reviewTextArea;

    private int bookingId;
    private int userId;

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    @FXML
    protected void saveReview() {

        String reviewText = reviewTextArea.getText();
        JavaPostgreSql.saveReviewToDatabase(bookingId, userId, reviewText);

        Stage stage = (Stage) saveButton.getScene().getWindow();

        stage.close();
    }
}
