package it.trenical.client.pagamento;

public class Pagamento {


    public static boolean inserimentoDati(String codiceCarta, int cvv,
                                          String scadenza, String circuito, String titolare) {
        try {
            return validaNumeroCarta(String.valueOf(codiceCarta)) &&
                    validaCVV(String.valueOf(cvv)) &&
                    validaScadenza(scadenza) &&
                    circuito != null && !circuito.trim().isEmpty() &&
                    titolare != null && !titolare.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean paga(int prezzo) {
        System.out.println("verifichiamo i dati inseriti");
        System.out.println("contattiamo la tua banca per la autorizzazione");
        System.out.println("Stai autorizzando il pagamento della cifra di euro: " + prezzo);
        System.out.println("transazione eseguita correttamente");

        return true;
    }
    public static String regexNumeroCarta() {
        return "^\\d{16}$";
    }

    public static String regexNumeroScadenza() {
        return "^(0[1-9]|1[0-2])\\/\\d{2}$";
    }

    public static String regexNumeroCVV() {
        return "^\\d{3}$";
    }
    public static boolean validaNumeroCarta(String numeroCarta) {
        return numeroCarta != null && numeroCarta.matches(regexNumeroCarta());
    }

    public static boolean validaScadenza(String scadenza) {
        return scadenza != null && scadenza.matches(regexNumeroScadenza());
    }

    public static boolean validaCVV(String cvv) {
        return cvv != null && cvv.matches(regexNumeroCVV());
    }

}
