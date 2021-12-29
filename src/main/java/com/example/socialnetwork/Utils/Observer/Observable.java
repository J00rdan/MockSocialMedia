package com.example.socialnetwork.Utils.Observer;

import com.example.socialnetwork.Utils.Events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
