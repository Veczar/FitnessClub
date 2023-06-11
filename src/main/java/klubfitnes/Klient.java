package klubfitnes;

import java.sql.Date;

public class Klient extends Osoba {

    private java.sql.Date dataWygasnieciaKarnetu;
    public Klient(int id, String imie, String nazwisko, String telefon, String login, String haslo, String typKonta, java.sql.Date dataWygasnieciaKarnetu) {
        super(id, imie, nazwisko, telefon, login, haslo, typKonta);
        this.dataWygasnieciaKarnetu = dataWygasnieciaKarnetu;
    }


    public Date getDataWygasnieciaKarnetu() {
        return dataWygasnieciaKarnetu;
    }
}
