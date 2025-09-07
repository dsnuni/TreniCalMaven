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
        try {
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
                double scontoPercentuale = promozioniTreni.get(0).getScontistica();
                double sconto = intToPercentage(scontoPercentuale);
                System.out.println("Promozione treno - Sconto: " + sconto);
                return calcolaPrezzoScontato(biglietto.getPrezzo(), sconto);
            } else if (haPromozioneValida(promozioniTratte, biglietto.getPrezzo())) {
                double scontoPercentuale = promozioniTratte.get(0).getScontistica();
                double sconto = intToPercentage(scontoPercentuale);
                System.out.println("Promozione tratta - Sconto: " + sconto);
                return calcolaPrezzoScontato(biglietto.getPrezzo(), sconto);
            } else if (haPromozioneValida(promozioniDataP, biglietto.getPrezzo())) {
                double scontoPercentuale = promozioniDataP.get(0).getScontistica();
                double sconto = intToPercentage(scontoPercentuale);
                System.out.println("Promozione data - Sconto: " + sconto);
                return calcolaPrezzoScontato(biglietto.getPrezzo(), sconto);
            }

            return biglietto.getPrezzo();
        } catch (Exception e) {
            System.err.println("Errore durante l'applicazione della promozione: " + e.getMessage());
            return biglietto.getPrezzo();
        }
    }
    public static String ciSonoPromozioni(String trenoID, String trattaID, int prezzo) {
        try {
            PromozioneImplDB ndb = PromozioneImplDB.getInstance();

            if (trenoID != null) {
                for(Promozione n : ndb.getPromozioneByFiltro("trenoID", trenoID)) {
                    if(prezzo >= n.getPrezzoPartenza()) {
                        return n.getPromozioneID();
                    }
                }
            }
            if (trattaID != null) {
                for(Promozione n : ndb.getPromozioneByFiltro("trattaID", trattaID)) {
                    if(prezzo >= n.getPrezzoPartenza()) {
                        return n.getPromozioneID();
                    }
                }
            }
                return null;
        } catch (NullPointerException e) {
            throw new RuntimeException("Errore: riferimento null", e);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella ricerca promozioni", e);
        }
    }
    public static double applicaPromozione(String promoID) {
        PromozioneImplDB ndb = PromozioneImplDB.getInstance();
        return ndb.getPromozione(promoID).getScontistica();
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
                if (prezzoPartenza >= n.getPrezzoPartenza()) {
                    lista.add(n);
                }
            }
            for (Promozione n : pdb.getPromozioneByFiltro("trattaID", trattaID)) {
                if (prezzoPartenza >= n.getPrezzoPartenza()) {
                    lista.add(n);
                }
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
        System.out.println("Sconto :");
        System.out.println( prezzoOriginale * (1 - percentuale));
        return prezzoOriginale * (1 - percentuale);
    }

    public static double intToPercentage(double value) {
        return value / 100.0;
    }
    public static double Promozione(BPrimaClasse b) { return Promozione((Biglietto) b); }
    public static double Promozione(BSecondaClasse b) { return Promozione((Biglietto) b); }
    public static double Promozione(BTerzaClasse b) { return Promozione((Biglietto) b); }
}
