package de.urkallinger.kallingapp.utils.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.utils.server.controller.ButtonBarController;
import de.urkallinger.kallingapp.utils.server.controller.ConsoleController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
	
	private Stage stage;
	private Scene scene;

	private ConsoleController consoleController;
	private ButtonBarController buttonBarController;
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		this.stage.setTitle("KallingApp - Server");

		Image icon = new Image(getClass().getResourceAsStream("/images/server.png"));
		primaryStage.getIcons().add(icon);

		initRootLayout();
		initConsoleLayout();
		initButtonBarLayout();
	}
	
	@Override
	public void stop(){
	    Server server = Server.getInstance();
	    if(server.isServerThreadAlive()) {
	    	server.stop();
	    }
	}
	
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(getClass().getResource("/ui/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.minHeightProperty().set(400.0);
			stage.minWidthProperty().set(600.0);
			stage.show();
		} catch (IOException e) {
			LOGGER.error("error while initializing root layout", e);
		}
	}

	private void initConsoleLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(getClass().getResource("/ui/ConsoleLayout.fxml"));
			BorderPane consoleLayout = (BorderPane) loader.load();
			rootLayout.setCenter(consoleLayout);

			consoleController = loader.getController();
		} catch (IOException e) {
			LOGGER.error("error while initializing console layout", e);
		}
	}

	private void initButtonBarLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(getClass().getResource("/ui/ButtonBar.fxml"));
			ButtonBar buttonBarLayout = (ButtonBar) loader.load();
			rootLayout.setTop(buttonBarLayout);

			buttonBarController = loader.getController();
			buttonBarController.setConsoleController(consoleController);
		} catch (IOException e) {
			LOGGER.error("error while initializing button bar", e);
		}
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
