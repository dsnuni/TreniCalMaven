package it.trenical.server.Cliente;


import io.grpc.stub.StreamObserver;
import it.trenical.grpc.Cliente;


public class ClienteServiceImpl extends it.trenical.grpc.ClienteServiceGrpc.ClienteServiceImplBase {

    @Override
    public void addCliente(it.trenical.grpc.AddClienteRequest request, StreamObserver<it.trenical.grpc.AddClienteResponse> responseObserver) {
        Cliente cliente = request.getCliente();
        System.out.println("Aggiunto cliente: " + cliente.getNome());

        it.trenical.grpc.AddClienteResponse response = it.trenical.grpc.AddClienteResponse.newBuilder()
                .setSuccess(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCliente(it.trenical.grpc.GetClienteRequest request, StreamObserver<Cliente> responseObserver) {
        String cf = request.getCodiceFiscale();
        System.out.println("Richiesta cliente con CF: " + cf);

        Cliente cliente = Cliente.newBuilder()
                .setCodiceFiscale(cf)
                .setNome("Mario")
                .setCognome("Rossi")
                .setCodiceCliente("TRNCL123")
                .setEta(30)
                .build();

        responseObserver.onNext(cliente);
        responseObserver.onCompleted();
    }

    @Override
    public void removeCliente(it.trenical.grpc.RemoveClienteRequest request, StreamObserver<it.trenical.grpc.RemoveClienteResponse> responseObserver) {
        String cf = request.getCodiceFiscale();
        System.out.println("Rimozione cliente con CF: " + cf);

        it.trenical.grpc.RemoveClienteResponse response = it.trenical.grpc.RemoveClienteResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
