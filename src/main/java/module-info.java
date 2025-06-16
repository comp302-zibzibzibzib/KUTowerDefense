module KUTowerDefense {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.media;

    opens ui to javafx.fxml;
    exports ui;
}
