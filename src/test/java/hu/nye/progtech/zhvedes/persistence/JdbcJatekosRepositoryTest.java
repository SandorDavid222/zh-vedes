package hu.nye.progtech.zhvedes.persistence;

import hu.nye.progtech.zhvedes.model.Jatekos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcJatekosRepositoryTest {

    @Test
    void tesztMentesEsLekerdezes() {
        JdbcJatekosRepository repository = new JdbcJatekosRepository();

        // Töröljük az adatbázis tartalmát a tiszta teszt érdekében
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:./jatekdb");
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM jatekosok");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        String tesztNev = "Teszt Jatekos";

        // Mentés
        repository.mentes(tesztNev);

        // Lekérdezés
        java.util.List<hu.nye.progtech.zhvedes.model.Jatekos> jatekosok = repository.osszesLekerdezese();
        boolean megtalalt = false;
        for (hu.nye.progtech.zhvedes.model.Jatekos j : jatekosok) {
            if (j.nev().equals(tesztNev)) {
                megtalalt = true;
                assertEquals(1, j.nyeresekSzama()); // <-- Itt most már 1 lesz
            }
        }
        assertTrue(megtalalt);

        // Még egy mentés
        repository.mentes(tesztNev);

        // Újra lekérdezés
        jatekosok = repository.osszesLekerdezese();
        for (hu.nye.progtech.zhvedes.model.Jatekos j : jatekosok) {
            if (j.nev().equals(tesztNev)) {
                assertEquals(2, j.nyeresekSzama());
            }
        }
    }
}