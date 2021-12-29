package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.FriendRequest;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ReceivedItemController extends Controller{
    private User user;
    private User friend;
    private MenuController menuController;
    private FriendRequest friendRequest;

    public Label friendName;
    public Label statusLabel;
    @FXML
    public Button btnAccept;
    @FXML
    public Button btnReject;

    public void init(User user, User friend, FriendRequest friendRequest, MenuController menuController) {
        this.user = user;
        this.friend = friend;
        this.friendRequest = friendRequest;
        friendName.setText(friend.getUsername());
        statusLabel.setText(friendRequest.getStatus());
        this.menuController = menuController;
    }

    public void acceptFriendRequest(){
        try{
            srv.answerRequest(friend.getUsername(), user.getUsername(), "Y");
            //System.out.println("Answer sent!");
            user = srv.getUserByUsername(user.getUsername());
            //menuController.initReceivedFriendRequest(user);
        }catch (IllegalArgumentException | ValidationException ex) {
            System.out.println(ex.toString());
        }
    }

    public void rejectFriendRequest(){
        try{
            srv.answerRequest(friend.getUsername(), user.getUsername(), "N");
            //System.out.println("Answer sent!");
            user = srv.getUserByUsername(user.getUsername());
            //menuController.initReceivedFriendRequest(user);
        }catch (IllegalArgumentException | ValidationException ex) {
            System.out.println(ex.toString());
        }
    }
}
