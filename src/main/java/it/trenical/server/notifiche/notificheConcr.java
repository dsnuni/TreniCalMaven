package it.trenical.server.notifiche;

public class notificheConcr implements Observer {
    @Override
    public void update(String messaggio) {
        System.out.println("[NOTIFICA] " + messaggio);
        // In futuro puoi inviare notifiche al client via gRPC o salvarle in log
    }
}
