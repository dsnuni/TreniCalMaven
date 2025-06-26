package it.trenical.server.Cliente;


import io.grpc.stub.StreamObserver;
import it.trenical.grpc.Cliente; // Cliente proto
import it.trenical.server.Cliente.ClienteConcr; // Cliente Java

import it.trenical.server.Cliente.ClienteConcr;
import it.trenical.server.Cliente.ClienteImplDB;

public class ClienteServiceImpl extends it.trenical.grpc.ClienteServiceGrpc.ClienteServiceImplBase {

    private final ClienteImplDB db = ClienteImplDB.getInstance();

    @Override
    public void addCliente(it.trenical.grpc.AddClienteRequest request, StreamObserver<it.trenical.grpc.AddClienteResponse> responseObserver) {

        ClienteConcr clienteJava = convertiProtoInJava(request.getCliente());

        db.setCliente(clienteJava);

        it.trenical.grpc.AddClienteResponse response = it.trenical.grpc.AddClienteResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCliente(it.trenical.grpc.GetClienteRequest request, StreamObserver<Cliente> responseObserver) {
        String cf = request.getCodiceFiscale();

        ClienteConcr clienteJava = (ClienteConcr)  db.getCliente(cf);
        Cliente clienteProto = convertiJavaInProto(clienteJava);

        responseObserver.onNext(clienteProto);
        responseObserver.onCompleted();
    }

    @Override
    public void removeCliente(it.trenical.grpc.RemoveClienteRequest request, StreamObserver<it.trenical.grpc.RemoveClienteResponse> responseObserver) {
        boolean successo = db.removeCliente(request.getCodiceFiscale());

        it.trenical.grpc.RemoveClienteResponse response = it.trenical.grpc.RemoveClienteResponse.newBuilder()
                .setSuccess(successo)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private ClienteConcr  convertiProtoInJava(Cliente proto) {
        return new ClienteConcr(
                proto.getCodiceFiscale(),
                proto.getNome(),
                proto.getCognome(),
                proto.getCodiceCliente(),
                proto.getEta()
        );
    }

    private Cliente convertiJavaInProto(ClienteConcr clienteJava) {
        return Cliente.newBuilder()
                .setCodiceFiscale(clienteJava.getCodiceFiscale())
                .setNome(clienteJava.getNome())
                .setCognome(clienteJava.getCognome())
                .setCodiceCliente(clienteJava.getCodiceCliente())
                .setEta(clienteJava.getEtà())
                .build();
    }
}
