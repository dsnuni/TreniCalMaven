package it.trenical.server.notifiche;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void notifyObservers(String messaggio) {
        for (Observer o : observers) {
            o.update(messaggio);
        }
    }
}
