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
        Jarmu a = new Jarmu("A", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 0));
        Jarmu b = new Jarmu("B", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 10));
        Tabla tabla = new Tabla(6, 12, List.of(a, b));

        Tabla ujTabla = ai.lep(tabla);

        assertNotNull(ujTabla);
        assertNotEquals(tabla.jarmuvek().get(1).pozicio(), ujTabla.jarmuvek().get(1).pozicio());
    }

    @Test
    void tesztAiLepAmikorBNemMozdithato() {
        AiJatekos ai = new AiJatekos();
        Jarmu a = new Jarmu("A", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 0));
        Jarmu b = new Jarmu("B", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 10));
        Jarmu c = new Jarmu("C", 2, Jarmu.Irany.FUGGOLEGES, new Pozicio(0, 9));
        Tabla tabla = new Tabla(6, 12, List.of(a, b, c));

        Tabla ujTabla = ai.lep(tabla);
        assertNotNull(ujTabla);
    }
}