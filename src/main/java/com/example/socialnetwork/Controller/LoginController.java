package com.example.socialnetwork.Controller;

import com.example.socialnetwork.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginController extends Controller{
    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label wrongLogin;

    public void userLogin(ActionEvent actionEvent) {
        String username = usernameField.getText().toString();
        String password2 = passwordField.getText().toString();

        if(srv.login(username)) {
            wrongLogin.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(wrongLogin, 0.0);
            AnchorPane.setRightAnchor(wrongLogin, 0.0);
            wrongLogin.setAlignment(Pos.CENTER);
            wrongLogin.setText("Success");

            try {
                gui.changeSceneToMenu(srv.getUserByUsername(username));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            wrongLogin.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(wrongLogin, 0.0);
            AnchorPane.setRightAnchor(wrongLogin, 0.0);
            wrongLogin.setAlignment(Pos.CENTER);
            wrongLogin.setText("Wrong Login Credentials");

        }

    }
}