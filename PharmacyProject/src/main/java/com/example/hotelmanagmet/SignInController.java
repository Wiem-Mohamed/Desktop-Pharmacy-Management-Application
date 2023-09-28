package com.example.hotelmanagmet;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    @FXML
    Button login;

    @FXML
    PasswordField passwordField;


    @FXML
    Button signup;

    @FXML
    TextField username;

    @FXML
    AnchorPane signUpContainer;

    @FXML
   public  AnchorPane mainContainer;

    @FXML
    AnchorPane signInContainer;


    @FXML
    Button gosignupBtn;
    @FXML
    Button gosigninBtn;

    @FXML
    Button gotoemployeesBTN;
    @FXML
    Button gotomedecinesBTN;
    @FXML
    Button gotosalesBTN;
    @FXML
    Button gotosupplierBTN;
    @FXML
   public  Button gotochatBTN;
    @FXML
    AnchorPane homeContainer;
    @FXML
    AnchorPane medecinesContainer;
    @FXML
    public  AnchorPane sideBarContainer;

    @FXML
    AnchorPane employeesContainer;
    @FXML
    AnchorPane salesContainer;
    @FXML
    AnchorPane supplierContainer;


    @FXML
    TextField medecineCodeField2;

    @FXML
    TextField medecineNameField2;

    @FXML
    TextField medecinePriceField2;

    @FXML
    TextField medecineQttField2;


    @FXML
    Button addmedecineBTNhome;


    //*********************tableview  and columns*****************
    @FXML
     TableView<Medecine> medecineTableView;
    @FXML
     TableColumn<Medecine, String> medecineCodeColumn;
    @FXML
     TableColumn<Medecine, String> nameColumn;

    @FXML
     TableColumn<Medecine,String> priceColumn;

    @FXML
     TableColumn<Medecine,String> quantityColumn;

    ObservableList<Medecine> medecineList;

    @FXML
    TextField chercher;

    @FXML
    AnchorPane chatContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        medecineList = FXCollections.observableArrayList ();
        //pour que lorsque je selectionne un med les texfield seront automatiquement remplis
        if(medecineTableView!=null){
            //update fiedls with selected row values
            medecineTableView.getSelectionModel ().selectedItemProperty ().addListener ( (observable, oldValue, newValue) -> {
            if (newValue != null) {
                medecineCodeField2.setText(newValue.getMedecineCode());
                medecineNameField2.setText(newValue.getName () );
                medecinePriceField2.setText(String.valueOf(newValue.getPrice()));
                medecineQttField2.setText(String.valueOf(newValue.getQuantity()));
            }
            } );
            //************ les colonnes de table view a quoi font référence ***************
            // Specify every column will contains each attribut of medecine object before using tableView.setItems
            medecineCodeColumn.setCellValueFactory ( new PropertyValueFactory<>( "medecineCode" ) );
            nameColumn.setCellValueFactory ( new PropertyValueFactory<> ( "name" ) );
            priceColumn.setCellValueFactory ( new PropertyValueFactory<> ( "price" ) );
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            //  the TableView will display the Medecine objects contained in the medecineList.
            medecineTableView.setItems ( medecineList );
            loadMedecineData();
        }
    }

    @FXML
    void loginbutton(ActionEvent event) {
        String uname =  username.getText();

        // Récupération du texte saisi dans le PasswordField
        String pass = passwordField.getText();

        if(uname.equals("") && pass.equals("")){
            System.out.println("yup");
            Alert alert = new Alert( Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username or password blank");
            alert.showAndWait();
        }
        else{
            ConnectionDb conn =new ConnectionDb ();
            conn.connecter ();
            // Query the database for the user with the given username and password
            String query = "SELECT * FROM admin WHERE name=? AND password=?";
            try (PreparedStatement stmt = conn.connecter ().prepareStatement (query)) {
                stmt.setString(1, uname);
                stmt.setString(2, pass);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotelmanagmet/home.fxml"));
                        homeContainer = loader.load();
                        mainContainer.getChildren().setAll(homeContainer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // The username and password are correct
                    int id = rs.getInt("id");
                    System.out.println("Welcome, admin with ID " + id);
                } else {
                    // The username or password is incorrect, show an error message
                    String query2 = "SELECT * FROM admin WHERE name=?";
                    try (PreparedStatement stmt2 = conn.connecter ().prepareStatement (query2)) {
                        stmt2.setString(1, uname);
                        ResultSet rs2 = stmt2.executeQuery();
                        if (rs2.next()) {
                            // The username is correct but the password is incorrect
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Login Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Invalid password");
                            alert.showAndWait();
                        } else {
                            String insertQuery = "INSERT INTO admin (name, password) VALUES (?, ?)";
                            PreparedStatement stmt3 = conn.connecter().prepareStatement (insertQuery);
                            stmt3.setString(1, uname);
                            stmt3.setString(2, pass);
                            int rowsAffected = stmt3.executeUpdate();
                            if (rowsAffected >0) {
                                System.out.println("New admin inserted into the database");
                            } else {
                                System.out.println("Error inserting new admin into the database");
                            }
                        }
                    } catch (SQLException ex) {
                        // Handle any SQL errors
                        ex.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                // Handle any SQL errors
                ex.printStackTrace();
            }


        }




    }



    @FXML void goToSignup(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotelmanagmet/sign-up.fxml"));
            signUpContainer = loader.load();
            mainContainer.getChildren().setAll(signUpContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML void goToSignin(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotelmanagmet/sign-in.fxml"));
            signInContainer = loader.load();
            mainContainer.getChildren().setAll(signInContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void signupbutton(ActionEvent event) {
        String uname = username.getText();
        String pass = passwordField.getText();
        if (uname.equals("") || pass.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username or password cannot be blank");
            alert.showAndWait();
        } else {
            ConnectionDb conn = new ConnectionDb();
            conn.connecter();
            String insertQuery = "INSERT INTO admin (name, password) VALUES (?, ?)";
            PreparedStatement stmt = null;
            try {
                stmt = conn.connecter().prepareStatement(insertQuery);
                stmt.setString(1, uname);
                stmt.setString(2, pass);
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Admin added to database");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting admin to database");
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    conn.connecter().close();
                } catch (SQLException e) {
                    System.out.println("Error closing database connection");
                    e.printStackTrace();
                }
            }
        }
    }




    @FXML void gotoemployees(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotelmanagmet/employees.fxml"));
            employeesContainer = loader.load();
            sideBarContainer.getChildren().setAll(employeesContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML void gotomedecines(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotelmanagmet/medecines.fxml"));
            medecinesContainer = loader.load();
            sideBarContainer.getChildren().setAll(medecinesContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML void gotosales(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotelmanagmet/sales.fxml"));
            salesContainer = loader.load();
            sideBarContainer.getChildren().setAll(salesContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void gotosupplier(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotelmanagmet/supplier.fxml"));
            supplierContainer = loader.load();
            sideBarContainer.getChildren().setAll(supplierContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
    @FXML void gotochat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader ( getClass ().getResource ( "/com/example/hotelmanagmet/chat.fxml" ) );
            chatContainer = loader.load ();
            sideBarContainer.getChildren ().setAll ( chatContainer );
            System.out.println ( "Lancement Serveur!" );

        } catch (IOException e) {
            e.printStackTrace ();
        }

    }
*/


    public void loadMedecineData() {
        ConnectionDb conn = new ConnectionDb ();
        try {
            medecineTableView.getItems ().clear ();
            String selectQuery = "SELECT * FROM medecine";
            ResultSet rs = conn.connecter ().createStatement ().executeQuery ( selectQuery );
            while (rs.next ()) {
                String name = rs.getString ( "name" );
                Float price = Float.valueOf(rs.getString( "price" ));
                Integer quantity =Integer.valueOf( rs.getString ( "quantity" ));
                String medecineCode = rs.getString ( "medecineCode" );
                Medecine medecine = new Medecine ( medecineCode, name, price, quantity);
                //TableView  immediately in real-time display the new Medecine object in the table view, without having to modify the medecineList
                // Specify every column will contains each attribut of medecine object before using tableView.getItems
                medecineTableView.getItems ().add (medecine);
            }
        } catch (SQLException e) {
//            e.printStackTrace ();
            System.out.println(e.getMessage());
        }

    }

    public void addmedecine(ActionEvent actionEvent) {
        String medecineCode = medecineCodeField2.getText();
        String medecineName = medecineNameField2.getText();
        String medecinePrice = medecinePriceField2.getText();
        String medecineQtt = medecineQttField2.getText();

        if (medecineCode.equals("") || medecineName.equals("")|| medecinePrice.equals("")|| medecineQtt.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Any Field cannot be blank");
            alert.showAndWait();
        } else {
            ConnectionDb conn = new ConnectionDb();
            conn.connecter();
            String insertQuery = "INSERT INTO medecine (medecineCode, name,price,quantity) VALUES (?, ?,?,?)";
            PreparedStatement stmt = null;
            try {
                stmt = conn.connecter().prepareStatement(insertQuery);
                stmt.setString(1, medecineCode);
                stmt.setString(2, medecineName);
                stmt.setString(3, medecinePrice);
                stmt.setString(4, medecineQtt);

                // Load the image from the ImageView

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("medecine added to data base");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting medecine  to data base");
                e.printStackTrace();
            } finally {
                try {
                    loadMedecineData();
                    if (stmt != null) {
                        stmt.close();
                    }
                    conn.connecter().close();
                } catch (SQLException e) {
                    System.out.println("Error closing database connection");
                    e.printStackTrace();
                }
            }
        }
    }




    @FXML
    private void deletemedecine(ActionEvent event){
        //retrieves the currently selected item from a table view of medicines
        Medecine selectedMedecine = medecineTableView.getSelectionModel ().getSelectedItem ();
        if (selectedMedecine != null) {
            String medecineCode = selectedMedecine.getMedecineCode();
            System.out.println ( "selection existe" );
            ConnectionDb conn = new ConnectionDb ();
            try {
                String deleteQuery = "DELETE FROM medecine WHERE medecineCode = ?";
                PreparedStatement preparedStatement = conn.connecter ().prepareStatement ( deleteQuery );
                preparedStatement.setString ( 1, medecineCode );
                int rowsDeleted = preparedStatement.executeUpdate ();
                System.out.println ( "executequery" );
                if (rowsDeleted > 0) {
                    // remove a selected medicine from the list of medicines displayed in the tableView
                    medecineTableView.getItems ().remove ( selectedMedecine );
                    medecineTableView.refresh ();
                    Alert alert = new Alert ( Alert.AlertType.INFORMATION );
                    alert.setTitle ( "Delete Medecine" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "Medecine deleted successfully." );
                    alert.showAndWait ();
                }
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        } else {
            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "Delete Medecine" );
            alert.setHeaderText ( null );
            alert.setContentText ( "Please select a medecine to delete." );
            alert.showAndWait ();
        }
    }




    @FXML
    private void updateMedecine(ActionEvent event) {
        Medecine selectedMedecine = medecineTableView.getSelectionModel ().getSelectedItem ();

        if (selectedMedecine != null) {
            ConnectionDb conn = new ConnectionDb ();
            String NEWmedecineCode = medecineCodeField2.getText ();
            String NEWname = medecineNameField2.getText ();
            Float NEWprice = Float.valueOf(medecinePriceField2.getText ());
            Integer NEWquantity = Integer.valueOf(medecineQttField2.getText ());
            try {
                String updateQuery = "UPDATE medecine SET medecineCode=?, name=?, price=?, quantity=? WHERE medecineCode=?";
                PreparedStatement stmt = conn.connecter ().prepareStatement ( updateQuery );
                stmt.setString ( 1, NEWmedecineCode ); // Set the new value of code
                stmt.setString ( 2, NEWname );
                stmt.setFloat ( 3, NEWprice );
                stmt.setInt ( 4, NEWquantity );

                stmt.setString ( 5, selectedMedecine.getMedecineCode () ); // Set the old value of code as a parameter in the WHERE clause

                int rowsAffected = stmt.executeUpdate ();
                if (rowsAffected > 0) {
                    selectedMedecine.setMedecineCode ( NEWmedecineCode ); // Update the code value in the selected medecine object
                    selectedMedecine.setName ( NEWname );
                    selectedMedecine.setPrice ( NEWprice );
                    selectedMedecine.setQuantity ( NEWquantity );
                    medecineTableView.refresh ();
                    Alert alert = new Alert ( Alert.AlertType.INFORMATION );
                    alert.setTitle ( "Update Medecin" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "Medecine updated successfully." );
                    alert.showAndWait ();
                } else {
                    System.out.println ( "Error updating medecine in the database" );
                }
            } catch (SQLException e) {
                throw new RuntimeException ( e );
            }

        } else {
            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "Update Employee" );
            alert.setHeaderText ( null );
            alert.setContentText ( "Please select an employee to update." );
            alert.showAndWait ();
        }
    }

    @FXML
    private void clearmedecinefields(ActionEvent event) {
        medecineCodeField2.clear();
        medecineNameField2.clear();
        medecinePriceField2.clear();
        medecineQttField2.clear();
    }



    @FXML
    //seach by code
    void handleSearchTextField(KeyEvent event) {
        String searchCode = chercher.getText();
        // Vérifier si le champ de recherche est vide
        if (searchCode.isEmpty()) {
            // Si le champ de recherche est vide, réinitialiser la table view
            medecineTableView.setItems(medecineList); // Remplacer "tableView" par le nom de votre table view
        } else {
            // Créer une liste temporaire pour stocker les employés correspondant au CIN recherché
            ObservableList<Medecine> filteredList = FXCollections.observableArrayList();
            // Parcourir la liste des employés actuelle
            for (Medecine medecine : medecineList) { // Remplacer "employeeList" par le nom de votre liste d'employés
                // Vérifier si le CIN de l'employé correspond à la recherche
                if (medecine.getMedecineCode().equals(searchCode)) { // Remplacer "getCin()" par la méthode qui récupère le CIN dans votre classe Employee
                    // Ajouter l'employé à la liste temporaire

                    filteredList.add(medecine);
                }
            }
            // Mettre à jour la table view avec la liste filtrée d'employés
            medecineTableView.setItems(filteredList); // Remplacer "tableView" par le nom de votre table view

        }
    }


    public static Server server;

    @FXML void gotochat(ActionEvent event) {
        try {


            FXMLLoader loader = new FXMLLoader ( getClass ().getResource ( "/com/example/hotelmanagmet/chat.fxml" ) );
            chatContainer = loader.load ();
            sideBarContainer.getChildren ().setAll ( chatContainer );

            System.out.println ( "go to chat" );

        } catch (IOException e) {
            e.printStackTrace ();
        }

        new Thread(() -> {
            try {
                server = new Server(new ServerSocket(2222));
                System.out.println("Server started on port 2222");
            } catch (IOException e) {
//                throw new RuntimeException(e);
                System.out.println("wait for a client");
            }
        }).start();

    }


    public static void addLabel(String messageFromClient, VBox vbox){
        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text=new Text(messageFromClient);
        TextFlow textFlow=new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(233,233,235)"+
                "-fx-background-radius:20px");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        //Ensure that  All modifications to the UI when adding smth to UI should be performed on the JavaFX application thread
        // to ensure proper synchronization and prevent concurrency issues.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);

            }
        });
    }




}