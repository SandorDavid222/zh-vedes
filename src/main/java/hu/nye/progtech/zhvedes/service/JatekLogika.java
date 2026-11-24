package hu.nye.progtech.zhvedes.service;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Pozicio;
import hu.nye.progtech.zhvedes.model.Tabla;

import java.util.ArrayList;
import java.util.List;

public class JatekLogika {

    private final LepesEllenorzo lepesEllenorzo;

    public JatekLogika() {
        this.lepesEllenorzo = new LepesEllenorzo();
    }

    /*
     * Végrehajtja a jármű mozgatását a táblán.
     * Visszatér a frissített táblával, vagy null-lal, ha a lépés érvénytelen.
     */
    public Tabla mozgat(Tabla tabla, Jarmu jarmu, int deltaSor, int deltaOszlop) {
        if (!lepesEllenorzo.elmozdithato(tabla, jarmu, deltaSor, deltaOszlop)) {
            //System.out.println("Nem jó lépés!");
            return null;
        }

        int aktualisSor = jarmu.pozicio().sor();
        int aktualisOszlop = jarmu.pozicio().oszlop();

        while (true) {
            int kovetkezoSor = aktualisSor + deltaSor;
            int kovetkezoOszlop = aktualisOszlop + deltaOszlop;

            if (kovetkezoSor < 0 || kovetkezoSor >= tabla.sorokSzama() ||
                    kovetkezoOszlop < 0 || kovetkezoOszlop >= tabla.oszlopokSzama()) {
                break;
            }

            if (vanJarmu(tabla, kovetkezoSor, kovetkezoOszlop, jarmu)) {
                break;
            }

            aktualisSor = kovetkezoSor;
            aktualisOszlop = kovetkezoOszlop;
        }

        // Új jármű létrehozása az új pozícióval
        Jarmu frissitettJarmu = new Jarmu(jarmu.azonosito(), jarmu.hossz(), jarmu.irany(), new Pozicio(aktualisSor, aktualisOszlop));

        // A járművek listájának frissítése
        List<Jarmu> ujJarmuvek = new ArrayList<>();
        for (Jarmu j : tabla.jarmuvek()) {
            if (j.azonosito().equals(jarmu.azonosito())) {
                ujJarmuvek.add(frissitettJarmu);
            } else {
                ujJarmuvek.add(j);
            }
        }

        //System.out.println("Jármű (" + jarmu.azonosito() + ") mozgatva: " +
                //jarmu.pozicio().sor() + "," + jarmu.pozicio().oszlop() +
                //" -> " + aktualisSor + "," + aktualisOszlop);

        // Visszaadjuk az új táblát
        return new Tabla(tabla.sorokSzama(), tabla.oszlopokSzama(), ujJarmuvek);
    }

    /*
     * Ellenőrzi hogy egy adott jármű kijutott e a tábláról a saját kijaratan
     */
    public boolean nyertE(Tabla tabla, String jarmuAzonosito) {
        for (Jarmu j : tabla.jarmuvek()) {
            if (j.azonosito().equals(jarmuAzonosito)) {
                if ("A".equals(jarmuAzonosito)) {
                    return j.pozicio().oszlop() >= tabla.oszlopokSzama();
                }
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