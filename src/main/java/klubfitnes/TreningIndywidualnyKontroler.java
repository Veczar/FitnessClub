package klubfitnes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TreningIndywidualnyKontroler {

    private Connection connection;
    private int id;
    private DzienTygodnia dzienTygodnia;
    @FXML
    private TextField godzinaField;
    @FXML
    private MenuButton dzienTygodniaMenuButton;

    String query = null;
    PreparedStatement preparedStatement;
    public void inicjalizacja(int id, Connection connection) {
        this.connection = connection;
        this.id = id;
    }

    @FXML
    private void WybierzePoniedzialek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.PONIEDZIALEK;
        System.out.println(dzienTygodnia.toString());
        System.out.println(dzienTygodnia.getWartosc());
    }
    @FXML
    private void WybierzeWtorek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.WTOREK;
        System.out.println(dzienTygodnia.toString());
        System.out.println(dzienTygodnia.getWartosc());
    }
    @FXML
    private void WybierzeSroda(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.SRODA;
        System.out.println(dzienTygodnia.toString());
        System.out.println(dzienTygodnia.getWartosc());
    }
    @FXML
    private void WybierzeCzwartek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.CZWARTEK;
        System.out.println(dzienTygodnia.toString());
        System.out.println(dzienTygodnia.getWartosc());
    }
    @FXML
    private void WybierzePiatek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.PIATEK;
        System.out.println(dzienTygodnia.toString());
        System.out.println(dzienTygodnia.getWartosc());
    }
    @FXML
    private void WybierzeSobota(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.SOBOTA;
        System.out.println(dzienTygodnia.toString());
        System.out.println(dzienTygodnia.getWartosc());
    }
    @FXML
    private void WybierzeNiedziela(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.NIEDZIELA;
        System.out.println(dzienTygodnia.toString());
        System.out.println(dzienTygodnia.getWartosc());
    }

    public String getQuery() {
        // Tworzenie zapytania SQL do dodania klienta
        return "INSERT INTO treningi_indywidualne( idTrenera,idKlienta,dzienTygodnia,godzina ) VALUES (? ,?, ?, ?)";
    }

    @FXML
    public void zapisz(javafx.scene.input.MouseEvent mouseEvent ) {
        try {
            query = getQuery();

            String czasTekst = godzinaField.getText();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            java.util.Date parsed;
            parsed = format.parse(czasTekst);
            java.sql.Time czasSql = new java.sql.Time(parsed.getTime());

            int dzien = dzienTygodnia.getWartosc();

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
            preparedStatement.setInt(3,dzienTygodnia.getWartosc());
            preparedStatement.setObject(4, czasSql);

            preparedStatement.executeUpdate();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Trening grupowy został dodany do bazy danych.");
            alert.showAndWait();

            // Czyszczenie pól tekstowych po dodaniu klienta
            clean(null);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił błąd podczas dodawania klienta do bazy danych.");
            alert.showAndWait();
        } catch (ParseException e) {
            e.printStackTrace();
            // Obsługa błędu konwersji
        }
    }

    public void clean(javafx.scene.input.MouseEvent mouseEvent){
        godzinaField.setText(null);
    }
}
