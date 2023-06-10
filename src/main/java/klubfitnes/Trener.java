package klubfitnes;

public class Trener {
    private DzienTygodnia dzien = DzienTygodnia.PONIEDZIALEK;

    public void dupa(){
        System.out.println(dzien.getWartosc());
    }
}
