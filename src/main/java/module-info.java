module com.example.klubfitnes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens klubfitnes to javafx.fxml;
    exports klubfitnes;
}