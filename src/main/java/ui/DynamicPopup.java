package ui;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

enum DynamicPopupAlignment {
    TOPLEFT, TOPRIGHT, TOPCENTER, CENTER;
}

public class DynamicPopup extends AnimationTimer {
    private static final double POPUP_ANIMATION_SPEED = 1500.0;

    private Pane popupPane;
    private Pane parent;
    private Pane background;
    private double speed;
    private double lastUpdate = 0.0;
    private double initialY;
    private DynamicPopupAlignment alignment;

    public DynamicPopup(Pane popupPane, Pane parent, DynamicPopupAlignment alignment, double speed) {
        this.popupPane = popupPane;
        this.parent = parent;
        this.speed = speed;
        background = new Pane();
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        parent.getChildren().add(background);
        initialY = popupPane.getPrefHeight();
        this.alignment = alignment;
        popupPane.setLayoutY(-initialY);
        Platform.runLater(this::setX);
        background.getChildren().add(popupPane);
        start();
    }

    @Override
    public void start() {
        super.start();
        popupPane.setVisible(true);
    }

    @Override
    public void handle(long now) {
        if (lastUpdate == 0.0) {
            lastUpdate = now;
            return;
        }

        setX();
        double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
        lastUpdate = now;

        double destinationY = getDestinationY();
        double currentY = popupPane.getLayoutY();
        double distance = destinationY - currentY;
        double halfLength = (destinationY + initialY) * 0.5;
        double easeInFactor = (distance < halfLength) ? distance / halfLength : 1.0;
        double displacement = easeInFactor * deltaTime * POPUP_ANIMATION_SPEED * speed;

        double newY = currentY + displacement;
        popupPane.setLayoutY(newY);

        if (destinationY - newY < 0.5) {
            stop();
        }
    }

    @Override
    public void stop() {
        setY();
        super.stop();
    }

    public void cleanupPopup() {
        background.getChildren().remove(popupPane);
        parent.getChildren().remove(background);
    }

    private void setX() {
        switch (alignment) {
            case CENTER, TOPCENTER -> popupPane.layoutXProperty().bind(parent.widthProperty().divide(2).subtract(popupPane.widthProperty().divide(2)));
            case TOPLEFT -> popupPane.layoutXProperty().bind(parent.widthProperty().divide(4).subtract(popupPane.widthProperty().divide(2)));
            case TOPRIGHT -> popupPane.layoutXProperty().bind(parent.widthProperty().multiply(0.75).subtract(popupPane.widthProperty().divide(2)));
        }
    }

    private void setY() {
        switch (alignment) {
            case CENTER -> popupPane.layoutYProperty().bind(background.heightProperty().divide(2).subtract(popupPane.heightProperty().divide(2)));
            case TOPLEFT, TOPRIGHT, TOPCENTER -> popupPane.layoutYProperty().bind(background.heightProperty().divide(4).subtract(popupPane.heightProperty().divide(2)));
        }
    }

    private double getDestinationY() {
        double dest = switch (alignment) {
            case CENTER -> background.heightProperty().divide(2).subtract(popupPane.heightProperty().divide(2)).doubleValue();
            case TOPLEFT, TOPRIGHT, TOPCENTER -> background.heightProperty().divide(4).subtract(popupPane.heightProperty().divide(2)).doubleValue();
        };
        return dest;
    }
}
