package klubfitnes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;

public class UsunTreningKontroler {

    private Connection connection;
    private int id;
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
    @FXML
    private TableColumn<Trening, String> dzienTygodniaKol;
    ObservableList<Trening> listaTreningow= FXCollections.observableArrayList();
    public void inicjalizacja(int id, Connection connection) {
        this.connection = connection;
        this.id = id;
        ustawTabele();
        wypiszTreningi();
    }
    private void ustawTabele() {
        try {
            trybKol.setCellValueFactory(new PropertyValueFactory<>("tryb"));
            salaKol.setCellValueFactory(new PropertyValueFactory<>("sala"));
            opisKol.setCellValueFactory(new PropertyValueFactory<>("opis"));
            godzinaKol.setCellValueFactory(new PropertyValueFactory<>("godzina"));
            dzienTygodniaKol.setCellValueFactory(new PropertyValueFactory<>("dzienTygodnia"));

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
            String queryGrupowe = "SELECT * FROM cwiczenia_grupowe WHERE idTrenera = " + id + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(queryGrupowe);
            ResultSet rsGrupowe = preparedStatement.executeQuery();

            while (rsGrupowe.next()){

                String dzienTygodnia = DzienTygodnia.values()[rsGrupowe.getInt("dzienTygodnia") - 1].name();

                String godzinaString = rsGrupowe.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);

                listaTreningow.add(new TreningGrupowy(
                        rsGrupowe.getInt("id"),
                        id,
                        rsGrupowe.getString("sala"),
                        rsGrupowe.getString("nazwa"),
                        godzina,
                        dzienTygodnia

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
            String queryIndywidulane = "SELECT CONCAT(klienci.imie, ' ', klienci.nazwisko) as nazwa, godzina, idKlienta, dzienTygodnia FROM treningi_indywidualne " +
                    "LEFT JOIN klienci ON treningi_indywidualne.idKlienta = klienci.id WHERE idTrenera = " + id + ";";

            PreparedStatement preparedStatement = connection.prepareStatement(queryIndywidulane);
            ResultSet rsIndywidualne = preparedStatement.executeQuery();

            while (rsIndywidualne.next()){

                int idKlienta = rsIndywidualne.getInt("idKlienta");
                String nazwa = rsIndywidualne.getString("nazwa");

                String godzinaString = rsIndywidualne.getString("godzina");
                Time godzina = Time.valueOf(godzinaString);
                String dzienTygodnia = DzienTygodnia.values()[rsIndywidualne.getInt("dzienTygodnia") - 1].name();

                if (nazwa == null){
                    // Jeśli idKlienta jest NULL, ustaw "wolne miejsce" jako nazwę
                    nazwa = "wolne miejsce";
                }

                listaTreningow.add(new TreningIndywidualny(
                        idKlienta,
                        id,
                        godzina,
                        nazwa,
                        dzienTygodnia
                ));

            }
            rsIndywidualne.close();
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTreningow;
    }
    @FXML
    private void usunTrening(javafx.scene.input.MouseEvent mouseEvent){
        Trening trening = tabelaTren.getSelectionModel().getSelectedItem();

        if(trening != null){
            if(trening instanceof TreningGrupowy){
                TreningGrupowy treningGrupowy = (TreningGrupowy) trening;

                String query = "DELETE FROM cwiczenia_grupowe WHERE id = ? ;";

                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, treningGrupowy.getIdCwiczenia() );

                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    wypiszTreningi();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                TreningIndywidualny treningIndywidualny = (TreningIndywidualny) trening;

                String query = "DELETE FROM treningi_indywidualne WHERE idTrenera = ? AND godzina = ? AND  dzienTygodnia = ? ;";

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, treningIndywidualny.getIdTrenera());
                    preparedStatement.setTime(2, treningIndywidualny.getGodzina());
                    preparedStatement.setInt(3, DzienTygodnia.valueOf(trening.getDzienTygodnia()).getWartosc());

                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    wypiszTreningi();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
