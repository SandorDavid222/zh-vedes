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
     * egy véletlen irányba.
     */
    public void lep(Tabla tabla) {
        List<Jarmu> jarmuvek = tabla.jarmuvek();
        boolean sikeresLepes = false;
        int probalkozasok = 0;
        while (!sikeresLepes && probalkozasok < 100) {
            Jarmu kivalasztott = jarmuvek.get(random.nextInt(jarmuvek.size()));
            int deltaSor = random.nextInt(3) - 1;
            int deltaOszlop = random.nextInt(3) - 1;
            // Ne lépjen átlósan
            if (deltaSor != 0 && deltaOszlop != 0) {
                continue;
            }
            if (jatekLogika.mozgat(tabla, kivalasztott, deltaSor, deltaOszlop)) {
                sikeresLepes = true;
            }
            probalkozasok++;
        }
    }
}