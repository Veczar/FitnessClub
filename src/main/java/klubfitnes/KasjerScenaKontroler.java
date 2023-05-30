package klubfitnes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KasjerScenaKontroler {
    @FXML
    private Label idLabel;

    private int id;

    public void Incjalizacja(int id) {
        this.id = id;
        idLabel.setText("ID: " + id);
    }
}
