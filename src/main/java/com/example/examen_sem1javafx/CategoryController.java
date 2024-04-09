package com.example.examen_sem1javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private TableColumn<Category, Integer> colid;

    @FXML
    private TableColumn<Category, String> collibelle;
    @FXML
    private TableView<Category> tab_categery;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static ObservableList<Category> getCategoriesFromDB() {
        ObservableList<Category> categories = FXCollections.observableArrayList();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM category");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String libelle = resultSet.getString("libelle");
                categories.add(new Category(id, libelle));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        return categories;
    }


        public void updateTableView(TableView<Category> tableViewCategories) {

                tableViewCategories.setItems(getCategoriesFromDB());

        }




    public static void addcategorie(ActionEvent event, String libelle, TextField textFieldLibelle){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM category WHERE libelle = ? ");
            psCheckUserExists.setString(1,libelle);
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isFirst()){
                System.out.println("Ce nom existe déjà !");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Cette catégorie exite déjà !");
                alert.show();
            }else {
                psInsert = connection.prepareStatement("INSERT INTO category (libelle) VALUES (?)");
                psInsert.setString(1,libelle);
                psInsert.executeUpdate();
                textFieldLibelle.clear();


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists!=null){
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert!=null){
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void updateCategory(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            String query = "UPDATE category SET libelle = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category.getLibelle());
            preparedStatement.setInt(2, category.getId());
            preparedStatement.executeUpdate();


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Catégorie mise à jour avec succes !");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteCategory(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            String query = "DELETE FROM category  WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, category.getId());
            preparedStatement.executeUpdate();


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Catégorie supprimer avec succes !");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
