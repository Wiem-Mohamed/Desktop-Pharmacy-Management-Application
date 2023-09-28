package com.example.hotelmanagmet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class SupplierController  implements Initializable {
    private ObservableList<Supplier> supplierList;
    @FXML
    private Button add;
    @FXML
    private Button clear;
    @FXML
    private Button delete;
    @FXML
    private Button update;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField contact;
    @FXML
    private TableView<Supplier> tableview;
    @FXML
    private TableColumn<Supplier, String> idColumn;
    @FXML
    private TableColumn<Supplier, String>nameColumn;
    @FXML
    private TableColumn<Supplier, String> addressColumn;

    @FXML
    private TableColumn<Supplier, String> contactColumn;
    @FXML
    private TextField cherchers;
    @FXML
    void handleSearchTextField(KeyEvent event) {
        String searchCin = cherchers.getText();
        // Vérifier si le champ de recherche est vide
        if (searchCin.isEmpty()) {
            // Si le champ de recherche est vide, réinitialiser la table view
            tableview.setItems(supplierList); // Remplacer "tableView" par le nom de votre table view
        } else {
            // Créer une liste temporaire pour stocker les employés correspondant au CIN recherché
            ObservableList<Supplier> filteredList = FXCollections.observableArrayList();
            // Parcourir la liste des employés actuelle
            for (Supplier supplier : supplierList) { // Remplacer "employeeList" par le nom de votre liste d'employés
                // Vérifier si le CIN de l'employé correspond à la recherche
                if (supplier.getId ().equals(searchCin)) { // Remplacer "getCin()" par la méthode qui récupère le CIN dans votre classe Employee
                    // Ajouter l'employé à la liste temporaire
                    filteredList.add(supplier);
                }
            }
            // Mettre à jour la table view avec la liste filtrée d'employés
            tableview.setItems(filteredList); // Remplacer "tableView" par le nom de votre table view

        }
    }
    @FXML
    void addsupplier(ActionEvent event) {
        ConnectionDb conn = new ConnectionDb ();
        String ids = id.getText ();
        String names = name.getText ();
        String addresss = address.getText ();
        String contacts = contact.getText ();

        if (ids.equals ( "" ) || names.equals ( "" ) || addresss.equals ( "" ) || contacts.equals ( "" ) ) {
            Alert alert = new Alert ( Alert.AlertType.ERROR );
            alert.setTitle ( "Error" );
            alert.setHeaderText ( null );
            alert.setContentText ( "There is one or more empty fields !" );
            alert.showAndWait ();
        } else {
            try (PreparedStatement stmt = conn.connecter ().prepareStatement ( "SELECT * FROM supplier WHERE id=?" )) {
                stmt.setString ( 1, ids );
                ResultSet rs = stmt.executeQuery ();
                if (rs.next ()) {
                    //employee existe
                    Alert alert = new Alert ( Alert.AlertType.ERROR );
                    alert.setTitle ( "Error" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "supplier exists" );
                    alert.showAndWait ();

                } else {

                    String insertQuery = "INSERT INTO supplier(id,name,address,contact)  VALUES (?, ?,?,?)";
                    PreparedStatement stmt2 = conn.connecter ().prepareStatement ( insertQuery );
                    stmt2.setString ( 1, ids );
                    stmt2.setString ( 2, names );
                    stmt2.setString ( 3, addresss );
                    stmt2.setString ( 4, contacts );
                    int rowsAffected = stmt2.executeUpdate ();
                    if (rowsAffected > 0) {
                        Supplier newSupplier = new Supplier ( ids, names, addresss, contacts);
                        supplierList.add ( newSupplier );
                        clearfields();
                        System.out.println ( "New Supplier inserted into the database" );
                    } else {
                        System.out.println ( "Error inserting new supplier into the database" );
                    }


                }

            } catch (SQLException e) {
                throw new RuntimeException ( e );
            }
        }
    }
    @FXML
    private void deletesupplier(ActionEvent event) {
        Supplier selectedSupplier = tableview.getSelectionModel ().getSelectedItem ();
        if (selectedSupplier != null) {
            String id = selectedSupplier.getId ();
            System.out.println ( "selection existe" );
            ConnectionDb conn = new ConnectionDb ();
            try {
                String deleteQuery = "DELETE FROM supplier WHERE id = ?";
                PreparedStatement preparedStatement = conn.connecter ().prepareStatement ( deleteQuery );
                preparedStatement.setString ( 1, id );
                int rowsDeleted = preparedStatement.executeUpdate ();
                System.out.println ( "executequery" );
                if (rowsDeleted > 0) {
                    tableview.getItems ().remove ( selectedSupplier );
                    tableview.refresh ();
                    Alert alert = new Alert ( Alert.AlertType.INFORMATION );
                    alert.setTitle ( "Delete Supplier" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "Supplier deleted successfully." );
                    alert.showAndWait ();
                    clearfields();
                }
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        } else {
            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "Delete Supplier" );
            alert.setHeaderText ( null );
            alert.setContentText ( "Please select an employee to delete." );
            alert.showAndWait ();
        }
    }
    @FXML
    private void updatesupplier(ActionEvent event) {
        Supplier selectedSupplier = tableview.getSelectionModel ().getSelectedItem ();

        if (selectedSupplier != null) {
            ConnectionDb conn = new ConnectionDb ();
            String ids = id.getText ();
            String names = name.getText ();
            String addresss = address.getText ();
            String contacts = contact.getText ();

            try {
                String updateQuery = "UPDATE supplier SET id=?, name=?, address=?, contact=? WHERE id=?";
                PreparedStatement stmt = conn.connecter ().prepareStatement ( updateQuery );
                stmt.setString ( 1, ids ); // Set the new value for id
                stmt.setString ( 2, names );
                stmt.setString ( 3, addresss );
                stmt.setString ( 4, contacts );
                stmt.setString ( 5, selectedSupplier.getId () ); // Set the old value for id as a parameter in the WHERE clause
                int rowsAffected = stmt.executeUpdate ();
                if (rowsAffected > 0) {
                    selectedSupplier.setId ( ids );// Update the id value in the selected supplier object
                    selectedSupplier.setName ( names );
                    selectedSupplier.setAddress ( addresss);
                    selectedSupplier.setContact ( contacts );
                    tableview.refresh ();
                    Alert alert = new Alert ( Alert.AlertType.INFORMATION );
                    alert.setTitle ( "Update Supplier" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "Supplier updated successfully." );
                    alert.showAndWait ();
                } else {
                    System.out.println ( "Error updating Supplier in the database" );
                }
            } catch (SQLException e) {
                throw new RuntimeException ( e );
            }

        } else {
            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "Update Supplier" );
            alert.setHeaderText ( null );
            alert.setContentText ( "Please select a supplier to update." );
            alert.showAndWait ();
        }
    }

    @FXML

    private void cleartextfields(ActionEvent event) {
        id.clear();
        name.clear();
        address.clear();
        contact.clear();
    }

    private void clearfields() {
        id.clear();
        name.clear();
        address.clear();
        contact.clear();
    }


    public void loadSupplierData() {
        ConnectionDb conn = new ConnectionDb ();
        try {
            tableview.getItems ().clear ();
            String selectQuery = "SELECT * FROM supplier";
            ResultSet rs = conn.connecter ().createStatement ().executeQuery ( selectQuery );
            while (rs.next ()) {
                String id = rs.getString ( "id" );
                String name = rs.getString ( "name" );
                String address = rs.getString ( "address" );
                String contact = rs.getString ( "contact" );
                Supplier sup = new Supplier ( id, name, address,contact);
                tableview.getItems ().add ( sup);
            }
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //pour que lorsque je selectionne un employé les texfield seront automatiquement remplis
        tableview.getSelectionModel ().selectedItemProperty ().addListener ( (observable, oldValue, newValue) -> {
            if (newValue != null) {
                id.setText ( newValue.getId () );
                name.setText ( newValue.getName () );
                address.setText ( newValue.getAddress () );
                contact.setText ( newValue.getContact () );

            }
        } );
      idColumn.setCellValueFactory ( new PropertyValueFactory<> ( "id" ) );
      nameColumn.setCellValueFactory ( new PropertyValueFactory<> ( "name" ) );
        addressColumn.setCellValueFactory ( new PropertyValueFactory<> ( "address" ) );
        contactColumn.setCellValueFactory ( new PropertyValueFactory<> ( "contact" ) );
      supplierList= FXCollections.observableArrayList ();
        tableview.setItems (supplierList );
        loadSupplierData ();

    }
}
