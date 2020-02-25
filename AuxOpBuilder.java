import java.util.ArrayList;
import java.util.Arrays;

import enums.AuxillaryOperatorType;
import enums.LogicType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuxOpBuilder {
	
	private Stage window;
	private Scene scene;
	private VBox layout;
	private HBox mainRow, buttons;
	private Label chosenPropositionLabel;
	private Button addAuxOp, confirm, cancel;
	private TextField chosenAuxOpsField;
	private ComboBox<AuxillaryOperator> auxOpsChoice;
	private ArrayList<AuxillaryOperator> chosenAuxOps;
	private String chosenAuxOpsString, chosenPropString;
	private LogicType logicType;
	private WindowController controller;
	
	public AuxOpBuilder(LogicType logicType, WindowController controller) {
		
		this.logicType = logicType;
		//this.chosenPropString = chosenPropString;
		this.controller = controller;
		
		window = new Stage();
		window.setTitle("Auxillary Operator Builder");
		
		
		
		
		// ========== INIT
		
		chosenAuxOps = new ArrayList<AuxillaryOperator>();
		chosenAuxOpsString = "";
		
		ArrayList<AuxillaryOperator> modalOps = new ArrayList<AuxillaryOperator>(Arrays.asList(new AuxillaryOperator(AuxillaryOperatorType.POSSIBLE), 
				new AuxillaryOperator(AuxillaryOperatorType.NECESSARY),
				new AuxillaryOperator(AuxillaryOperatorType.NOTPOSSIBLE),
				new AuxillaryOperator(AuxillaryOperatorType.NOTNECESSARY)));
		ArrayList<AuxillaryOperator> predicateOps = new ArrayList<AuxillaryOperator>(Arrays.asList(new AuxillaryOperator(AuxillaryOperatorType.UNIVERSAL), 
				new AuxillaryOperator(AuxillaryOperatorType.EXISTENTIAL), 
				new AuxillaryOperator(AuxillaryOperatorType.NOTUNIVERSAL), 
				new AuxillaryOperator(AuxillaryOperatorType.NOTEXISTENTIAL)));
		AuxillaryOperator negationOp = new AuxillaryOperator(AuxillaryOperatorType.NEGATION);
		
		// ========= COMBO BOXES
		
		auxOpsChoice = new ComboBox<AuxillaryOperator>();
		auxOpsChoice.getItems().addAll(negationOp);
		if (logicType == LogicType.PREDICATE) {
			auxOpsChoice.getItems().addAll(predicateOps);
		}else if (logicType == LogicType.MODAL) {
			auxOpsChoice.getItems().addAll(modalOps);
		}
		
		// ========= TEXTFIELDS
	
		
		chosenAuxOpsField = new TextField();
		chosenAuxOpsField.setEditable(false);
		
		// ========== BUTTONS
		
		addAuxOp = new Button("+");
		addAuxOp.setOnAction(e -> {
			addAuxOp();
		});
		
		confirm = new Button("Confirm");
		confirm.setOnAction(e -> {
			controller.setStoredAuxOps(chosenAuxOps);
			window.close();
		});
		
		cancel = new Button("Cancel");
		cancel.setOnAction(e -> {
			window.close();
		});
		
		// ====== LABELS
		
		//chosenPropositionLabel = new Label(chosenPropString);
		
		// ============== LAYOUTS
		
		
		mainRow = new HBox(10);
		mainRow.setAlignment(Pos.CENTER);
		mainRow.getChildren().addAll(auxOpsChoice, addAuxOp, chosenAuxOpsField);
		
		buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(confirm, cancel);
		
		layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(mainRow, buttons);
		
		scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
	
	public void addAuxOp() {
		if (auxOpsChoice.getSelectionModel().isEmpty()) {
			WarningModal warning = new WarningModal("Please choose an auxillary operator");
		}else {
			AuxillaryOperator auxOp = auxOpsChoice.getSelectionModel().getSelectedItem().copy();
			chosenAuxOpsString = auxOp.toString() + chosenAuxOpsString;
			chosenAuxOps.add(0, auxOp);
			chosenAuxOpsField.setText(chosenAuxOpsString);
		}
		
	}
}
