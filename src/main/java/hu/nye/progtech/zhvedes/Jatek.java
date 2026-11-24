package hu.nye.progtech.zhvedes;

import hu.nye.progtech.zhvedes.ai.AiJatekos;
import hu.nye.progtech.zhvedes.model.Jarmu;
import hu.nye.progtech.zhvedes.model.Jatekos;
import hu.nye.progtech.zhvedes.model.Tabla;
import hu.nye.progtech.zhvedes.persistence.JatekosRepository;
import hu.nye.progtech.zhvedes.persistence.JdbcJatekosRepository;
import hu.nye.progtech.zhvedes.persistence.SzovegesTablaBetolto;
import hu.nye.progtech.zhvedes.persistence.TablaBetolto;
import hu.nye.progtech.zhvedes.service.JatekLogika;
import hu.nye.progtech.zhvedes.ui.TablaUI;

import java.util.List;
import java.util.Scanner;

public class Jatek {

    private final Scanner scanner;
    private final JatekLogika jatekLogika;
    private final AiJatekos aiJatekos;
    private final TablaBetolto tablaBetolto;
    private final JatekosRepository jatekosRepository;
    private final TablaUI tablaUI;

    public Jatek() {
        this.scanner = new Scanner(System.in);
        this.jatekLogika = new JatekLogika();
        this.aiJatekos = new AiJatekos();
        this.tablaBetolto = new SzovegesTablaBetolto();
        this.jatekosRepository = new JdbcJatekosRepository();
        this.tablaUI = new TablaUI();
    }

    public void start() {
        System.out.println("Üdvözöllek a játékban.");

        // Játékos nevének bekérése
        System.out.print("Név:  ");
        String jatekosNev = scanner.nextLine();
        System.out.println("Szia " + jatekosNev + "! Kezdődhet a játék.");

        // High score megjelenítése
        System.out.println("\nHigh Score");
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
        Tabla tabla = tablaBetolto.betolt("palya.txt");

        boolean jatekFut = true;
        while (jatekFut) {
            // Tábla kiírása
            System.out.println("\nJelenlegi tábla állapota");
            tablaUI.kirajzol(tabla);

            // Játékos lépése
            System.out.println("\nTe jössz! (Írj be egy jármű azonosítót és irányt, pl: 'A jobbra' vagy 'B fel')");
            System.out.print("Lépés: ");
            String bemenet = scanner.nextLine();

            // Kilépés lehetősége
            if (bemenet.equalsIgnoreCase("kilépés") || bemenet.equalsIgnoreCase("exit")) {
                System.out.println("Játék vége.");
                break;
            }

            //
            String[] parancsok = bemenet.split(" ");
            if (parancsok.length != 2) {
                System.out.println("Hibás bemenet! (<jármű azonosító> <irány> (pl: A jobbra))");
                continue;
            }

            String jarmuAzonosito = parancsok[0];
            String irany = parancsok[1].toLowerCase();

            // Megkeressük a járművet a táblán
            Jarmu kivalasztottJarmu = null;
            for (Jarmu j : tabla.jarmuvek()) {
                if (j.azonosito().equalsIgnoreCase(jarmuAzonosito)) {
                    kivalasztottJarmu = j;
                    break;
                }
            }

            if (kivalasztottJarmu == null) {
                System.out.println("Nincs ilyen jármű a táblán!");
                continue;
            }

            // Irány lefordítása deltákra
            int deltaSor = 0;
            int deltaOszlop = 0;
            switch (irany) {
                case "jobbra":
                    deltaOszlop = 1;
                    break;
                case "balra":
                    deltaOszlop = -1;
                    break;
                case "fel":
                    deltaSor = -1;
                    break;
                case "le":
                    deltaSor = 1;
                    break;
                default:
                    System.out.println("Érvénytelen irány! Használat: jobbra, balra, fel, le");
                    continue;
            }

            // Mozgatás végrehajtása és a tábla frissítése
            Tabla ujTabla = jatekLogika.mozgat(tabla, kivalasztottJarmu, deltaSor, deltaOszlop);
            if (ujTabla != null) {
                tabla = ujTabla; // Tábla frissítése
                System.out.println("Sikeres lépés!");
            }

            // Győzelem ellenőrzése
            if (jatekLogika.nyertE(tabla, "A")) {
                System.out.println("Gratulálok " + jatekosNev + "! Megnyerted a játékot!");
                jatekosRepository.mentes(jatekosNev);
                jatekFut = false;
            } else {
                // AI lépése
                System.out.println("Másik játékos lép...");
                Tabla aiTabla = aiJatekos.lep(tabla);
                if (aiTabla != null) {
                    tabla = aiTabla;
                }

                // AI győzelmének ellenőrzése
                if (jatekLogika.nyertE(tabla, "B")) {
                    System.out.println("Sajnos az ellenfeled nyert! Próbáld újra!");
                    jatekFut = false;
                }
            }
        }
    }
}