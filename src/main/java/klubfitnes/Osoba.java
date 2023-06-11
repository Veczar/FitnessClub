package klubfitnes;

public abstract class Osoba {
    protected String login;
    protected String haslo;

    protected int id;
    protected String imie;
    protected String nazwisko;
    protected String telefon;
    protected String typKonta;

    public Osoba(int id, String imie, String nazwisko, String telefon, String login, String haslo, String typKonta) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.telefon = telefon;
        this.login = login;
        this.haslo = haslo;
        this.typKonta = typKonta;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getLogin() {
        return login;
    }

    public String getHaslo() {
        return haslo;
    }

    public String toString() {
        return id + " " + imie + " " + nazwisko + " " + telefon;
    }
}
