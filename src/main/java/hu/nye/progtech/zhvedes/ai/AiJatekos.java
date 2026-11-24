package hu.nye.progtech.zhvedes.ai;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Tabla;
import hu.nye.progtech.zhvedes.service.JatekLogika;

import java.util.List;
import java.util.Random;

public class AiJatekos {

    private final Random random;
    private final JatekLogika jatekLogika;

    public AiJatekos() {
        this.random = new Random();
        this.jatekLogika = new JatekLogika();
    }

    public Tabla lep(Tabla tabla) {
        List<Jarmu> jarmuvek = tabla.jarmuvek();

        // 1. Elsődlegesen a "B" járművet próbáljuk balra mozgatni
        for (Jarmu j : jarmuvek) {
            if (j.azonosito().equals("B")) {
                Tabla ujTabla = jatekLogika.mozgat(tabla, j, 0, -1);
                if (ujTabla != null) {
                    return ujTabla;
                }
            }
        }

        // 2. Ha a B nem mozdítható, próbáljunk véletlenszerűen egy járművet mozgatni
        // A listát összekeverjük, hogy ne mindig ugyanazzal kezdjük
        List<Jarmu> kevertJarmuvek = new java.util.ArrayList<>(jarmuvek);
        java.util.Collections.shuffle(kevertJarmuvek, random);

        for (Jarmu j : kevertJarmuvek) {
            // Próbáljunk meg 4 irányból egyet-egyet
            int[][] iranyok = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] irany : iranyok) {
                Tabla ujTabla = jatekLogika.mozgat(tabla, j, irany[0], irany[1]);
                if (ujTabla != null) {
                    return ujTabla;
                }
            }
        }

        // 3. Ha semmi nem mozdítható, visszaadjuk az eredeti táblát
        return tabla;
    }
}