package klubfitnes;

public class Trening {
    protected String tryb;
    protected String sala;
    protected String opis;
    protected java.sql.Time godzina;


    public Trening(String tryb, String sala, String opis,java.sql.Time  godzina) {
        this.tryb = tryb;
        this.sala = sala;
        this.opis = opis;
        this.godzina = godzina;
    }

    public String getTryb() {
        return tryb;
    }

    public String getSala() {
        return sala;
    }

    public String getOpis() {
        return opis;
    }

    public java.sql.Time getGodzina() {
        return godzina;
    }
}
