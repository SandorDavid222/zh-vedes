package hu.nye.progtech.zhvedes.persistence;

import hu.nye.progtech.zhvedes.model.Jatekos;
import java.util.List;

public interface JatekosRepository {
    void mentes(String nev);
    List<Jatekos> osszesLekerdezese();
}