package it.trenical.server.Cliente;

public interface ClienteImpl {



    public Cliente getCliente(String codiceFiscale);
    public void setCliente(Cliente cl);
    public boolean removeCliente(String codiceFiscale);
}
