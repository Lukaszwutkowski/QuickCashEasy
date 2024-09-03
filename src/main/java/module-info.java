module com.retailtech.quickcasheasy {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.retailtech.quickcasheasy to javafx.fxml;
    exports com.retailtech.quickcasheasy;
}