package hu.nye.progtech.zhvedes.ai;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Pozicio;
import hu.nye.progtech.zhvedes.model.Tabla;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AiJatekosTest {

    @Test
    void tesztAiLep() {
        AiJatekos ai = new AiJatekos();

        // Létrehozunk egy táblát, ahol a B jármű balra mozdítható
        Jarmu a = new Jarmu("A", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 0));
        Jarmu b = new Jarmu("B", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 10));
        Tabla tabla = new Tabla(6, 12, List.of(a, b));

        Tabla ujTabla = ai.lep(tabla);

        assertNotNull(ujTabla);
        // Mivel az AI a B-t próbálja balra mozgatni, a B pozíciójának változnia kell
        assertNotEquals(tabla.jarmuvek().get(1).pozicio(), ujTabla.jarmuvek().get(1).pozicio());
    }
}