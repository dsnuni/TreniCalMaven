
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.igGenerator.IdGenerator;
import it.trenical.server.promozione.Promozione;

public class PromozioneTest {

    public static void main(String[] args) {
        TrattaStandard tratta = new TrattaStandard("TRT-8c3eae47", "Terni", "Bressanone", "12-06-2025 10:00", "12-06-2025 12:00 ", 468, 2);
        TrenoConcr treno = new TrenoConcr(IdGenerator.generaTrenoID(),IdGenerator.generaTipoTreno(), tratta,100,0,0,0,0,0,"");


        // Creo la Promozione con il Builder
        Promozione promo = new Promozione.PromozioneBuilder()
                .setPromozioneID("PROMO01")
                .setTreno(treno)
                .setTratta(tratta)
                .setDataPartenza("2025-07-01")
                .setDataFine("2025-07-10")
                .setClientiFedelta(true)
                .setPrezzoPartenza(89)
                .setScontistica(0.20)
                .build();

        // Output di test
        System.out.println("Promozione creata:");
        System.out.println("ID: " + promo.getPromozioneID());
        System.out.println("Treno: " + promo.getTreno().getTrenoID());
        System.out.println("Tratta: da " + promo.getTratta().getCodiceTratta() +
                " a " + promo.getTratta().getStazioneArrivo());
        System.out.println("Prezzo iniziale: " + promo.getPrezzoPartenza());
        System.out.println("Sconto: " + (promo.getScontistica() * 100) + "%");
        System.out.println("Valida dal " + promo.getDataPartenza() + " al " + promo.getDataFine());
        System.out.println("Solo clienti fedeltà: " + promo.isClientiFedelta());
    }
}
