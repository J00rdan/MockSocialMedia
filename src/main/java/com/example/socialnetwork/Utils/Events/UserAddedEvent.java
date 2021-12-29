package com.example.socialnetwork.Utils.Events;

import com.example.socialnetwork.Domain.User;

public class UserAddedEvent implements Event{
    private ChangeEventType type;
    private Iterable<User> allUsers, oldAllUsers;


    public UserAddedEvent(ChangeEventType type, Iterable<User> allUsers) {
        this.type = type;
        this.allUsers = allUsers;
    }

    public UserAddedEvent(ChangeEventType type, Iterable<User> allUsers, Iterable<User> oldAllUsers) {
        this.type = type;
        this.allUsers = allUsers;
        this.oldAllUsers = oldAllUsers;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Iterable<User> getAllUsers() {
        return allUsers;
    }

    public Iterable<User> getOldAllUsers() {
        return oldAllUsers;
    }
}
