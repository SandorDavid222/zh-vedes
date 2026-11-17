package hu.nye.progtech.zhvedes.model;

/*
 * Egy jármű a táblán. A hossz 2 vagy 3.
 */
public record Jarmu(
        String azonosito,
        int hossz,
        Irany irany,
        Pozicio pozicio
) {
    public enum Irany {
        VIZSZINTES,
        FUGGOLEGES
    }
}