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
        // Csak a saját tengelye mentén mozoghat
        if (jarmu.irany() == Jarmu.Irany.VIZSZINTES && deltaSor != 0) {
            return false;
        }
        if (jarmu.irany() == Jarmu.Irany.FUGGOLEGES && deltaOszlop != 0) {
            return false;
        }

        // Végigmegyünk az útvonalon, amíg el nem érjük a pálya szélét vagy egy másik járművet
        int aktualisSor = jarmu.pozicio().sor();
        int aktualisOszlop = jarmu.pozicio().oszlop();

        while (true) {
            // Egy lépéssel tovább
            aktualisSor += deltaSor;
            aktualisOszlop += deltaOszlop;

            // Ha lelépne a pályáról akkor megállunk
            if (aktualisSor < 0 || aktualisSor >= tabla.sorokSzama() ||
                    aktualisOszlop < 0 || aktualisOszlop >= tabla.oszlopokSzama()) {
                break;
            }

            // Ha ütközik egy másik járművel akkor sem léphet oda
            if (vanJarmu(tabla, aktualisSor, aktualisOszlop, jarmu)) {
                break;
            }

            return true;
        }
        return false;
    }

    /*
     * Ellenőrzés hogy egy adott mezőn áll e más jármű.
     */
    private boolean vanJarmu(Tabla tabla, int sor, int oszlop, Jarmu sajatJarmu) {
        for (Jarmu j : tabla.jarmuvek()) {
            if (j.azonosito().equals(sajatJarmu.azonosito())) {
                continue;
            }
            // Ha a jármű pozíciója megegyezik a vizsgált mezővel
            if (j.pozicio().sor() == sor && j.pozicio().oszlop() == oszlop) {
                return true;
            }
        }
        return false;
    }
}