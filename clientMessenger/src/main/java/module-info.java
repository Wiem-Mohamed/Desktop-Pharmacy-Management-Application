module com.example.clientmessenger {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.clientmessenger to javafx.fxml;
    exports com.example.clientmessenger;
}