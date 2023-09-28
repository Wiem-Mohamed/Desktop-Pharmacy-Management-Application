package com.example.hotelmanagmet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class SalesController implements Initializable {


    @FXML
    TableView<Sale>salesTableView;
    @FXML
    ObservableList<Sale> saleList;

    @FXML
    TextField medecineCodeField;
    @FXML
    Spinner<Integer>saleSpinner;
    @FXML
    Label saleAmount;

    @FXML
    Button addToCard;
    @FXML
    Label totalSale;
    @FXML
    Button payBTN;
    @FXML
    private TableColumn<Sale, String> medecineCodeColumn;
    @FXML
    private TableColumn<Sale, String> medecineNameColumn;
    @FXML
    private TableColumn<Sale, Integer> saleQttColumn;

    @FXML
    private TableColumn<Sale, Float> saleAmountColumn;
    @FXML
    private TableColumn<Sale, Integer> SaleCodeColumn;


    private float total=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        saleList = FXCollections.observableArrayList ();
        //pour que lorsque je selectionne un med les texfield seront automatiquement remplis
        if(salesTableView!=null){
            //update fiedls with selected row values
            salesTableView.getSelectionModel ().selectedItemProperty ().addListener ( (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    medecineCodeField.setText(newValue.getMedecineCode());
                    saleAmount.setText(String.valueOf(newValue.getAmount()));
                    SpinnerValueFactory<Integer> valueFactorySet = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, newValue.getQuantity(), 1);
                    saleSpinner.setValueFactory(valueFactorySet);
                }
            } );


            saleSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                float price;
                float amount = 0;
                if(!medecineCodeField.getText().isEmpty()){
                    String medecineCode = medecineCodeField.getText();

                    ConnectionDb conn = new ConnectionDb();
                    try {
                        //Error request here:
                        String selectQuery = "SELECT medecine.price, medecine.name, medecine.medecineCode " +
                                "FROM medecine " +
                                "WHERE medecine.medecineCode = '" + medecineCodeField.getText() + "'";

                        ResultSet rs = conn.connecter().createStatement().executeQuery(selectQuery);
                            while (rs.next()) {
                                String currentMedecineCode = rs.getString("medecineCode");
                                    price = rs.getFloat("price");
                                    System.out.println(price);
                                    amount = newValue * price;
                                 }
                                // check if amount is null before inserting sale
                                if (amount == 0) {
                                    throw new SQLException("Could not find sale with code " + medecineCode);
                                }

                            } catch (SQLException e) {
                                System.err.println("An error occurred while inserting sale  : " + e.getMessage());
                                return; // exit the method without inserting sale
                            }

                        }

                        saleAmount.setText(String.valueOf(amount));
                    });

            //************ les colonnes de table view a quoi font référence ***************
            // Specify every column will contains each attribut of medecine object before using tableView.setItems

            SaleCodeColumn.setCellValueFactory ( new PropertyValueFactory<>( "saleCode" ) );
            medecineCodeColumn.setCellValueFactory ( new PropertyValueFactory<>( "medecineCode" ) );
            medecineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            saleQttColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            saleAmountColumn.setCellValueFactory ( new PropertyValueFactory<> ( "amount" ) );

            //  the TableView will display the Medecine objects contained in the medecineList.
            salesTableView.setItems ( saleList );
            //initialize spinner
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1);
            saleSpinner.setValueFactory(valueFactory);
            loadSaleData();

        }

    }


    public void loadSaleData() {
        ConnectionDb conn = new ConnectionDb();
        try {
            salesTableView.getItems().clear();
            String selectQuery = "SELECT sale.saleCode, sale.medecineCode, medecine.name, sale.quantity, sale.amount " +
                    "FROM sale " +
                    "JOIN medecine ON sale.medecineCode = medecine.medecineCode";
            ResultSet rs = conn.connecter().createStatement().executeQuery(selectQuery);
            while (rs.next()) {
                Integer saleCode=rs.getInt("saleCode");
                String medecineCode = rs.getString("medecineCode");
                String name = rs.getString("name");
                Integer quantity = rs.getInt("quantity");
                Float amount = rs.getFloat("amount");
                Sale sale = new Sale(saleCode,medecineCode, name, quantity, amount);
                salesTableView.getItems().add(sale);
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while executing a database query: " + e.getMessage());
        }
    }


    //test si codeMed saisie ne corr a aucun code dans la base faire une alerte !!!
    public void addsale(ActionEvent actionEvent) {
        String medecineCode = medecineCodeField.getText();
        String medecineName = "";
        Integer quantity = saleSpinner.getValue();
        Float amount = null; // initialize to null
        Float price ;

        ConnectionDb conn = new ConnectionDb();
        try {
            salesTableView.getItems().clear();
            String selectQuery = "SELECT medecine.price, medecine.name, medecine.medecineCode " +
                    "FROM medecine " +
                    "WHERE medecine.medecineCode = '" + medecineCodeField.getText() + "'";

            ResultSet rs = conn.connecter().createStatement().executeQuery(selectQuery);
            while (rs.next()) {
                    medecineName = rs.getString("name");
                    price = rs.getFloat("price");
                    System.out.println(price);
                    amount = quantity * price;
                    // do something with the price value, for example:
                    System.out.println("The price of medicine " + medecineCode + " is " + price + " name " + medecineName);
                    break; // exit the loop since we found the medicine we were looking for
            }
            // check if amount is null before inserting sale
            if (amount == null) {
                Alert alertCode = new Alert(Alert.AlertType.ERROR);
                alertCode.setTitle("Error");
                alertCode.setHeaderText(null);
                alertCode.setContentText("Could not find medicine with code");
                alertCode.showAndWait();
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting sale  : " + e.getMessage());
            return; // exit the method without inserting sale
        }

        if (medecineCode.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Any Field cannot be blank");
            alert.showAndWait();
        } else {
            ConnectionDb con = new ConnectionDb();
            con.connecter();
            String insertQuery = "INSERT INTO sale (medecineCode,quantity,amount) VALUES (?,?,?)";
            PreparedStatement stmt = null;
            try {
                stmt = con.connecter().prepareStatement(insertQuery);
                stmt.setString(1, medecineCode);
                stmt.setString(2, String.valueOf(quantity));
                stmt.setString(3, String.valueOf(amount));

                // Load the image from the ImageView

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("sale added to data base");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting sale to data base");
                e.printStackTrace();
            } finally {
                try {
                    loadSaleData();
                    total=amount+total;
                    totalSale.setText(String.valueOf(total));
                    clear();
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
    private void clear() {
        medecineCodeField.clear();
        SpinnerValueFactory<Integer> valueFactorySet = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1);
        saleSpinner.setValueFactory(valueFactorySet);
    }

    public void payment(ActionEvent actionEvent) {
        totalSale.setText("0 $");
        total=0;

    }


    @FXML
    private void clearsalefields(ActionEvent event) {
        medecineCodeField.clear();
        SpinnerValueFactory<Integer> valueFactorySet = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1);
        saleSpinner.setValueFactory(valueFactorySet);
    }

    @FXML
    private void deletesale(ActionEvent event){
        //retrieves the currently selected item from a table view of medicines
        Sale selectedSale = salesTableView.getSelectionModel ().getSelectedItem ();
        if (selectedSale != null) {
            Integer saleCode = selectedSale.getSaleCode();
            System.out.println ( "selection existe" );
            ConnectionDb conn = new ConnectionDb ();
            try {
                String deleteQuery = "DELETE FROM sale WHERE saleCode = ?";
                PreparedStatement preparedStatement = conn.connecter ().prepareStatement ( deleteQuery );
                preparedStatement.setInt ( 1, saleCode );
                int rowsDeleted = preparedStatement.executeUpdate ();
                System.out.println ( "executequery" );
                if (rowsDeleted > 0) {
                    // remove a selected medicine from the list of medicines displayed in the tableView
                    salesTableView.getItems ().remove ( selectedSale );
                    salesTableView.refresh ();
                    Alert alert = new Alert ( Alert.AlertType.INFORMATION );
                    alert.setTitle ( "Delete Sale" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "Sale deleted successfully." );
                    alert.showAndWait ();
                    clear();
                }
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        } else {
            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "Delete Sale" );
            alert.setHeaderText ( null );
            alert.setContentText ( "Please select a Sale to delete." );
            alert.showAndWait ();
        }
    }


    @FXML
    private void updateSale(ActionEvent event) {
        Sale selectedSale = salesTableView.getSelectionModel ().getSelectedItem ();
        float price;
        float NEWamount = 0;
        Integer NEWquantity;
        String NEWmedecineCode = null;

        if (selectedSale != null) {
            ConnectionDb conn = new ConnectionDb ();
            NEWquantity= Integer.valueOf(saleSpinner.getValue ());


            if(!medecineCodeField.getText().isEmpty()){
                NEWmedecineCode=medecineCodeField.getText();

                ConnectionDb con = new ConnectionDb();
                try {
                    String selectQuery = "SELECT medecine.price, medecine.name, medecine.medecineCode " +
                            "FROM medecine " +
                            "WHERE medecine.medecineCode = '" + medecineCodeField.getText() + "'";

                    ResultSet rs = con.connecter().createStatement().executeQuery(selectQuery);
                    while (rs.next()) {
                        String currentMedecineCode = rs.getString("medecineCode");
                            price = rs.getFloat("price");
                            System.out.println(price);
                            NEWamount = NEWquantity * price;
                            // do something with the price value, for example:
                            break; // exit the loop since we found the medicine we were looking for

                    }
                    // check if amount is null before inserting sale
                    if (NEWamount == 0) {
                        Alert alertCode = new Alert(Alert.AlertType.ERROR);
                        alertCode.setTitle("Error");
                        alertCode.setHeaderText(null);
                        alertCode.setContentText("Could not find medicine with code");
                        alertCode.showAndWait();                    }
                } catch (SQLException e) {
                    System.err.println("An error occurred while inserting sale  : " + e.getMessage());
                    return; // exit the method without inserting sale
                }

            }


            try {
                String updateQuery = "UPDATE sale SET medecineCode=? ,quantity=?, amount=? WHERE saleCode=?";
                PreparedStatement stmt = conn.connecter ().prepareStatement ( updateQuery );
                stmt.setString ( 1, NEWmedecineCode );
                stmt.setInt ( 2, NEWquantity );
                stmt.setFloat ( 3, NEWamount );
                stmt.setInt(4, selectedSale.getSaleCode());


                int rowsAffected = stmt.executeUpdate ();
                if (rowsAffected > 0) {
                    selectedSale.setMedecineCode ( NEWmedecineCode ); // Update the code value in the selected medecine object
                    selectedSale.setQuantity ( NEWquantity );
                    selectedSale.setAmount ( NEWamount );
                    selectedSale.setQuantity ( NEWquantity );
                    salesTableView.refresh ();
                    Alert alert = new Alert ( Alert.AlertType.INFORMATION );
                    alert.setTitle ( "Update sale" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "SAle updated successfully." );
                    alert.showAndWait ();
                } else {

                    System.out.println ( "Error updating sale in the database" );
                }
            } catch (SQLException e) {
                System.err.println("An error occurred while inserting sale  : " + e.getMessage());
            }

        } else {
            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "Update Sale" );
            alert.setHeaderText ( null );
            alert.setContentText ( "Please select an sale to update." );
            alert.showAndWait ();
        }
    }



}
