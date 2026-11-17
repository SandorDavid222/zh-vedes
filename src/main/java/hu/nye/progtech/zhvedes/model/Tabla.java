package hu.nye.progtech.zhvedes.model;

import java.util.List;


/* A játéktábla, ami tartalmazza a méreteket és a járműveket.
* */


public record Tabla(int sorokSzama, int oszlopokSzama, List<Jarmu> jarmuvek) {
}