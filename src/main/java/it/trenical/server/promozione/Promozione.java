package it.trenical.server.promozione;


import it.trenical.server.Biglietto.BPrimaClasse;
import it.trenical.server.Biglietto.Biglietto;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoImpl;
import it.trenical.server.Treno.TrenoImplDB;

public class Promozione {
    private String promozioneID;
    private Treno treno;
    private TrattaPrototype tratta;
    private String dataPartenza;
    private String dataFine;
    private boolean clientiFedelta;
    private static int prezzoPartenza;
    private double scontistica;


    private Promozione(PromozioneBuilder builder) {
        this.promozioneID = builder.promozioneID;
        this.treno = builder.treno;
        this.tratta = builder.tratta;
        this.dataPartenza = builder.dataPartenza;
        this.dataFine = builder.dataFine;
        this.clientiFedelta = builder.clientiFedelta;
        this.prezzoPartenza = builder.prezzoPartenza;
        this.scontistica = builder.scontistica;

    }

    // Getter opzionali
    public String getPromozioneID() { return promozioneID; }
    public Treno getTreno() { return treno; }
    public TrattaPrototype getTratta() { return tratta; }
    public String getDataPartenza() { return dataPartenza; }
    public String getDataFine() { return dataFine; }
    public boolean isClientiFedelta() { return clientiFedelta; }
    public int getPrezzoPartenza() { return prezzoPartenza; }
    public double getScontistica() { return scontistica; }

    // Static Builder
    public static class PromozioneBuilder {
        private String promozioneID;
        private Treno treno;
        private TrattaPrototype tratta;
        private String dataPartenza;
        private String dataFine;
        private boolean clientiFedelta;
        private int prezzoPartenza;
        private double scontistica;

        public PromozioneBuilder setPromozioneID(String promozioneID) {
            this.promozioneID = promozioneID;
            return this;
        }
        public PromozioneBuilder setTreno(Treno treno) {
            this.treno = treno;
            return this;
        }

        public PromozioneBuilder setTratta(TrattaPrototype tratta) {
            this.tratta = tratta;
            return this;
        }

        public PromozioneBuilder setDataPartenza(String dataPartenza) {
            this.dataPartenza = dataPartenza;
            return this;
        }

        public PromozioneBuilder setDataFine(String dataFine) {
            this.dataFine = dataFine;
            return this;
        }

        public PromozioneBuilder setClientiFedelta(boolean clientiFedelta) {
            this.clientiFedelta = clientiFedelta;
            return this;
        }
        public PromozioneBuilder setPrezzoPartenza(int prezzoPartenza) {
            this.prezzoPartenza = prezzoPartenza;
            return this;
        }
        public PromozioneBuilder setScontistica(double scontistica) {
            this.scontistica = scontistica;
            return this;
        }

        public Promozione build() {
            return new Promozione(this);
        }
    }
    //Verifica idoneità
    public boolean verificaValidità() {
        boolean almenoUnoSettato =
            this.treno != null ||
            this.tratta != null ||
            (this.dataPartenza != null && !this.dataPartenza.isEmpty()) ||
            (this.dataFine != null && !this.dataFine.isEmpty()) ||
            this.clientiFedelta;


        return almenoUnoSettato && this.prezzoPartenza > 0 && this.scontistica > 0.0;
    }
    //Applica
    public boolean applica(Biglietto biglietto) {
        TrenoImpl db = TrenoImplDB.getInstance();
        double nuovoPrezzo = 0;
            if (! verificaValidità()) {
                System.out.println("Nessuna scontistica applicata");
                return false;

            } else {
                if( biglietto.getPrezzo() < prezzoPartenza ) {
                    if(biglietto.getTrenoBiglietto().equals(treno.getTrenoID())) {
                        nuovoPrezzo = (biglietto.getPrezzo() /100 ) * scontistica;
                    } else if( biglietto.getTrenoBiglietto().getTratta().getCodiceTratta().equals(tratta.getCodiceTratta()) ) {
                        nuovoPrezzo = (biglietto.getPrezzo() /100 ) * scontistica;
                    } else if (biglietto.getTrenoBiglietto().getTratta().getDataPartenza().equals(dataPartenza) &&
                            biglietto.getTrenoBiglietto().getTratta().getDataArrivo().equals(tratta.getDataArrivo())) {
                        nuovoPrezzo = (biglietto.getPrezzo() /100 ) * scontistica;
                    } else if(clientiFedelta) {
                        nuovoPrezzo = (biglietto.getPrezzo() /100 ) * scontistica;
                    }

                        }
                }
                    BPrimaClasse bpc = new BPrimaClasse.Builder()
                            .bigliettoID(biglietto.getBigliettoID())
                            .titolareBiglietto(biglietto.getTitolareBiglietto())
                            .trenoBiglietto(biglietto.getTrenoBiglietto())
                            .carrozza(biglietto.getCarrozza())
                            .posto(biglietto.getCarrozza())
                            .priorità(biglietto.getPriorità())
                            .prezzo((int)nuovoPrezzo)
                            .build();
        return true;
    }
}
