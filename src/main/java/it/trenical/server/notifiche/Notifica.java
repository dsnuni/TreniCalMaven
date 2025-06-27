package it.trenical.server.notifiche;

public class Notifica {
    private String cliente;
    private String treno;
    private String Partenza;
    private String arrivo;
    private int tempo;
    private String biglietto;

    public Notifica(String cliente, String treno, String Partenza, String Arrivo, int tempo,String biglietto) {
        this.cliente = cliente;
        this.treno = treno;
        this.Partenza = Partenza;
        this.arrivo = Arrivo;
        this.tempo = tempo;
        this.biglietto = biglietto;
    }

    public String getArrivo() {
        return arrivo;
    }

    public String getBiglietto() {
        return biglietto;
    }

    public String getCliente() {
        return cliente;
    }

    public String getPartenza() {
        return Partenza;
    }

    public int getTempo() {
        return tempo;
    }

    public String getTreno() {
        return treno;
    }
}
