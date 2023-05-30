package klubfitnes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class KlientScenaKontroler {

    @FXML
    private Label idLabel;

    private int id;

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

}
