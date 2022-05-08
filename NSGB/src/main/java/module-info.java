module org.sherlock {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.sherlock to javafx.fxml;
    exports org.sherlock;
}