module com.retailtech.quickcasheasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.h2database;
    requires java.net.http;

    opens com.retailtech.quickcasheasy to javafx.fxml;
    opens com.retailtech.quickcasheasy.user to javafx.fxml;
    opens com.retailtech.quickcasheasy.user.dto to javafx.base;
    opens com.retailtech.quickcasheasy.cart.dto to javafx.base;
    opens com.retailtech.quickcasheasy.payment to javafx.fxml;
    opens com.retailtech.quickcasheasy.payment.dto to javafx.base;

    exports com.retailtech.quickcasheasy;
}