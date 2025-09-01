package it.trenical.server.Biglietto;

import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.igGenerator.IdGenerator;
import it.trenical.server.promozione.ApplicaPromozione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatoreBiglietto {

    public static String creaBiglietto(ArrayList<String> dati) {
        try {
            TrenoImplDB db = TrenoImplDB.getInstance();
            String bigliettoID = IdGenerator.generaBigliettoID();
            String classe = dati.get(0); // "PrimaClasse", "SecondaClasse", "TerzaClasse"
            String trenoID = dati.get(1);
            //String carrozza = dati.get(3);
            //String posto = dati.get(4);
            String clienteID = dati.get(2);
            String prioritaCSV = dati.get(3); // valori separati da virgole
            int prezzo = db.getTreno(trenoID).getPrezzo();

            Cliente cliente = ClienteImplDB.getInstance().getCliente(clienteID);
            Treno treno = TrenoImplDB.getInstance().getTreno(trenoID);
            ArrayList<String> priorita = new ArrayList<>(Arrays.asList(prioritaCSV.split(",")));

            Biglietto biglietto;
            Biglietto promosso;
            double sconto;

            switch (classe) {
                case "PrimaClasse":
                    String postoP =(treno.getPostiPrima() - 1) + "A";
                    int prezzoPrimaClasse= (prezzo/100)*25;
                    biglietto = new BPrimaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza("A")
                            .posto((treno.getPostiPrima() - 1) + "A")
                            .priorità(priorita)
                            .prezzo(prezzo+prezzoPrimaClasse)
                            .implementazione(BigliettoDB.getInstance())
                            .build();
                    treno.setPostiPrima(treno.getPostiPrima() - 1);
                    treno.setPostiTot(treno.getPostiTot() - 1);
                    sconto = ApplicaPromozione.Promozione((BPrimaClasse) biglietto);
                    promosso = Biglietto.clonaConPrezzo(biglietto, (int) sconto);

                    break;
                case "SecondaClasse":
                    String postoS = (treno.getPostiSeconda() - 1) + "B";
                    int prezzoSecondaClasse= (prezzo/100)*15;
                    biglietto = new BSecondaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza("B")
                            .posto(postoS)
                            .priorità(priorita)
                            .prezzo(prezzo+prezzoSecondaClasse)
                            .implementazione(BigliettoDB.getInstance())
                            .build();
                    treno.setPostiSeconda(treno.getPostiSeconda() - 1);
                    treno.setPostiTot(treno.getPostiTot() - 1);
                    sconto = ApplicaPromozione.Promozione((BSecondaClasse) biglietto);
                    promosso = Biglietto.clonaConPrezzo(biglietto, (int) sconto);
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
                            .prezzo(prezzo)
                            .implementazione(BigliettoDB.getInstance())
                            .build();
                    treno.setPostiTerza(treno.getPostiTerza() - 1);
                    treno.setPostiTot(treno.getPostiTot() - 1);
                    sconto = ApplicaPromozione.Promozione((BTerzaClasse) biglietto);
                    promosso = Biglietto.clonaConPrezzo(biglietto, (int) sconto);
                    break;
            }

            db.setTreno(treno);
            BigliettoDB.getInstance().setBiglietto(promosso);
            return bigliettoID;

        } catch (Exception e) {
            System.err.println("Errore creazione biglietto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("🟢 Inizio test creazione biglietto...");

        ArrayList<String> dati = new ArrayList<>(List.of(
                "TerzaClasse",
                "TRN-6bd73740",           // trenoID (esistente nel DB)
                "ciao",           // clienteID (esistente nel DB)
                "Finestrino,Silenzio"  // priorità CS
        ));

        System.out.println("🔍 Dati pronti: " + dati);

        String successo = CreatoreBiglietto.creaBiglietto(dati);

        if (successo != null) {
            System.out.println("✅ Biglietto creato e salvato con successo.");
        } else {
            System.out.println("❌ Creazione biglietto fallita.");
        }
    }
}