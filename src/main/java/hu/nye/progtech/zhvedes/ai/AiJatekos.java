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

    /*
     * Az AI kiválaszt egy véletlen járművet és megpróbálja elmozdítani
     * egy véletlen irányba. Visszatér a frissített táblával.
     */
    public Tabla lep(Tabla tabla) {
        List<Jarmu> jarmuvek = tabla.jarmuvek();

        // Elsődlegesen a "B" járművet próbáljuk balra mozgatni (hogy kijusson)
        for (Jarmu j : jarmuvek) {
            if (j.azonosito().equals("B")) {
                Tabla ujTabla = jatekLogika.mozgat(tabla, j, 0, -1);
                if (ujTabla != null) {
                    return ujTabla;
                }
            }
        }

        // Ha a B nem mozdítható, próbáljunk véletlenszerű járművekkel
        for (int i = 0; i < 100; i++) {
            Jarmu kivalasztott = jarmuvek.get(random.nextInt(jarmuvek.size()));

            int deltaSor = random.nextInt(3) - 1;
            int deltaOszlop = random.nextInt(3) - 1;

            // Ne lépjen átlósan
            if (deltaSor != 0 && deltaOszlop != 0) {
                continue;
            }

            Tabla ujTabla = jatekLogika.mozgat(tabla, kivalasztott, deltaSor, deltaOszlop);
            if (ujTabla != null) {
                return ujTabla;
            }
        }

        // Ha nem találtunk érvényes lépést, visszaadjuk az eredeti táblát
        return tabla;
    }
}