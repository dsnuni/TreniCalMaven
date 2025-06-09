package it.trenical.server.igGenerator;

import io.grpc.stub.StreamObserver;
import it.trenical.grpc.GetGeneratedIDRequest;
import it.trenical.grpc.GetGeneratedIDResponse;
import it.trenical.grpc.IDGeneratorServiceGrpc;

public class IDGeneratorServiceImpl extends IDGeneratorServiceGrpc.IDGeneratorServiceImplBase {

    @Override
    public void getGeneratedID(GetGeneratedIDRequest request, StreamObserver<GetGeneratedIDResponse> responseObserver) {
        String clienteID = IdGenerator.generaClienteID();
        String codiceCliente = IdGenerator.generaCodiceCliente();
        String bigliettoID = IdGenerator.generaBigliettoID();
        int trenoID = IdGenerator.generaTrenoID();

        GetGeneratedIDResponse response = GetGeneratedIDResponse.newBuilder()
                .setClienteID(clienteID)
                .setCodiceCliente(codiceCliente)
                .setBigliettoID(bigliettoID)
                .setTrenoID(trenoID)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
