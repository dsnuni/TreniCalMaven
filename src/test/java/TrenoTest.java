

import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrenoTest {

    private TrenoImpl db;
    private TrenoConcr treno;
    private TrattaStandard tratta;

    @BeforeEach
    public void setup() {
        db = TrenoImplDB.getInstance();
        db.removeTreno("T123");
        tratta = new TrattaStandard("TRT-8c3eae47", "Terni", "Bressanone", "12-06-2025 10:00", "12-06-2025 12:00 ", 468, 2);
        treno = new TrenoConcr("T123", "FrecciaRossa", tratta, 100, 10, 40, 50, 100);
    }

    @Test
    public void testSetAndGetTreno() {
        db = TrenoImplDB.getInstance();
        assertNotNull(TrenoImplDB.getInstance(), "Il Singleton non è stato istanziato correttamente");
        db.setTreno(treno);
        Treno t = db.getTreno("T123");
            System.out.println(t.toString());
          assertNotNull(t);
          assertEquals("T123", t.getTrenoID());
          assertEquals("FrecciaRossa", t.getTipoTreno());
          assertEquals("Terni", t.getTratta().getStazionePartenza());
    }

    @Test
    public void testRemoveTreno() {
        db.setTreno(treno);
        assertTrue(db.removeTreno("T123"));
        assertNull(db.getTreno("T123"));
    }

    @Test
    public void testGetAllTreno() {
        TrenoImpl db = TrenoImplDB.getInstance();
        List<Treno> treni = db.getAllTreno();

        assertNotNull(treni, "La lista dei treni non dovrebbe essere null");

        System.out.println("Numero treni trovati: " + treni.size());
        for (Treno t : treni) {
            assertNotNull(t.getTrenoID(), "Ogni treno deve avere un ID");
            assertNotNull(t.getTipoTreno(), "Ogni treno deve avere un tipo");
            assertNotNull(t.getTratta(), "Ogni treno deve avere una tratta");
            assertTrue(t.getPostiTot() >= 0, "Posti totali devono essere >= 0");

            System.out.println("Treno: " + t.getTrenoID() +
                    ", Tipo: " + t.getTipoTreno() +
                    ", Tratta: " + t.getTratta().getCodiceTratta());
        }
    }

    @Test
    public void testGetTrenoByID() {
        TrenoImplDB db = TrenoImplDB.getInstance();
        Treno treno = new TrenoConcr(
                "TRNTEST123",
                "FrecciaRossa",
                new TrattaStandard("TRT-8c3eae47", "Roma", "Milano", "2025-06-15", "2025-06-15", 600, 2),
                100,
                50, 80, 100,
                230
        );

        db.setTreno(treno);
        Treno result = db.getTreno("TRNTEST123");
        assertNotNull(result);
        assertEquals("TRNTEST123", result.getTrenoID());
        assertEquals("FrecciaRossa", result.getTipoTreno());
        assertEquals(100, result.getPrezzo());
    }

}