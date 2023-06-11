package klubfitnes;

public class TreningGrupowy extends Trening {
    private int idCwiczenia;
    public TreningGrupowy(int idCwiczenia, int idTrenera, String sala, String nazwa, java.sql.Time godzina, String dzienTygodnia) {
        super(idTrenera, "Grupowy", sala, nazwa, godzina, dzienTygodnia);
        this.idCwiczenia = idCwiczenia;
    }

    public int getIdCwiczenia() {
        return idCwiczenia;
    }
}
