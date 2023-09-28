package com.example.hotelmanagmet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerChat  implements Initializable {

    @FXML
    private VBox chat_vbox_msg_server;


    @FXML

    public TextField chat_txt_field;
    @FXML

    public Button chat_btnSend;
    @FXML

    public ScrollPane chat_Sc_pane;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        if(chat_vbox_msg_server!=null) {

            SignInController.server.receiveMessageFromClient ( chat_vbox_msg_server );

            chat_btnSend.setOnAction ( new EventHandler<ActionEvent> () {
                @Override
                public void handle(ActionEvent event) {
                    String messageTosend = chat_txt_field.getText ();
                    if (!messageTosend.isEmpty ()) {
                        HBox hBox = new HBox ();
                        hBox.setAlignment ( Pos.CENTER_RIGHT );
                        hBox.setPadding ( new Insets ( 5, 5, 5, 10 ) );
                        Text text = new Text ( messageTosend );
                        TextFlow textFlow = new TextFlow ( text );
                        textFlow.setStyle ( "-fx-color:rgb(239,242,255);" +
                                "-fx-background-color:rgb(15,125,242);" +
                                "-fx-background-radius:25px;" );
                        textFlow.setPadding ( new Insets ( 5, 10, 5, 10 ) );
                        text.setFill ( Color.color ( 0.934, 0.945, 0.996 ) );

                        hBox.getChildren ().add ( textFlow );
                        chat_vbox_msg_server.getChildren ().add ( hBox );
                        SignInController.server.sendMessageToClient ( messageTosend );
                        chat_txt_field.clear ();
                    }
                }

            } );
        }
        else{
            System.out.println("chat box null ");
        }
                    System.out.println("intialise s sevrer ");

    }
    }
