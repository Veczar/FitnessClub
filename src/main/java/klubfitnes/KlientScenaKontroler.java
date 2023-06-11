package klubfitnes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class KlientScenaKontroler {

    @FXML
    private Label imieNazwiskoLabel;
    @FXML
    private Label dzienLabel;

    @FXML
    private Label karnetLabel;

    @FXML
    private TableView<Trening> tabelaTren;

    @FXML
    private TableColumn<Trening, String> trybKol;

    @FXML
    private TableColumn<Trening, String> salaKol;

    @FXML
    private TableColumn<Trening, String> opisKol;

    @FXML
    private TableColumn<Trening, java.sql.Time> godzinaKol;

    private ObservableList<Trening> listaTreningow = FXCollections.observableArrayList();


    private DzienTygodnia dzienTygodnia = DzienTygodnia.PONIEDZIALEK;
    private int idKonta;
    private Connection connection;
    private Klient klient;

    public void incjalizacja(int id, Connection connection) {
        this.idKonta = id;

        this.connection = connection;

        ustawTabele();
        daneKlienta();
        wypiszTreningi();
    }

    @FXML
    private void daneKlienta() {
        try {
            String query = "SELECT * FROM klienci WHERE id = " + idKonta;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            klient = new Klient(rs.getInt("id"),
                    rs.getString("imie"),
                    rs.getString("nazwisko"),
                    rs.getString("telefon"));

            wypiszDaneKlienta();

            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void wypiszDaneKlienta() {
        imieNazwiskoLabel.setText(klient.getImie() + " " + klient.getNazwisko());
    }

    public void wyloguj(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(Logowanie.class.getResource("Logowanie.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene logowanieScena = new Scene(loader.load());

        stage.setScene(logowanieScena);
        stage.setTitle("Logowanie");
        stage.show();
    }

    private void ustawTabele() {
        try {
            trybKol.setCellValueFactory(new PropertyValueFactory<>("tryb"));
            salaKol.setCellValueFactory(new PropertyValueFactory<>("sala"));
            opisKol.setCellValueFactory(new PropertyValueFactory<>("opis"));
            godzinaKol.setCellValueFactory(new PropertyValueFactory<>("godzina"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void wypiszTreningi(){
        listaTreningow.clear();
        listaTreningow.addAll(pobierzTreningiIndywidualne());
        listaTreningow.addAll(pobierzTreningiGrupowe());
        tabelaTren.setItems(listaTreningow);
    }
    private ArrayList<TreningGrupowy> pobierzTreningiGrupowe() {
        ArrayList<TreningGrupowy> listaTreningow = new ArrayList<>();

        try{
            String queryGrupowe = "SELECT id, nazwa, sala, godzina, idTrenera FROM cwiczenia_grupowe " +
                    "JOIN treningi_grupowe ON treningi_grupowe.idCwiczenia = cwiczenia_grupowe.id " +
                    "WHERE treningi_grupowe.idKlienta = " + idKonta +
                    " AND dzienTygodnia = " + dzienTygodnia.getWartosc();

            PreparedStatement preparedStatement = connection.prepareStatement(queryGrupowe);
            ResultSet rsGrupowe = preparedStatement.executeQuery();

            while (rsGrupowe.next()){

                String godzinaString = rsGrupowe.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);

                listaTreningow.add(new TreningGrupowy(
                        rsGrupowe.getInt("id"),
                        rsGrupowe.getInt("idTrenera"),
                        rsGrupowe.getString("sala"),
                        rsGrupowe.getString("nazwa"),
                        godzina,
                        dzienTygodnia.name()
                ));
            }

            preparedStatement.close();
            rsGrupowe.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTreningow;
    }

    private ArrayList<TreningIndywidualny> pobierzTreningiIndywidualne() {
        ArrayList<TreningIndywidualny> listaTreningow = new ArrayList<>();
        try{
            String queryIndywidulane = "SELECT CONCAT(trenerzy.imie, ' ', trenerzy.nazwisko) as nazwa, trenerzy.id, godzina FROM treningi_indywidualne " +
                    "JOIN trenerzy ON treningi_indywidualne.idTrenera = trenerzy.id " +
                    "WHERE idKlienta = " + idKonta + " AND dzienTygodnia = " + dzienTygodnia.getWartosc();
            PreparedStatement preparedStatement = connection.prepareStatement(queryIndywidulane);
            ResultSet rsIndywidualne = preparedStatement.executeQuery();

            while (rsIndywidualne.next()){



                String godzinaString = rsIndywidualne.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);

                listaTreningow.add(new TreningIndywidualny(
                        idKonta,
                        rsIndywidualne.getInt("id"),
                        godzina,
                        rsIndywidualne.getString("nazwa"),
                        dzienTygodnia.name()

                ));
            }
            preparedStatement.close();
            rsIndywidualne.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTreningow;
    }

    //region Wybieranie dnia tygodnia
    @FXML
    private void wybierzePoniedzialek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.PONIEDZIALEK;
        ustawDzienLabel();
        wypiszTreningi();
    }
    @FXML
    private void wybierzeWtorek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.WTOREK;
        ustawDzienLabel();
        wypiszTreningi();
    }
    @FXML
    private void wybierzeSroda(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.SRODA;
        ustawDzienLabel();
        wypiszTreningi();
    }
    @FXML
    private void wybierzeCzwartek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.CZWARTEK;
        ustawDzienLabel();
        wypiszTreningi();
    }
    @FXML
    private void wybierzePiatek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.PIATEK;
        ustawDzienLabel();
        wypiszTreningi();
    }
    @FXML
    private void wybierzeSobota(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.SOBOTA;
        ustawDzienLabel();
        wypiszTreningi();
    }
    @FXML
    private void wybierzeNiedziela(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.NIEDZIELA;
        ustawDzienLabel();
        wypiszTreningi();
    }

    private void ustawDzienLabel() {
        dzienLabel.setText("Dzień: " + dzienTygodnia);
    }
    //endregion

    @FXML
    private void zapiszNaTreningIndywidualny() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ZapisTreningIndywidualnyKontroler.class.getResource("zapiszNaTreningIndywidualny.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            ZapisTreningIndywidualnyKontroler zapisTreningIndywidualnyKontroler = fxmlLoader.getController();
            zapisTreningIndywidualnyKontroler.inizjalizacja(connection, idKonta);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void zapiszNaTreningGrupowy() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ZapisTreningGrupowyKontroler.class.getResource("zapiszNaTreningGrupowy.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            ZapisTreningGrupowyKontroler zapisTreningGrupowyKontroler = fxmlLoader.getController();
            zapisTreningGrupowyKontroler.inizjalizacja(connection, idKonta);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void zrezygnujZTreningu() {
        Trening trening = tabelaTren.getSelectionModel().getSelectedItem();

        if (trening == null)
            return;

        if (trening instanceof TreningGrupowy) {
            TreningGrupowy treningGrupowy = (TreningGrupowy) trening;
            try {
                String query = "DELETE FROM treningi_grupowe WHERE idCwiczenia = ? AND idKlienta = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, treningGrupowy.getIdCwiczenia());
                preparedStatement.setInt(2, idKonta);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (trening instanceof TreningIndywidualny) {
            TreningIndywidualny treningIndywidualny = (TreningIndywidualny) trening;
            try {
                String query = "UPDATE treningi_indywidualne SET idKlienta = NULL " +
                        "WHERE idKlienta = ? AND idTrenera = ? AND dzienTygodnia = ? AND godzina = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, idKonta);
                preparedStatement.setInt(2, treningIndywidualny.getIdTrenera());
                preparedStatement.setInt(3, DzienTygodnia.valueOf(treningIndywidualny.getDzienTygodnia()).getWartosc());
                preparedStatement.setTime(4, treningIndywidualny.getGodzina());

                preparedStatement.executeUpdate();

                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nieznany typ treningu");
        }


    }
}
