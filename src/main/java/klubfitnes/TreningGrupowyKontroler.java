package klubfitnes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TreningGrupowyKontroler {
    private Connection connection;
    private int id;
    private DzienTygodnia dzienTygodnia;

    @FXML
    private MenuButton dzienTygodniaMenuButton;
    @FXML
    private TextField nazwaField;
    @FXML
    private TextField godzinaField;
    @FXML
    private TextField salaField;
    @FXML
    private TextField liczbaMiejscField;
    @FXML
    private Button zapiszButton;

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
        return "INSERT INTO cwiczenia_grupowe(id,idTrenera, nazwa, dzienTygodnia,godzina,sala,liczbaMiejsc) VALUES (? ,?, ?, ?, ? ,?, ?)";
    }

    public int getNewClientId(Connection connection) {
        String query = "SELECT MAX(id) FROM cwiczenia_grupowe";
        int maxId = 0;

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędu, np. wyświetlenie komunikatu
        }

        // Zwiększenie wartości o 1
        int newId = maxId + 1;

        return newId;
    }
    @FXML
    public void zapisz(javafx.scene.input.MouseEvent mouseEvent ) {
        try {
            query = getQuery();
            int newId = getNewClientId(connection);

            String czasTekst = godzinaField.getText();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            java.util.Date parsed;
            parsed = format.parse(czasTekst);
            java.sql.Time czasSql = new java.sql.Time(parsed.getTime());

            int dzien = dzienTygodnia.getWartosc();

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newId);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(3, nazwaField.getText());
            preparedStatement.setInt(4,dzienTygodnia.getWartosc());
            preparedStatement.setObject(5, czasSql);
            preparedStatement.setString(6,salaField.getText());
            preparedStatement.setInt(7,liczbaMiejscField.getText().isEmpty() ? 0 : Integer.parseInt(liczbaMiejscField.getText()));

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
        nazwaField.setText(null);
        godzinaField.setText(null);
        salaField.setText(null);
        liczbaMiejscField.setText(null);
    }


}
