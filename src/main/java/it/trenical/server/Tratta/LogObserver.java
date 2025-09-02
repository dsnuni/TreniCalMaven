package it.trenical.server.Tratta;

import it.trenical.server.notifiche.Observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Observer che logga tutte le operazioni con timestamp
 */
class LogObserver implements Observer {
    private String loggerName;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogObserver(String name) {
        this.loggerName = name;
    }

    @Override
    public void update(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("📝 [" + loggerName + " " + timestamp + "] " + message);
    }
}
