package klubfitnes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KlientScenaKontroler {

    @FXML
    private Label idLabel;

    @FXML
    private TableView<Klient> tabela;

    @FXML
    private TableColumn<Klient, String> imieKol;

    @FXML
    private TableColumn<Klient, String> nazwiskoKol;

    @FXML
    private TableColumn<Klient, String> telefonKol;


    private int id;
    private Connection connection;

    public void Incjalizacja(int id, Connection connection) {
        this.id = id;
        idLabel.setText("ID: " + id);

        this.connection = connection;

        ustawTabele();
        wypiszDane();
    }

    public void Wyloguj(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(Logowanie.class.getResource("Logowanie.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene logowanieScena = new Scene(loader.load());

        stage.setScene(logowanieScena);
        stage.setTitle("Logowanie");
        stage.show();
    }

    private void ustawTabele() {
        try {
            imieKol.setCellValueFactory(new PropertyValueFactory<>("imie"));
            nazwiskoKol.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
            telefonKol.setCellValueFactory(new PropertyValueFactory<>("telefon"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    private void wypiszDane() {
        try {
            tabela.getItems().clear();

            String query = "SELECT * FROM klienci WHERE id = " + id;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            Klient klient = new Klient( rs.getInt("id"),
                                        rs.getString("imie"),
                                        rs.getString("nazwisko"),
                                        rs.getString("telefon"));

            System.out.println(klient);
            tabela.getItems().add(klient);

            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
