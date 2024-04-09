package com.example.examen_sem1javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private Button btn_login;

    @FXML
    private Button btn_signup;

    @FXML
    private TextField ch_email;

    @FXML
    private TextField ch_login;

    @FXML
    private TextField ch_nom;

    @FXML
    private TextField ch_prenom;

    @FXML
    private TextField ch_pwd;

    @FXML
    private TextField ch_tel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!ch_nom.getText().trim().isEmpty() && !ch_prenom.getText().trim().isEmpty()&& !ch_email.getText().trim().isEmpty() && !ch_pwd.getText().trim().isEmpty()&& !ch_tel.getText().trim().isEmpty()&& !ch_login.getText().trim().isEmpty()){
                    connexionDB.signUpUser(event, ch_nom.getText(), ch_prenom.getText(), Integer.parseInt(ch_tel.getText()), ch_email.getText(), ch_login.getText(), ch_pwd.getText());
                }else {
                    System.out.println("Veillez remplir tous les champs");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Veuillez remplir tous les champs avant inscription !");
                    alert.show();
                }
            }
        });

        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                connexionDB.changeScene(event, "/Fxml/login.fxml", "connexion", null);
            }
        });
    }
}
