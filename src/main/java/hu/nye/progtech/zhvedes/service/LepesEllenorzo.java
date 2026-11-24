package hu.nye.progtech.zhvedes.service;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Pozicio;
import hu.nye.progtech.zhvedes.model.Tabla;

public class LepesEllenorzo {

    /*
      Eldönti, hogy a jármű elmozdítható-e a kért irányba.
      deltaSor és deltaOszlop csak -1, 0 vagy 1 lehet (egy lépés iránya).
      A metódus addig "tolja" a járművet, amíg szabad az út.
     */
    public boolean elmozdithato(Tabla tabla, Jarmu jarmu, int deltaSor, int deltaOszlop) {
        if (jarmu.irany() == Jarmu.Irany.VIZSZINTES && deltaSor != 0) return false;
        if (jarmu.irany() == Jarmu.Irany.FUGGOLEGES && deltaOszlop != 0) return false;

        int aktualisSor = jarmu.pozicio().sor();
        int aktualisOszlop = jarmu.pozicio().oszlop();

        while (true) {
            aktualisSor += deltaSor;
            aktualisOszlop += deltaOszlop;

            // Ha lelépne a pályáról, megállunk
            if (aktualisSor < 0 || aktualisSor >= tabla.sorokSzama() ||
                    aktualisOszlop < 0 || aktualisOszlop >= tabla.oszlopokSzama()) {
                break;
            }

            // Ha ütközik, megállunk
            if (vanJarmu(tabla, aktualisSor, aktualisOszlop, jarmu)) {
                break;
            }
            return true; // Ha az első szabad mezőig eljutott, akkor mozdítható
        }
        return false;
    }

    private boolean vanJarmu(Tabla tabla, int sor, int oszlop, Jarmu sajatJarmu) {
        for (Jarmu j : tabla.jarmuvek()) {
            if (j.azonosito().equals(sajatJarmu.azonosito())) continue;

            // Végigmegyünk a másik jármű összes elfoglalt mezőjén
            for (int i = 0; i < j.hossz(); i++) {
                int jSor = j.pozicio().sor() + (j.irany() == Jarmu.Irany.FUGGOLEGES ? i : 0);
                int jOszlop = j.pozicio().oszlop() + (j.irany() == Jarmu.Irany.VIZSZINTES ? i : 0);
                if (jSor == sor && jOszlop == oszlop) {
                    return true;
                }
            }
        }
        return false;
    }
}