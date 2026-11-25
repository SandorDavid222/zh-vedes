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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Jatek {

    private static final Logger logger = LoggerFactory.getLogger(Jatek.class);

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
        logger.info("Üdvözöllek a játékban.");

        // Játékos nevének bekérése
        System.out.print("Név:  ");
        String jatekosNev = scanner.nextLine();
        logger.info("Szia {}! Kezdődhet a játék.", jatekosNev);

        // High score megjelenítése
        logger.info("\nHigh Score");
        List<Jatekos> highScores = jatekosRepository.osszesLekerdezese();
        if (highScores.isEmpty()) {
            logger.info("Még nincs mentett játékos.");
        } else {
            for (Jatekos j : highScores) {
                logger.info("{}: {} győzelem", j.nev(), j.nyeresekSzama());
            }
        }
        logger.info("------------------\n");

        // Pálya betöltése
        Tabla tabla = tablaBetolto.betolt("palya.txt");

        boolean jatekFut = true;
        while (jatekFut) {
            // Tábla kiírása
            logger.info("\nJelenlegi tábla állapota");
            tablaUI.kirajzol(tabla);

            // Játékos lépése
            System.out.println("\nTe jössz! (Írj be egy jármű azonosítót és irányt, pl: 'A jobbra' vagy 'B fel')");
            System.out.print("Lépés: ");
            String bemenet = scanner.nextLine();

            // Kilépés lehetősége
            if (bemenet.equalsIgnoreCase("kilépés") || bemenet.equalsIgnoreCase("exit")) {
                logger.info("Játék vége.");
                break;
            }

            String[] parancsok = bemenet.split(" ");
            if (parancsok.length != 2) {
                logger.warn("Hibás bemenet! (<jármű azonosító> <irány> (pl: A jobbra))");
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
                logger.warn("Nincs ilyen jármű a táblán!");
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
                    logger.warn("Érvénytelen irány! Használat: jobbra, balra, fel, le");
                    continue;
            }

            // Mozgatás végrehajtása és a tábla frissítése
            Tabla ujTabla = jatekLogika.mozgat(tabla, kivalasztottJarmu, deltaSor, deltaOszlop);
            if (ujTabla != null) {
                tabla = ujTabla; // Tábla frissítése
                logger.info("Sikeres lépés!");
            }

            // Győzelem ellenőrzése
            if (jatekLogika.nyertE(tabla, "A")) {
                logger.info("Gratulálok {}! Megnyerted a játékot!", jatekosNev);
                jatekosRepository.mentes(jatekosNev);
                jatekFut = false;
            } else {
                // AI lépése
                logger.info("Másik játékos lép...");
                Tabla aiTabla = aiJatekos.lep(tabla);
                if (aiTabla != null) {
                    tabla = aiTabla;
                }

                // AI győzelmének ellenőrzése
                if (jatekLogika.nyertE(tabla, "B")) {
                    logger.info("Sajnos az ellenfeled nyert! Próbáld újra!");
                    jatekFut = false;
                }
            }
        }
    }
}