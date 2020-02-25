import java.util.*;
import java.util.Date;

import enums.AuxillaryOperatorType;
import enums.LogicType;
import enums.OperatorType;

import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.geometry.*;

public class PropBuilder implements EventHandler<ActionEvent> {
	
	private Stage window;
	private Scene scene;
	private GridPane grid, propositionGrid;
	private VBox logicTypeVBox;
	private String firstAuxOpsStr, secondAuxOpsStr, currentPropositionStr;
	private HBox completionButtonsRow;
	private VBox layout;
	//private StackPane leftLabelStack, rightLabelStack, operatorLabelStack, auxOpsLabelStack, leftOperandChoiceStack, rightOperandChoiceStack, operatorChoiceStack;
	private Label singlePropLabel, firstOperandLabel, rightOperandLabel, operatorLabel, currentPropositionLabel;
	private ComboBox<Proposition> firstOperandChoice, secondOperandChoice; 
	private ComboBox<Operator> operatorChoice;
	//private ComboBox<String> chainWithOperandChoice;
	//private ComboBox<AuxillaryOperator> stagingAuxOpsChoice, chainWithAuxOpsChoice;
	private CheckBox singlePropCheck;
	private TextField currentPropositionField;
	private Button addAuxOpToFirst, addAuxOpToSecond, addToPropLists, clearAll, completeProp;
	private LogicType logicType;
	private WindowController windowController;
	private Proposition rootProposition;
	private ArrayList<Proposition> firstPropositionList, secondPropositionList;
	private ArrayList<AuxillaryOperator> firstAuxOps, secondAuxOps;
	private Tooltip auxOpPopup;
	
	
	public PropBuilder(LogicType chosenType, WindowController controller) {
		logicType = chosenType;
		windowController = controller;
		
		
		initDefaults();

		

		
		window = new Stage();
		window.setTitle("Proposition Builder");
		

		
		layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20, 20, 20, 20));
		
		

		
		
		
		
		
		
		
		singlePropCheck = new CheckBox();
		singlePropCheck.setAlignment(Pos.CENTER);
		singlePropCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observed, Boolean oldVal, Boolean newVal) {
				if (singlePropCheck.isSelected()) {
					operatorChoice.setDisable(true);
					addAuxOpToSecond.setDisable(true);
					secondOperandChoice.setDisable(true);
				}else {
					operatorChoice.setDisable(false);
					addAuxOpToSecond.setDisable(false);
					secondOperandChoice.setDisable(false);
				}
				
			}
		});
		GridPane.setConstraints(singlePropCheck, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		singlePropLabel = new Label("Single Proposition");
		singlePropLabel.setAlignment(Pos.CENTER);
		singlePropLabel.setPrefWidth(80);
		GridPane.setConstraints(singlePropLabel, 0, 0);
		
		firstOperandLabel = new Label("First operand");
		firstOperandLabel.setAlignment(Pos.CENTER);
		firstOperandLabel.setPrefWidth(100);
		GridPane.setConstraints(firstOperandLabel, 2, 0);
		
		rightOperandLabel = new Label("Second operand");
		rightOperandLabel.setAlignment(Pos.CENTER);
		rightOperandLabel.setPrefWidth(100);
		GridPane.setConstraints(rightOperandLabel, 5, 0);
		
		operatorLabel = new Label("Operator");
		operatorLabel.setAlignment(Pos.CENTER);
		operatorLabel.setPrefWidth(100);
		GridPane.setConstraints(operatorLabel, 3, 0);
		
		currentPropositionLabel = new Label("Current proposition");
		currentPropositionLabel.setAlignment(Pos.CENTER);
		currentPropositionLabel.setPrefWidth(150);

		
		firstOperandChoice = new ComboBox<Proposition>();
		firstOperandChoice.getItems().addAll(firstPropositionList);
		GridPane.setConstraints(firstOperandChoice, 2, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		secondOperandChoice = new ComboBox<Proposition>();
		secondOperandChoice.getItems().addAll(secondPropositionList);
		GridPane.setConstraints(secondOperandChoice, 5, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		operatorChoice = new ComboBox<Operator>();
		operatorChoice.getItems().addAll(new Operator(OperatorType.OR), 
				new Operator(OperatorType.AND), 
				new Operator(OperatorType.IF), 
				new Operator(OperatorType.EQUALS));
		GridPane.setConstraints(operatorChoice, 3, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// ====== BUTTONS
		
		addAuxOpToFirst = new Button("+");
		addAuxOpToFirst.setOnAction(e -> {
			firstAuxOps = addAuxOps();
			currentPropositionField.setText(currentPropositionField.getText() + buildAuxOpStr(firstAuxOps, firstAuxOpsStr));
		});
		GridPane.setConstraints(addAuxOpToFirst, 1, 1);
		
		addAuxOpToSecond = new Button("+");
		addAuxOpToSecond.setOnAction(e -> {
			secondAuxOps = addAuxOps();
			currentPropositionField.setText(currentPropositionField.getText() + buildAuxOpStr(secondAuxOps, secondAuxOpsStr));
		});
		GridPane.setConstraints(addAuxOpToSecond, 4, 1);
		
		addToPropLists = new Button("Add to lists");
		addToPropLists.setOnAction(e -> {
			addPropToLists();
		});
		GridPane.setConstraints(addToPropLists, 6, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		clearAll = new Button("Clear all");
		clearAll.setPrefWidth(180);
		clearAll.setOnAction(e -> {
			initDefaults();
			
		});
		
		completeProp = new Button("Complete proposition");
		completeProp.setPrefWidth(180);
		completeProp.setOnAction(e -> {
			completeProposition();
		});
		
		
		
		// ============ SEPARATORS
		
		Separator sep1 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(sep1, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS, new Insets(20, 0, 20, 0));
				
		Separator sep2 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(sep2, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS, new Insets(20, 0, 20, 0));
		
		// ======== TEXTFIELDS
		
		currentPropositionField = new TextField();
		currentPropositionField.setEditable(false);
		
		// =========================== LAYOUTS
		
		propositionGrid = new GridPane();
		propositionGrid.setVgap(20);
		propositionGrid.setHgap(20);
		propositionGrid.getChildren().addAll(singlePropCheck, singlePropLabel, firstOperandLabel, rightOperandLabel, operatorLabel, 
				firstOperandChoice, secondOperandChoice, operatorChoice, 
				addAuxOpToFirst, addAuxOpToSecond, addToPropLists);
		
		completionButtonsRow = new HBox(10);
		completionButtonsRow.setAlignment(Pos.CENTER); 
		completionButtonsRow.getChildren().addAll(clearAll, completeProp);
		GridPane.setConstraints(completionButtonsRow, 0, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		
		layout.getChildren().addAll(propositionGrid, sep1, currentPropositionLabel, currentPropositionField, sep2, completionButtonsRow);
		

		
		scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
	

	
	public boolean isValidProposition() {
		if ((firstOperandChoice.getSelectionModel().isEmpty() & singlePropCheck.isSelected() == true) | 
				((firstOperandChoice.getSelectionModel().isEmpty() |
						operatorChoice.getSelectionModel().isEmpty() | 
						secondOperandChoice.getSelectionModel().isEmpty()) & singlePropCheck.isSelected() == false)) {
			return false;
		
		}else {
			return true;
		}
	}
	
	public void addPropToLists() {
		if (isValidProposition()) {
			
			Proposition firstOperand = firstOperandChoice.getSelectionModel().getSelectedItem();
			Proposition secondOperand = secondOperandChoice.getSelectionModel().getSelectedItem();
			Operator op = operatorChoice.getSelectionModel().getSelectedItem();
			AtomicProposition atomicToAdd = null;
			CompoundProposition compoundToAdd = null;
			
			if (singlePropCheck.isSelected()) {
				if (firstOperand instanceof AtomicProposition) {
					atomicToAdd = (AtomicProposition) firstOperand.copy();
					atomicToAdd.setAuxOps(firstAuxOps); // PROBABLY WANT TO ADD TO AUXOPS RATHER THAN OVERIDE !!
				}else {
					compoundToAdd = (CompoundProposition) firstOperand.copy();
					compoundToAdd.setAuxOps(firstAuxOps);
				}
				
			}else {

				compoundToAdd = new CompoundProposition(firstOperand, secondOperand, op, null);
			}
			
			
			if (atomicToAdd != null) {
				firstPropositionList.add(atomicToAdd.copy());
				secondPropositionList.add(atomicToAdd.copy());
				rootProposition = atomicToAdd.copy();
			}else if (compoundToAdd != null) {
				firstPropositionList.add(compoundToAdd.copy());
				secondPropositionList.add(compoundToAdd.copy());
				rootProposition = compoundToAdd.copy();
			}else {
				System.out.println("Something broke");
			}
			
			firstOperandChoice.getItems().clear();
			secondOperandChoice.getItems().clear();
			
			firstOperandChoice.getItems().addAll(firstPropositionList);
			secondOperandChoice.getItems().addAll(secondPropositionList);
		}else {
			WarningModal warning = new WarningModal("Please choose a valid proposition");
		}
		
		
			
		
		
	}
	
	public void completeProposition() {
		windowController.setStoredProp(rootProposition);
		window.close();
	}
	
	public void initDefaults() {
		firstPropositionList = new ArrayList<Proposition>(Arrays.asList(new AtomicProposition(null, "p"), 
				new AtomicProposition(null, "q"), 
				new AtomicProposition(null, "r"), 
				new AtomicProposition(null, "a"), 
				new AtomicProposition(null, "b"), 
				new AtomicProposition(null, "c")
				));
		
		secondPropositionList = new ArrayList<Proposition>(Arrays.asList(new AtomicProposition(null, "p"), 
				new AtomicProposition(null, "q"), 
				new AtomicProposition(null, "r"), 
				new AtomicProposition(null, "a"), 
				new AtomicProposition(null, "b"), 
				new AtomicProposition(null, "c")
				));
		
		firstAuxOps = null;
		secondAuxOps = null;
		
		firstAuxOpsStr = "";
		secondAuxOpsStr = "";
		currentPropositionStr = "";
	}
	
	public ArrayList<AuxillaryOperator> addAuxOps(){
		WindowController controller = new WindowController();
		AuxOpBuilder auxOpBuilder = new AuxOpBuilder(logicType, controller);
		ArrayList<AuxillaryOperator> returnedAuxOps = controller.getStoredAuxOps();
		return returnedAuxOps;
	}
	
	public String buildAuxOpStr(ArrayList<AuxillaryOperator> auxOps, String str) {
		for (AuxillaryOperator auxOp : auxOps) {
			str = auxOp + str;
		}
		return str;
	}
	
	@Override
	public void handle(ActionEvent e) {
		
	}
}
