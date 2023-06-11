package klubfitnes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ZapisTreningIndywidualnyKontroler {

    @FXML
    private TableView<Trening> tabelaTren;
    @FXML
    private TableColumn<Trening, String> dzienTygodniaKol;
    @FXML
    private TableColumn<Trening, java.sql.Time> godzinaKol;
    @FXML
    private TableColumn<Trening, String> prowadzacyKol;

    private Connection connection;
    private int idKlienta;

    PreparedStatement preparedStatement;
    ObservableList<Trening> listaTreningow = FXCollections.observableArrayList();

    public void inizjalizacja(Connection connection, int idKlienta) {
        this.connection = connection;
        this.idKlienta = idKlienta;

        ustawTabele();
        wyswietlTreningi();
    }
    private void ustawTabele() {
        try {
            dzienTygodniaKol.setCellValueFactory(new PropertyValueFactory<>("tryb"));
            godzinaKol.setCellValueFactory(new PropertyValueFactory<>("godzina"));
            prowadzacyKol.setCellValueFactory(new PropertyValueFactory<>("opis"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void wyswietlTreningi() {
        try {
            listaTreningow.clear();

            String queryIndywidulane = "SELECT idTrenera, dzienTygodnia, godzina, CONCAT(trenerzy.imie, ' ', trenerzy.nazwisko) as prowadzacy " +
                    "FROM treningi_indywidualne JOIN trenerzy ON treningi_indywidualne.idTrenera = trenerzy.id " +
                    "WHERE idKlienta IS NULL; ";
            PreparedStatement preparedStatement = connection.prepareStatement(queryIndywidulane);
            ResultSet rsIndywidualne = preparedStatement.executeQuery();

            while (rsIndywidualne.next()){

                String godzinaString = rsIndywidualne.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);
                // trenerId tryb sala opis godzina

                String dzienTygodnia = DzienTygodnia.values()[rsIndywidualne.getInt("dzienTygodnia") - 1].name();

                listaTreningow.add(new TreningIndywidualny(
                        idKlienta,
                        rsIndywidualne.getInt("idTrenera"),
                        godzina,
                        "Grupowe",
                        rsIndywidualne.getString("prowadzacy")));
            }

            tabelaTren.getItems().addAll(listaTreningow);

            preparedStatement.close();
            rsIndywidualne.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void zapisz() {
        Trening trening = tabelaTren.getSelectionModel().getSelectedItem();

        String query = "UPDATE treningi_indywidualne SET idKlienta = ? WHERE idTrenera = ? AND dzienTygodnia = ? AND godzina = ?;";
        if (trening == null) {
            return;
        }
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idKlienta);
            preparedStatement.setInt(2, trening.getIdTrenera());
            preparedStatement.setInt(3, DzienTygodnia.valueOf(trening.getTryb()).getWartosc());
            preparedStatement.setTime(4, trening.getGodzina());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            listaTreningow.remove(trening);
            tabelaTren.getItems().remove(trening);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
