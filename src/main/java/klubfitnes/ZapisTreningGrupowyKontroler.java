package klubfitnes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ZapisTreningGrupowyKontroler {

    @FXML
    private TableView<Trening> tabelaTren;
    @FXML
    private TableColumn<Trening, String> dzienTygodniaKol;
    @FXML
    private TableColumn<Trening, Time> godzinaKol;
    @FXML
    private TableColumn<Trening, String> salaKol;
    @FXML
    private TableColumn<Trening, String> nazwaKol;

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
            dzienTygodniaKol.setCellValueFactory(new PropertyValueFactory<>("dzienTygodnia"));
            godzinaKol.setCellValueFactory(new PropertyValueFactory<>("godzina"));
            salaKol.setCellValueFactory(new PropertyValueFactory<>("sala"));
            nazwaKol.setCellValueFactory(new PropertyValueFactory<>("opis"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void wyswietlTreningi() {
        try {
            listaTreningow.clear();

                    String queryGrupowe = "SELECT id, idTrenera, nazwa, dzienTygodnia, godzina, sala, liczbaMiejsc, " +
                    "COUNT(treningi_grupowe.idCwiczenia) as liczbaUczestnikow FROM cwiczenia_grupowe " +
                    "LEFT JOIN treningi_grupowe ON cwiczenia_grupowe.id = treningi_grupowe.idCwiczenia " +
                    "WHERE id NOT IN (SELECT idCwiczenia FROM treningi_grupowe WHERE idKlienta = ?) " +
                    "GROUP BY cwiczenia_grupowe.id " +
                    "HAVING liczbaUczestnikow < liczbaMiejsc;";

            PreparedStatement preparedStatement = connection.prepareStatement(queryGrupowe);
            preparedStatement.setInt(1, idKlienta);

            ResultSet rsGrupowe = preparedStatement.executeQuery();

            while (rsGrupowe.next()){

                String godzinaString = rsGrupowe.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);
                // trenerId tryb sala opis godzina

                String dzienTygodnia = DzienTygodnia.values()[rsGrupowe.getInt("dzienTygodnia") - 1].name();

                listaTreningow.add(new TreningGrupowy(
                        rsGrupowe.getInt("id"), // id cwiczenia
                        rsGrupowe.getInt("idTrenera"),
                        rsGrupowe.getString("sala"),
                        rsGrupowe.getString("nazwa"),
                        godzina,
                        dzienTygodnia));
            }

            tabelaTren.getItems().addAll(listaTreningow);

            preparedStatement.close();
            rsGrupowe.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void zapisz() {
        Trening trening = tabelaTren.getSelectionModel().getSelectedItem();

        String query = "INSERT INTO treningi_grupowe(idCwiczenia, idKlienta) VALUE (? , ?);";
        if (trening == null) {
            return;
        }
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ((TreningGrupowy)trening).getIdCwiczenia());
            preparedStatement.setInt(2, idKlienta);

            preparedStatement.executeUpdate();

            preparedStatement.close();

            listaTreningow.remove(trening);
            tabelaTren.getItems().remove(trening);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
