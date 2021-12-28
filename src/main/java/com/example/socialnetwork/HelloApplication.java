package com.example.socialnetwork;

import com.example.socialnetwork.Domain.FriendRequest;
import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Message;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.FriendRequestValidator;
import com.example.socialnetwork.Domain.Validator.FriendshipValidator;
import com.example.socialnetwork.Domain.Validator.MessageValidator;
import com.example.socialnetwork.Domain.Validator.UserValidator;
import com.example.socialnetwork.Repository.Database.FriendRequestDBRepository;
import com.example.socialnetwork.Repository.Database.FriendshipDBRepository;
import com.example.socialnetwork.Repository.Database.MessageDBRepository;
import com.example.socialnetwork.Repository.Database.UserDBRepository;
import com.example.socialnetwork.Repository.Repository;
import com.example.socialnetwork.Service.Service;
import com.example.socialnetwork.UI.UI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication{
    public static void main(String[] args) {

        GUI gui = new GUI();
        gui.run();

        //asd
    }
}