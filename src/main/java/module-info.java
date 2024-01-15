module sample.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;


    opens sample.demo2 to javafx.fxml;
    exports sample.demo2;
}