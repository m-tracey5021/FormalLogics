package uiComponents;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WarningModal {
	private Stage window;
	private Scene scene;
	private VBox layout;
	private Label warningLabel;
	private Button okButton;
	
	public WarningModal(String message) {
		window = new Stage();
		window.setTitle("Warning");
		window.initModality(Modality.APPLICATION_MODAL);
		
		
		
		
		warningLabel = new Label(message);
		okButton = new Button("OK");
		okButton.setOnAction(e -> {
			window.close();
		});
		
		
		layout = new VBox(40);
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(warningLabel, okButton);
		
		scene = new Scene(layout);
		
		window.setScene(scene);
		window.showAndWait();
		
		
	}
	
	
}
