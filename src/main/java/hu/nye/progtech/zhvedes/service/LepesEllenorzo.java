package hu.nye.progtech.zhvedes.service;

import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Pozicio;
import hu.nye.progtech.zhvedes.model.Tabla;

public class LepesEllenorzo {

    /*
     * Eldönti, hogy a jármű elmozdítható-e a kért irányba (deltaSor, deltaOszlop).
     */
    public boolean elmozdithato(Tabla tabla, Jarmu jarmu, int deltaSor, int deltaOszlop) {
        // 1. Csak a saját tengelye mentén mozoghat
        if (jarmu.irany() == Jarmu.Irany.VIZSZINTES && deltaSor != 0) {
            return false;
        }
        if (jarmu.irany() == Jarmu.Irany.FUGGOLEGES && deltaOszlop != 0) {
            return false;
        }

        // Kiszámoljuk az új pozíciót
        int ujSor = jarmu.pozicio().sor() + deltaSor;
        int ujOszlop = jarmu.pozicio().oszlop() + deltaOszlop;

        //Nem léphet le a pályáról
        if (ujSor < 0 || ujSor >= tabla.sorokSzama() || ujOszlop < 0 || ujOszlop >= tabla.oszlopokSzama()) {
            return false;
        }
        return true;
    }
}