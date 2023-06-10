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
import java.sql.SQLException;

public class KasjerScenaKontroler {
    private Connection connection;
    @FXML
    private Label idLabel;
    @FXML
    private TableView<Klient> tabelaKlientow;
    @FXML
    private TableColumn<Klient, String> idKol;
    @FXML
    private TableColumn<Klient,String> imieKol;
    @FXML
    private TableColumn<Klient, String> nazwiskoKol;

    @FXML
    private TableColumn<Klient, String> telefonKol;

    private int id;

    ObservableList<Klient> listaKlientow = FXCollections.observableArrayList();

    public void Incjalizacja(int id) {
        this.id = id;
        idLabel.setText("ID: " + id);
    }
    public void Wyloguj(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(Logowanie.class.getResource("Logowanie.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene logowanieScena = new Scene(loader.load());

        stage.setScene(logowanieScena);
        stage.setTitle("Logowanie");
        stage.show();
    }
    public void Incjalizacja(int id, Connection connection) {
        this.id = id;
        idLabel.setText("ID: " + id);

        this.connection = connection;

        ustawTabele();
        wypiszKlientow();
    }

    private void ustawTabele() {
        try {
            idKol.setCellValueFactory(new PropertyValueFactory<>("id"));
            imieKol.setCellValueFactory(new PropertyValueFactory<>("imie"));
            nazwiskoKol.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
            telefonKol.setCellValueFactory(new PropertyValueFactory<>("telefon"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public void odswiezTabele(){
        wypiszKlientow();
    }
    public void wypiszKlientow(){
        try{
            listaKlientow.clear();

            String query = "SELECT * FROM Klienci";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                listaKlientow.add(new Klient(
                        rs.getInt("id"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("telefon")));
            }
            for (Klient klient : listaKlientow) {
                System.out.println(klient);
            }
            tabelaKlientow.setItems(listaKlientow);


            preparedStatement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void dodajKlienta(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DodajKlientaKontroler.class.getResource("dodajKlienta.fxml"));
            Scene scena = new Scene(fxmlLoader.load());

            DodajKlientaKontroler dodajKlientaKontroler = fxmlLoader.getController();
            dodajKlientaKontroler.inicjalizacja(connection);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
