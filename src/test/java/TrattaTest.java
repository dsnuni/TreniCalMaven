
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrattaTest {

    private TrattaImplDB db;
    private TrattaPrototype tratta;

    @BeforeEach
    public void setup() {
        db = TrattaImplDB.getInstance();
        tratta = new TrattaStandard("T100", "Torino", "Genova", "2025-06-11", "2025-06-11", 150, 90);
    }

    @Test
    public void testSetAndGetTratta() {
        db.setTratta(tratta);
        TrattaPrototype recuperata = db.getTratta("T100");

        assertNotNull(recuperata);
        assertEquals("T100", recuperata.getCodiceTratta());
        assertEquals("Torino", recuperata.getStazionePartenza());
        assertEquals("Genova", recuperata.getStazioneArrivo());
    }

    @Test
    public void testRemoveTratta() {
        db.setTratta(tratta);
        boolean rimossa = db.removeTratta("T100");

        assertTrue(rimossa);
        assertNull(db.getTratta("T100"));
    }
}
