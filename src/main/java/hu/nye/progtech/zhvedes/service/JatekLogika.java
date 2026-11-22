package hu.nye.progtech.zhvedes.service;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Tabla;

public class JatekLogika {

    private final LepesEllenorzo lepesEllenorzo;

    public JatekLogika() {
        this.lepesEllenorzo = new LepesEllenorzo();
    }

    /*
     * Végrehajtja a jármű mozgatását a táblán
     */
    public boolean mozgat(Tabla tabla, Jarmu jarmu, int deltaSor, int deltaOszlop) {
        if (!lepesEllenorzo.elmozdithato(tabla, jarmu, deltaSor, deltaOszlop)) {
            System.out.println("Nem jó lépés!");
            return false;
        }

        // Végigmegyünk a járművel az útvonalon amíg szabad
        int aktualisSor = jarmu.pozicio().sor();
        int aktualisOszlop = jarmu.pozicio().oszlop();

        while (true) {
            int kovetkezoSor = aktualisSor + deltaSor;
            int kovetkezoOszlop = aktualisOszlop + deltaOszlop;

            if (kovetkezoSor < 0 || kovetkezoSor >= tabla.sorokSzama() ||
                    kovetkezoOszlop < 0 || kovetkezoOszlop >= tabla.oszlopokSzama()) {
                break;
            }

            // Ha ütközés lenne
            if (vanJarmu(tabla, kovetkezoSor, kovetkezoOszlop, jarmu)) {
                break;
            }

            aktualisSor = kovetkezoSor;
            aktualisOszlop = kovetkezoOszlop;
        }

        // Jarmu pozicioja frissítése a táblán
        System.out.println("Jármű (" + jarmu.azonosito() + ") mozgatva: " +
                jarmu.pozicio().sor() + "," + jarmu.pozicio().oszlop() +
                " -> " + aktualisSor + "," + aktualisOszlop);

        return true;
    }

    /*
     * Ellenőrzi hogy egy adott jármű kijutott e a tábláról a saját kijaratan
     * pl hogy az A jármű a jobb oldalon a B jármű a bal oldalon
     */
    public boolean nyertE(Tabla tabla, String jarmuAzonosito) {
        for (Jarmu j : tabla.jarmuvek()) {
            if (j.azonosito().equals(jarmuAzonosito)) {
                // Ha az A járműről van szó akkor a jobb szélen kell lennie
                if ("A".equals(jarmuAzonosito)) {
                    return j.pozicio().oszlop() >= tabla.oszlopokSzama();
                }
                // Ha a B járműről van szó akkor a bal szélen kell lennie
                if ("B".equals(jarmuAzonosito)) {
                    return j.pozicio().oszlop() < 0;
                }
            }
        }
        return false;
    }

    private boolean vanJarmu(Tabla tabla, int sor, int oszlop, Jarmu sajatJarmu) {
        for (Jarmu j : tabla.jarmuvek()) {
            if (j.azonosito().equals(sajatJarmu.azonosito())) continue;
            if (j.pozicio().sor() == sor && j.pozicio().oszlop() == oszlop) {
                return true;
            }
        }
        return false;
    }
}