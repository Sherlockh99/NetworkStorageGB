module org.sherlock {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires io.netty.buffer;
    requires java.logging;
    requires java.sql;
    requires lombok;

    opens org.sherlock to javafx.fxml;
    exports org.sherlock;
}