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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClienteConcr that = (ClienteConcr) o;

        return codiceFiscale.equals(that.codiceFiscale)
                && nome.equals(that.nome)
                && cognome.equals(that.cognome)
                && codiceCliente.equals(that.codiceCliente);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(codiceFiscale, nome, cognome, codiceCliente);
    }

    @Override
    public String toString() {
        return "ClienteConcr{" +
                "codiceFiscale='" + codiceFiscale + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", codiceCliente='" + codiceCliente + '\'' +
                '}';
    }

}

