

import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class TrenoTest {

    private TrenoImpl db;
    private TrenoConcr treno;
    private TrattaPrototype tratta;

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
}