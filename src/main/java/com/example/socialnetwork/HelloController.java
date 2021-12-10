package com.example.socialnetwork;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    public Label welcomeText;

    @FXML
    public void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}