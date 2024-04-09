module com.example.examen_sem1javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;
    requires itextpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens com.example.examen_sem1javafx to javafx.fxml;
    exports com.example.examen_sem1javafx;
}