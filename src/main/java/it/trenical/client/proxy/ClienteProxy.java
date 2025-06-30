package it.trenical.client.proxy;

import io.grpc.ManagedChannel;
import it.trenical.grpc.*;


public class ClienteProxy {

    private final ManagedChannel channel;
    private Cliente cliente;

    public ClienteProxy(ManagedChannel channel) {
        this.channel = channel;
    }

    public static class Builder {
        private final ManagedChannel channel;
        private String cf, nome, cognome, etaStr;
        private boolean isFedelta;

        public Builder(ManagedChannel channel) {
            this.channel = channel;
        }

        public Builder cf(String cf) {
            this.cf = cf;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder cognome(String cognome) {
            this.cognome = cognome;
            return this;
        }

        public Builder eta(String etaStr) {
            this.etaStr = etaStr;
            return this;
        }

        public Builder fedelta(boolean fedelta) {
            this.isFedelta = fedelta;
            return this;
        }

        public ClienteProxy buildAndRegister() {
            ClienteProxy proxy = new ClienteProxy(channel);
            proxy.registerCliente(cf, nome, cognome, etaStr, isFedelta);
            return proxy;
        }
    }

    private void registerCliente(String cf, String nome, String cognome, String etaStr, boolean isFedelta) {
        try {
            IDGeneratorServiceGrpc.IDGeneratorServiceBlockingStub idStub = IDGeneratorServiceGrpc.newBlockingStub(channel);
            GetGeneratedIDResponse idResponse = idStub.getGeneratedID(
                    GetGeneratedIDRequest.newBuilder().setFidelizzato(isFedelta).build()
            );

            ClienteServiceGrpc.ClienteServiceBlockingStub clienteStub = ClienteServiceGrpc.newBlockingStub(channel);
            this.cliente = Cliente.newBuilder()
                    .setCodiceFiscale(cf)
                    .setNome(nome)
                    .setCognome(cognome)
                    .setEta(Integer.parseInt(etaStr))
                    .setCodiceCliente(idResponse.getCodiceCliente())
                    .build();

            AddClienteResponse response = clienteStub.addCliente(
                    AddClienteRequest.newBuilder().setCliente(this.cliente).build()
            );

            if (!response.getSuccess()) {
                this.cliente = null;
                throw new RuntimeException("Registrazione cliente fallita");
            }

        } catch (Exception e) {
            this.cliente = null;
            throw new RuntimeException("Errore durante la registrazione del cliente: " + e.getMessage(), e);
        }
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public Cliente getCliente(String codiceCliente) {
        try {
            ClienteServiceGrpc.ClienteServiceBlockingStub clienteStub = ClienteServiceGrpc.newBlockingStub(channel);
            GetClienteRequest request = GetClienteRequest.newBuilder()
                    .setCodiceFiscale(codiceCliente)
                    .build();
            return clienteStub.getCliente(request);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del cliente: " + e.getMessage(), e);
        }
    }
}
