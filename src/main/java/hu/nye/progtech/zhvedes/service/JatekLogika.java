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
    public void mozgat(Tabla tabla, Jarmu jarmu, int deltaSor, int deltaOszlop) {
        if (!lepesEllenorzo.elmozdithato(tabla, jarmu, deltaSor, deltaOszlop)) {
            System.out.println("Nem jó lépés!");
            return;
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