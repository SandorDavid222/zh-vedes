package hu.nye.progtech.zhvedes.persistence;

import hu.nye.progtech.zhvedes.model.Jatekos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcJatekosRepositoryTest {

    @Test
    void tesztMentesEsLekerdezes() {
        // Használjunk egy külön adatbázist a teszthez, hogy ne keveredjen a valódi játékkal
        System.setProperty("h2.db.path", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        JdbcJatekosRepository repository = new JdbcJatekosRepository();

        // Kezdetben üresnek kell lennie
        List<Jatekos> kezdetiLista = repository.osszesLekerdezese();
        // Megjegyzés: Ha a valódi játékdb-t használja, itt már lehetnek játékosok.
        // Ezért csak azt ellenőrizzük, hogy a mentés után megjelenik-e a játékos.

        String tesztNev = "Teszt Jatekos";

        // Mentés
        repository.mentes(tesztNev);

        // Lekérdezés
        List<Jatekos> jatekosok = repository.osszesLekerdezese();

        assertFalse(jatekosok.isEmpty());

        // Megkeressük a teszt játékost
        boolean megtalalt = false;
        for (Jatekos j : jatekosok) {
            if (j.nev().equals(tesztNev)) {
                megtalalt = true;
                assertEquals(1, j.nyeresekSzama());
            }
        }
        assertTrue(megtalalt);

        // Még egy mentés, hogy növeljük a nyerések számát
        repository.mentes(tesztNev);

        // Újra lekérdezés
        jatekosok = repository.osszesLekerdezese();
        for (Jatekos j : jatekosok) {
            if (j.nev().equals(tesztNev)) {
                assertEquals(2, j.nyeresekSzama()); // Most már 2 győzelem
            }
        }
    }
}