package it.trenical.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.server.Biglietto.BigliettoServiceImpl;
import it.trenical.server.Cliente.ClienteServiceImpl;
import it.trenical.server.notifiche.LogObserver;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaServiceImpl;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.Treno.TrenoServiceImpl;
import it.trenical.server.gui.AdminDashboardSwing;
import it.trenical.server.igGenerator.IDGeneratorServiceImpl;
import it.trenical.server.notifiche.AnalizzatoreTratte;
import it.trenical.server.notifiche.NotificaServiceImpl;


import javax.swing.*;
import java.io.IOException;

public class TreniCalServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        TrenoImplDB trenoDB = TrenoImplDB.getInstance();
        TrattaImplDB trattaDB = TrattaImplDB.getInstance();

        LogObserver logObserver = new LogObserver("SISTEMA_NOTIFICHE");

        trenoDB.addObserver(logObserver);
        trattaDB.addObserver(logObserver);

        System.out.println("✅ LogObserver registrato - notifiche automatiche attive!");
        AnalizzatoreTratte pulitore = new AnalizzatoreTratte();
        //pulitore.rimuoviTratteObsolete();
        pulitore.avviaControlloPeriodico();
        //Generatore.genera(50,30,100,70);
        //TrenoImplDB trenoDB = TrenoImplDB.getInstance();
       // trenoDB.addObserver(new NotificheConcr());
       // System.setProperty("sun.java2d.uiScale", "3.0");
        SwingUtilities.invokeLater(() -> {
            //   System.setProperty("sun.java2d.uiScale", "3.0");
            new AdminDashboardSwing().setVisible(true);

        });
        Server server = ServerBuilder.forPort(50051)
                .addService(new ClienteServiceImpl())
                .addService(new TrenoServiceImpl())
                .addService(new BigliettoServiceImpl())
                .addService(new TrattaServiceImpl())
                .addService(new IDGeneratorServiceImpl())
                .addService(new NotificaServiceImpl())
                .build();

        server.start();
        System.out.println("Server gRPC avviato sulla porta 50051");
        server.awaitTermination();
    }
}
