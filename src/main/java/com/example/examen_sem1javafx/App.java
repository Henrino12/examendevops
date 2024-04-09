package com.example.examen_sem1javafx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/login.fxml"));
        stage.setTitle("Connexion");
        stage.setScene(new Scene(root, 600,400));
        stage.show();


    }

    public static void main(String[] args){
        launch();

    }
}
