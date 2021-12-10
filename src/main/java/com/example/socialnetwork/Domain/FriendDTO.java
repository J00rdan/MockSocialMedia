package com.example.socialnetwork.Domain;


import java.time.LocalDateTime;

public class FriendDTO {
    String lastName;
    String firstName;
    LocalDateTime date;


    public FriendDTO(String lastName, String firstName, LocalDateTime date) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "FriendDTO{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", date=" + date +
                '}';
    }
}
