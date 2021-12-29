package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.FriendRequest;
import com.example.socialnetwork.Domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SentItemController extends Controller{
    private User user;
    private User friend;
    private MenuController menuController;
    private FriendRequest friendRequest;

    public Label friendName;
    public Label statusLabel;
    @FXML
    public Button btnUndo;

    public void init(User user, User friend, FriendRequest friendRequest, MenuController menuController) {
        this.user = user;
        this.friend = friend;
        this.friendRequest = friendRequest;
        friendName.setText(friend.getUsername());
        statusLabel.setText(friendRequest.getStatus());
        this.menuController = menuController;
    }

    public void undoFriendRequest(){
        if(friendRequest.getStatus().equals("Pending")) {
            srv.deleteFriendRequest(friendRequest);
            menuController.initSentFriendRequest(user);
        }
    }
}
