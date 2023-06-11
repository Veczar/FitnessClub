package klubfitnes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableColumn<Osoba, String> rolaKol;
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

    }
    @FXML
    public void dodajKasjera(javafx.scene.input.MouseEvent mouseEvent){

    }
    @FXML
    public void dodajAdministratora(javafx.scene.input.MouseEvent mouseEvent){

    }
    @FXML
    public void usun(javafx.scene.input.MouseEvent mouseEvent){

    }

    private void ustawTabele() {
        try {
            idKol.setCellValueFactory(new PropertyValueFactory<>("id"));
            loginKol.setCellValueFactory(new PropertyValueFactory<>("login"));
            hasloKol.setCellValueFactory(new PropertyValueFactory<>("haslo"));
            rolaKol.setCellValueFactory(new PropertyValueFactory<>("rola"));
            imieKol.setCellValueFactory(new PropertyValueFactory<>("imie"));
            nazwiskoKol.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void wypiszTreningi(){
        listaPracownik.clear();
        listaPracownik.addAll(pobierzDaneTrener());
        //listaPracownik.addAll(pobierzDaneKasjer());
        //listaPracownik.addAll(pobierzDaneAdmin());
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
                        rsTrener.getString("login"),
                        rsTrener.getString("haslo"),
                        rsTrener.getString("imie"),
                        rsTrener.getString("nazwisko"),
                        rsTrener.getString("telefon"),
                        //TypKonta.TRENER.name()

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


}
