package it.trenical.server.Biglietto;

import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Treno.Treno;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Biglietto {
    protected final String bigliettoID;
    protected final Cliente titolareBiglietto;
    protected final Treno trenoBiglietto;
    protected final String carrozza;
    protected final String posto;
    protected final List<String> priorità;
    protected final int prezzo;
    protected final BigliettoImpl implementazione;

    // Costruttore protetto: usato solo dal builder
    protected Biglietto(Builder<?> builder) {
        this.bigliettoID = builder.bigliettoID;
        this.titolareBiglietto = builder.titolareBiglietto;
        this.trenoBiglietto = builder.trenoBiglietto;
        this.carrozza = builder.carrozza;
        this.posto = builder.posto;
        this.priorità = builder.priorità;
        this.prezzo = builder.prezzo;
        this.implementazione = builder.implementazione;
    }

    // ======== GETTER ========
    public String getBigliettoID() { return bigliettoID; }
    public Cliente getTitolareBiglietto() { return titolareBiglietto; }
    public Treno getTrenoBiglietto() { return trenoBiglietto; }
    public String getCarrozza() { return carrozza; }
    public String getPosto() { return posto; }
    public List<String> getPriorità() { return priorità; }
    public int getPrezzo() { return prezzo; }
    public BigliettoImpl getImplementazione() { return implementazione; }

    // ======== INTERFACCIA DB ========
    public Biglietto getBiglietto(String Codice) {
        return implementazione.getBiglietto(Codice);
    }

    public void setBiglietto(Biglietto biglietto) {
        implementazione.setBiglietto(biglietto);
    }

    public boolean removeBiglietto(String bigliettoID) {
        return implementazione.removeBiglietto(bigliettoID);
    }

    // ======== BUILDER ASTRATTO ========
    public static abstract class Builder<T extends Builder<T>> {
        private String bigliettoID;
        private Cliente titolareBiglietto;
        private Treno trenoBiglietto;
        private String carrozza;
        private String posto;
        private List<String> priorità = new ArrayList<>();
        private int prezzo;
        private BigliettoImpl implementazione;

        public T bigliettoID(String id) {
            this.bigliettoID = id;
            return self();
        }

        public T titolareBiglietto(Cliente cliente) {
            this.titolareBiglietto = cliente;
            return self();
        }

        public T trenoBiglietto(Treno treno) {
            this.trenoBiglietto = treno;
            return self();
        }

        public T carrozza(String carrozza) {
            this.carrozza = carrozza;
            return self();
        }

        public T posto(String posto) {
            this.posto = posto;
            return self();
        }

        public T priorità(List<String> priorità) {
            this.priorità = priorità;
            return self();
        }

        public T prezzo(int prezzo) {
//            int prezzoScontato = PromozioniGenerator.getInstance()
//                    .calcolaPrezzo(prezzo, titolareBiglietto.getCodiceCliente(), trenoBiglietto.getTratta());
//            this.prezzo = prezzoScontato;
            this.prezzo = prezzo;
            return self();
        }

        public T implementazione(BigliettoImpl implementazione) {
            this.implementazione = implementazione;
            return self();
        }

        protected abstract T self();
        public abstract Biglietto build();
    }

    // ======== EQUALS / HASH / TOSTRING ========
    @Override
    public String toString() {
        return "Biglietto{" +
                "bigliettoID='" + bigliettoID + '\'' +
                ", titolareBiglietto=" + titolareBiglietto +
                ", trenoBiglietto=" + trenoBiglietto +
                ", carrozza='" + carrozza + '\'' +
                ", posto='" + posto + '\'' +
                ", priorità=" + priorità +
                ", prezzo=" + prezzo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Biglietto that = (Biglietto) o;
        return Objects.equals(bigliettoID, that.bigliettoID) &&
                Objects.equals(titolareBiglietto, that.titolareBiglietto) &&
                Objects.equals(trenoBiglietto, that.trenoBiglietto) &&
                Objects.equals(carrozza, that.carrozza) &&
                Objects.equals(posto, that.posto) &&
                Objects.equals(priorità, that.priorità);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bigliettoID, titolareBiglietto, trenoBiglietto, carrozza, posto, priorità);
    }

    public static Biglietto clonaConPrezzo(Biglietto originale, int nuovoPrezzo) {
        if (originale instanceof BPrimaClasse) {
            return new BPrimaClasse.Builder()
                    .bigliettoID(originale.getBigliettoID())
                    .titolareBiglietto(originale.getTitolareBiglietto())
                    .trenoBiglietto(originale.getTrenoBiglietto())
                    .carrozza(originale.getCarrozza())
                    .posto(originale.getPosto())
                    .priorità(originale.getPriorità())
                    .prezzo(nuovoPrezzo)
                    .implementazione(BigliettoDB.getInstance())
                    .build();
        } else if (originale instanceof BSecondaClasse) {
            return new BSecondaClasse.Builder()
                    .bigliettoID(originale.getBigliettoID())
                    .titolareBiglietto(originale.getTitolareBiglietto())
                    .trenoBiglietto(originale.getTrenoBiglietto())
                    .carrozza(originale.getCarrozza())
                    .posto(originale.getPosto())
                    .priorità(originale.getPriorità())
                    .prezzo(nuovoPrezzo)
                    .implementazione(BigliettoDB.getInstance())
                    .build();
        } else if (originale instanceof BTerzaClasse) {
            return new BTerzaClasse.Builder()
                    .bigliettoID(originale.getBigliettoID())
                    .titolareBiglietto(originale.getTitolareBiglietto())
                    .trenoBiglietto(originale.getTrenoBiglietto())
                    .carrozza(originale.getCarrozza())
                    .posto(originale.getPosto())
                    .priorità(originale.getPriorità())
                    .prezzo(nuovoPrezzo)
                    .implementazione(BigliettoDB.getInstance())
                    .build();
        }

        return null;
    }

}
