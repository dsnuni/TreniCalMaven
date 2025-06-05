package it.trenical.server.Cliente;


public abstract class Cliente {
    String codiceFiscale ="";
    String nome = "";
    String cognome = "";
    String codiceCliente = "";
    int età = 0;

    public Cliente(String codiceFiscale, String nome, String cognome, String codiceCliente) {
        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceCliente = codiceCliente;
    }

    public String getCodiceCliente() {
        return codiceCliente;
    }

    public void setCodiceCliente(String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public int getEtà() {
        return età;
    }

    public void setEtà(int età) {
        this.età = età;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public abstract Cliente getCliente(); //da passare a private con implementazione nel metodo
    public abstract void setCLiente();
    public abstract boolean remove();

    @Override
    public String toString() {
        return "";
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

