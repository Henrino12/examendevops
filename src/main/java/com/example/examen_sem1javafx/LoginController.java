package com.example.examen_sem1javafx;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.examen_sem1javafx.connexionDB.changeScene;

public class LoginController implements Initializable {
    @FXML
    private Button btn_login;

    @FXML
    private Button btn_signup;

    @FXML
    private TextField con_login;

    @FXML
    private TextField con_password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                connexionDB.login(event, con_login.getText(), con_password.getText());
            }
        });

    btn_signup.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            connexionDB.changeScene(event, "/Fxml/sign_up.fxml", "Inscription", null);
        }
    });
    }

}


