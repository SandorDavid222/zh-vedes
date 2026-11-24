package hu.nye.progtech.zhvedes.persistence;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Pozicio;
import hu.nye.progtech.zhvedes.model.Tabla;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SzovegesTablaBetolto implements TablaBetolto {

    // Megprobalja beolvasni a szöveges filet, addig próbálkozik amig van sor a fileban. ha valami üres akkor
    // a countinue miatt folytatodik.
    @Override
    public Tabla betolt(String fajlUtvonal) {
        List<Jarmu> jarmuvek = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fajlUtvonal))) {
            String sor;
            while ((sor = br.readLine()) != null) {
                if (sor.trim().isEmpty()) continue;
                String[] adatok = sor.split(" ");
                String azonosito = adatok[0];
                int hossz = Integer.parseInt(adatok[1]);
                Jarmu.Irany irany = Jarmu.Irany.valueOf(adatok[2]);
                int sorPoz = Integer.parseInt(adatok[3]);
                int oszlopPoz = Integer.parseInt(adatok[4]);

                jarmuvek.add(new Jarmu(azonosito, hossz, irany, new Pozicio(sorPoz, oszlopPoz)));
            }
            return new Tabla(6, 12, jarmuvek);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Hiba a fájl beolvasása során: " + e.getMessage());
            return betoltAlap();
        }
    }

    @Override
    public Tabla betoltAlap() {
        List<Jarmu> jarmuvek = new ArrayList<>();
        //pálya
        jarmuvek.add(new Jarmu("A", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 0)));
        jarmuvek.add(new Jarmu("B", 2, Jarmu.Irany.VIZSZINTES, new Pozicio(2, 10)));
        jarmuvek.add(new Jarmu("C", 2, Jarmu.Irany.FUGGOLEGES, new Pozicio(0, 5)));
        jarmuvek.add(new Jarmu("D", 3, Jarmu.Irany.VIZSZINTES, new Pozicio(5, 4)));
        jarmuvek.add(new Jarmu("E", 2, Jarmu.Irany.FUGGOLEGES, new Pozicio(1, 8)));

        return new Tabla(6, 12, jarmuvek);
    }
}