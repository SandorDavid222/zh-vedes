package hu.nye.progtech.zhvedes.service;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Pozicio;
import hu.nye.progtech.zhvedes.model.Tabla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JatekLogikaTest {

    private JatekLogika jatekLogika;
    private Tabla tabla;

    @BeforeEach
    void setUp() {
        jatekLogika = new JatekLogika();
        Jarmu a = new Jarmu("A", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 0));
        Jarmu b = new Jarmu("B", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 10));
        tabla = new Tabla(6, 12, List.of(a, b));
    }

    @Test
    void tesztSikeresMozgatas() {
        Tabla ujTabla = jatekLogika.mozgat(tabla, tabla.jarmuvek().get(0), 0, 1);
        assertNotNull(ujTabla);
        assertEquals(9, ujTabla.jarmuvek().get(0).pozicio().oszlop());
    }

    @Test
    void tesztSikertelenMozgatas() {
        Tabla ujTabla = jatekLogika.mozgat(tabla, tabla.jarmuvek().get(0), 0, -1);
        assertNull(ujTabla);
    }

    @Test
    void tesztNyertE() {
        // Az A járművet a 13-as oszlopra tesszük, hogy egyértelműen kijusson
        Jarmu a = new Jarmu("A", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 13));
        Jarmu b = new Jarmu("B", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, -1));
        Tabla nyertesTabla = new Tabla(6, 12, List.of(a, b));

        assertTrue(jatekLogika.nyertE(nyertesTabla, "A"));
        assertTrue(jatekLogika.nyertE(nyertesTabla, "B"));
    }
}