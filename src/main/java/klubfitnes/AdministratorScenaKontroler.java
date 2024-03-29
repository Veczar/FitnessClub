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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AdministratorScenaKontroler {
    @FXML
    private Label idLabel;

    private int id;
    private Connection connection;
    @FXML
    private TableView<Osoba> tabelaPracownikow;
    @FXML
    private TableColumn<Osoba, String> idKol;

    @FXML
    private TableColumn<Osoba, String> loginKol;

    @FXML
    private TableColumn<Osoba, String> hasloKol;

    @FXML
    private TableColumn<Osoba, String> typKontaKol;
    @FXML
    private TableColumn<Osoba, String> imieKol;
    @FXML
    private TableColumn<Osoba, String> nazwiskoKol;
    ObservableList<Osoba> listaPracownik= FXCollections.observableArrayList();

    public void Incjalizacja(int id, Connection connection) {
        this.connection = connection;
        this.id = id;
        idLabel.setText("ID: " + id);
        ustawTabele();
        wypiszTreningi();
    }

    @FXML
    public void dodajTrenera(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DodajTreneraKontroler.class.getResource("dodajTrenera.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            DodajTreneraKontroler dodajTreneraKontroler = fxmlLoader.getController();
            dodajTreneraKontroler.inicjalizacja(connection);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void dodajKasjera(javafx.scene.input.MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DodajKasjeraKontroler.class.getResource("dodajKasjera.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            DodajKasjeraKontroler kasjeraKontroler = fxmlLoader.getController();
            kasjeraKontroler.inicjalizacja(connection);


            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void dodajAdministratora(javafx.scene.input.MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DodajAdminaKontroler.class.getResource("dodajAdmina.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            DodajAdminaKontroler adminaKontroler = fxmlLoader.getController();
            adminaKontroler.inicjalizacja(connection);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void usun(javafx.scene.input.MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UsunPracownikaKontroler.class.getResource("usunPracownika.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            UsunPracownikaKontroler usunPracownikaKontroler = fxmlLoader.getController();
            usunPracownikaKontroler.inicjalizacja(connection);

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
            idKol.setCellValueFactory(new PropertyValueFactory<>("id"));
            loginKol.setCellValueFactory(new PropertyValueFactory<>("login"));
            hasloKol.setCellValueFactory(new PropertyValueFactory<>("haslo"));
            typKontaKol.setCellValueFactory(new PropertyValueFactory<>("typKonta"));
            imieKol.setCellValueFactory(new PropertyValueFactory<>("imie"));
            nazwiskoKol.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void odswiez(){
        wypiszTreningi();
    }

    public void wypiszTreningi(){
        listaPracownik.clear();
        listaPracownik.addAll(pobierzDaneTrener());
        listaPracownik.addAll(pobierzDaneKasjer());
        listaPracownik.addAll(pobierzDaneAdmin());
        tabelaPracownikow.setItems(listaPracownik);
    }



    private ArrayList<Trener> pobierzDaneTrener() {
        ArrayList<Trener> listaTrenerow = new ArrayList<>();
        try {
            String query = "SELECT id,imie,nazwisko,telefon,login,haslo FROM `trenerzy`T JOIN `konta`K ON T.id = K.idKonta;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rsTrener = preparedStatement.executeQuery();

            while(rsTrener.next()){

                listaTrenerow.add(new Trener(
                        rsTrener.getInt("id"),
                        rsTrener.getString("imie"),
                        rsTrener.getString("nazwisko"),
                        rsTrener.getString("telefon"),
                        rsTrener.getString("login"),
                        rsTrener.getString("haslo"),
                        TypKonta.TRENER.name()
                ));

            }
            rsTrener.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return listaTrenerow;
    }

    private ArrayList<Kasjer> pobierzDaneKasjer() {
        ArrayList<Kasjer> listaKasjerow = new ArrayList<>();
        try {
            String query = "SELECT id,imie,nazwisko,telefon,login,haslo FROM `kasjer`T JOIN `konta`K ON T.id = K.idKonta;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rsKasjer = preparedStatement.executeQuery();

            while(rsKasjer.next()){

                listaKasjerow.add(new Kasjer(
                        rsKasjer.getInt("id"),
                        rsKasjer.getString("imie"),
                        rsKasjer.getString("nazwisko"),
                        rsKasjer.getString("telefon"),
                        rsKasjer.getString("login"),
                        rsKasjer.getString("haslo"),
                        TypKonta.KASJER.name()
                ));

            }
            rsKasjer.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return listaKasjerow;
    }

    private ArrayList<Admin> pobierzDaneAdmin() {
        ArrayList<Admin> listaAdminow = new ArrayList<>();
        try {
            String query = "SELECT id,imie,nazwisko,telefon,login,haslo FROM `administrator`T JOIN `konta`K ON T.id = K.idKonta;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rsAdmin = preparedStatement.executeQuery();

            while(rsAdmin.next()){

                listaAdminow.add(new Admin(
                        rsAdmin.getInt("id"),
                        rsAdmin.getString("imie"),
                        rsAdmin.getString("nazwisko"),
                        rsAdmin.getString("telefon"),
                        rsAdmin.getString("login"),
                        rsAdmin.getString("haslo"),
                        TypKonta.ADMINISTRATOR.name()
                ));

            }
            rsAdmin.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return listaAdminow;
    }

    public void wyloguj(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(Logowanie.class.getResource("Logowanie.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene logowanieScena = new Scene(loader.load());

        stage.setScene(logowanieScena);
        stage.setTitle("Logowanie");
        stage.show();
    }
}
