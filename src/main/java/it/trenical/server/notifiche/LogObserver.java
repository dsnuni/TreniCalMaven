package it.trenical.server.notifiche;

import it.trenical.server.Biglietto.Biglietto;
import it.trenical.server.Biglietto.BigliettoDB;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoImplDB;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class LogObserver implements Observer {
    private String loggerName;
    private static LocalTime adesso = LocalTime.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public LogObserver(String name) {
        this.loggerName = name;
    }

    @Override
    public void update(String[] message) {
        NotificaDB ndb = NotificaDB.getInstance();
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("📝 [" + loggerName + " " + timestamp + "] " + message);

        System.out.println("INTERCETTATA NOTIFICA "+message[0]+" per elemento "+message[1]+" LOG <"+adesso.format(formatter)+">");

        if(inizaTRT(message[1])) {
            TrenoImplDB dbt= TrenoImplDB.getInstance();
            List<Treno> trenID = dbt.getTrenoByTrattaID(message[1]);

            for(Treno treno : trenID){

                BigliettoDB bdb= BigliettoDB.getInstance();
                List<Biglietto> bigliettID = bdb.getBigliettiByTrenoID(treno.getTrenoID());
                for(Biglietto biglietto : bigliettID){

                    Notifica n  = new Notifica(biglietto.getTitolareBiglietto().getCodiceFiscale(),
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
                    System.out.println("La notifica "+message[0]+" per elemento "+message[1]+"è stata aggiunta al dbNotifiche LOG <"+adesso.format(formatter)+">");

                }
            }
        } else {
           String treno = message[1];
            BigliettoDB bdb= BigliettoDB.getInstance();
            List<Biglietto> bigliettID = bdb.getBigliettiByTrenoID(treno);

            for(Biglietto biglietto : bigliettID){
                Notifica n  = new Notifica(biglietto.getTitolareBiglietto().getCodiceFiscale(),
                        treno,
                        message[3],
                        message[4],
                        0,
                        biglietto.getBigliettoID(),
                        message[0],
                        biglietto.getPosto(),
                        0,
                        timestamp);
                ndb.setNotifica(n);
                System.out.println("La notifica "+message[0]+" per elemento "+message[1]+"è stata aggiunta al dbNotifiche LOG <"+adesso.format(formatter)+">");
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
