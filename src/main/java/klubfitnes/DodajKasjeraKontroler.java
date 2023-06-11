package klubfitnes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DodajKasjeraKontroler {
    private Connection connection;
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
    public void inicjalizacja(Connection connection) {
        this.connection = connection;

    }

    public String getNewId(Connection connection) {
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

        String idTrenera = getNewId(connection);

        dodajKonto(connection, idTrenera);
        dodajKasjera(connection, idTrenera);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Kasjer został dodany do bazy danych.");
        alert.showAndWait();

        // Czyszczenie pól po dodaniu
        clean(null);
    }
    private void dodajKonto(Connection connection, String idKasjera) {
        String query = "INSERT INTO konta (idKonta, login, haslo, typKonta) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idKasjera);
            statement.setString(2, loginField.getText());
            statement.setString(3, hasloField.getText());
            statement.setInt(4, TypKonta.KASJER.getWartosc());

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

    private void dodajKasjera(Connection connection, String idKasjera) {
        String query = "INSERT INTO kasjer (id, imie, nazwisko, telefon) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idKasjera);
            statement.setString(2, imieField.getText());
            statement.setString(3, nazwiskoField.getText());
            statement.setString(4, telefonField.getText());

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
    }
}
