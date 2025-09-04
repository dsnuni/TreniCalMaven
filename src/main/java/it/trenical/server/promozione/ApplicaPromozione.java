package it.trenical.server.promozione;

import it.trenical.server.Biglietto.*;
import it.trenical.server.Biglietto.BSecondaClasse;
import it.trenical.server.Biglietto.BTerzaClasse;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Treno.Treno;
import it.trenical.server.notifiche.Notifica;
import it.trenical.server.notifiche.NotificaDB;

import java.util.ArrayList;
import java.util.List;

public class ApplicaPromozione {

    public static double Promozione(Biglietto biglietto) {
        PromozioneImplDB db = PromozioneImplDB.getInstance();
        Treno treno = biglietto.getTrenoBiglietto();
        TrattaPrototype tratta = treno.getTratta();

        String trenoID = treno.getTrenoID();
        String trattaID = tratta.getCodiceTratta();
        String dataP = tratta.getDataPartenza();

        List<Promozione> promozioniTreni = db.cercaPromozioniContenenti(trenoID);
        List<Promozione> promozioniTratte = db.cercaPromozioniContenenti(trattaID);
        List<Promozione> promozioniDataP = db.cercaPromozioniContenenti(dataP);

        if (haPromozioneValida(promozioniTreni, biglietto.getPrezzo())) {
            double sconto = promozioniTreni.get(0).getScontistica();
            return calcolaPrezzoScontato(biglietto.getPrezzo(), sconto);
        } else if (haPromozioneValida(promozioniTratte, biglietto.getPrezzo())) {
            double sconto = promozioniTratte.get(0).getScontistica();
            return calcolaPrezzoScontato(biglietto.getPrezzo(), sconto);
        } else if (haPromozioneValida(promozioniDataP, biglietto.getPrezzo())) {
            double sconto = promozioniDataP.get(0).getScontistica();
            return calcolaPrezzoScontato(biglietto.getPrezzo(), sconto);
        }

        return biglietto.getPrezzo();
    }
    public static String controllaPromozione(String trenoID, String trattaID, int prezzoPartenza) {
        try {
            List<Promozione> lista = new ArrayList<>();
            PromozioneImplDB pdb = PromozioneImplDB.getInstance();
            TrattaImplDB tdb = TrattaImplDB.getInstance();
            TrattaPrototype tr = tdb.getTratta(trattaID);

            if (tr == null) {
                return "Nessuna promozione disponibile";
            }

            String dataP = tr.getDataPartenza();
            String dataA = tr.getDataArrivo();

            for (Promozione n : pdb.getPromozioneByFiltro("trenoID", trenoID)) {
                lista.add(n);
            }
            for (Promozione n : pdb.getPromozioneByFiltro("trattaID", trattaID)) {
                lista.add(n);
            }
            for (Promozione n : pdb.getPromozioneByFiltro("dataPartenza", dataP)) {
                lista.add(n);
            }
            for (Promozione n : pdb.getPromozioneByFiltro("dataFine", dataA)) {
                lista.add(n);
            }
            for (Promozione n : pdb.getByPrezzo(prezzoPartenza)) {
                lista.add(n);
            }

            if (lista.isEmpty()) {
                return "Nessuna promozione disponibile";
            }

            return "PROMO PER TE :" + lista.get(0).getScontistica() + "% DI SCONTO SU QUESTO TRENO";

        } catch (Exception e) {
            System.err.println("Errore durante il controllo delle promozioni: " + e.getMessage());
            return "Nessuna promozione disponibile";
        }
    }
    private static boolean haPromozioneValida(List<Promozione> promozioni, double prezzoBiglietto) {
        return promozioni != null && !promozioni.isEmpty() &&
                promozioni.get(0).getPrezzoPartenza() <= prezzoBiglietto;
    }

    private static double calcolaPrezzoScontato(double prezzoOriginale, double percentuale) {
        return (prezzoOriginale / 100.0) * percentuale;
    }

    // Overload per supportare tutte le classi concrete (se serve mantenere firme distinte)
    public static double Promozione(BPrimaClasse b) { return Promozione((Biglietto) b); }
    public static double Promozione(BSecondaClasse b) { return Promozione((Biglietto) b); }
    public static double Promozione(BTerzaClasse b) { return Promozione((Biglietto) b); }
}
