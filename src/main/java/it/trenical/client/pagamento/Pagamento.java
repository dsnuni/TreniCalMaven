package it.trenical.client.pagamento;

public class Pagamento {


    public static boolean inserimentoDati(int codiceCarta, int cvv,
                  int scadenza, String circuito, String titolare) {
        return codiceCarta != 0 &&
                cvv != 0 &&
                scadenza != 0 &&
                circuito != null && !circuito.trim().isEmpty() &&
                titolare != null && !titolare.trim().isEmpty();

    }
    public static boolean paga(int prezzo) {
        System.out.println("verifichiamo i dati inseriti");
        System.out.println("contattiamo la tua banca per la autorizzazione");
        System.out.println("Stai autorizzando il pagamento della cifra di euro: " + prezzo);
        System.out.println("transazione eseguita correttamente");

        return true;
    }

}
