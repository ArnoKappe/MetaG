module eu.kwrhannover.jufo.metag {
    requires javafx.controls;
    requires javafx.fxml;
    requires colt;

    opens eu.kwrhannover.jufo.metag to javafx.fxml;
    exports eu.kwrhannover.jufo.metag;
}