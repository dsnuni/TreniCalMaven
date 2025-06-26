package it.trenical.server.igGenerator;

import io.grpc.stub.StreamObserver;
import it.trenical.grpc.GetGeneratedIDRequest;
import it.trenical.grpc.GetGeneratedIDResponse;
import it.trenical.grpc.IDGeneratorServiceGrpc;

public class IDGeneratorServiceImpl extends IDGeneratorServiceGrpc.IDGeneratorServiceImplBase {

    @Override
    public void getGeneratedID(GetGeneratedIDRequest request, StreamObserver<GetGeneratedIDResponse> responseObserver) {
        String clienteID = IdGenerator.generaClienteID();
        boolean fidelizzato = request.getFidelizzato();
        String codiceCliente = IdGenerator.generaCodiceCliente(fidelizzato);
        String bigliettoID = IdGenerator.generaBigliettoID();
        String trenoID = IdGenerator.generaTrenoID();
        String trattaID = IdGenerator.generaTrattaID();

        GetGeneratedIDResponse response = GetGeneratedIDResponse.newBuilder()
                .setClienteID(clienteID)
                .setCodiceCliente(codiceCliente)
                .setBigliettoID(bigliettoID)
                .setTrenoID(trenoID)
                .setTrattaID(trattaID)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
