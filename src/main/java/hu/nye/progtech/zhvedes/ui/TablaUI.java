package hu.nye.progtech.zhvedes.ui;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Tabla;

public class TablaUI {

    public void kirajzol(Tabla tabla) {
        int sorok = tabla.sorokSzama();
        int oszlopok = tabla.oszlopokSzama();

        // Oszlopfejléc (a, b, c...)
        System.out.print("   ");
        for (int i = 0; i < oszlopok; i++) {
            System.out.print((char) ('a' + i) + " ");
        }
        System.out.println();

        // Sorok kirajzolása
        for (int sor = 0; sor < sorok; sor++) {
            // Sorszám
            System.out.printf("%2d ", (sor + 1));

            for (int oszlop = 0; oszlop < oszlopok; oszlop++) {
                Jarmu jarmuItt = jarmuKeresese(tabla, sor, oszlop);
                if (jarmuItt != null) {
                    System.out.print(jarmuItt.azonosito() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    /*
     * Megkeresi hogy az adott (sor, oszlop) koordinátán melyik jármű található.
     * Figyelembe veszi a jármű hosszát és irányát is.
     */
    private Jarmu jarmuKeresese(Tabla tabla, int sor, int oszlop) {
        for (Jarmu j : tabla.jarmuvek()) {
            int jSor = j.pozicio().sor();
            int jOszlop = j.pozicio().oszlop();

            if (j.irany() == Jarmu.Irany.VIZSZINTES) {
                // Ha vízszintes, akkor az oszlopokban foglal helyet
                if (sor == jSor && oszlop >= jOszlop && oszlop < jOszlop + j.hossz()) {
                    return j;
                }
            } else {
                // Ha függőleges, akkor a sorokban foglal helyet
                if (oszlop == jOszlop && sor >= jSor && sor < jSor + j.hossz()) {
                    return j;
                }
            }
        }
        return null;
    }
}