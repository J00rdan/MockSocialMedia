package com.example.socialnetwork.Domain;

import java.time.LocalDateTime;

public class FriendRequestDTO {
    String username;
    String status;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    LocalDateTime date;

    public FriendRequestDTO(String username, String status, LocalDateTime date) {
        this.username = username;
        this.status = status;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
