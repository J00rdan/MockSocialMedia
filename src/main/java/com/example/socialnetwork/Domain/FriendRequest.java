package com.example.socialnetwork.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendRequest extends Entity<Long>{
    private Long senderID;
    private Long receiverID;
    private String status;
    private LocalDateTime date;


    public FriendRequest(Long senderID, Long receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.status = "Pending";
        this.date = LocalDateTime.now();
    }

    public FriendRequest(Long senderID, Long receiverID, String status) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.status = status;
        this.date = LocalDateTime.now();
    }

    public FriendRequest(Long senderID, Long receiverID, String status, LocalDateTime date) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.status = status;
        this.date = date;
    }


    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public Long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Long receiverID) {
        this.receiverID = receiverID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(senderID, that.senderID) && Objects.equals(receiverID, that.receiverID) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), senderID, receiverID, status);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + id +
                ", senderID=" + senderID +
                ", receiverID=" + receiverID +
                ", status='" + status + '\'' +
                '}';
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
