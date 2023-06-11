package klubfitnes;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class Logowanie extends Application {
    private String login;
    private String haslo;

    public static Connection conn;

    @FXML
    private TextField loginText;
    @FXML
    private PasswordField hasloText;

    private void setConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/klubfitnes", "root", "");
            System.out.println("Connected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Logowanie.class.getResource("logowanie.fxml"));
        Scene logowanieScena = new Scene(fxmlLoader.load());

        stage.setScene(logowanieScena);
        stage.setTitle("Logowanie");
        stage.show();

        setConnection();
    }
    public static void main(String[] args) {
            launch();
    }

    public void Zaloguj(ActionEvent event) throws IOException {
        login = loginText.getText();
        haslo = hasloText.getText();

        boolean logowanie = false;
        int typKonta = -1;
        int idKonta = -1;

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT idKonta, typKonta FROM konta WHERE login = ? AND haslo = ?");
            ps.setString(1, login);
            ps.setString(2, haslo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idKonta = rs.getInt("idKonta");
                typKonta = rs.getInt("typKonta");
            } else {
                throw new SQLException();
            }

            logowanie = true;
        } catch (SQLException sqlException) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Bledny login lub haslo.");
            alert.showAndWait();
        }

        if (!logowanie) {
            return;
        }

        TypKonta typKontaEnum = TypKonta.values()[typKonta - 1];

        switch (typKontaEnum) {
            case KLIENT -> {
                FXMLLoader fxmlLoader = new FXMLLoader(KlientScenaKontroler.class.getResource("klient.fxml"));
                Scene klientScena = new Scene(fxmlLoader.load());

                KlientScenaKontroler klientScenaController = fxmlLoader.getController();
                klientScenaController.incjalizacja(idKonta, conn);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(klientScena);
                stage.setTitle("Klient");
                stage.show();
            }
            case TRENER -> {
                FXMLLoader fxmlLoader = new FXMLLoader(TrenerScenaKontroler.class.getResource("trener.fxml"));
                Scene trenerScena = new Scene(fxmlLoader.load());

                TrenerScenaKontroler trenerScenaController = fxmlLoader.getController();
                trenerScenaController.Incjalizacja(idKonta, conn);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(trenerScena);
                stage.setTitle("Trener");
                stage.show();
            }
            case KASJER -> {
                FXMLLoader fxmlLoader = new FXMLLoader(KasjerScenaKontroler.class.getResource("kasjer.fxml"));
                Scene kasjerScena = new Scene(fxmlLoader.load());

                KasjerScenaKontroler kasjerScenaController = fxmlLoader.getController();
                kasjerScenaController.Incjalizacja(idKonta,conn);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(kasjerScena);
                stage.setTitle("Kasjer");
                stage.show();
            }
            case ADMINISTRATOR -> {
                FXMLLoader fxmlLoader = new FXMLLoader(AdministratorScenaKontroler.class.getResource("administrator.fxml"));
                Scene adminScena = new Scene(fxmlLoader.load());

                AdministratorScenaKontroler adminScenaController = fxmlLoader.getController();
                adminScenaController.Incjalizacja(idKonta, conn);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(adminScena);
                stage.setTitle("Administrator");
                stage.show();
            }
        }
    }

}
