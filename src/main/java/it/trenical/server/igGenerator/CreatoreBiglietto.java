package it.trenical.server.igGenerator;

import it.trenical.server.Biglietto.*;
import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.Treno.TrenoImpl;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.promozione.ApplicaPromozione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatoreBiglietto {

    public static String creaBiglietto(ArrayList<String> dati) {
        try {
            TrenoImplDB db = TrenoImplDB.getInstance();
            String bigliettoID = IdGenerator.generaBigliettoID();
            String classe = dati.get(0);
            String trenoID = dati.get(1);
            String clienteID = dati.get(2);
            String prioritaCSV = dati.get(3);
            int prezzoFInale = Integer.parseInt(dati.get(4));
            Cliente cliente = ClienteImplDB.getInstance().getCliente(clienteID);
            Treno treno = TrenoImplDB.getInstance().getTreno(trenoID);
            ArrayList<String> priorita = new ArrayList<>(Arrays.asList(prioritaCSV.split(",")));

            Biglietto biglietto;
            Biglietto promosso;
            double sconto;

            switch (classe) {
                case "PrimaClasse":
                    String postoP =(treno.getPostiPrima() - 1) + "A";
                    biglietto = new BPrimaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza("A")
                            .posto((treno.getPostiPrima() - 1) + "A")
                            .priorità(priorita)
                            .prezzo(prezzoFInale)
                            .implementazione(BigliettoDB.getInstance())
                            .build();
                    treno.setPostiPrima(treno.getPostiPrima() - 1);
                    treno.setPostiTot(treno.getPostiTot() - 1);
                    break;
                case "SecondaClasse":
                    String postoS = (treno.getPostiSeconda() - 1) + "B";
                    biglietto = new BSecondaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza("B")
                            .posto(postoS)
                            .priorità(priorita)
                            .prezzo(prezzoFInale)
                            .implementazione(BigliettoDB.getInstance())
                            .build();
                    treno.setPostiSeconda(treno.getPostiSeconda() - 1);
                    treno.setPostiTot(treno.getPostiTot() - 1);
                    break;
                default:
                    String postoT =(treno.getPostiTerza() - 1) + "C";
                    biglietto = new BTerzaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza("C")
                            .posto(postoT)
                            .priorità(priorita)
                            .prezzo(prezzoFInale)
                            .implementazione(BigliettoDB.getInstance())
                            .build();
                    treno.setPostiTerza(treno.getPostiTerza() - 1);
                    treno.setPostiTot(treno.getPostiTot() - 1);
                    break;
            }

            db.setTreno(treno);
            BigliettoDB.getInstance().setBiglietto(biglietto);
            return bigliettoID;

        } catch (Exception e) {
            System.err.println("Errore creazione biglietto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static int calcoloPrezzoPrePagamento(String trenoID, String classe) {
        try {
            TrenoImplDB db = TrenoImplDB.getInstance();
           // String bigliettoID = IdGenerator.generaBigliettoID();
            String trattaID = db.getTreno(trenoID).getTratta().getCodiceTratta();
            int prezzo = db.getTreno(trenoID).getPrezzo();
            int prezzoFinale = 0;
            int sovraPrezzo = 0;
            switch (classe) {
                case "PrimaClasse":
                    sovraPrezzo = prezzo * 25 / 100;
                    break;
                case "SecondaClasse":
                    sovraPrezzo = prezzo * 15 / 100;
                    break;
                default:
                    prezzoFinale = prezzo;
            }
            prezzoFinale = prezzo + sovraPrezzo;
            String codicePromo=ApplicaPromozione.ciSonoPromozioni(trenoID,trattaID,prezzoFinale);
            if (codicePromo != null) {
                double percentualeSconto = ApplicaPromozione.applicaPromozione(codicePromo);
                prezzoFinale = (int) (prezzoFinale - (prezzoFinale * percentualeSconto));
            }
            return prezzoFinale;
        } catch (NullPointerException e) {
            throw new RuntimeException("Errore: uno dei parametri è null", e);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Errore: dati insufficienti nella lista", e);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel calcolo del prezzo", e);
        }
    }
    public static void main(String[] args) {
        System.out.println("Inizio test creazione biglietto...");

        ArrayList<String> dati = new ArrayList<>(List.of(
                "TerzaClasse",
                "TRN-6bd73740",
                "ciao",
                "Finestrino,Silenzio"
        ));

        System.out.println(" Dati pronti: " + dati);

        String successo = CreatoreBiglietto.creaBiglietto(dati);

        if (successo != null) {
            System.out.println("✅Biglietto creato e salvato con successo.");
        } else {
            System.out.println("Creazione biglietto fallita.");
        }
    }
}