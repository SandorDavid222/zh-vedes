package hu.nye.progtech.zhvedes;

import hu.nye.progtech.zhvedes.ai.AiJatekos;
import hu.nye.progtech.zhvedes.model.Jatekos;
import hu.nye.progtech.zhvedes.model.Tabla;
import hu.nye.progtech.zhvedes.persistence.JatekosRepository;
import hu.nye.progtech.zhvedes.persistence.JdbcJatekosRepository;
import hu.nye.progtech.zhvedes.persistence.SzovegesTablaBetolto;
import hu.nye.progtech.zhvedes.persistence.TablaBetolto;
import hu.nye.progtech.zhvedes.service.JatekLogika;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jatek {

    private final Scanner scanner;
    private final JatekLogika jatekLogika;
    private final AiJatekos aiJatekos;
    private final TablaBetolto tablaBetolto;
    private final JatekosRepository jatekosRepository;

    public Jatek() {
        this.scanner = new Scanner(System.in);
        this.jatekLogika = new JatekLogika();
        this.aiJatekos = new AiJatekos();
        this.tablaBetolto = new SzovegesTablaBetolto();
        this.jatekosRepository = new JdbcJatekosRepository();
    }

    public void start() {
        System.out.println("Üdvözöllek a játékban.");

        // Játékos nevének bekérése
        System.out.print("Név:  ");
        String jatekosNev = scanner.nextLine();
        System.out.println("Szia " + jatekosNev + "! Kezdődhet a játék.");

        // High score megjelenítése
        System.out.println("\n--- High Score ---");
        List<Jatekos> highScores = jatekosRepository.osszesLekerdezese();
        if (highScores.isEmpty()) {
            System.out.println("Még nincs mentett játékos.");
        } else {
            for (Jatekos j : highScores) {
                System.out.println(j.nev() + ": " + j.nyeresekSzama() + " győzelem");
            }
        }
        System.out.println("------------------\n");

        // Pálya betöltése
        Tabla tabla = tablaBetolto.betoltAlap();

        // Ha a betöltés null t adna vissza
        if (tabla == null) {
            System.out.println("FIGYELEM: A tábla betöltése még nincs kész.");
            tabla = new Tabla(6, 12, new ArrayList<>());
        }

        boolean jatekFut = true;
        while (jatekFut) {
            // Tábla kiírása
            System.out.println("--- Jelenlegi tábla állapota ---");
            System.out.println("Méret: " + tabla.sorokSzama() + "x" + tabla.oszlopokSzama());
            System.out.println("Járművek száma: " + tabla.jarmuvek().size());

            // Játékos lépése
            System.out.println("\nTe jössz! (Írj be egy jármű azonosítót és irányt, pl: 'A jobbra' vagy 'B fel')");
            System.out.print("Lépés: ");
            String bemenet = scanner.nextLine();

            // Kilépés lehetősége
            if (bemenet.equalsIgnoreCase("kilépés") || bemenet.equalsIgnoreCase("exit")) {
                System.out.println("Játék vége. Viszlát!");
                break;
            }

            // Győzelem ellenőrzése
            if (jatekLogika.nyertE(tabla, "A")) {
                System.out.println("Gratulálok " + jatekosNev + "! Megnyerted a játékot!");
                jatekosRepository.mentes(jatekosNev); // <-- ELMENTJÜK A GYŐZELMET AZ ADATBÁZISBA
                jatekFut = false;
            } else {
                // AI lépése
                System.out.println("Másik játékos lép...");
                aiJatekos.lep(tabla);

                // AI győzelmének ellenőrzése
                if (jatekLogika.nyertE(tabla, "B")) {
                    System.out.println("Sajnos az ellenfeled nyert! Próbáld újra!");
                    jatekFut = false;
                }
            }
        }
    }
}