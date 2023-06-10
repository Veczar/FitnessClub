package klubfitnes;

public class Klient {
    private int id;
    private String imie;
    private String nazwisko;
    private String telefon;


    public Klient(int id, String imie, String nazwisko, String telefon) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.telefon = telefon;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImie() {
        return imie;
    }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() {
        return nazwisko;
    }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getTelefon() {
        return telefon;
    }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String toString() {
        return id + " " + imie + " " + nazwisko + " " + telefon;
    }
}
