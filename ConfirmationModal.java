import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationModal {
	private Stage window;
	private Scene scene;
	private VBox layout;
	private HBox buttons;
	private Label warningLabel;
	private Button okButton, cancelButton;
	private WindowController controller;
	
	public ConfirmationModal(String message, WindowController windowController) {
		controller = windowController;
		window = new Stage();
		window.setTitle("Warning");
		window.initModality(Modality.APPLICATION_MODAL);
		
		
		
		
		warningLabel = new Label(message);
		
		

		
		okButton = new Button("OK");
		okButton.setPrefWidth(100);
		okButton.setOnAction(e -> {
			controller.setBoolRet(true);
			window.close();
		});
		
		cancelButton = new Button("Cancel");
		cancelButton.setPrefWidth(100);
		cancelButton.setOnAction(e -> {
			controller.setBoolRet(false);
			window.close();
		});
		
		buttons = new HBox(20);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(okButton, cancelButton);
		
		layout = new VBox(40);
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(warningLabel, buttons);
		
		scene = new Scene(layout);
		
		window.setScene(scene);
		window.showAndWait();
	}
}
