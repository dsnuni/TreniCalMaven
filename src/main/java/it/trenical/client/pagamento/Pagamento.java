package it.trenical.client.pagamento;

public class Pagamento {
    private int codiceCarta;
    private int cvv;
    private int scadenza;
    private String circuito;
    private String titolare;
    private int prezzo;

    public Pagamento(int codiceCarta, int cvv, int scadenza, String circuito, String titolare, int prezzo) {
        this.codiceCarta = codiceCarta;
        this.cvv = cvv;
        this.scadenza = scadenza;
        this.circuito = circuito;
        this.titolare = titolare;
        this.prezzo = prezzo;

    }
    public boolean paga() {
        System.out.println("verifichiamo i dati inseriti");
        System.out.println("contattiamo la tua banca per la autorizzazione");
        System.out.println("transazione eseguita correttamente");
        return true;
    }
}
