package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ItemController extends Controller{
    private User user;
    private User friend;
    private MenuController menuController;

    public Label friendName;
    @FXML
    public Button btnDeleteFriend;

    public void init(User user, User friend, MenuController menuController) {
        this.user = user;
        this.friend = friend;
        friendName.setText(friend.getUsername());
        this.menuController = menuController;
    }

    public void deleteFriend(){
        try{
            srv.removeFriendship(user.getUsername(), friend.getUsername());
            //user = srv.getUserById(user.getId());
            //menuController.init(user);
            //messageLabel.setText("Friend Removed");
            //initModelFriendTable();
        }catch (IllegalArgumentException | ValidationException ex) {
            //messageLabel.setText(ex.toString());
        }
    }
}
