package it.trenical.server.Cliente;


public class ClienteFactory {

    private static ClienteImpl db = new ClienteImplDB();

    public static ClienteConcr getClienteByCodiceFiscale(String codiceFiscale) {



        return (ClienteConcr) db.getCliente(codiceFiscale);
    }
}

