module com.example.hotelmanagmet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.hotelmanagmet to javafx.fxml;
    exports com.example.hotelmanagmet;

}