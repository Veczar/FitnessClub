package klubfitnes;

public enum DzienTygodnia {
    PONIEDZIALEK(1),
    WTOREK(2),
    SRODA(3),
    CZWARTEK(4),
    PIATEK(5),
    SOBOTA(6),
    NIEDZIELA(7);

    private int wartosc;

    DzienTygodnia(int wartosc) {
        this.wartosc = wartosc;
    }

    public int getWartosc() {
        return wartosc;
    }
}
