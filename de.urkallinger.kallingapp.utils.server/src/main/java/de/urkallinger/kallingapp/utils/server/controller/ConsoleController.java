package de.urkallinger.kallingapp.utils.server.controller;

import de.urkallinger.kallingapp.utils.server.model.ConsoleEntry;
import de.urkallinger.kallingapp.utils.server.utils.ListViewAppender;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;

public class ConsoleController {

	@FXML
	private ListView<ConsoleEntry> console = new ListView<>();
	
	@FXML
	public void initialize() {
		console.setCellFactory(value -> new ListCell<ConsoleEntry>() {
			@Override
			protected void updateItem(ConsoleEntry item, boolean empty) {
				super.updateItem(item, empty);
				if (!isEmpty()) {
					setText(item.getText());
					setTextFill(item.getColor());
					setFont(new Font("Consolas", 16));
				} else {
					setText(null);
				}
			}
		});
		
		console.getItems().addListener(new ListChangeListener<ConsoleEntry>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends ConsoleEntry> c) {
				Platform.runLater(() -> console.scrollTo(console.getItems().size() - 1));
			}
		});
		
		ListViewAppender.addListView(console);
	}
	
	public void clear() {
		console.getItems().clear();
		console.refresh();
	}
}
