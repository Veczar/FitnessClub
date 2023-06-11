package klubfitnes;

public class TreningIndywidualny extends Trening{
    private int idKlienta;

    public TreningIndywidualny(int idKlienta, int idTrenera, java.sql.Time godzina, String opis, String dzienTygodnia) {
        super(idTrenera, "Indywidualny", "brak", opis, godzina, dzienTygodnia);
        this.idKlienta = idKlienta;
    }

    public int getIdKlienta() {
        return idKlienta;
    }
}
