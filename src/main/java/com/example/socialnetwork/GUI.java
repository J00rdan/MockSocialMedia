package com.example.socialnetwork;

import com.example.socialnetwork.Controller.*;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class GUI extends Application {
    private Service srv;
    private static Stage stage;
    private MenuController menuController;

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
        Scene scene = new Scene(fxmlLoader.load(), 1050, 600);

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

        Controller loginController = fxmlLoader.getController();
        loginController.setService(srv, this);

        stage.getScene().setRoot(pane);
    }

    public void changeSceneToMenu(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu2.fxml"));
        Parent pane = fxmlLoader.load();

        menuController = fxmlLoader.getController();
        menuController.setService(srv, this);
        menuController.init(user);

        stage.getScene().setRoot(pane);
    }

    public Node[] loadFriends(User user) throws IOException{
        Node[] nodes = new Node[user.getFriends().size()];

        int i = 0;

        for(long id:user.getFriends()){
            final int j = i;

            User friend = srv.getUserByID(id);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Item.fxml"));
            nodes[i] = fxmlLoader.load();

            ItemController itemController = fxmlLoader.getController();
            itemController.setService(srv, this);
            itemController.init(user, friend, menuController);

            nodes[i].setOnMouseEntered(event -> {
                nodes[j].setStyle("-fx-background-color : #0A0E3F");
            });
            nodes[i].setOnMouseExited(event -> {
                nodes[j].setStyle("-fx-background-color : #02030A");
            });

            i++;

        }
        return nodes;
    }

    public Node[] loadMessages(User user) throws IOException{
        Node[] nodes = new Node[user.getFriends().size()];

        int i = 0;

        for(long id:user.getFriends()){
            final int j = i;

            User friend = srv.getUserByID(id);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MessageItem.fxml"));
            nodes[i] = fxmlLoader.load();

            MessageItemController itemController = fxmlLoader.getController();
            itemController.setService(srv, this);
            itemController.init(user, friend, menuController);

            nodes[i].setOnMouseEntered(event -> {
                nodes[j].setStyle("-fx-background-color : #0A0E3F");
            });
            nodes[i].setOnMouseExited(event -> {
                nodes[j].setStyle("-fx-background-color : #02030A");
            });

            i++;

        }
        return nodes;
    }

    public Node[] loadChatRoomMessages(User user, User friend) throws IOException{
        List<Message> conversation = srv.messagesBetween2Users(user.getUsername(), friend.getUsername());
        conversation = srv.sortConversationsByDate(conversation);

        Node[] nodes = new Node[conversation.size()];

        int i = 0;

        for(Message m: conversation){
            final int j = i;

            //User friend = srv.getUserByID(id);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatRoomMessageItem.fxml"));
            nodes[i] = fxmlLoader.load();

            ChatRoomMessageItemController itemController = fxmlLoader.getController();
            itemController.setService(srv, this);
            itemController.init(user, friend, srv.getUserById(m.getFrom()), m, menuController);

            nodes[i].setOnMouseEntered(event -> {
                nodes[j].setStyle("-fx-background-color : #0A0E3F");
            });
            nodes[i].setOnMouseExited(event -> {
                nodes[j].setStyle("-fx-background-color : #02030A");
            });

            i++;

        }
        return nodes;
    }

    public Node[] loadUsers(int count, User user) throws IOException{
        Node[] nodes = new Node[count - 1];

        int i = 0;

        for(User friend: srv.getAllUsers()){
            if(!friend.equals(user)){
                final int j = i;

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserItem.fxml"));
                nodes[i] = fxmlLoader.load();

                UserItemController itemController = fxmlLoader.getController();
                itemController.setService(srv, this);
                itemController.init(user, friend, menuController);

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #02030A");
                });

                i++;
            }
        }
        return nodes;
    }

    public Node[] loadSentFriendRequest(int count, User user) throws IOException{
        Node[] nodes = new Node[count];

        int i = 0;

        for(FriendRequest fr:srv.getSentFriendRequest(user)){
                final int j = i;

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SentFriendRequestItem.fxml"));
                nodes[i] = fxmlLoader.load();

                SentItemController itemController = fxmlLoader.getController();
                itemController.setService(srv, this);
                itemController.init(user, srv.getUserById(fr.getReceiverID()), fr, menuController);

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #02030A");
                });

                i++;
        }
        return nodes;
    }

    public Node[] loadReceivedFriendRequest(int count, User user) throws IOException{
        Node[] nodes = new Node[count];

        int i = 0;

        for(FriendRequest fr:srv.getReceivedFriendRequest(user)){
            final int j = i;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReceivedFriendRequestItem.fxml"));
            nodes[i] = fxmlLoader.load();

            ReceivedItemController itemController = fxmlLoader.getController();
            itemController.setService(srv, this);
            itemController.init(user, srv.getUserById(fr.getSenderID()), fr, menuController);

            nodes[i].setOnMouseEntered(event -> {
                nodes[j].setStyle("-fx-background-color : #0A0E3F");
            });
            nodes[i].setOnMouseExited(event -> {
                nodes[j].setStyle("-fx-background-color : #02030A");
            });

            i++;
        }
        return nodes;
    }

    public void run(){
        launch();
    }


}
