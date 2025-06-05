package it.trenical.server.Cliente;


public class ClienteFactory {

    private ClienteImpl db = new ClienteImplDB();

    public static Cliente getClienteByCodiceFiscale(String codiceFiscale) {

         ClienteImpl db = new ClienteImplDB();

        return db.getCliente(codiceFiscale);
    }
}

