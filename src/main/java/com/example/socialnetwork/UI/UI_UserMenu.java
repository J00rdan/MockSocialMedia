package com.example.socialnetwork.UI;


import com.example.socialnetwork.Domain.FriendRequest;
import com.example.socialnetwork.Domain.Message;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.EntityNotFoundException;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI_UserMenu {
    private Service srv;
    private User user;
    private static Scanner sc;

    public UI_UserMenu(Service srv, String username) {
        this.srv = srv;
        this.user = srv.getUserByUsername(username);
        sc = new Scanner(System.in);
    }

    private static final String menu =
                    "1. Send message\n" +
                    "2. Reply to message\n"+
                    "3. Show received messages\n"+
                    "4. Show sent messages\n"+
                    "5. Delete message\n"+
                            "6. Add Friend\n" +
                            "7. Remove Friend\n" +
                            "8. Show Sent Friend Requests\n" +
                            "9. Show Received Friend Requests\n" +
                            "10. Answer Friend Request\n" +
                            "11. Show Friends\n" +
                            "0. Logout\n" +
            "Command Input:";

    public void run(){
        boolean isRunning = true;

        while(isRunning){
            System.out.print(menu);
            int command = sc.nextInt();
            switch (command){
                case 0:
                    isRunning = false;
                    break;
                case 1:{
                    System.out.println("Introduce message: ");
                    sc.nextLine();
                    String text = sc.nextLine();
                    System.out.println("User to send to (username): ");
                    String username = sc.next();
                    List<Long> receivers = new ArrayList<>();
                    try{
                        User user1 = srv.getUserByUsername(username);
                        receivers.add(user1.getId());
                        String answear = "";
                        while (!answear.equals("Y") && !answear.equals("N"))
                        {
                            System.out.println("Send to other users?[Y/N]");
                            answear = sc.next();
                            if (answear.equals("N"))
                                break;
                            else
                            while (answear.equals("Y"))
                            {

                                System.out.println("User to send to (username): ");
                                String username2 = sc.next();
                                try{
                                    User user2 = srv.getUserByUsername(username2);
                                    receivers.add(user2.getId());
                                }catch (IllegalArgumentException | ValidationException ex) {
                                    System.out.println(ex.toString());
                                }
                                System.out.println("Send to other users?[Y/N]");
                                answear = sc.next();
                                if (answear.equals("N"))
                                    break;
                            }
                        }
                        srv.addMessage(this.user.getId(),receivers,text,null);
                        System.out.println("Message sent!");
                    }catch (IllegalArgumentException | ValidationException ex) {
                            System.out.println(ex.toString());
                    }
                    break;}
                case 2:{
                    System.out.println("Reply to message (enter id):");
                    try{
                        Long idToReply = Long.parseLong(sc.next());
                        System.out.println("Introduce message: ");
                        sc.nextLine();
                        String text = sc.nextLine();
                        List<Long> receivers = new ArrayList<>();
                        receivers.add(srv.getMessageById(idToReply).getFrom());
                        String answear = "";
                        while (!answear.equals("Y") && !answear.equals("N"))
                        {
                            System.out.println("Send to other users?[Y/N]");
                            answear = sc.next();
                            if (answear.equals("N"))
                                break;
                            else
                                while (answear.equals("Y"))
                                {
                                    System.out.println("User to send to (username): ");
                                    String username2 = sc.next();
                                    try{
                                        User user2 = srv.getUserByUsername(username2);
                                        receivers.add(user2.getId());
                                    }catch (IllegalArgumentException | ValidationException ex) {
                                        System.out.println(ex.toString());
                                    }
                                    System.out.println("Send to other users?[Y/N]");
                                    answear = sc.next();
                                    if (answear.equals("N"))
                                        break;
                                }
                        }
                        srv.addMessage(this.user.getId(),receivers,text,idToReply);
                        System.out.println("Reply sent!");
                    }catch(IllegalArgumentException | EntityNotFoundException e)
                    {
                        System.out.println(e.toString());
                    }
                    break;}
                case 3:{
                    List<Message> receivedMessages = srv.allReceivedMessages(user.getUsername());
                    if (receivedMessages.size() !=0)
                    {
                        System.out.println("Received messages: ");
                        for (Message m: receivedMessages)
                        {
                            System.out.println(srv.stringReceivedMessage(m));
                        }
                    }
                    else
                        System.out.println("You haven't received any messages yet.");
                    break;}
                case 4:{
                    List<Message> sentMessages = srv.allSentMessages(user.getUsername());
                    if (sentMessages.size() !=0)
                    {
                        System.out.println("Sent messages: ");
                        for (Message m: sentMessages)
                        {
                            System.out.println(srv.stringSentMessage(m));
                        }
                    }
                    else
                        System.out.println("You haven't sent any messages yet.");
                    break;
                }
                case 5:{
                    System.out.println("Enter id of the message to delete: ");
                    try{
                        Long idToDelete = Long.parseLong(sc.next());
                        srv.deleteMessage(idToDelete);
                        System.out.println("Message deleted!");
                    }catch (IllegalArgumentException | EntityNotFoundException e)
                    {
                        System.out.println(e.toString());
                    }
                    break;
                }

                case 6: {
                    System.out.print("User Name: ");
                    String username = sc.next();

                    try{
                        if(srv.sendFriendRequest(user.getUsername(), username) == null)
                            System.out.println("Friend Request Sent!");
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 7: {
                    System.out.print("User Name : ");
                    String username = sc.next();

                    try{
                        srv.removeFriendship(user.getUsername(), username);
                        System.out.println("Friendship removed");
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 8: {
                    try{
                        for(FriendRequest friendRequest:srv.getSentFriendRequest(user)){
                            System.out.println(srv.getUserById(friendRequest.getReceiverID()).getUsername() + " " + friendRequest.getStatus());
                        }
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 9: {
                    try{
                        for(FriendRequest friendRequest:srv.getReceivedFriendRequest(user)){
                            System.out.println(srv.getUserById(friendRequest.getSenderID()).getUsername() + " " + friendRequest.getStatus());
                        }
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 10: {
                    System.out.print("User Name : ");
                    String username = sc.next();
                    String answer = "";

                    while(!(answer.equals("Y") || answer.equals("N"))){
                        System.out.print("Answer[Y/N]: ");
                        answer = sc.next();
                    }

                    try{
                        srv.answerRequest(username, user.getUsername(), answer);
                        System.out.println("Answer sent!");
                        user = srv.getUserByUsername(user.getUsername());
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 11: {
                    for(Long id:user.getFriends()){
                        System.out.println(srv.getUserById(id).getUsername());
                    }
                    break;
                }

                default: {
                    System.out.println("Unexpected value: " + command);
                }
            }
        }
    }
}
