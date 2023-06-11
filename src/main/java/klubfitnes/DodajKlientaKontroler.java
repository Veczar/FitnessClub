package klubfitnes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DodajKlientaKontroler {
    @FXML
    private TextField imieField;
    @FXML
    private TextField nazwiskoField;
    @FXML
    private TextField telefonField;
    @FXML
    private TextField loginField;
    @FXML
    private TextField hasloField;
    @FXML
    private DatePicker karnetDate;


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement;


    public void inicjalizacja(Connection connection) {
        this.connection = connection;
    }

    public String getNewClientId(Connection connection) {
        String query = "SELECT MAX(idKonta) FROM konta";
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

        return String.valueOf(newId);
    }
    @FXML
    public void save(javafx.scene.input.MouseEvent mouseEvent) {

            String idKlienta = getNewClientId(connection);

            dodajKonto(connection, idKlienta);
            dodajKlienta(connection, idKlienta);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Klient został dodany do bazy danych.");
            alert.showAndWait();

            // Czyszczenie pól po dodaniu klienta
            clean(null);
    }

    private void dodajKonto(Connection connection, String idKlienta) {
        String query = "INSERT INTO konta (idKonta, login, haslo, typKonta) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idKlienta);
            statement.setString(2, loginField.getText());
            statement.setString(3, hasloField.getText());
            statement.setInt(4, TypKonta.KLIENT.getWartosc());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił błąd podczas dodawania Konta do bazy danych.");
            alert.showAndWait();
        }
    }

    private void dodajKlienta(Connection connection, String idKlienta) {
        String query = "INSERT INTO klienci (id, imie, nazwisko, telefon, dataKarnetu) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idKlienta);
            statement.setString(2, imieField.getText());
            statement.setString(3, nazwiskoField.getText());
            statement.setString(4, telefonField.getText());
            statement.setString(5, karnetDate.getValue().toString());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił błąd podczas dodawania klienta do bazy danych.");
            alert.showAndWait();
        }
    }
    @FXML
    public void clean(javafx.scene.input.MouseEvent mouseEvent) {
        imieField.setText(null);
        nazwiskoField.setText(null);
        telefonField.setText(null);
        loginField.setText(null);
        hasloField.setText(null);
        karnetDate.setValue(null);
    }

}
