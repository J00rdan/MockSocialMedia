package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class MenuController extends Controller{

    ObservableList<User> model = FXCollections.observableArrayList();
    User user;

    @FXML
    public Label usernameLabel;
    @FXML
    public Label messageLabel;
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
        String username = friendTextField.getText();

        try{
            if(srv.sendFriendRequest(user.getUsername(), username) == null)
                messageLabel.setText("Friend Request Sent!");
        }catch (IllegalArgumentException | ValidationException ex) {
            messageLabel.setText(ex.toString());
        }
    }

    public void deleteFriend(){
        User friend = friendTableView.getSelectionModel().getSelectedItem();
        if(friend == null){
            messageLabel.setText("Please select a friend");
        }else{
            try{
                srv.removeFriendship(user.getUsername(), friend.getUsername());
                user = srv.getUserById(user.getId());
                messageLabel.setText("Friend Removed");
                initModelFriendTable();
            }catch (IllegalArgumentException | ValidationException ex) {
                messageLabel.setText(ex.toString());
            }
        }


    }

    public void acceptFriendship(){

    }

    public void declineFriendship(){

    }

    @FXML
    public void initialize(){
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(messageLabel, 0.0);
        AnchorPane.setRightAnchor(messageLabel, 0.0);
        messageLabel.setAlignment(Pos.CENTER);

        friendColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        friendTableView.setItems(model);
    }

    private void initModelFriendTable(){
        List<User> friends = new ArrayList<>();
        for(Long id: user.getFriends()){
            friends.add(srv.getUserByID(id));
        }
        model.setAll(friends);
    }

    public void init(User user){
        setUser(user);
        initModelFriendTable();
    }

    private void setUser(User user){
        this.user = user;
        usernameLabel.setText("Current User: " + user.getUsername());
    }
}
