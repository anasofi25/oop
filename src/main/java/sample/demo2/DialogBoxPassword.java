package sample.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogBoxPassword {
    private int userID;
    @FXML
    private Button saveButton;
    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confrimPassword;

    private Account AccountController;

    public void setUserId(int userID, Account myaccountController) {
        this.userID = userID;
        this.AccountController = myaccountController;
    }

    @FXML
    protected void changePassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confrimPassword.getText();

        if (!newPassword.isEmpty() && !confirmPassword.isEmpty()) {
            if (newPassword.equals(confirmPassword)) {

                boolean success = updatePassword(userID, newPassword);

                if (success) {
                    System.out.println("Password changed successfully");

                    AccountController.updatePasswordLabel(newPassword);

                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();

                    newPasswordField.clear();
                    confrimPassword.clear();
                } else {
                    System.out.println("Failed to change password");
                }
            } else {
                System.out.println("New password and confirm password do not match");
            }
        } else {
            System.out.println("New password and confirm password cannot be empty");
        }
    }


    private boolean updatePassword(int userId, String newPassword) {

        return JavaPostgreSql.updatePassword(userId, newPassword);
    }

}
