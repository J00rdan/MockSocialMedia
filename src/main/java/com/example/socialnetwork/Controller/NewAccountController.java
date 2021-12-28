package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NewAccountController extends Controller{
    @FXML
    public Button createAccountButton;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label wrongLogin;

    public void createAccount(ActionEvent actionEvent) {
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String username = usernameField.getText().toString();
        String password2 = passwordField.getText().toString();

        try{
            if(srv.addUser(firstName, lastName, username) == null){
                wrongLogin.setMaxWidth(Double.MAX_VALUE);
                AnchorPane.setLeftAnchor(wrongLogin, 0.0);
                AnchorPane.setRightAnchor(wrongLogin, 0.0);
                wrongLogin.setAlignment(Pos.CENTER);
                wrongLogin.setText("Success");

                try {
                    gui.changeScene("Login.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (IllegalArgumentException | ValidationException ex) {
            wrongLogin.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(wrongLogin, 0.0);
            AnchorPane.setRightAnchor(wrongLogin, 0.0);
            wrongLogin.setAlignment(Pos.CENTER);
            wrongLogin.setText(ex.toString());
        }

    }
}
