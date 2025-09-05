package it.trenical.server.Cliente;


public class ClienteFactory {

    private static ClienteImpl db = ClienteImplDB.getInstance();

    public static ClienteConcr getClienteByCodiceFiscale(String codiceFiscale) {



        return (ClienteConcr) db.getCliente(codiceFiscale);
    }
}

