package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.Message;
import com.example.socialnetwork.Domain.User;
import javafx.scene.control.Label;

public class ChatRoomMessageItemController extends Controller{
    private User user;
    private User friend;
    private MenuController menuController;

    public Label senderName;
    public Label messageText;

    public void init(User user, User friend, User sender, Message message, MenuController menuController) {
        this.user = user;
        this.friend = friend;
        senderName.setText(sender.getUsername());
        messageText.setText(message.getMessage());
        this.menuController = menuController;
    }
}
