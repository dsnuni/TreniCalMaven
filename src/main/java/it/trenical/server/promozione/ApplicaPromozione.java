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


    public static String ciSonoPromozioni(String trenoID, String trattaID, int prezzo) {
        try {
            PromozioneImplDB ndb = PromozioneImplDB.getInstance();

            if (trenoID != null) {
                List<Promozione> promozioniTreno = ndb.getPromozioneByFiltro("trenoID", trenoID);
                if (promozioniTreno != null) {
                    for(Promozione n : promozioniTreno) {
                        if(n != null && prezzo >= n.getPrezzoPartenza()) {
                            for (Promozione p : promozioniTreno) {
                                System.out.println("ID PROMOZIONE "+   p.toString());
                            }
                            return n.getPromozioneID();
                        }
                    }
                }
            }

            if (trattaID != null) {
                List<Promozione> promozioniTratta = ndb.getPromozioneByFiltro("trattaID", trattaID);
                if (promozioniTratta != null) {
                    for(Promozione n : promozioniTratta) {
                        if(n != null && prezzo >= n.getPrezzoPartenza()) {
                            for (Promozione p : promozioniTratta) {
                                System.out.println("ID PROMOZIONE "+   p.toString());
                            }
                            return n.getPromozioneID();
                        }
                    }
                }
            }

            return null;
        } catch (Exception e) {
            System.err.println("Errore nella ricerca promozioni: " + e.getMessage());
            return null;
        }
    }

    public static double applicaPromozione(String promoID) {
        try {
            if (promoID == null) {
                return 0.0;
            }

            PromozioneImplDB ndb = PromozioneImplDB.getInstance();
            Promozione promo = ndb.getPromozione(promoID);

            if (promo == null) {
                System.err.println("Promozione non trovata per ID: " + promoID);
                return 0.0;
            }

            return promo.getScontistica();
        } catch (Exception e) {
            System.err.println("Errore nell'applicazione della promozione: " + e.getMessage());
            return 0.0;
        }
    }

    public static String controllaPromozione(String trenoID, String trattaID, int prezzoPartenza) {
        try {
            String promoID = ciSonoPromozioni(trenoID, trattaID, prezzoPartenza);
           if(promoID!=null) {
               PromozioneImplDB ndb = PromozioneImplDB.getInstance();
               Promozione promo = ndb.getPromozione(promoID);
               return "PROMO PER TE :" + promo.getScontistica() + "% DI SCONTO SU QUESTO TRENO";
           }


        } catch (Exception e) {
            System.err.println("Errore durante il controllo delle promozioni: " + e.getMessage());
            return "Nessuna promozione disponibile";
        }
        return "Nessuna promozione disponibile";
    }

}
