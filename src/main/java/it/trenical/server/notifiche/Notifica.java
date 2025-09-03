package it.trenical.server.notifiche;

public class Notifica {
    private String cliente;
    private String treno;
    private String partenza;
    private String arrivo;
    private int tempo;
    private String biglietto;
    private String stato;
    private String posto;
    private int binario;
    private String log;

    public Notifica(String cliente, String treno, String partenza, String arrivo,
                    int tempo, String biglietto, String stato, String posto,
                    int binario, String log) {
        this.cliente = cliente;
        this.treno = treno;
        this.partenza = partenza;
        this.arrivo = arrivo;
        this.tempo = tempo;
        this.biglietto = biglietto;
        this.stato = stato;
        this.posto = posto;
        this.binario = binario;
        this.log = log;
    }

    public String getCliente() {
        return cliente;
    }

    public String getTreno() {
        return treno;
    }

    public String getPartenza() {
        return partenza;
    }

    public String getArrivo() {
        return arrivo;
    }

    public int getTempo() {
        return tempo;
    }

    public String getBiglietto() {
        return biglietto;
    }

    public String getStato() {
        return stato;
    }

    public String getPosto() {
        return posto;
    }

    public int getBinario() {
        return binario;
    }

    public String getLog() {
        return log;
    }


    @Override
    public String toString() {
        return "Notifica{" +
                "cliente='" + cliente + '\'' +
                ", treno='" + treno + '\'' +
                ", partenza='" + partenza + '\'' +
                ", arrivo='" + arrivo + '\'' +
                ", tempo=" + tempo +
                ", biglietto='" + biglietto + '\'' +
                ", stato='" + stato + '\'' +
                ", posto='" + posto + '\'' +
                ", binario=" + binario +
                ", log='" + log + '\'' +
                '}';
    }
}
