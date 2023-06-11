package klubfitnes;

public enum TypKonta {
    KLIENT(1),
    TRENER(2),
    KASJER(3),
    ADMINISTRATOR(4);

    private int wartosc;

    TypKonta(int wartosc) {
        this.wartosc = wartosc;
    }

    public int getWartosc() {
        return wartosc;
    }
}
