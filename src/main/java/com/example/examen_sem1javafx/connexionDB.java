package com.example.examen_sem1javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class connexionDB {

    private static String driver ="com.mysql.jdbc.Driver";;

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String nom) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(connexionDB.class.getResource(fxmlFile));
            root = loader.load();
            if (nom != null) {
                MenueController menueController = loader.getController();
                menueController.setInformation(nom);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du fichier FXML: " + fxmlFile);
            return;
        }

        if (root == null) {
            System.out.println("Le fichier FXML chargé est null: " + fxmlFile);
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }



    public static Connection getConnection() {
        Connection connection = null;
        try {

            Class.forName(driver);
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static void signUpUser(ActionEvent event, String nom, String prenom, Integer telephone, String email, String login, String password  ){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE email = ? OR login = ?");
            psCheckUserExists.setString(1,email);
            psCheckUserExists.setString(2,login);
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isFirst()){
                System.out.println("Email ou login déjà utilié !");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vous ne pouvez pas utiliser ces identifiants !");
                alert.show();
            }else {
                psInsert = connection.prepareStatement("INSERT INTO user (nom, prenom, telephone, email, login, password) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setString(1,nom);
                psInsert.setString(2,prenom);
                psInsert.setInt(3,telephone);
                psInsert.setString(4,email);
                psInsert.setString(5,login);
                psInsert.setString(6,password);
                psInsert.executeUpdate();

                changeScene(event, "/Fxml/menu.fxml", "Menue",nom);
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
    public static void login(ActionEvent envent, String login, String password){
        Connection connection=null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =null;


        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            preparedStatement=connection.prepareStatement("SELECT login, password FROM user WHERE  login = ?");
            preparedStatement.setString(1, login);
            resultSet =preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()){
                System.out.println("Utilisateur inexistant !");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Login ou mot de passe incorrects !");
                alert.show();
            }else {
                        changeScene(envent, "/Fxml/menu.fxml", "Menue", null);


                }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }


        }
    }
}
