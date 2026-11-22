package hu.nye.progtech.zhvedes.persistence;

import hu.nye.progtech.zhvedes.model.Jatekos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcJatekosRepository implements JatekosRepository {

    private static final String DB_URL = "jdbc:h2:./jatekdb";

    public JdbcJatekosRepository() {
        inicializalas();
    }

    private void inicializalas() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // Létrehozzuk a táblát, ha még nem létezik
            stmt.execute("CREATE TABLE IF NOT EXISTS jatekosok (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nev VARCHAR(255) NOT NULL UNIQUE, " +
                    "nyeresek_szama INT DEFAULT 0)");
        } catch (SQLException e) {
            System.err.println("Hiba az adatbázis inicializálásakor: " + e.getMessage());
        }
    }

    @Override
    public void mentes(String nev) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Először megnézzük létezik-e már a játékos
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT id FROM jatekosok WHERE nev = ?")) {
                checkStmt.setString(1, nev);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // Ha létezik növeljük a nyerések számát
                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE jatekosok SET nyeresek_szama = nyeresek_szama + 1 WHERE nev = ?")) {
                        updateStmt.setString(1, nev);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Ha nem létezik beszúrjuk 1 győzelemmel
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO jatekosok (nev, nyeresek_szama) VALUES (?, 1)")) {
                        insertStmt.setString(1, nev);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Hiba a mentés során: " + e.getMessage());
        }
    }

    // Kiolvassa az adatbázisból a játékosok nevét és nyeréseik számát majd egy listában visszaadja őket.
    @Override
    public List<Jatekos> osszesLekerdezese() {
        List<Jatekos> eredmeny = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nev, nyeresek_szama FROM jatekosok ORDER BY nyeresek_szama DESC")) {
            while (rs.next()) {
                eredmeny.add(new Jatekos(rs.getString("nev"), rs.getInt("nyeresek_szama")));
            }
        } catch (SQLException e) {
            System.err.println("Hiba a lekérdezés során: " + e.getMessage());
        }
        return eredmeny;
    }
}