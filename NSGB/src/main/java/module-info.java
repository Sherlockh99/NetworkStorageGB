module org.sherlock {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires io.netty.buffer;
    requires java.logging;

    opens org.sherlock to javafx.fxml;
    exports org.sherlock;
}