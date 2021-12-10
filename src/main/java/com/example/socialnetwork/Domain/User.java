package com.example.socialnetwork.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String username;
    private List<Long> friends;

    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        friends = new ArrayList<Long>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }

    public void addFriend(User newFriend){
        friends.add(newFriend.getId());
    }

    public void removeFriend(User friend){
        friends.remove(friend.getId());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends()) &&
                getUsername().equals((that.getUsername()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends(), getUsername());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", friends=" + friends +
                "}\n";
    }

    /*@Override
    public String toString() {
        String user="";
        user+="User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName= ' " + username + '\''+
                ", friends=";
        if (this.friends.size() != 0)
            for (User u : this.friends)
                user+=u.getUsername()+";";
        else
            user+="null";
        user+='}';
        return user;
    }*/
}