package klubfitnes;

public class TreningIndywidualny extends Trening{

    public TreningIndywidualny(int idTrenera, java.sql.Time godzina, String opis) {
        super(idTrenera, "Indywidualny", "brak", opis, godzina);

    }

    public TreningIndywidualny(int idTrenera, String dzienTygodnia, java.sql.Time godzina, String prowadzacy) {
        super(idTrenera, dzienTygodnia, "brak", prowadzacy, godzina);
    }
}
