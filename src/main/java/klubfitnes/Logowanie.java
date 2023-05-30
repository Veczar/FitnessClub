package klubfitnes;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Logowanie extends Application {
    private String login;
    private String haslo;

    @FXML
    private TextField loginText;
    @FXML
    private PasswordField hasloText;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Logowanie.class.getResource("logowanie.fxml"));
        Scene logowanieScena = new Scene(fxmlLoader.load());

        stage.setScene(logowanieScena);
        stage.setTitle("Logowanie");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

    public void Zaloguj(ActionEvent event) throws IOException {
        login = loginText.getText();
        haslo = hasloText.getText();

        System.out.println("Zalogowano: " + login + " " + haslo);

        if (login.equals("klient") && haslo.equals("klient")) {
            int id = 1;

            FXMLLoader fxmlLoader = new FXMLLoader(KlientScenaKontroler.class.getResource("klient.fxml"));
            Scene klientScena = new Scene(fxmlLoader.load());

            KlientScenaKontroler klientScenaController = fxmlLoader.getController();
            klientScenaController.Incjalizacja(id);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(klientScena);
            stage.setTitle("Klient");
            stage.show();
        } else if (login.equals("admin") && haslo.equals("admin")) {
            int id = 2;

            FXMLLoader fxmlLoader = new FXMLLoader(AdministratorScenaKontroler.class.getResource("admin.fxml"));
            Scene adminScena = new Scene(fxmlLoader.load());

            AdministratorScenaKontroler adminScenaController = fxmlLoader.getController();
            adminScenaController.Incjalizacja(id);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(adminScena);
            stage.setTitle("Administrator");
            stage.show();
        } else if (login.equals("trener") && haslo.equals("trener")) {
            int id = 3;

            FXMLLoader fxmlLoader = new FXMLLoader(TrenerScenaKontroler.class.getResource("trener.fxml"));
            Scene trenerScena = new Scene(fxmlLoader.load());

            TrenerScenaKontroler trenerScenaController = fxmlLoader.getController();
            trenerScenaController.Incjalizacja(id);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(trenerScena);
            stage.setTitle("Trener");
            stage.show();
        } else if (login.equals("kasjer") && haslo.equals("kasjer")) {
            int id = 4;

            FXMLLoader fxmlLoader = new FXMLLoader(KasjerScenaKontroler.class.getResource("kasjer.fxml"));
            Scene kasjerScena = new Scene(fxmlLoader.load());

            KasjerScenaKontroler kasjerScenaController = fxmlLoader.getController();
            kasjerScenaController.Incjalizacja(id);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(kasjerScena);
            stage.setTitle("Kasjer");
            stage.show();
        } else {
            System.out.println("Niepoprawne dane logowania");
        }


    }

}
