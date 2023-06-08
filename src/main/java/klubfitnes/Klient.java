package klubfitnes;

public class Klient {
    private String imie;
    private String nazwisko;
    private String telefon;


    public Klient(String imie, String nazwisko, String telefon) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.telefon = telefon;
    }


    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getTelefon() {
        return telefon;
    }

    public String toString() {
        return imie + " " + nazwisko + " " + telefon;
    }
}
