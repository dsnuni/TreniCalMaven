

import it.trenical.server.promozione.Promozione;
import it.trenical.server.promozione.PromozioneImplDB;

import java.util.List;

public class MainPromozioneTest {
    public static void main(String[] args) {
        PromozioneImplDB db = PromozioneImplDB.getInstance();

        String chiaveRicerca = "";

        List<Promozione> risultati = db.cercaPromozioniContenenti(chiaveRicerca);

        if (risultati.isEmpty()) {
            System.out.println("Nessuna promozione trovata contenente: " + chiaveRicerca);
        } else {
            System.out.println("Promozioni trovate contenenti \"" + chiaveRicerca + "\":");
            for (Promozione riga : risultati) {
                System.out.println("- " + riga.toString());
            }
        }
    }
}
