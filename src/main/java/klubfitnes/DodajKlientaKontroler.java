package klubfitnes;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DodajKlientaKontroler {
    @FXML
    private JFXTextArea imieFid;
    @FXML
    private JFXTextArea nazwiskoFid;
    @FXML
    private JFXTextArea telefonFid;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement;


    public void inicjalizacja(Connection connection) {
        this.connection = connection;
    }
    public String getQuery() {
        // Tworzenie zapytania SQL do dodania klienta
        return "INSERT INTO klienci (id,imie, nazwisko, telefon) VALUES (? ,?, ?, ?)";
    }
    public String getNewClientId(Connection connection) {
        String query = "SELECT MAX(id) FROM klienci";
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
    public void save(javafx.scene.input.MouseEvent mouseEvent ){

        try {
            query = getQuery();
            String newId = getNewClientId(connection);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newId);
            preparedStatement.setString(2, imieFid.getText());
            preparedStatement.setString(3, nazwiskoFid.getText());
            preparedStatement.setString(4, telefonFid.getText());

            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Klient został dodany do bazy danych.");
            alert.showAndWait();

            // Czyszczenie pól tekstowych po dodaniu klienta
            clean(null);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił błąd podczas dodawania klienta do bazy danych.");
            alert.showAndWait();
        }
    }
    @FXML
    public void clean(javafx.scene.input.MouseEvent mouseEvent){
        imieFid.setText(null);
        nazwiskoFid.setText(null);
        telefonFid.setText(null);
    }

}
