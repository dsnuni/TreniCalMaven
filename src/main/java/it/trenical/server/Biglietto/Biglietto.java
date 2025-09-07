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


    protected Biglietto(Builder builder) {
        this.bigliettoID = builder.bigliettoID;
        this.titolareBiglietto = builder.titolareBiglietto;
        this.trenoBiglietto = builder.trenoBiglietto;
        this.carrozza = builder.carrozza;
        this.posto = builder.posto;
        this.priorità = builder.priorità;
        this.prezzo = builder.prezzo;
        this.implementazione = builder.implementazione;
    }

    public String getBigliettoID() { return bigliettoID; }
    public Cliente getTitolareBiglietto() { return titolareBiglietto; }
    public Treno getTrenoBiglietto() { return trenoBiglietto; }
    public String getCarrozza() { return carrozza; }
    public String getPosto() { return posto; }
    public List<String> getPriorità() { return priorità; }
    public int getPrezzo() { return prezzo; }
    public BigliettoImpl getImplementazione() { return implementazione; }


    public Biglietto getBiglietto(String Codice) {
        return implementazione.getBiglietto(Codice);
    }

    public void setBiglietto(Biglietto biglietto) {
        implementazione.setBiglietto(biglietto);
    }

    public boolean removeBiglietto(String bigliettoID) {
        return implementazione.removeBiglietto(bigliettoID);
    }


    public static abstract class Builder  {
        private String bigliettoID;
        private Cliente titolareBiglietto;
        private Treno trenoBiglietto;
        private String carrozza;
        private String posto;
        private List<String> priorità = new ArrayList<>();
        private int prezzo;
        private BigliettoImpl implementazione;

        public Builder bigliettoID(String id) {
            this.bigliettoID = id;
            return this;
        }

        public Builder titolareBiglietto(Cliente cliente) {
            this.titolareBiglietto = cliente;
            return this;
        }

        public Builder trenoBiglietto(Treno treno) {
            this.trenoBiglietto = treno;
            return this;
        }

        public Builder carrozza(String carrozza) {
            this.carrozza = carrozza;
            return this;
        }

        public Builder posto(String posto) {
            this.posto = posto;
            return this;
        }

        public Builder priorità(List<String> priorità) {
            this.priorità = priorità;
            return this;
        }

        public Builder prezzo(int prezzo) {
//            int prezzoScontato = PromozioniGenerator.getInstance()
//                    .calcolaPrezzo(prezzo, titolareBiglietto.getCodiceCliente(), trenoBiglietto.getTratta());
//            this.prezzo = prezzoScontato;
            this.prezzo = prezzo;
            return this;
        }

        public Builder implementazione(BigliettoImpl implementazione) {
            this.implementazione = implementazione;
            return this;
        }


        public abstract Biglietto build();
    }


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
        try {
            if (nuovoPrezzo == 0) {
                return originale;
            }
            System.out.println("PREZZO FIANELC "+originale.getPrezzo()+"  PREZZO DA SOTTRARRE : "+nuovoPrezzo);
            System.out.println("Prezzo da scontare :");
            System.out.println(originale.getPrezzo() - nuovoPrezzo);
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

        } catch (Exception e) {
            System.err.println("Errore durante l'applicazione della promozione: " + e.getMessage());

        }
        return null;
    }

}
