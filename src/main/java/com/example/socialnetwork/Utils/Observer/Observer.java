package com.example.socialnetwork.Utils.Observer;

import com.example.socialnetwork.Utils.Events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}