package it.trenical.server.Cliente;


public class ClienteFactory {

    private static final ClienteImpl db = new ClienteImplDB();


    public static Cliente getClienteByCodiceFiscale(String codiceFiscale) {
        return db.getCliente(codiceFiscale);
    }
}

