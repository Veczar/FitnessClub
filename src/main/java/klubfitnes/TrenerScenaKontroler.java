package klubfitnes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;

public class TrenerScenaKontroler {

    @FXML
    private Label idLabel;
    @FXML Label dzienTygodniaLabel;
    private DzienTygodnia dzienTygodnia;
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

    private Connection connection;
    private int id;
    ObservableList<Trening> listaTreningow= FXCollections.observableArrayList();

    public void Incjalizacja(int id, Connection connection) {
        this.id = id;
        idLabel.setText("ID: " + id);
        this.connection = connection;
        ustawTabele();
        WybierzePoniedzialek(null);
    }

    private void ustawDzienTygodniaLabel(DzienTygodnia dzienTygodnia){
        dzienTygodniaLabel.setText(dzienTygodnia.toString());
    }
    @FXML
    private void WybierzePoniedzialek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.PONIEDZIALEK;
        ustawDzienTygodniaLabel(dzienTygodnia);
        wypiszTreningi();
    }
    @FXML
    private void WybierzeWtorek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.WTOREK;
        ustawDzienTygodniaLabel(dzienTygodnia);
        wypiszTreningi();
    }
    @FXML
    private void WybierzeSroda(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.SRODA;
        ustawDzienTygodniaLabel(dzienTygodnia);
        wypiszTreningi();
    }
    @FXML
    private void WybierzeCzwartek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.CZWARTEK;
        ustawDzienTygodniaLabel(dzienTygodnia);
        wypiszTreningi();
    }
    @FXML
    private void WybierzePiatek(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.PIATEK;
        ustawDzienTygodniaLabel(dzienTygodnia);
        wypiszTreningi();
    }
    @FXML
    private void WybierzeSobota(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.SOBOTA;
        ustawDzienTygodniaLabel(dzienTygodnia);
        wypiszTreningi();
    }
    @FXML
    private void WybierzeNiedziela(ActionEvent event) {
        dzienTygodnia = DzienTygodnia.NIEDZIELA;
        ustawDzienTygodniaLabel(dzienTygodnia);
        wypiszTreningi();
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

    public void wypiszTreningi(){
        listaTreningow.clear();
        listaTreningow.addAll(pobierzTreningiIndywidualne());
        listaTreningow.addAll(pobierzTreningiGrupowe());
        tabelaTren.setItems(listaTreningow);
    }
    private ArrayList<TreningGrupowy> pobierzTreningiGrupowe() {
        ArrayList<TreningGrupowy> listaTreningow = new ArrayList<>();
        try{
            String queryGrupowe = "SELECT * FROM cwiczenia_grupowe WHERE idTrenera = " + id + " AND dzienTygodnia = " + dzienTygodnia.getWartosc();
            PreparedStatement preparedStatement = connection.prepareStatement(queryGrupowe);
            ResultSet rsGrupowe = preparedStatement.executeQuery();

            while (rsGrupowe.next()){

                String godzinaString = rsGrupowe.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);

                listaTreningow.add(new TreningGrupowy(
                        rsGrupowe.getString("sala"),
                        rsGrupowe.getString("nazwa"),
                        godzina
                ));

                preparedStatement.close();
                rsGrupowe.close();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTreningow;
    }
    private ArrayList<TreningIndywidualny> pobierzTreningiIndywidualne() {
        ArrayList<TreningIndywidualny> listaTreningow = new ArrayList<>();
        try{
            String queryIndywidulane = "SELECT * FROM treningi_indywidualne WHERE idTrenera = " + id + " AND dzienTygodnia = " + dzienTygodnia.getWartosc();
            PreparedStatement preparedStatement = connection.prepareStatement(queryIndywidulane);
            ResultSet rsIndywidualne = preparedStatement.executeQuery();

            while (rsIndywidualne.next()){

                String godzinaString = rsIndywidualne.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);

                String daneKlienta = daneKlienta(rsIndywidualne.getInt("idKlienta"));

                listaTreningow.add(new TreningIndywidualny(
                        godzina,
                        daneKlienta
                ));

                preparedStatement.close();
                rsIndywidualne.close();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTreningow;
    }

    private String daneKlienta(int idKlienta) throws SQLException {
        String queryKlient = "SELECT * FROM klienci WHERE id = " + idKlienta;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(queryKlient);
            ResultSet rsKlient = preparedStatement.executeQuery();

            rsKlient.next();

            String daneKlienta = rsKlient.getString("imie") + " " + rsKlient.getString("nazwisko");

            preparedStatement.close();
            rsKlient.close();
            return daneKlienta;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new SQLException("Nie znaleziono klienta o id: " + idKlienta);
    }
}
