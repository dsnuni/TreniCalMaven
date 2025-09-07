import it.trenical.server.Biglietto.*;
import it.trenical.server.Cliente.ClienteConcr;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.promozione.ApplicaPromozione;
import it.trenical.server.promozione.Promozione;
import it.trenical.server.promozione.PromozioneImplDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BigliettoTest {

    private BigliettoDB db;
    private ClienteImplDB clienteDB;
    private TrenoImplDB trenoDB;
    private TrattaImplDB trattaDB;

    private String bigliettoID;
    private ClienteConcr cliente;
    private TrenoConcr treno;
    private TrattaStandard tratta;
    private Biglietto biglietto;

    @BeforeEach
    public void setup() {
        db = BigliettoDB.getInstance();
        clienteDB = ClienteImplDB.getInstance();
        trenoDB = TrenoImplDB.getInstance();
        trattaDB = TrattaImplDB.getInstance();

        bigliettoID = "TEST_" + System.currentTimeMillis();

        cliente = new ClienteConcr("TSTCOD01A01H501Z", "Mario", "Rossi", "CL001", 30, "");
        clienteDB.setCliente(cliente);

        tratta = new TrattaStandard("TR001", "Milano", "Roma", "2025-12-01 10:00", "2025-12-01 15:00", 570, 300);
        trattaDB.setTratta(tratta);

        treno = new TrenoConcr("TRE001", "FrecciaRossa", tratta, 100, 20, 50, 80, 150,23," ");
        trenoDB.setTreno(treno);

        List<String> priorita = new ArrayList<>();
        priorita.add("Finestrino");

        biglietto = new BTerzaClasse.Builder()
                .bigliettoID(bigliettoID)
                .titolareBiglietto(cliente)
                .trenoBiglietto(treno)
                .carrozza("C")
                .posto("1C")
                .priorità(priorita)
                .prezzo(100)
                .implementazione(db)
                .build();
    }

    @AfterEach
    public void cleanup() {
        db.removeBiglietto(bigliettoID);
        trenoDB.removeTreno("TRE001");
        trattaDB.removeTratta("TR001");
        clienteDB.removeCliente("TSTCOD01A01H501Z");
    }

    @Test
    public void testSetBiglietto() {
        db.setBiglietto(biglietto);
        Biglietto recuperato = db.getBiglietto(bigliettoID);
        assertNotNull(recuperato);
    }

    @Test
    public void testGetBiglietto() {
        db.setBiglietto(biglietto);
        Biglietto recuperato = db.getBiglietto(bigliettoID);
        assertEquals(bigliettoID, recuperato.getBigliettoID());
        assertEquals(cliente.getCodiceFiscale(), recuperato.getTitolareBiglietto().getCodiceFiscale());
        assertEquals(treno.getTrenoID(), recuperato.getTrenoBiglietto().getTrenoID());
        assertEquals("C", recuperato.getCarrozza());
        assertEquals("1C", recuperato.getPosto());
        assertEquals(100, recuperato.getPrezzo());
    }

    @Test
    public void testRemoveBiglietto() {
        db.setBiglietto(biglietto);
        assertTrue(db.removeBiglietto(bigliettoID));
        assertNull(db.getBiglietto(bigliettoID));
    }

    @Test
    public void testGetBigliettoInesistente() {
        assertNull(db.getBiglietto("ID_INESISTENTE"));
    }

    @Test
    public void testGetAllBiglietti() {
        db.setBiglietto(biglietto);
        List<Biglietto> biglietti = db.getAllBiglietti();
        assertTrue(biglietti.size() > 0);
    }

    @Test
    public void testGetBigliettiByFiltro() {
        db.setBiglietto(biglietto);
        List<Biglietto> biglietti = db.getByFiltro("cliente_id", cliente.getCodiceFiscale());
        assertTrue(biglietti.size() > 0);
    }

    @Test
    public void testGetBigliettiByTrenoID() {
        db.setBiglietto(biglietto);
        List<Biglietto> biglietti = db.getBigliettiByTrenoID(treno.getTrenoID());
        assertTrue(biglietti.size() > 0);
    }

    @Test
    public void testPrioritaSerializzazione() {
        List<String> priorita = new ArrayList<>();
        priorita.add("Finestrino");
        priorita.add("Silenzio");

        Biglietto b = new BTerzaClasse.Builder()
                .bigliettoID(bigliettoID + "_PRIO")
                .titolareBiglietto(cliente)
                .trenoBiglietto(treno)
                .carrozza("C")
                .posto("2C")
                .priorità(priorita)
                .prezzo(100)
                .implementazione(db)
                .build();

        db.setBiglietto(b);
        Biglietto recuperato = db.getBiglietto(bigliettoID + "_PRIO");
        assertEquals(2, recuperato.getPriorità().size());
        assertTrue(recuperato.getPriorità().contains("Finestrino"));
        assertTrue(recuperato.getPriorità().contains("Silenzio"));

        db.removeBiglietto(bigliettoID + "_PRIO");
    }

    @Test
    public void testClassiBiglietto() {
        Biglietto prima = new BPrimaClasse.Builder()
                .bigliettoID(bigliettoID + "_PRIMA")
                .titolareBiglietto(cliente)
                .trenoBiglietto(treno)
                .carrozza("A")
                .posto("1A")
                .priorità(new ArrayList<>())
                .prezzo(125)
                .implementazione(db)
                .build();

        db.setBiglietto(prima);
        Biglietto recuperato = db.getBiglietto(bigliettoID + "_PRIMA");
        assertTrue(recuperato instanceof BPrimaClasse);

        db.removeBiglietto(bigliettoID + "_PRIMA");
    }

}