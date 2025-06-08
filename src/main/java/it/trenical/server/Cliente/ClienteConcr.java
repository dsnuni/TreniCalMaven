package it.trenical.server.Cliente;

public class ClienteConcr extends Cliente{

    public ClienteConcr(String codiceFiscale, String nome, String cognome, String codiceCliente, int età) {
        super(codiceFiscale, nome, cognome, codiceCliente, età);
    }

    @Override
    public Cliente getCliente() {
        return null;
    }

    @Override
    public boolean remove() {
        return false;
    }

    @Override
    public void setCLiente() {

    }
}
