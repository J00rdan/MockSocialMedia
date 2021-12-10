package com.example.socialnetwork;

import com.example.socialnetwork.Controller.Controller;
import com.example.socialnetwork.Controller.LoginController;
import com.example.socialnetwork.Controller.MenuController;
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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {
    private Service srv;
    private static Stage stage;

    public GUI(){
        setService();
    }

    public void setService(){
        String password = "postgres";
        Repository<Long, User> userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres", password, new UserValidator());
        Repository<Long, Friendship> friendshipRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres", password, new FriendshipValidator());
        Repository<Long, Message> messageRepository = new MessageDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres",password,new MessageValidator());

        Repository<Long, FriendRequest> friendRequestDBRepository = new FriendRequestDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres", password, new FriendRequestValidator());


        srv = new Service(userDBRepository, friendshipRepository, messageRepository, friendRequestDBRepository);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        Controller loginController = fxmlLoader.getController();
        loginController.setService(srv, this);

        primaryStage.setTitle("Social Network");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent pane = fxmlLoader.load();
        stage.getScene().setRoot(pane);
    }

    public void changeSceneToMenu(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent pane = fxmlLoader.load();

        MenuController menuController = fxmlLoader.getController();
        menuController.setService(srv, this);
        menuController.setUser(user);

        stage.getScene().setRoot(pane);
    }

    public void run(){
        launch();
    }


}
