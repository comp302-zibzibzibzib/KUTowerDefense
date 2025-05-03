module kutowerdefense {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens ui to javafx.fxml;
    opens Images to javafx.graphics;
    opens domain.controller to javafx.fxml;

    exports ui;
    exports domain.kutowerdefense;
    exports domain.controller;
    exports domain.entities;
    exports domain.map;
    exports domain.services;
    exports domain.tower;
}