

import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrenoTest {

    static class InMemoryTrenoImpl  extends TrenoImplDB {
        private final Map<Integer, Treno> storage = new HashMap<>();




    }

    static class TestableTrenoConcr extends TrenoConcr {
        TestableTrenoConcr(int trenoID, String tipoTreno, TrattaPrototype tratta, TrenoImpl impl) {
            super(impl);
            try {
                Field idF = Treno.class.getDeclaredField("trenoID");
                idF.setAccessible(true);
                idF.setInt(this, trenoID);
                Field typeF = Treno.class.getDeclaredField("tipoTreno");
                typeF.setAccessible(true);
                typeF.set(this, tipoTreno);
                Field trattaF = Treno.class.getDeclaredField("tratta");
                trattaF.setAccessible(true);
                trattaF.set(this, tratta);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    void testSetAndGetTreno() {
        InMemoryTrenoImpl impl = new InMemoryTrenoImpl();
        TrattaPrototype tratta = new TrattaStandard("T1", "A", "B", "dp", "da", 100, 2);
        TestableTrenoConcr treno = new TestableTrenoConcr(1, "Freccia", tratta, impl);
        treno.setTreno();
        Treno result = treno.getTreno();
        System.out.println("testSetAndGetTreno -> " + result);
        assertNotNull(result);
        assertEquals(1, result.getTrenoID());
        assertEquals("Freccia", result.getTipoTreno());
        assertEquals(tratta, result.getTratta());
        TrenoImplDB.removeAll();
    }

    @Test
    void testRemoveTreno() {
        InMemoryTrenoImpl impl = new InMemoryTrenoImpl();
        TrattaPrototype tratta = new TrattaStandard("T1", "A", "B", "dp", "da", 100, 2);
        TestableTrenoConcr treno = new TestableTrenoConcr(2, "Regionale", tratta, impl);
        treno.setTreno();
        boolean removed = treno.remove();
        System.out.println("testRemoveTreno -> " + removed);
        assertTrue(removed);
        assertNull(impl.getTreno(2));
    }

    @Test
    void testTrenoFactoryGetTrenoByID() {
        InMemoryTrenoImpl impl = new InMemoryTrenoImpl();
        TrenoFactory factory = new TrenoFactory(impl);
        TrattaPrototype tratta = new TrattaStandard("T1", "A", "B", "dp", "da", 100, 2);
        TestableTrenoConcr treno = new TestableTrenoConcr(42, "AltaVelocita", tratta, impl);
        treno.setTreno();
        Treno result = factory.getTrenoByID("42");
        System.out.println("testTrenoFactoryGetTrenoByID -> " + result);
        assertNotNull(result);
        assertEquals(42, result.getTrenoID());
    }

    @Test
    void testTrenoGetters() {
        InMemoryTrenoImpl impl = new InMemoryTrenoImpl();
        TrattaPrototype tratta = new TrattaStandard("T2", "X", "Y", "dp2", "da2", 80, 3);
        TestableTrenoConcr treno = new TestableTrenoConcr(5, "Regional", tratta, impl);
        System.out.println("testTrenoGetters -> " + treno.getTrenoID() + "," + treno.getTipoTreno());
        assertEquals(5, treno.getTrenoID());
        assertEquals("Regional", treno.getTipoTreno());
        assertEquals(tratta, treno.getTratta());
    }
}