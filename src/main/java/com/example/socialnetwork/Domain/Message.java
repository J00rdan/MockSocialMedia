package com.example.socialnetwork.Domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long>{
    Long from;
    List<Long> to;
    String message;
    LocalDateTime date;
    Long reply;

    public Message (Long from, String message, LocalDateTime date,Long reply)
    {
        this.from = from;
        this.to = new ArrayList<>();
        this.message = message;
        this.date = date;
        this.reply = reply;
    }

    public Message(Long from, String message) {
        this.from = from;
        this.to = new ArrayList<>();
        this.message = message;
        this.date = LocalDateTime.now();
        this.reply = null;
    }

    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getReply() {
        return reply;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(List<Long> to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", reply=" + reply +
                '}';
    }
}
