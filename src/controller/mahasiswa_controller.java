package controller;

import helpers.db_connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.mahasiswa;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mahasiswa_controller implements Initializable {

    @FXML
    private TextField name_field;
    @FXML
    private TextField nim_field;
    @FXML
    private TableView <mahasiswa> mahasiswa_table;
    @FXML
    private TableColumn <mahasiswa, Integer> no_urut_coloumn;
    @FXML
    private TableColumn <mahasiswa, String> nim_coloumn;
    @FXML
    private TableColumn <mahasiswa, String> nama_coloumn;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    mahasiswa mahasiswa = null;

    ObservableList<mahasiswa> list_mahasiswa = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        DBLoad();
    }

    //Insert Query Action Button
    @FXML
    public void save_btn() {
        connection = db_connection.getConnect();
        String nim = nim_field.getText();
        String nama = name_field.getText();

        if (nim.isEmpty() || nama.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All Data!!");
            alert.showAndWait();
        } else {
            getQuery();
            insert();
            clean();
        }
    }

    private void clean() {
        nim_field.setText(null);
        name_field.setText(null);
    }

    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,nim_field.getText());
            preparedStatement.setString(2,name_field.getText());
            preparedStatement.execute();
            refresh_table();
        } catch (SQLException ex) {
            Logger.getLogger(mahasiswa_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getQuery() {
        try {
            query = "INSERT INTO mahasiswa (`nim`, `nama`) VALUES (?,?)";
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(mahasiswa_controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //Update Query Action Button
    @FXML
    public void edit_btn() {
        connection = db_connection.getConnect();
        String nim = nim_field.getText();
        String nama = name_field.getText();

        if (nim.isEmpty() || nama.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All Data!!");
            alert.showAndWait();
        } else {
            getQuery_update();
            insert_update();
            clean_update();
        }
    }

    private void clean_update() {
        nim_field.setText(null);
        name_field.setText(null);
    }

    private void insert_update() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,nim_field.getText());
            preparedStatement.setString(2,name_field.getText());
            preparedStatement.execute();
            refresh_table();
        } catch (SQLException ex) {
            Logger.getLogger(mahasiswa_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getQuery_update() {
        try {
            query = "UPDATE mahasiswa SET nim=?, nama=? WHERE id = '"+no_urut_coloumn.getCellData(mahasiswa_table.getSelectionModel().getSelectedItem())+"'";
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(mahasiswa_controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void delete_btn() {
        try {
            mahasiswa = mahasiswa_table.getSelectionModel().getSelectedItem();
            query = "DELETE FROM mahasiswa WHERE id =" +mahasiswa.getId();
            connection = db_connection.getConnect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            refresh_table();
        } catch (SQLException ex) {
            Logger.getLogger(mahasiswa_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh_table() {
        try {
            list_mahasiswa.clear();

            query = "SELECT * FROM mahasiswa";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list_mahasiswa.add(new mahasiswa(
                        resultSet.getInt("id"),
                        resultSet.getString("nim"),
                        resultSet.getString("nama")));
                mahasiswa_table.setItems(list_mahasiswa);
            }
        } catch (SQLException ex) {
            Logger.getLogger(mahasiswa_controller.class.getName()).log(Level.SEVERE,null, ex);
        }
    }


    public void DBLoad(){
        connection = db_connection.getConnect();
        refresh_table();

        no_urut_coloumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nim_coloumn.setCellValueFactory(new PropertyValueFactory<>("nim"));
        nama_coloumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
    }

    public void selected_row() {
        mahasiswa mahasiswa = mahasiswa_table.getSelectionModel().getSelectedItem();
        nim_field.setText(mahasiswa.getNim());
        name_field.setText(mahasiswa.getNama());
    }

}

