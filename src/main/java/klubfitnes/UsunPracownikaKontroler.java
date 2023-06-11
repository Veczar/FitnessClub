package klubfitnes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UsunPracownikaKontroler {
    private Connection connection;
    public void inicjalizacja(Connection connection) {
        this.connection = connection;

        ustawTabele();
        wypiszPracownikow();

    }

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


    public void wypiszPracownikow(){
        listaPracownik.clear();
        listaPracownik.addAll(pobierzDaneTrener());
        listaPracownik.addAll(pobierzDaneKasjer());
        listaPracownik.addAll(pobierzDaneAdmin());
        tabelaPracownikow.setItems(listaPracownik);
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

    @FXML
    public void usunPracownika(javafx.scene.input.MouseEvent mouseEvent){
         Osoba osoba = tabelaPracownikow.getSelectionModel().getSelectedItem();

        if (osoba != null) {
            try {
                int id = osoba.getId();

                // Usuń rekord z bazy danych
                String query = "DELETE FROM konta WHERE idKonta = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                preparedStatement.close();

                // Odśwież listę pracowników
                wypiszPracownikow();

              } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
              }

        }
    }
}
