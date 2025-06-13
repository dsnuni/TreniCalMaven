package it.trenical.server.promozione;

import it.trenical.server.Biglietto.BPrimaClasse;
import it.trenical.server.Biglietto.BSecondaClasse;
import it.trenical.server.Biglietto.BTerzaClasse;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Treno.Treno;

import java.util.ArrayList;
import java.util.List;

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
        return 0007;
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
        return 0007;
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
        return 0007;
    }
}
