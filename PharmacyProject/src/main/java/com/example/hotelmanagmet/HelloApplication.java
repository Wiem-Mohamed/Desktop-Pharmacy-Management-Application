package com.example.hotelmanagmet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HelloApplication extends Application {
    Parent signInLoader;

    @Override
    public void start(Stage primaryStage) throws Exception {

        signInLoader = FXMLLoader.load(getClass().getResource("/com/example/hotelmanagmet/sign-in.fxml"));
        Scene scene = new Scene(signInLoader);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}
