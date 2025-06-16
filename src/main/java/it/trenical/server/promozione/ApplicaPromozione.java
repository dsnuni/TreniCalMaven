package it.trenical.server.promozione;

import it.trenical.server.Biglietto.*;
import it.trenical.server.Biglietto.BSecondaClasse;
import it.trenical.server.Biglietto.BTerzaClasse;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Treno.Treno;

import java.util.ArrayList;
import java.util.List;
/**
public class ApplicaPromozione {

    public static double Promozione( BPrimaClasse biglietto) {
        PromozioneImplDB db = PromozioneImplDB.getInstance();
        Treno treno = biglietto.getTrenoBiglietto();
        TrattaPrototype tratta = treno.getTratta();

        String trenoID = treno.getTrenoID();
        String trattaID = tratta.getCodiceTratta();
        String dataP = tratta.getDataPartenza();
        List<Promozione> promozioniTreni = db.cercaPromozioniContenenti(trenoID);
        List<Promozione> promozioniTratte = db.cercaPromozioniContenenti(trattaID);
        List<Promozione> promozioniDataP = db.cercaPromozioniContenenti(dataP);
        if(promozioniTreni != null && promozioniTreni.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
           double percentuale = promozioniTreni.get(0).getScontistica();
           double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        } else if (promozioniTratte != null && promozioniTratte.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
            double percentuale = promozioniTratte.get(0).getScontistica();
            double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        } else if (promozioniDataP != null && promozioniDataP.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
            double percentuale = promozioniDataP.get(0).getScontistica();
            double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        }
        return biglietto.getPrezzo();
    }

    public static double Promozione(BSecondaClasse biglietto) {
        PromozioneImplDB db = PromozioneImplDB.getInstance();
        Treno treno = biglietto.getTrenoBiglietto();
        TrattaPrototype tratta = treno.getTratta();

        String trenoID = treno.getTrenoID();
        String trattaID = tratta.getCodiceTratta();
        String dataP = tratta.getDataPartenza();
        List<Promozione> promozioniTreni = db.cercaPromozioniContenenti(trenoID);
        List<Promozione> promozioniTratte = db.cercaPromozioniContenenti(trattaID);
        List<Promozione> promozioniDataP = db.cercaPromozioniContenenti(dataP);
        if(promozioniTreni != null && promozioniTreni.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
            double percentuale = promozioniTreni.get(0).getScontistica();
            double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        } else if (promozioniTratte != null && promozioniTratte.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
            double percentuale = promozioniTratte.get(0).getScontistica();
            double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        } else if (promozioniDataP != null && promozioniDataP.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
            double percentuale = promozioniDataP.get(0).getScontistica();
            double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        }
        return biglietto.getPrezzo();
    }

    public static double Promozione(BTerzaClasse biglietto) {
        PromozioneImplDB db = PromozioneImplDB.getInstance();
        Treno treno = biglietto.getTrenoBiglietto();
        TrattaPrototype tratta = treno.getTratta();

        String trenoID = treno.getTrenoID();
        String trattaID = tratta.getCodiceTratta();
        String dataP = tratta.getDataPartenza();
        List<Promozione> promozioniTreni = db.cercaPromozioniContenenti(trenoID);
        List<Promozione> promozioniTratte = db.cercaPromozioniContenenti(trattaID);
        List<Promozione> promozioniDataP = db.cercaPromozioniContenenti(dataP);
        if(promozioniTreni != null && promozioniTreni.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
            double percentuale = promozioniTreni.get(0).getScontistica();
            double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        } else if (promozioniTratte != null && promozioniTratte.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
            double percentuale = promozioniTratte.get(0).getScontistica();
            double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        } else if (promozioniDataP != null && promozioniDataP.get(0).getPrezzoPartenza() <= biglietto.getPrezzo()) {
            double percentuale = promozioniDataP.get(0).getScontistica();
            double prezzoNuovo =   (biglietto.getPrezzo() / 100) * percentuale;
            return prezzoNuovo;
        }
        return biglietto.getPrezzo() ;
    }
}
**/

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
