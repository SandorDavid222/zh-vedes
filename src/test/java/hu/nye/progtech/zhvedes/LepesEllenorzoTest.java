package hu.nye.progtech.zhvedes.service;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Pozicio;
import hu.nye.progtech.zhvedes.model.Tabla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LepesEllenorzoTest {

    private LepesEllenorzo lepesEllenorzo;
    private Tabla tabla;
    private Jarmu vizszintesJarmu;
    private Jarmu fuggolegesJarmu;

    @BeforeEach
    void setUp() {
        lepesEllenorzo = new LepesEllenorzo();

        vizszintesJarmu = new Jarmu("A", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 0));
        fuggolegesJarmu = new Jarmu("B", 2, Jarmu.Irany.FUGGOLEGES, new Pozicio(0, 5));

        tabla = new Tabla(6, 12, List.of(vizszintesJarmu, fuggolegesJarmu));
    }

    @Test
    void tesztVizszintesJarmuMozdithatoJobbra() {
        assertTrue(lepesEllenorzo.elmozdithato(tabla, vizszintesJarmu, 0, 1));
    }

    @Test
    void tesztVizszintesJarmuNemMozdithatoFuggolegesen() {
        assertFalse(lepesEllenorzo.elmozdithato(tabla, vizszintesJarmu, 1, 0));
    }

    @Test
    void tesztFuggolegesJarmuMozdithatoLefele() {
        assertTrue(lepesEllenorzo.elmozdithato(tabla, fuggolegesJarmu, 1, 0));
    }

    @Test
    void tesztFuggolegesJarmuNemMozdithatoVizszintesen() {
        assertFalse(lepesEllenorzo.elmozdithato(tabla, fuggolegesJarmu, 0, 1));
    }

    @Test
    void tesztJarmuNemMozdithatoLeAPalyarol() {
        // A vízszintes jármű a bal szélen van, balra nem mozdítható
        assertFalse(lepesEllenorzo.elmozdithato(tabla, vizszintesJarmu, 0, -1));
    }

    @Test
    void tesztUtkozes() {
        // Az A jármű a 2,0-n van (2 hosszú, tehát 2,0 és 2,1).
        // Tegyünk egy akadályt a 2,2-re.
        Jarmu akadaly = new Jarmu("C", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 1));
        Tabla utkozosTabla = new Tabla(6, 12, List.of(vizszintesJarmu, akadaly));

        // Az A jobbra próbál menni, de a 2,2-n ott az akadály, így nem mozdítható!
        assertFalse(lepesEllenorzo.elmozdithato(utkozosTabla, vizszintesJarmu, 0, 1));
    }
}