package com.example.clientmessenger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public TextField chat_txt_field;
    public AnchorPane chat_form;
    public Button chat_btnSend;
    public VBox chat_vbox_msg;
    public ScrollPane chat_Sc_pane;
    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Lancement dun client!");

        try {
            client =new Client(new Socket("127.0.0.1",2222));
            System.out.println("Connected to server");
        } catch (IOException e) {
            System.out.println("there is an error")
           /* throw new RuntimeException(e)*/;
        }

        client.receiveMessageFromServer(chat_vbox_msg);

        chat_btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageTosend=chat_txt_field.getText();
                if(!messageTosend.isEmpty()){
                    HBox hBox=new HBox();
                    hBox.setAlignment((Pos.CENTER_RIGHT));
                    hBox.setPadding(new Insets(5,5,5,10));
                    Text text=new Text(messageTosend);
                    TextFlow textFlow=new TextFlow(text);
                    textFlow.setStyle("-fx-color:rgb(239,242,255);"+
                            "-fx-background-color:rgb(15,125,242);" +
                            "-fx-background-radius:25px;");
                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));

                    hBox.getChildren().add(textFlow);
                    chat_vbox_msg.getChildren().add(hBox);
                    client.sendMessageToServer(messageTosend);
                    chat_txt_field.clear();
                }
            }
        });




    }

    public static void addLabel(String messageFromServer,VBox vbox){
        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text=new Text(messageFromServer);
        TextFlow textFlow=new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(233,233,235);" +
                "-fx-background-radius:20px;");

        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);

            }
        });
    }
}