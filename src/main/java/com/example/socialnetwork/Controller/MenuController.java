package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.FriendRequest;
import com.example.socialnetwork.Domain.FriendRequestDTO;
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

    ObservableList<User> friendModel = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> friendRequestsModel = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> sentFriendRequestModel = FXCollections.observableArrayList();
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

    @FXML
    public TableColumn<FriendRequestDTO,String> receivedFriendRequestColumn;
    @FXML
    public TableColumn<FriendRequestDTO,String> receivedFriendRequestStatus;
    @FXML
    public TableView<FriendRequestDTO> receivedFriendRequestTable;

    @FXML
    public TableColumn<FriendRequestDTO,String> sentFriendRequestColumn;
    @FXML
    public TableColumn<FriendRequestDTO,String> sentFriendRequestStatus;
    @FXML
    public TableView<FriendRequestDTO> sentFriendRequestTable;

    public void sendFriendRequest(){
        String username = friendTextField.getText();

        try{
            if(srv.sendFriendRequest(user.getUsername(), username) == null) {
                messageLabel.setText("Friend Request Sent!");
                initModelSentFriendRequestTable();
            }
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
        String sender = receivedFriendRequestTable.getSelectionModel().getSelectedItem().getUsername();
        if(sender == null){
            messageLabel.setText("Please select a friend request");
        }
        else{
            try{
                srv.answerRequest(sender, user.getUsername(), "Y");
                messageLabel.setText("Answer sent!");
                user = srv.getUserByUsername(user.getUsername());
                initModelFriendTable();
                initModelReceivedFriendRequestTable();
            }catch (IllegalArgumentException | ValidationException ex) {
                messageLabel.setText(ex.toString());
            }
        }
    }

    public void declineFriendship(){
        FriendRequestDTO senderDTO = receivedFriendRequestTable.getSelectionModel().getSelectedItem();
        if(senderDTO == null){
            messageLabel.setText("Please select a friend request");
        }
        else{
            try{
                String sender = senderDTO.getUsername();
                srv.answerRequest(sender, user.getUsername(), "N");
                messageLabel.setText("Answer sent!");
                user = srv.getUserByUsername(user.getUsername());
                initModelFriendTable();
                initModelReceivedFriendRequestTable();
            }catch (IllegalArgumentException | ValidationException ex) {
                messageLabel.setText(ex.toString());
            }
        }
    }

    @FXML
    public void initialize(){
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(messageLabel, 0.0);
        AnchorPane.setRightAnchor(messageLabel, 0.0);
        messageLabel.setAlignment(Pos.CENTER);

        friendColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        friendTableView.setItems(friendModel);

        receivedFriendRequestColumn.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("username"));
        receivedFriendRequestStatus.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("status"));
        receivedFriendRequestTable.setItems(friendRequestsModel);

        sentFriendRequestColumn.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("username"));
        sentFriendRequestStatus.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("status"));
        sentFriendRequestTable.setItems(sentFriendRequestModel);
    }

    private void initModelFriendTable(){
        List<User> friends = new ArrayList<>();
        for(Long id: user.getFriends()){
            friends.add(srv.getUserByID(id));
        }
        friendModel.setAll(friends);
    }

    private void initModelReceivedFriendRequestTable(){
        List<FriendRequestDTO> friendRequests = new ArrayList<>();
        for(FriendRequest friendRequest:srv.getReceivedFriendRequest(user)){
            FriendRequestDTO friendRequestDTO = new FriendRequestDTO(srv.getUserByID(friendRequest.getSenderID()).getUsername(), friendRequest.getStatus());
            friendRequests.add(friendRequestDTO);
        }
        friendRequestsModel.setAll(friendRequests);
    }

    private void initModelSentFriendRequestTable(){
        List<FriendRequestDTO> friendRequests = new ArrayList<>();
        for(FriendRequest friendRequest:srv.getSentFriendRequest(user)){
            FriendRequestDTO friendRequestDTO = new FriendRequestDTO(srv.getUserByID(friendRequest.getReceiverID()).getUsername(), friendRequest.getStatus());
            friendRequests.add(friendRequestDTO);
        }
        sentFriendRequestModel.setAll(friendRequests);
    }

    public void init(User user){
        setUser(user);
        initModelFriendTable();
        initModelReceivedFriendRequestTable();
        initModelSentFriendRequestTable();
    }

    private void setUser(User user){
        this.user = user;
        usernameLabel.setText("Current User: " + user.getUsername());
    }
}
