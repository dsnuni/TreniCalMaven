import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoImpl;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.Tratta.TrattaStandard;

import java.util.List;

public class MainGetAllTreniServer {

    public static void main(String[] args) {
        // Ottieni istanza DB
        TrenoImpl db = TrenoImplDB.getInstance();

        // Recupera tutti i treni
        List<Treno> lista = db.getAllTreno();

        // Stampa risultati
        if (lista == null || lista.isEmpty()) {
            System.out.println("Nessun treno trovato nel database.");
        } else {
            System.out.println("Treni presenti nel database:");
            for (Treno t : lista) {
                TrattaStandard tratta = (TrattaStandard) t.getTratta();
                System.out.println("ID: " + t.getTrenoID()
                        + ", Tipo: " + t.getTipoTreno()
                        + ", Tratta: " + tratta.getCodiceTratta()
                        + " (" + tratta.getStazionePartenza() + " -> " + tratta.getStazioneArrivo() + ")"
                        + ", Prezzo: " + t.getPrezzo()
                        + ", Posti 1ª: " + t.getPostiPrima()
                        + ", 2ª: " + t.getPostiSeconda()
                        + ", 3ª: " + t.getPostiTerza()
                        + ", Tot: " + t.getPostiTot());
            }
        }
    }
}
