package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class MenuController extends Controller{

    ObservableList<User> model = FXCollections.observableArrayList();
    User user;

    @FXML
    public Label usernameLabel;
    @FXML
    public TextField friendTextField;
    @FXML
    public Button friendRequestButton;
    @FXML
    public Button acceptFriendshipButton;
    @FXML
    public Button declineFriendshipButton;
    @FXML
    public Button deleteFriendButton;
    @FXML
    public TableColumn<User,String> friendColumn;
    @FXML
    public TableView<User> friendTableView;

    public void sendFriendRequest(){

    }

    public void deleteFriend(){

    }

    public void acceptFriendship(){

    }

    public void declineFriendship(){

    }

    @FXML
    public void initialize(){
        friendColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        friendTableView.setItems(model);
    }

    private void initModel(){
        List<User> friends = new ArrayList<>();
        for(Long id: user.getFriends()){
            friends.add(srv.getUserByID(id));
        }
        model.setAll(friends);
    }

    public void init(User user){
        setUser(user);
        initModel();
    }

    private void setUser(User user){
        this.user = user;
        usernameLabel.setText("Current User: " + user.getUsername());
    }
}
