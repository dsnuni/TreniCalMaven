package it.trenical.server.Tratta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;


public class GeneratorConcr implements Generator {
    String[] cittaItaliane = {
            "Lecce",
            "Brindisi",
            "Taranto",
            "Matera",
            "Bari",
            "Potenza",
            "Foggia",
            "Campobasso",
            "Pescara",
            "L'Aquila",
            "Teramo",
            "Rieti",
            "Roma",
            "Viterbo",
            "Terni",
            "Perugia",
            "Arezzo",
            "Siena",
            "Firenze",
            "Prato",
            "Bologna",
            "Modena",
            "Parma",
            "Reggio Emilia",
            "Mantova",
            "Verona",
            "Vicenza",
            "Trento",
            "Bolzano",
            "Bressanone"
    };

    String[] orari = {"00:00", "02:00", "04:00", "06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00"};

    private final Random random = new Random();
    private final TrattaPrototype ts;
    String codiceTratta = null;
    String stazPartenza = null;
    String stazArrivo = null;
    String oraPartenza = null;
    String dataPartenza = null;
    String oraArrivo = null;
    String dataArrivo = null;
    int tempoPercorrenza = 0;
    int distanza = 0;

    public GeneratorConcr(TrattaPrototype ts) {
        this.ts = ts;

    }

    @Override
    public TrattaPrototype genera() {
        TrattaStandard nuovaTratta = (TrattaStandard) ts.clone();

        int cittaPartenza = setCittaPartenza();
        int cittaArrivo = setCittaArrivo(cittaPartenza);
        setDistanza(cittaPartenza, cittaArrivo);
        int tempoPerc = tempoPercorrenza();
        setDPartenza();
        setOPartenza();
        setDArrivo();
        //setOArrivo();

        nuovaTratta.setStazionePartenza(stazPartenza);
        nuovaTratta.setStazioneArrivo(stazArrivo);
        String dataP= dataPartenza+" "+oraPartenza;
        nuovaTratta.setDataPartenza(dataP);
        String dataA =dataArrivo+" "+oraArrivo;
        nuovaTratta.setDataArrivo(dataA);
        nuovaTratta.setTempoPercorrenza(tempoPerc);
        nuovaTratta.setDistanza(distanza);

        return nuovaTratta;
    }


    private int setCittaPartenza() {
        int citta = random.nextInt(cittaItaliane.length);
        this.stazPartenza = cittaItaliane[citta];
        return citta;
    }

    private int setCittaArrivo(int cittaPartenza) {
        int cittaArrivo= 0;
        boolean flag = false;
        while (!flag) {
            cittaArrivo = random.nextInt(cittaItaliane.length);
            if (cittaPartenza != cittaArrivo) {
                this.stazArrivo = cittaItaliane[cittaArrivo];
                flag = true;
            }
        }
        return cittaArrivo;
    }

    private int setDistanza(int cittaPartenza, int cittaArrivo) {
        int distanza = Math.abs((cittaArrivo - cittaPartenza) * 40);
        this.distanza = distanza;
        return distanza;
    }

    private void setDPartenza() {
        String[] ls = creaRangeDate(10);
        double dataP = random.nextInt(ls.length);
        this.dataPartenza = ls[(int) dataP];
    }
    private void setOPartenza() {
        int ora = random.nextInt(orari.length);
        oraPartenza = orari[ora];
        int tempo = tempoPercorrenza;
        int tempoMeta = (tempo % 2 == 0) ? tempo / 2 : (tempo / 2) + 1;
        int indiceArrivo = (ora + tempoMeta) % orari.length;

        oraArrivo = orari[indiceArrivo];
    }

    private void setDArrivo() {
        if (verificaOra(oraPartenza, tempoPercorrenza)) {
            this.dataArrivo = incrementaGiorno(dataPartenza);
        } else {
            this.dataArrivo = dataPartenza;
        }
    }

    private int tempoPercorrenza() {
        this.tempoPercorrenza=distanza/40;
        return (distanza/ 40); // in ore
    }

    public static boolean verificaOra(String orarioPartenza, int durataOre) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime oraPartenza = LocalTime.parse(orarioPartenza, formatter);
            LocalTime oraArrivo = oraPartenza.plusHours(durataOre);
            return oraArrivo.isBefore(oraPartenza);

        } catch (DateTimeParseException e) {
            System.out.println("Orario non valido: " + orarioPartenza);
            return false;
        }
    }
    public static String incrementaGiorno(String dataStringa) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate data = LocalDate.parse(dataStringa, formatter);
            LocalDate giornoSuccessivo = data.plusDays(1);

            return giornoSuccessivo.format(formatter);

        } catch (DateTimeParseException e) {
            System.out.println("Formato data non valido: " + dataStringa);
            return null;
        }
    }
    private static String[] creaRangeDate(int giorni) {
        String[] date = new String[giorni + 1];
        LocalDate oggi = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (int i = 0; i <= giorni; i++) {
            LocalDate nuovaData = oggi.plusDays(i);
            date[i] = nuovaData.format(formatter);
        }
        return date;
    }


        public static void main(String[] args) {
            TrattaPrototype prototipoBase = new TrattaStandard();
            Generator generatore = new GeneratorConcr(prototipoBase);

            for (int i = 0; i < 5; i++) {
                TrattaPrototype tratta = generatore.genera();
                System.out.println(tratta);
            }
        }

}