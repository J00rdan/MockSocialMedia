package com.example.socialnetwork.Utils;

import com.example.socialnetwork.Domain.Message;

import java.util.Comparator;

public class ComparatorByDate implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
