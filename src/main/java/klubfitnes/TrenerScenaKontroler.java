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
    @FXML
    private void usunTrening(javafx.scene.input.MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UsunTreningKontroler.class.getResource("usunTrening.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            UsunTreningKontroler usunTreningKontroler = fxmlLoader.getController();
            usunTreningKontroler.inicjalizacja(id,connection);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void dodajTreningGrupowy(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TreningGrupowyKontroler.class.getResource("dodajTreningGrupowy.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            TreningGrupowyKontroler treningGrupowyKontroler = fxmlLoader.getController();
            treningGrupowyKontroler.inicjalizacja(id,connection);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void dodajTreningIndywidualny(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TreningIndywidualnyKontroler.class.getResource("dodajTreningIndywidualny.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            TreningIndywidualnyKontroler treningIndywidualnyKontroler = fxmlLoader.getController();
            treningIndywidualnyKontroler.inicjalizacja(id,connection);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    @FXML   //odswiezanie tabeli
    public void odswiez(){
        wypiszTreningi();
    }
    @FXML
    public void wyloguj(javafx.scene.input.MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(Logowanie.class.getResource("Logowanie.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene logowanieScena = new Scene(loader.load());

        stage.setScene(logowanieScena);
        stage.setTitle("Logowanie");
        stage.show();
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
                        rsGrupowe.getInt("id"),
                        id,
                        rsGrupowe.getString("sala"),
                        rsGrupowe.getString("nazwa"),
                        godzina,
                        dzienTygodnia.name()
                ));


            }
            rsGrupowe.close();
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTreningow;
    }
    private ArrayList<TreningIndywidualny> pobierzTreningiIndywidualne() {
        ArrayList<TreningIndywidualny> listaTreningow = new ArrayList<>();
        try{
            String queryIndywidulane = "SELECT CONCAT(klienci.imie, ' ', klienci.nazwisko) as nazwa, godzina, idKlienta FROM treningi_indywidualne " +
                    "LEFT JOIN klienci ON treningi_indywidualne.idKlienta = klienci.id WHERE idTrenera = " + id + " AND dzienTygodnia = " + dzienTygodnia.getWartosc() ;

            PreparedStatement preparedStatement = connection.prepareStatement(queryIndywidulane);
            ResultSet rsIndywidualne = preparedStatement.executeQuery();

            while (rsIndywidualne.next()){

                String nazwa = rsIndywidualne.getString("nazwa");
                int idKlienta = rsIndywidualne.getInt("idKlienta");

                String godzinaString = rsIndywidualne.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);

                if (nazwa == null){
                    // Jeśli idKlienta jest NULL, ustaw "wolne miejsce" jako nazwę
                    nazwa = "wolne miejsce";
                }

                listaTreningow.add(new TreningIndywidualny(
                        idKlienta,
                        id,
                        godzina,
                        nazwa,
                        dzienTygodnia.name()
                ));

            }
            rsIndywidualne.close();
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTreningow;
    }

}
