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

public class EmployeeController implements Initializable {


    @FXML
    private TextField cin;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField phone;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private TextField birth;
    @FXML
    private TextField member;
    @FXML
    private TextField chercher;
    @FXML
    private TextField salary;
    @FXML
    private Button add;
    @FXML
    private Button clear;
    @FXML
    private Button delete;
    @FXML
    private Button update;
    //*********************tableview  and columns*****************
    @FXML
    private TableView<employee> tableviewe;
    @FXML
    private TableColumn<employee, String> cineColumn;
    @FXML
    private TableColumn<employee, String> datebirtheColumn;

    @FXML
    private TableColumn<employee, String> datemembereColumn;

    @FXML
    private TableColumn<employee, String> firstnameeColumn;
    @FXML
    private TableColumn<employee, String> lastnameeColumn;

    @FXML
    private TableColumn<employee, String> phoneeColumn;

    @FXML
    private TableColumn<employee, String> salaryeColumn;

    @FXML
    private TableColumn<employee, String> gendereColumn;
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeList = FXCollections.observableArrayList ();
        //pour que lorsque je selectionne un employé les texfield seront automatiquement remplis
        tableviewe.getSelectionModel ().selectedItemProperty ().addListener ( (observable, oldValue, newValue) -> {
            if (newValue != null) {
                cin.setText ( newValue.getCin () );
                firstname.setText ( newValue.getFirstname () );
                lastname.setText ( newValue.getLastname () );
                phone.setText ( newValue.getPhone () );
                salary.setText ( newValue.getSalary () );
                member.setText ( newValue.getDatemember () );
                gender.setValue ( newValue.getGender () );
                birth.setText ( newValue.getDatebirth () );
            }
        } );
        ///************les colonnes de table view a quoi font référence***************
        cineColumn.setCellValueFactory ( new PropertyValueFactory<> ( "cin" ) );
        firstnameeColumn.setCellValueFactory ( new PropertyValueFactory<> ( "firstname" ) );
        lastnameeColumn.setCellValueFactory ( new PropertyValueFactory<> ( "lastname" ) );
        phoneeColumn.setCellValueFactory ( new PropertyValueFactory<> ( "phone" ) );
        gendereColumn.setCellValueFactory ( new PropertyValueFactory<> ( "gender" ) );
        salaryeColumn.setCellValueFactory ( new PropertyValueFactory<> ( "salary" ) );
        datebirtheColumn.setCellValueFactory ( new PropertyValueFactory<> ( "datebirth" ) );
        datemembereColumn.setCellValueFactory ( new PropertyValueFactory<> ( "datemember" ) );
        tableviewe.setItems ( employeeList );
        ObservableList<String> genderlist = FXCollections.observableArrayList ( "Male", "Female" );
        gender.setItems ( genderlist );
        loadEmployeeData ();

    }
    @FXML
    void handleSearchTextField(KeyEvent event) {
        String searchCin = chercher.getText();
        // Vérifier si le champ de recherche est vide
        if (searchCin.isEmpty()) {
            // Si le champ de recherche est vide, réinitialiser la table view
            tableviewe.setItems(employeeList); // Remplacer "tableView" par le nom de votre table view
        } else {
            // Créer une liste temporaire pour stocker les employés correspondant au CIN recherché
            ObservableList<employee> filteredList = FXCollections.observableArrayList();
            // Parcourir la liste des employés actuelle
            for (employee employee : employeeList) { // Remplacer "employeeList" par le nom de votre liste d'employés
                // Vérifier si le CIN de l'employé correspond à la recherche
                if (employee.getCin().equals(searchCin)) { // Remplacer "getCin()" par la méthode qui récupère le CIN dans votre classe Employee
                    // Ajouter l'employé à la liste temporaire
                    filteredList.add(employee);
                }
            }
            // Mettre à jour la table view avec la liste filtrée d'employés
            tableviewe.setItems(filteredList); // Remplacer "tableView" par le nom de votre table view

        }
    }
    ObservableList<employee> employeeList;
    @FXML
    void addemployee(ActionEvent event) {
        ConnectionDb conn = new ConnectionDb ();
        String cine = cin.getText ();
        String firstnamee = firstname.getText ();
        String lastnamee = lastname.getText ();
        String phonee = phone.getText ();
        String datebirthe = birth.getText ();
        String datemembere = member.getText ();
        String salarye = salary.getText ();
        String gendere = gender.getValue ();
        if (cine.equals ( "" ) || lastnamee.equals ( "" ) || phonee.equals ( "" ) || datebirthe.equals ( "" ) || datemembere.equals ( "" ) || salarye.equals ( "" ) || cine.equals ( "" ) || gendere.equals ( "choose" )) {
            Alert alert = new Alert ( Alert.AlertType.ERROR );
            alert.setTitle ( "Error" );
            alert.setHeaderText ( null );
            alert.setContentText ( "There is one or more empty fields !" );
            alert.showAndWait ();
        } else {
            try (PreparedStatement stmt = conn.connecter ().prepareStatement ( "SELECT * FROM employee WHERE cin=?" )) {
                stmt.setString ( 1, cine );
                ResultSet rs = stmt.executeQuery ();
                if (rs.next ()) {
                    //employee existe
                    Alert alert = new Alert ( Alert.AlertType.ERROR );
                    alert.setTitle ( "Error" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "Employee exists" );
                    alert.showAndWait ();

                } else {

                    String insertQuery = "INSERT INTO employee(cin,firstname,lastname,phone,gender,salary,datebirth,datemember)  VALUES (?, ?,?,?,?,?,?,?)";
                    PreparedStatement stmt2 = conn.connecter ().prepareStatement ( insertQuery );
                    stmt2.setString ( 1, cine );
                    stmt2.setString ( 2, firstnamee );
                    stmt2.setString ( 3, lastnamee );
                    stmt2.setString ( 4, phonee );
                    stmt2.setString ( 5, gendere );
                    stmt2.setString ( 6, salarye );
                    stmt2.setString ( 7, datebirthe );
                    stmt2.setString ( 8, datemembere );
                    int rowsAffected = stmt2.executeUpdate ();
                    employee newEmployee = new employee ( cine, firstnamee, lastnamee, phonee, gendere, salarye, datebirthe, datemembere );
                    if (rowsAffected > 0) {
                        employeeList.add ( newEmployee );
                        clearfields();
                        System.out.println ( "New employee inserted into the database" );
                    } else {
                        System.out.println ( "Error inserting new employee into the database" );
                    }


                }

            } catch (SQLException e) {
                throw new RuntimeException ( e );
            }
        }
    }

    @FXML
    private void deleteemployee(ActionEvent event) {
        employee selectedEmployee = tableviewe.getSelectionModel ().getSelectedItem ();
        if (selectedEmployee != null) {
            String cin = selectedEmployee.getCin ();
            System.out.println ( "selection existe" );
            ConnectionDb conn = new ConnectionDb ();
            try {
                String deleteQuery = "DELETE FROM employee WHERE cin = ?";
                PreparedStatement preparedStatement = conn.connecter ().prepareStatement ( deleteQuery );
                preparedStatement.setString ( 1, cin );
                int rowsDeleted = preparedStatement.executeUpdate ();
                System.out.println ( "executequery" );
                if (rowsDeleted > 0) {
                    tableviewe.getItems ().remove ( selectedEmployee );
                    tableviewe.refresh ();
                    Alert alert = new Alert ( Alert.AlertType.INFORMATION );
                    alert.setTitle ( "Delete Employee" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "Employee deleted successfully." );
                    alert.showAndWait ();
                    clearfields();

                }
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        } else {
            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "Delete Employee" );
            alert.setHeaderText ( null );
            alert.setContentText ( "Please select an employee to delete." );
            alert.showAndWait ();
        }
    }

    @FXML
    private void updateemployee(ActionEvent event) {
        employee selectedEmployee = tableviewe.getSelectionModel ().getSelectedItem ();

        if (selectedEmployee != null) {
            ConnectionDb conn = new ConnectionDb ();
            String cine = cin.getText ();
            String firstnamee = firstname.getText ();
            String lastnamee = lastname.getText ();
            String phonee = phone.getText ();
            String datebirthe = birth.getText ();
            String datemembere = member.getText ();
            String salarye = salary.getText ();
            String gendere = gender.getValue ();
            try {
                String updateQuery = "UPDATE employee SET cin=?, firstname=?, lastname=?, phone=?, gender=?, salary=?, datebirth=?, datemember=? WHERE cin=?";
                PreparedStatement stmt = conn.connecter ().prepareStatement ( updateQuery );
                stmt.setString ( 1, cine ); // Set the new value for cin
                stmt.setString ( 2, firstnamee );
                stmt.setString ( 3, lastnamee );
                stmt.setString ( 4, phonee );
                stmt.setString ( 5, gendere );
                stmt.setString ( 6, salarye );
                stmt.setString ( 7, datebirthe );
                stmt.setString ( 8, datemembere );
                stmt.setString ( 9, selectedEmployee.getCin () ); // Set the old value for cin as a parameter in the WHERE clause
                int rowsAffected = stmt.executeUpdate ();
                if (rowsAffected > 0) {
                    selectedEmployee.setCin ( cine ); // Update the cin value in the selected employee object
                    selectedEmployee.setFirstname ( firstnamee );
                    selectedEmployee.setLastname ( lastnamee );
                    selectedEmployee.setPhone ( phonee );
                    selectedEmployee.setGender ( gendere );
                    selectedEmployee.setSalary ( salarye );
                    selectedEmployee.setDatebirth ( datebirthe );
                    selectedEmployee.setDatemember ( datemembere );
                    tableviewe.refresh ();
                    Alert alert = new Alert ( Alert.AlertType.INFORMATION );
                    alert.setTitle ( "Update Employee" );
                    alert.setHeaderText ( null );
                    alert.setContentText ( "Employee updated successfully." );
                    alert.showAndWait ();
                } else {
                    System.out.println ( "Error updating employee in the database" );
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
    private void cleartextfields(ActionEvent event) {
        cin.clear();
        firstname.clear();
        lastname.clear();
        phone.clear();
        birth.clear();
        member.clear();
        salary.clear();
        gender.getSelectionModel().clearSelection();
    }

    private void clearfields() {
        cin.clear();
        firstname.clear();
        lastname.clear();
        phone.clear();
        birth.clear();
        member.clear();
        salary.clear();
        gender.getSelectionModel().clearSelection();
    }

    public void loadEmployeeData() {
        ConnectionDb conn = new ConnectionDb ();
        try {
            tableviewe.getItems ().clear ();
            String selectQuery = "SELECT * FROM employee";
            ResultSet rs = conn.connecter ().createStatement ().executeQuery ( selectQuery );
            while (rs.next ()) {
                String cine = rs.getString ( "cin" );
                String firstnamee = rs.getString ( "firstname" );
                String lastnamee = rs.getString ( "lastname" );
                String phonee = rs.getString ( "phone" );
                String gendere = rs.getString ( "gender" );
                String salarye = rs.getString ( "salary" );
                String datebirthe = rs.getString ( "datebirth" );
                String datemembere = rs.getString ( "datemember" );
                employee emp = new employee ( cine, firstnamee, lastnamee, phonee, gendere, salarye, datebirthe, datemembere );
                tableviewe.getItems ().add ( emp );
            }
        } catch (SQLException e) {
            e.printStackTrace ();
        }

    }

}

