package com.example.hotelmanagmet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MedecineController implements Initializable {

    //*********************tableview  and columns*****************
    @FXML
    TextField medecineCodeField1;

    @FXML
    TextField medecineNameField1;

    @FXML
    TextField medecinePriceField1;

    @FXML
    TextField medecineQttField1;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medecineList = FXCollections.observableArrayList ();
        //pour que lorsque je selectionne un med les texfield seront automatiquement remplis
        if(medecineTableView!=null){
            //update fiedls with selected row values
            medecineTableView.getSelectionModel ().selectedItemProperty ().addListener ( (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    medecineCodeField1.setText(newValue.getMedecineCode());
                    medecineNameField1.setText(newValue.getName () );
                    medecinePriceField1.setText(String.valueOf(newValue.getPrice()));
                    medecineQttField1.setText(String.valueOf(newValue.getQuantity()));
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
        String medecineCode = medecineCodeField1.getText();
        String medecineName = medecineNameField1.getText();
        String medecinePrice = medecinePriceField1.getText();
        String medecineQtt = medecineQttField1.getText();

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
                    clearfields();
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
                    clearfields();
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
            String NEWmedecineCode = medecineCodeField1.getText ();
            String NEWname = medecineNameField1.getText ();
            Float NEWprice = Float.valueOf(medecinePriceField1.getText ());
            Integer NEWquantity = Integer.valueOf(medecineQttField1.getText ());
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
                    clearfields();
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
    private void clearfields() {
        medecineCodeField1.clear();
        medecineNameField1.clear();
        medecinePriceField1.clear();
        medecineQttField1.clear();
    }

    @FXML
    private void clearmedecinefields(ActionEvent event) {
        medecineCodeField1.clear();
        medecineNameField1.clear();
        medecinePriceField1.clear();
        medecineQttField1.clear();
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

}
