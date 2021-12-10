package com.example.socialnetwork.UI;


import com.example.socialnetwork.Domain.FriendDTO;
import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Message;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.EntityNotFoundException;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class UI {
    private Service srv;
    private UI_UserMenu userMenu;
    private static final String menu = "\n1. Add User\n2. Remove User\n3. Add Friend\n" +
            "4. Remove Friend\n" +
            "5. Number of communities\n" +
            "6. Most sociable community\n" +
            "7. Print all\n" +
            "8. Login\n" +
            "9. Print all Friendships\n" +
            "10. Print all friendships for a user in a given month\n" +
            "11. Show conversations between two users\n" +
            "0. Exit\n" +
            "Command Input:";

    private static Scanner sc;

    public UI(Service srv) {
        this.srv = srv;
        sc = new Scanner(System.in);
    }

    public void run(){
        boolean isRunning = true;

        while(isRunning){
            System.out.print(menu);
            int command = sc.nextInt();
            switch (command){
                case 0:
                    isRunning = false;
                    break;
                case 1: {
                    System.out.print("First Name: ");
                    String firstName = sc.next();
                    System.out.print("Last Name: ");
                    String lastName = sc.next();
                    System.out.print("User Name: ");
                    String username = sc.next();

                    try{
                        if(srv.addUser(firstName, lastName, username) == null)
                            System.out.println("User added!");
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 2: {
                    System.out.print("User Name: ");
                    String username = sc.next();

                    try{
                        srv.removeUser(username);
                        System.out.println("User removed!");
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 3: {
                    System.out.print("User Name 1: ");
                    String username1 = sc.next();
                    System.out.print("User Name 2: ");
                    String username2 = sc.next();

                    try{
                        if(srv.addFriendship(username1, username2) == null)
                            System.out.println("Friendship realised");
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 4: {
                    System.out.print("User Name 1: ");
                    String username1 = sc.next();
                    System.out.print("User Name 2: ");
                    String username2 = sc.next();

                    try{
                        srv.removeFriendship(username1, username2);
                        System.out.println("Friendship removed");
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }
                case 5:{
                    System.out.println(srv.numberOfCommunities());
                    break;
                }
                case 6:{
                    System.out.println(srv.mostSocialCommunity());
                    break;
                }
                case 7:{
                    System.out.println(srv.getAllUsers());
                    System.out.println(srv.getAllFriendships());
                    break;
                }
                case 8:{
                    System.out.print("User Name: ");
                    String username = sc.next();
                    if(srv.login(username)) {
                        System.out.println("Login Successful!");
                        userMenu = new UI_UserMenu(srv, username);
                        userMenu.run();
                    }
                    else{
                        System.out.println("Invalid Username!");
                    }
                    break;
                }
                case 9:{
                    System.out.print("User Name: ");
                    String username = sc.next();
                    try{
                        User user1 = srv.getUserByUsername(username);
                        Predicate<Friendship> byUser1 = x -> x.getID1().equals(user1.getId());
                        Predicate<Friendship> byUser2 = x ->x.getID2().equals(user1.getId());

                        List<Friendship> friendships = new ArrayList<>();
                        Iterable<Friendship> allFriendships = srv.getAllFriendships();
                        allFriendships.forEach(x -> {
                            friendships.add(x);
                        });

                        friendships.stream().filter(byUser1)
                                .map(x -> new FriendDTO(srv.getUserByID(x.getID2()).getLastName(),srv.getUserByID(x.getID2()).getFirstName(),x.getDate()))
                                .forEach(x->System.out.println(x));
                        friendships.stream().filter(byUser2)
                                .map(x -> new FriendDTO(srv.getUserByID(x.getID1()).getLastName(),srv.getUserByID(x.getID1()).getFirstName(),x.getDate()))
                                .forEach(x->System.out.println(x));
                    }catch(ValidationException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 10:{
                    System.out.print("User Name: ");
                    String username = sc.next();
                    System.out.print("Month: ");
                    String month = sc.next();
                    int monthNumber;
                    switch (month){
                        case "January" -> monthNumber = 1;
                        case "February" -> monthNumber = 2;
                        case "March" -> monthNumber = 3;
                        case "April" -> monthNumber = 4;
                        case "May" -> monthNumber = 5;
                        case "June" -> monthNumber = 6;
                        case "July" -> monthNumber = 7;
                        case "August" -> monthNumber = 8;
                        case "September" -> monthNumber = 9;
                        case "October" -> monthNumber = 10;
                        case "November" -> monthNumber = 11;
                        case "December" -> monthNumber = 12;
                        default -> monthNumber = 0;
                    }
                    try{
                        srv.getFriendshipsOfUserByMonth(username, monthNumber);
                    }catch (IllegalArgumentException | ValidationException ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                }

                case 11:{
                    System.out.print("First User Name: ");
                    String username1 = sc.next();
                    System.out.print("Second User Name: ");
                    String username2 = sc.next();
                    try{
                        //de pus apelul functiei care imi da mesajele sortate
                        List<Message> conversation = srv.messagesBetween2Users(username1,username2);
                        conversation = srv.sortConversationsByDate(conversation);
                        for (Message m : conversation)
                        {
                            System.out.println(srv.stringReceivedMessage(m)+"| "+m.getDate());
                        }
                    }catch(IllegalArgumentException | EntityNotFoundException ex){
                        System.out.println(ex.toString());
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