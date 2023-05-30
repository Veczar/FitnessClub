module com.example.klubfitnes {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                        
    opens com.example.klubfitnes to javafx.fxml;
    exports com.example.klubfitnes;
}