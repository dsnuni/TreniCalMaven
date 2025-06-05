package it.trenical.server.Biglietto;

import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Treno.Treno;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Biglietto {
    String bigliettoID = "";
    Cliente titolareBiglietto = null;
    Treno trenoBiglietto = null;
    String carrozza = "";
    String posto = "";
    List<String> priorità = new ArrayList<>();
    int prezzo = 0;

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    protected BigliettoImpl implementazione;


    public String getBigliettoID() {
        return bigliettoID;
    }

    public void setBigliettoID(String bigliettoID) {
        this.bigliettoID = bigliettoID;
    }

    public String getCarrozza() {
        return carrozza;
    }

    public void setCarrozza(String carrozza) {
        this.carrozza = carrozza;
    }

    public BigliettoImpl getImplementazione() {
        return implementazione;
    }

    public void setImplementazione(BigliettoImpl implementazione) {
        this.implementazione = implementazione;
    }

    public String getPosto() {
        return posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public List<String> getPriorità() {
        return priorità;
    }

    public void setPriorità(List<String> priorità) {
        this.priorità = priorità;
    }

    public Cliente getTitolareBiglietto() {
        return titolareBiglietto;
    }

    public void setTitolareBiglietto(Cliente titolareBiglietto) {
        this.titolareBiglietto = titolareBiglietto;
    }

    public Treno getTrenoBiglietto() {
        return trenoBiglietto;
    }

    public void setTrenoBiglietto(Treno trenoBiglietto) {
        this.trenoBiglietto = trenoBiglietto;
    }


    public Biglietto getBiglietto(String Codice) {
        return implementazione.getBiglietto(Codice);
    }


    public void setBiglietto(Biglietto biglietto) {
        implementazione.setBiglietto(biglietto);
    }


    public boolean removeBiglietto(String bigliettoID) {
        if (implementazione.removeBiglietto(bigliettoID)){
            return true;
        } return false;

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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Biglietto that = (Biglietto) o;
        return Objects.equals(bigliettoID, that.bigliettoID) && Objects.equals(titolareBiglietto, that.titolareBiglietto) && Objects.equals(trenoBiglietto, that.trenoBiglietto) && Objects.equals(carrozza, that.carrozza) && Objects.equals(posto, that.posto) && Objects.equals(priorità, that.priorità);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bigliettoID, titolareBiglietto, trenoBiglietto, carrozza, posto, priorità);
    }
}
