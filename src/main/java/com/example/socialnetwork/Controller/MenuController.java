package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Controller.Controller;
import com.example.socialnetwork.Domain.FriendRequest;
import com.example.socialnetwork.Domain.FriendRequestDTO;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Utils.Events.UserAddedEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class MenuController extends Controller implements com.example.socialnetwork.Utils.Observer.Observer<UserAddedEvent> {
    User user;

    @FXML
    public Label usernameLabel;
    @FXML
    public Label numberOfFriends;
    @FXML
    public Label numberOfUsers;
    @FXML
    public Label numberOfSentFriendRequest;
    @FXML
    public Label numberOfReceivedFriendRequest;
    @FXML
    public Label numberOfMessages;
    @FXML
    public Label friendNameChat;
    @FXML
    public Button btnLogout;
    @FXML
    public Button btnOverview;
    @FXML
    public Button btnSearch;
    @FXML
    public Button btnSentFriendRequest;
    @FXML
    public Button btnReceivedFriendRequest;
    @FXML
    public Button btnMessages;
    @FXML
    public Pane pnlSearch;
    @FXML
    public Pane pnlOverview;
    @FXML
    public Pane pnlSentFriendRequest;
    @FXML
    public Pane pnlReceivedFriendRequest;
    @FXML
    public Pane pnlMessages;
    @FXML
    public Pane pnlChatRoom;
    @FXML
    public VBox pnItems;
    @FXML
    public VBox pnItemsFriend;
    @FXML
    public VBox pnSentItems;
    @FXML
    public VBox pnReceivedItems;
    @FXML
    public VBox pnMessages;
    @FXML
    public VBox pnChatRoomMessages;


    private void setUser(User user){
        this.user = user;
        usernameLabel.setText(user.getUsername());
    }

    public void init(User user) {
        srv.addObserver(this);
        setUser(user);
        initOverview(user);
        pnlOverview.toFront();

        initSearch(user);
        initSentFriendRequest(user);
        initReceivedFriendRequest(user);
        initMessages(user);
    }

    private void initOverview(User user){
        numberOfFriends.setText(String.valueOf(user.getFriends().size()));

        pnItems.getChildren().clear();

        try {
            for(Node node: gui.loadFriends(user)){
                pnItems.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSearch(User user){
        int count = 0;
        for(User u:srv.getAllUsers())
            count++;

        numberOfUsers.setText(String.valueOf(count));


        pnItemsFriend.getChildren().clear();

        try {
            for(Node node: gui.loadUsers(count, user)){
                pnItemsFriend.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initSentFriendRequest(User user){
        int count = 0;
        for(FriendRequest fr: srv.getSentFriendRequest(user))
            count++;

        numberOfSentFriendRequest.setText(String.valueOf(count));


        pnSentItems.getChildren().clear();

        try {
            for(Node node: gui.loadSentFriendRequest(count, user)){
                pnSentItems.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initReceivedFriendRequest(User user){
        int count = 0;
        for(FriendRequest fr: srv.getReceivedFriendRequest(user))
            count++;

        numberOfReceivedFriendRequest.setText(String.valueOf(count));


        pnReceivedItems.getChildren().clear();

        try {
            for(Node node: gui.loadReceivedFriendRequest(count, user)){
                pnReceivedItems.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initMessages(User user){
        numberOfMessages.setText(String.valueOf(user.getFriends().size()));

        pnMessages.getChildren().clear();

        try {
            for(Node node: gui.loadMessages(user)){
                pnMessages.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initChatRoom(User user, User friend){
        friendNameChat.setText(friend.getUsername());

        pnChatRoomMessages.getChildren().clear();

        try {
            for(Node node: gui.loadChatRoomMessages(user, friend)){
                pnChatRoomMessages.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        pnlChatRoom.toFront();
    }


    public void handleClicks(ActionEvent actionEvent){
        if (actionEvent.getSource() == btnSearch) {
            //initSearch(user);
            pnlSearch.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            //user = srv.getUserByUsername(user.getUsername());
            //init(user);
            //initOverview(user);
            pnlOverview.toFront();
        }
        if (actionEvent.getSource() == btnSentFriendRequest) {
            //initSentFriendRequest(user);
            pnlSentFriendRequest.toFront();
        }
        if (actionEvent.getSource() == btnReceivedFriendRequest) {
            //initReceivedFriendRequest(user);
            //user = srv.getUserByUsername(user.getUsername());
            pnlReceivedFriendRequest.toFront();
        }
        if (actionEvent.getSource() == btnMessages) {
            //initReceivedFriendRequest(user);
            //user = srv.getUserByUsername(user.getUsername());
            pnlMessages.toFront();
        }
    }

    public void logout(){
        try {
            gui.changeScene("Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(UserAddedEvent userAddedEvent) {
        System.out.println("Update");
        user = srv.getUserByUsername(user.getUsername());
        setUser(user);
        initOverview(user);
        initSearch(user);
        initSentFriendRequest(user);
        initReceivedFriendRequest(user);
        initMessages(user);
    }

    /*ObservableList<User> friendModel = FXCollections.observableArrayList();
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
    public TableColumn<FriendRequestDTO,LocalDateTime> receivedFriendRequestDate;
    @FXML
    public TableView<FriendRequestDTO> receivedFriendRequestTable;

    @FXML
    public TableColumn<FriendRequestDTO,String> sentFriendRequestColumn;
    @FXML
    public TableColumn<FriendRequestDTO,String> sentFriendRequestStatus;
    @FXML
    public TableColumn<FriendRequestDTO,LocalDateTime> sentFriendRequestDate;
    @FXML
    public TableView<FriendRequestDTO> sentFriendRequestTable;

    @FXML
    public Button logoutButton;

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
        FriendRequestDTO senderDTO = receivedFriendRequestTable.getSelectionModel().getSelectedItem();
        if(senderDTO == null){
            messageLabel.setText("Please select a friend request");
        }
        else{
            try{
                String sender = senderDTO.getUsername();
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
        receivedFriendRequestDate.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, LocalDateTime>("date"));
        receivedFriendRequestTable.setItems(friendRequestsModel);

        sentFriendRequestColumn.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("username"));
        sentFriendRequestStatus.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("status"));
        sentFriendRequestDate.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, LocalDateTime>("date"));
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
            FriendRequestDTO friendRequestDTO = new FriendRequestDTO(srv.getUserByID(friendRequest.getSenderID()).getUsername(), friendRequest.getStatus(), friendRequest.getDate());
            friendRequests.add(friendRequestDTO);
        }
        friendRequestsModel.setAll(friendRequests);
    }

    private void initModelSentFriendRequestTable(){
        List<FriendRequestDTO> friendRequests = new ArrayList<>();
        for(FriendRequest friendRequest:srv.getSentFriendRequest(user)){
            FriendRequestDTO friendRequestDTO = new FriendRequestDTO(srv.getUserByID(friendRequest.getReceiverID()).getUsername(), friendRequest.getStatus(), friendRequest.getDate());
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

    public void logout(){
        try {
            gui.changeScene("Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUser(User user){
        this.user = user;
        usernameLabel.setText("Current User: " + user.getUsername());
    }*/
}
