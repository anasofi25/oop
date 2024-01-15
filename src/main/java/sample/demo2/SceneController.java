package sample.demo2;
import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
public class SceneController {

    public SceneController (AnchorPane currentAnchorPane, String fxml) throws IOException {
      AnchorPane nextAnchorPane=FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml)));
      currentAnchorPane.getChildren().removeAll();
      currentAnchorPane.getChildren().setAll(nextAnchorPane);
    }
    public static void switchScene(Parent root, Stage stage) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
