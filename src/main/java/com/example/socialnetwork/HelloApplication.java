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

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch();
        String password = "postgres";
        Repository<Long, User> userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres", password, new UserValidator());
        Repository<Long, Friendship> friendshipRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres", password, new FriendshipValidator());
        Repository<Long, Message> messageRepository = new MessageDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres",password,new MessageValidator());

        Repository<Long, FriendRequest> friendRequestDBRepository = new FriendRequestDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres", password, new FriendRequestValidator());


        Service srv = new Service(userDBRepository, friendshipRepository, messageRepository, friendRequestDBRepository);

        UI ui = new UI(srv);
        ui.run();
    }
}