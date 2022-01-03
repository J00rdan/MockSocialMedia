package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MessageItemController extends Controller{
    private User user;
    private User friend;
    private MenuController menuController;

    public Label friendName;

    public void init(User user, User friend, MenuController menuController) {
        this.user = user;
        this.friend = friend;
        friendName.setText(friend.getUsername());
        this.menuController = menuController;
    }

    public void openChat(){
        menuController.initChatRoom(user, friend);
    }
}
