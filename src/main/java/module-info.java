module KUTowerDefense {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens ui to javafx.fxml;
    exports ui;
}