package hu.nye.progtech.zhvedes.persistence;

import hu.nye.progtech.zhvedes.model.Tabla;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SzovegesTablaBetoltoTest {

    @Test
    void tesztBetoltAlap() {
        SzovegesTablaBetolto betolto = new SzovegesTablaBetolto();
        Tabla tabla = betolto.betoltAlap();

        assertNotNull(tabla);
        assertEquals(6, tabla.sorokSzama());
        assertEquals(12, tabla.oszlopokSzama());
        assertFalse(tabla.jarmuvek().isEmpty());
    }

    @Test
    void tesztNemLetezoFajl() {
        SzovegesTablaBetolto betolto = new SzovegesTablaBetolto();
        // Ha nem létezik a fájl, a default pályát kell visszaadnia
        Tabla tabla = betolto.betolt("nem_letezo_fajl.txt");

        assertNotNull(tabla);
        assertEquals(5, tabla.jarmuvek().size()); // Az alap pályán 5 jármű van
    }
}