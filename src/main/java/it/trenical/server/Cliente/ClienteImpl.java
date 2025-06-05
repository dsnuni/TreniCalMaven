package it.trenical.server.Cliente;

public interface ClienteImpl {



    public Cliente getCliente(String codiceFiscale); //da passare a private con implementazione nel metodo
    public void setCliente(Cliente cl);
    public boolean remove();
}
