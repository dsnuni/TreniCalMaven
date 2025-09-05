package it.trenical.server.igGenerator;


public class Regex {

    public static String regexCodiceFiscale() {
        return "^[A-Z]{6}\\d{5}$";
    }

    public static String regexNome() {
        return "^[A-Za-zÀ-ÿ\\s']{2,30}$";
    }

    public static String regexCognome() {
        return "^[A-Za-zÀ-ÿ\\s']{2,50}$";
    }

    public static String regexemail() {
        return "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    }

    public static boolean validaCodiceFiscale(String codiceFiscale) {
        return codiceFiscale != null && codiceFiscale.matches(regexCodiceFiscale());
    }

    public static boolean validaNome(String nome) {
        return nome != null && nome.matches(regexNome());
    }

    public static boolean validaCognome(String cognome) {
        return cognome != null && cognome.matches(regexCognome());
    }

    public static boolean validaEmail(String email) {
        return email != null && email.matches(regexemail());
    }
}