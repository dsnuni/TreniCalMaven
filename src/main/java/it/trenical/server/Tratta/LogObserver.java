package it.trenical.server.Tratta;

import it.trenical.server.Biglietto.Biglietto;
import it.trenical.server.Biglietto.BigliettoDB;
import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.notifiche.Notifica;
import it.trenical.server.notifiche.NotificaDB;
import it.trenical.server.notifiche.Observer;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public void update(String[] message) {
        NotificaDB ndb = NotificaDB.getInstance();
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("📝 [" + loggerName + " " + timestamp + "] " + message);
        if(inizaTRT(message[2])) {
            TrenoImplDB dbt= TrenoImplDB.getInstance();
            List<Treno> trenID = dbt.getTrenoByTrattaID(message[2]);
            for(Treno treno : trenID){
                BigliettoDB bdb= BigliettoDB.getInstance();
                List<Biglietto> bigliettID = bdb.getBigliettiByTrenoID(treno.getTrenoID());
                for(Biglietto biglietto : bigliettID){
                    Notifica n  = new Notifica(biglietto.getTitolareBiglietto().getCodiceCliente(),
                            treno.getTrenoID(),
                            message[3],
                            message[4],
                            Integer.parseInt(message[7]),
                            biglietto.getBigliettoID(),
                            message[0],
                            biglietto.getPosto(),
                            0,
                            timestamp);
                    ndb.setNotifica(n);
                    System.out.println("Notifica: " + n);
                }
            }
        } else {
           String treno = message[2];
            BigliettoDB bdb= BigliettoDB.getInstance();
            List<Biglietto> bigliettID = bdb.getBigliettiByTrenoID(treno);
            for(Biglietto biglietto : bigliettID){
                Notifica n  = new Notifica(biglietto.getTitolareBiglietto().getCodiceCliente(),
                        treno,
                        message[3],
                        message[4],
                        Integer.parseInt(message[7]),
                        biglietto.getBigliettoID(),
                        message[0],
                        biglietto.getPosto(),
                        0,
                        timestamp);
                ndb.setNotifica(n);
                System.out.println("Notifica: " + n);
            }
        }
    }

    private static boolean inizaTRT(String input) {
        if (input == null) {
            return false;
        }

        return input.startsWith("TRT");
    }

}
