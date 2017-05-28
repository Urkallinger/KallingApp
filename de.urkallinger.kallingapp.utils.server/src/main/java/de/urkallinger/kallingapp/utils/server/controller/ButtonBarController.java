package de.urkallinger.kallingapp.utils.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.utils.server.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonBarController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ButtonBarController.class);
	
	@FXML
	private Button btnStart = new Button();
	@FXML
	private Button btnStop = new Button();
	@FXML
	private Button btnRestart = new Button();
	@FXML
	private Button btnClear = new Button();
	@FXML
	private Button btnUpdate = new Button();
	@FXML
	private Button btnSettings = new Button();
	
	private ConsoleController console = null;
	
	@FXML
	public void initialize() {
		btnStart.setGraphic(getImage("/images/buttons/start.png"));
		btnStop.setGraphic(getImage("/images/buttons/stop.png"));
		btnRestart.setGraphic(getImage("/images/buttons/restart.png"));
		btnUpdate.setGraphic(getImage("/images/buttons/update.png"));
		btnSettings.setGraphic(getImage("/images/buttons/settings.png"));
		btnClear.setGraphic(getImage("/images/buttons/clear.png"));
	}
	
	@FXML
	private void onStart() {
		Server server = Server.getInstance();
		server.start();
	}
	
	@FXML
	private void onStop() {
		Server server = Server.getInstance();
		server.stop();
	}
	
	@FXML
	private void onRestart() {
		Server server = Server.getInstance();
		server.restart();
	}
	
	@FXML
	private void onClear() {
		console.clear();
	}
	
	@FXML
	private void onUpdate() {
		LOGGER.warn("update is not implemented yet.");
	}
	
	@FXML
	private void onSettings() {
		LOGGER.warn("settings are not implemented yet.");
	}
	
	private ImageView getImage(String resPath) {
		Image imgRes = new Image(getClass().getResourceAsStream(resPath));
		return new ImageView(imgRes);
	}
	
	public void setConsoleController(ConsoleController console) {
		this.console = console;
	}
}
