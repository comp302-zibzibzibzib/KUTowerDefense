package ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import domain.controller.GameOptionsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OptionScene {
	private static final String SPRITE_PATH = "/Images/HUD/";

	private final List<String> optionCategories = Arrays.asList("Spawning", "Player", "Enemies", "Projectile", "Towers");
	private final List<Integer> titleIndicies = Arrays.asList(0, 10, 12, 19, 23);

	private KuTowerDefenseApp app;
	private VBox vbox;
	private Scene scene;

	private Image buttonBlue = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb.png"));
	private Image buttonPressed = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb_pressed.png"));
	private Image buttonDisabled = new Image(getClass().getResourceAsStream(SPRITE_PATH + "disableb.png"));
	private Image buttonDisabled3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "disableb3.png"));
	private Image buttonHover = new Image(getClass().getResourceAsStream(SPRITE_PATH + "hoverb.png"));
	private Image buttonHover3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "hoverb3.png"));
	private Image buttonBlue3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb3.png"));
	private Image blueRibbon = new Image(getClass().getResourceAsStream(SPRITE_PATH + "ribbon_blue.png"));

	private HashMap<String, Option> optionMap = new HashMap<>();

	private abstract static class Option<T extends Number> extends HBox {
		Label nameLabel;
		Label valueLabel;

		public Consumer<T> setter;
		public Supplier<T> getter;

		public Label getValueLabel() {
			return valueLabel;
		}
	}

	private class OptionSlider<T extends Number> extends Option<T> {
		Slider slider;
		boolean isResetting = false;

		public OptionSlider(String name, Supplier<T> getter, Consumer<T> setter, T min, T max, T step) {
			setSpacing(10);
			setAlignment(Pos.CENTER);
			setPadding(new Insets(5));

			this.setter = setter;
			this.getter = getter;

			nameLabel = new Label(name);

			if (step instanceof Integer) {
				valueLabel = new Label(String.valueOf(getter.get()));
			} else if (step instanceof Double) {
				valueLabel = new Label(String.format("%.2f", getter.get().doubleValue()));
			}

			StackPane nameStack = createLabelStackPane(buttonBlue3, nameLabel, 15, 200);
			StackPane valueStack = createLabelStackPane(buttonBlue3, valueLabel, 25, 100);

			slider = new Slider(min.doubleValue(), max.doubleValue(), getter.get().doubleValue());
			slider.setMajorTickUnit(step.doubleValue());
			slider.setMinorTickCount(0);
			slider.setShowTickMarks(true);

			slider.valueProperty().addListener((obs, oldVal, newVal) -> {
				double roundedValue = Math.round(newVal.doubleValue() / step.doubleValue()) * step.doubleValue();
				if (name.equals("Music Volume")) {
					MusicPlayer.setVolume(roundedValue);
				}

				if (isResetting) return; // JavaFX hates me and I hate her back
				if (step instanceof Integer) {
					setter.accept((T) Integer.valueOf((int) roundedValue));
					valueLabel.setText(String.valueOf((int) roundedValue));
				} else if (step instanceof Double) {
					setter.accept((T) Double.valueOf(roundedValue));
					valueLabel.setText(String.format("%.2f", getter.get().doubleValue()));
				}

				if (name.equals("Knight Percentage") || name.equals("Goblin Percentage")) {
					OptionSlider<Double> optionSlider = (name.equals("Knight Percentage")) ?
							(OptionSlider<Double>) optionMap.get("Goblin Percentage") :
							(OptionSlider<Double>) optionMap.get("Knight Percentage");

					double value = optionSlider.getter.get();
					optionSlider.valueLabel.setText(String.format("%.2f", value));

					optionSlider.isResetting = true;
					optionSlider.slider.setValue(value);
					optionSlider.isResetting = false;
				}
			});

			getChildren().addAll(nameStack, valueStack, slider);
		}
	}

	private class OptionDiscrete<T extends Number> extends Option<T> {
		Button increment;
		Button decrement;

		public OptionDiscrete(String text, Supplier<T> getter, Consumer<T> setter, T min, T max, T incrementAmount) {
			this.getter = getter;
			this.setter = setter;
			createOption(text, getter, setter, min, max, incrementAmount);
		}

		private void createOption(String text, Supplier<T> getter, Consumer<T> setter, T min, T max, T incrementAmount) {
			nameLabel = new Label(text);
			valueLabel = new Label(String.valueOf(getter.get()));
			StackPane labelPane = createLabelStackPane(buttonBlue3, nameLabel, 15, 200);
			StackPane valuePane = createLabelStackPane(buttonBlue3, valueLabel, 25, 150);

			increment = new Button();
			increment.setBackground(null);
			StackPane incrementPane = createButtonStackPane(buttonBlue, buttonHover, buttonPressed, new Label(">"), 30, 50);
			increment.setGraphic(incrementPane);
			configureButton(increment);

			decrement = new Button();
			decrement.setBackground(null);
			StackPane decrementPane = createButtonStackPane(buttonBlue, buttonHover, buttonPressed, new Label("<"), 30, 50);
			decrement.setGraphic(decrementPane);
			configureButton(decrement);

			increment.setOnAction(e -> {
				T current = getter.get();
				if (current.doubleValue() + incrementAmount.doubleValue() <= max.doubleValue()) {
					if (current instanceof Integer) {
						setter.accept((T) Integer.valueOf(current.intValue() + incrementAmount.intValue()));
						valueLabel.setText(String.valueOf(getter.get()));
					} else if (current instanceof Double) {
						setter.accept((T) Double.valueOf(current.doubleValue() + incrementAmount.doubleValue()));
						valueLabel.setText(String.valueOf(getter.get()));
					}
				}
			});

			decrement.setOnAction(e -> {
				T current = getter.get();
				if (current.doubleValue() - incrementAmount.doubleValue() >= min.doubleValue()) {
					if (current instanceof Integer) {
						setter.accept((T) Integer.valueOf(current.intValue() - incrementAmount.intValue()));
						valueLabel.setText(String.valueOf(getter.get()));
					} else if (current instanceof Double) {
						setter.accept((T) Double.valueOf(current.doubleValue() - incrementAmount.doubleValue()));
						valueLabel.setText(String.valueOf(getter.get()));
					}
				}
			});

			this.getChildren().addAll(labelPane, decrement, valuePane, increment);

			this.setAlignment(Pos.CENTER);
		}
	}

	public OptionScene(KuTowerDefenseApp app, StackPane root) {
		this.app = app;
		initializeScene(root);
	}

	public Scene getScene() {
		return scene;
	}

	private void configureButton(Button button) {
		StackPane graphic = (StackPane) button.getGraphic();
		button.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
		button.setBackground(Background.EMPTY);
		button.setPickOnBounds(false);
		button.prefWidthProperty().bind(graphic.widthProperty());
		button.prefHeightProperty().bind(graphic.heightProperty());
		button.minWidthProperty().bind(graphic.widthProperty());
		button.minHeightProperty().bind(graphic.heightProperty());
		button.maxWidthProperty().bind(graphic.widthProperty());
		button.maxHeightProperty().bind(graphic.heightProperty());
	}

	private StackPane createButtonStackPane(Image image, Image hoverImage, Image clickedImage, Label label, int fontSize, int width) {
		ImageView view = new ImageView(image);
		view.setFitHeight(50);
		view.setFitWidth(width);

		Font font = Font.font("Calibri", FontWeight.BOLD, fontSize);
		label.setFont(font);
		label.setStyle("-fx-text-fill: black;");

		StackPane pane = new StackPane();
		pane.getChildren().addAll(view, label);

		label.setTranslateY(-5);

		pane.setOnMouseEntered(e -> { view.setImage(hoverImage); });
		pane.setOnMouseExited(e -> { view.setImage(image); });

		pane.setOnMousePressed(e -> { view.setImage(clickedImage); });
		pane.setOnMouseReleased(e -> {
			if (pane.isHover()) {
				view.setImage(hoverImage);
			} else {
				view.setImage(image);
			}
		});

		pane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

		return pane;
	}

	private StackPane createLabelStackPane(Image image, Label label, int fontSize, int width) {
		ImageView view = new ImageView(image);
		view.setFitHeight(50);
		view.setFitWidth(width);

		Font font = Font.font("Calibri", FontWeight.BOLD, fontSize);
		label.setFont(font);
		label.setStyle("-fx-text-fill: black;");

		StackPane pane = new StackPane();
		pane.getChildren().addAll(view, label);

		label.setTranslateY(-5);

		return pane;
	}

	private void addDiscreteOptions(Pane pane) {
		// temporarily unavailable
	}

	private void addSliderOptions(Pane pane) {
		pane.getChildren().clear();
		for (String name : GameOptionsController.getOptionNames()) {
			int index = GameOptionsController.getOptionNames().indexOf(name);
			if (titleIndicies.contains(index)) {
				int titleIndex = titleIndicies.indexOf(index);
				StackPane titlePane = createLabelStackPane(blueRibbon, new Label(optionCategories.get(titleIndex)), 20, 150);
				pane.getChildren().add(titlePane);
			}

			final Number[] constraints = GameOptionsController.getOptionConstraints(name);

			OptionSlider option = new OptionSlider(name, GameOptionsController.getSupplier(name),
							GameOptionsController.getConsumer(name), constraints[0], constraints[1], constraints[2]);
			if (option != null) {
				pane.getChildren().add(option);
				optionMap.put(name, option);
			}
		}

		StackPane musicTitle = createLabelStackPane(blueRibbon, new Label("Music"), 20, 150);
		pane.getChildren().add(musicTitle);

		OptionSlider musicOption = new OptionSlider("Music Volume", GameOptionsController.getSupplier("Music Volume"),
				GameOptionsController.getConsumer("Music Volume"), 0, 1, 0.01);

		pane.getChildren().add(musicOption);
	}

	private void initializeScene(StackPane root) {
		Image backgroundImage = new Image(getClass().getResourceAsStream("/Images/options_background.png"));
		ImageView backgroundView = new ImageView(backgroundImage);

		backgroundView.setPreserveRatio(false);
		backgroundView.fitWidthProperty().bind(root.widthProperty());
		backgroundView.fitHeightProperty().bind(root.heightProperty());


		vbox = new VBox(5);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));
		vbox.setFillWidth(true);

		GameOptionsController.initializeGameOptions();
		addSliderOptions(vbox);

		Button saveButton = new Button();
		saveButton.setBackground(null);
		saveButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3,
								new Label("Save"), 20, 100));
		saveButton.setOnAction(e -> {
			GameOptionsController.saveOptions();
		});
		configureButton(saveButton);

		Button resetButton = new Button();
		resetButton.setBackground(null);
		resetButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3,
								new Label("Reset"), 20, 100));
		resetButton.setOnAction(e -> {
			GameOptionsController.resetOptions();
			List<Node> vboxNodes = vbox.getChildren();
			for(Node node : vboxNodes) {
				if (node instanceof Option) {
					Option option = (Option) node;
					Label value = option.getValueLabel();
					value.setText(String.valueOf(option.getter.get()));
					if (option instanceof OptionSlider optSlider) {
                        if (optSlider.getter.get() instanceof Integer) {
							value.setText(String.valueOf(optSlider.getter.get()));
						} else if (optSlider.getter.get() instanceof Double) {
							value.setText(String.format("%.2f", ((Double) optSlider.getter.get()).doubleValue()));
						}
						optSlider.isResetting = true; // Sneaky workaround because JavaFX has broken code?
						optSlider.slider.setValue(((Number)optSlider.getter.get()).doubleValue());
						optSlider.isResetting = false;
					}
				}
			}
		});
		configureButton(resetButton);

		Button mainMenuButton = new Button();
		mainMenuButton.setBackground(null);
		mainMenuButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3,
								new Label("Main Menu"), 20, 150));
		mainMenuButton.setOnAction(e -> {
			GameOptionsController.nullifyOptions();
			GameOptionsController.initializeGameOptions();
			MusicPlayer.setVolume(GameOptionsController.getSupplier("Music Volume").get().doubleValue());
			app.showMainMenu(new StackPane());
		});
		configureButton(mainMenuButton);

		vbox.setFillWidth(true);
		ScrollPane scrollPane = new ScrollPane(vbox);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("""
		-fx-background-color: transparent;
		-fx-background: transparent;
		-fx-control-inner-background: transparent;
		""");
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		HBox resetAndSaveButtons = new HBox(resetButton, saveButton);
		resetAndSaveButtons.setAlignment(Pos.BOTTOM_CENTER);
		resetAndSaveButtons.setSpacing(20);
		resetAndSaveButtons.setPadding(new Insets(10, 0, 5, 0));

		VBox bottomVBox = new VBox(resetAndSaveButtons, mainMenuButton);
		bottomVBox.setAlignment(Pos.BOTTOM_CENTER);
		bottomVBox.setPadding(new Insets(0,0,10,0));

		VBox fullLayout = new VBox();
		fullLayout.setSpacing(10);
		fullLayout.getChildren().addAll(scrollPane, bottomVBox);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);

		root.getChildren().addAll(backgroundView, fullLayout);

		scene = new Scene(root, app.getPrimaryStage().getWidth(), app.getPrimaryStage().getHeight());
		scene.getStylesheets().add(getClass().getResource("/styles/scrollPaneStyle.css").toExternalForm());
	}
}