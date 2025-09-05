package it.trenical.server.igGenerator;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import it.trenical.server.Biglietto.Biglietto;
import it.trenical.server.Biglietto.BigliettoDB;
import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.notifiche.Notifica;
import it.trenical.server.notifiche.NotificaDB;

public class AnalizzatoreTratte {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
    public void avviaControlloPeriodico() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Controllo periodico tratte LOG : "+"<"+LocalDateTime.now().format(formatter)+">");
                controllaTratte();
            } catch (Exception e) {
                System.err.println("Errore durante il controllo tratte: " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    public void controllaTratte() {
        System.out.println("Controllo tratte LOG : "+"<"+LocalDateTime.now().format(formatter)+">");
        rimuoviTratteObsolete();
        System.out.println("Avvio l'estrazione di informazioni!!!");
        estraiInformazioniTratte();
        notificheClientiFidelizzati();
    }
    public void estraiInformazioniTratte() {
        System.out.println("Estrai informazioni tratte LOG : "+"<"+LocalDateTime.now().format(formatter)+">");
        TrattaImplDB trdb= TrattaImplDB.getInstance();
        List<TrattaStandard> trt = trdb.getAllTratte();
        for( TrattaStandard tr : trt ) {
            String dataPartenza = tr.getDataPartenza();
            if(isOggi(dataPartenza) ) {
                TrenoImplDB trndb= TrenoImplDB.getInstance();
                List<Treno> treniInPartenza = trndb.getTrenoByTrattaID(tr.getCodiceTratta());
                for(Treno trn : treniInPartenza ) {
                    BigliettoDB bigliettoDB = BigliettoDB.getInstance();
                    List<Biglietto> listaClienti = bigliettoDB.getBigliettiByTrenoID(trn.getTrenoID());
                    for(Biglietto biglietto : listaClienti ) {
                        System.out.println("Il cliente "+biglietto.getTitolareBiglietto().getCodiceFiscale()+" deve affrettarsi a raggiungere il treno "+ trn.getTrenoID()
                                +" LOG<"+LocalDateTime.now().format(formatter)+">");
                        Notifica nt = new Notifica(biglietto.getTitolareBiglietto().getCodiceFiscale(),
                                trn.getTrenoID(),tr.getDataPartenza(),tr.getDataArrivo(),
                                0, biglietto.getBigliettoID(),
                                "IMMINENTE",null,0,null);
                        NotificaDB ndb = NotificaDB.getInstance();
                        ndb.setNotifica(nt);
                        System.out.println("Inserimento notifica IMMINENTE a database notifiche LOG<"+LocalDateTime.now().format(formatter)+">");

                    }
                }
            }

        }
    }
    public void notificheClientiFidelizzati() {
        ClienteImplDB cdb = ClienteImplDB.getInstance();
        List<Cliente> lista = cdb.getByFiltro(null,null);
        for(Cliente c : lista) {
            if(c.getCodiceCliente().startsWith("FTRCL") ) {
                String messaggioRandom = new String[]{
                        "Gentile Cliente ci sono tante novità per lei, resti in attesa...",
                        "Gentile Cliente a breve introdurremo un sistema a punti, resti in attesa...",
                        "Gentile Cliente la invitamo a controllare la sua email per non perdere nessuna promozione."
                }[new Random().nextInt(3)];

                Notifica nt = new Notifica(c.getCodiceFiscale(),
                        messaggioRandom,
                        "",
                        " ",
                        0,
                        " ",
                        "PROMO",
                        " ",
                        0,
                        LocalDateTime.now().format(formatter)
                        );
                NotificaDB ndb = NotificaDB.getInstance();
                ndb.setNotifica(nt);
                System.out.println("Inserimento notifica PROMO a database notifiche LOG<"+LocalDateTime.now().format(formatter)+">");
            }
        }
    }
    public void rimuoviTratteObsolete() {
        System.out.println("Rimuovi tratte LOG : "+"<"+LocalDateTime.now().format(formatter)+">");
        TrattaImplDB trattaDB = TrattaImplDB.getInstance();
        TrenoImplDB trenoDB = TrenoImplDB.getInstance();
        BigliettoDB bigliettoDB = BigliettoDB.getInstance();

        List<String> tratteObsolete = new ArrayList<>();
        List<String> treniDaRimuovere = new ArrayList<>();
       // System.out.println("OU" + trattaDB.getAllTratte().size());

        for (TrattaStandard tratta : trattaDB.getAllTratte()) {
            // System.out.println(tratta);
            String trattaID = tratta.getCodiceTratta();
            String data = tratta.getDataPartenza();
            // System.out.println("Data " + trattaID + " " + data);
            String[] convertito = converti(data);

            if (!isDataFutura(convertito[0]) && !isOggi(data)) {//
                tratteObsolete.add(trattaID);
                System.out.println("Convertito :" + " <" + convertito[0].toString() + "> <" + convertito[1].toString() + "> ");


            }
        }System.out.println(tratteObsolete);

        for (Treno treno : trenoDB.getAllTreno()) {
            System.out.println(treno.getTratta());
            if (tratteObsolete.contains(treno.getTratta().getCodiceTratta())) {
                treniDaRimuovere.add(treno.getTrenoID());

            }
        }
        System.out.println("Rimuovere " + treniDaRimuovere.size());


        for (Biglietto biglietto : bigliettoDB.getAllBiglietti()) {
            String trenoID = biglietto.getTrenoBiglietto().getTrenoID();
            if (treniDaRimuovere.contains(trenoID)) {
                bigliettoDB.removeBiglietto(biglietto.getBigliettoID()); // ✅ Rimozione dal DB
               }
            }
        for (String treno : treniDaRimuovere) {
            trenoDB.removeTreno(treno);
            }
        for (String tratta : tratteObsolete) {
            trattaDB.removeTratta(tratta);
        }

        }

    public static String[] converti(String input) {
        StringBuilder primaParte = new StringBuilder();
        StringBuilder secondaParte = new StringBuilder();

        boolean trovatoSpazio = false;

        for (char c : input.toCharArray()) {
            if (!trovatoSpazio && c != ' ') {
                primaParte.append(c);
            } else if (!trovatoSpazio && c == ' ') {
                trovatoSpazio = true;
            } else {
                secondaParte.append(c);
            }
        }

        return new String[]{ primaParte.toString(), secondaParte.toString() };
    }

    private static boolean isOggi(String dataStr) {
        try {
            // dataOraStr esempio: "15/06/2025 14:30"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime dataOraInput = LocalDateTime.parse(dataStr, formatter);
            LocalDateTime adesso = LocalDateTime.now();

            // Verifica che la data sia oggi
            boolean stessaData = dataOraInput.toLocalDate().equals(adesso.toLocalDate());

            // Calcola differenza in minuti
            long differenzaMinuti = Duration.between(adesso, dataOraInput).toMinutes();

            // Deve essere oggi e mancare meno di 60 minuti (ma non negativa)
            return stessaData && differenzaMinuti >= 0 && differenzaMinuti < 60;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isDataFutura(String dataInput) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dataFornita = LocalDate.parse(dataInput, formatter);
            LocalDate oggi = LocalDate.now();

            return dataFornita.isAfter(oggi); // true se futura, false se oggi o passata
        } catch (DateTimeParseException e) {
            System.err.println("Formato data non valido: " + dataInput);
            return false;
        }
    }


        public static void main(String[] args) {
            System.out.println("Avvio pulizia delle tratte obsolete...");
         //   Generatore.genera(50,30,100,70);
            // Istanza della classe contenente il metodo
            // Supponiamo che la classe si chiami GestorePulizia (modifica se diversa)
            AnalizzatoreTratte pulitore = new AnalizzatoreTratte();
            //pulitore.rimuoviTratteObsolete();
            pulitore.controllaTratte();

            System.out.println("Pulizia completata.");
        }



}
