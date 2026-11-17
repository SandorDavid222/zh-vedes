package hu.nye.progtech.zhvedes.persistence;

import hu.nye.progtech.zhvedes.model.Tabla;

public interface TablaBetolto {
    Tabla betolt(String fajlUtvonal);
    Tabla betoltAlap();
}