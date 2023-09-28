package com.example.clientmessenger;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public  Client(Socket socket){
        try {
            this.socket=socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending to the client");
                   }
    }

    public void sendMessageToServer(String messageToServer){
        try{
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending to the client");
        }
    }
  public void  receiveMessageFromServer(VBox vBox){
      new Thread(new Runnable() {
          @Override
          public void run() {
              while (socket.isConnected()){
                  try {
                      String  messageFromServer = bufferedReader.readLine();
                      HelloController.addLabel(messageFromServer,vBox);

                  } catch (IOException e) {
                      e.printStackTrace();
                      System.out.println("Error sending to the client");
                      break;
                  }

              }

          }
      }).start();
  }





}
