module com.kensoftph.javafxnotes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.kensoftph.javafxnotes to javafx.fxml;
    exports com.kensoftph.javafxnotes;
}