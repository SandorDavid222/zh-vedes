package hu.nye.progtech.zhvedes.persistence;

import hu.nye.progtech.zhvedes.model.Tabla;

public class SzovegesTablaBetolto implements TablaBetolto {

    @Override
    public Tabla betolt(String fajlUtvonal) {
        return betoltAlap();
    }

    @Override
    public Tabla betoltAlap() {
        return null;
    }
}