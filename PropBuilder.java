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
	private GridPane grid;
	private VBox logicTypeVBox;
	private String auxOpsString, negationSymbol, possibilitySymbol, necessitySymbol, universalSymbol, existentialSymbol;
	private HBox chainWithRow;
	private VBox operandRadioButtons;
	//private StackPane leftLabelStack, rightLabelStack, operatorLabelStack, auxOpsLabelStack, leftOperandChoiceStack, rightOperandChoiceStack, operatorChoiceStack;
	private Label logicTypeLabel, atomicLabel, firstOperandLabel, rightOperandLabel, operatorLabel, auxOpsLabel, stagingLabel, chainWithOperatorLabel, asLabel, currentChainLabel;
	private ComboBox<AtomicProposition> firstOperandChoice, secondOperandChoice; 
	private ComboBox<Operator> stagingOperatorChoice, chainWithOperatorChoice;
	private ComboBox<String> auxOpsChoice, chainWithOperandChoice;
	private CheckBox atomicCheck;
	private TextField auxOpsChainField, stagedPropField, currentChainField;
	private Button addAuxOp, addPropToStaging, addPropToChain, completeProp, clearAll;
	private RadioButton asFirstOperandCheck, asSecondOperandCheck;
	private ToggleGroup operandToggleGroup, logicTypeGroup;
	private LogicType logicType;
	private WindowController windowController;
	private Proposition rootProposition, stagedProposition;
	private ArrayList<AtomicProposition> propositionList;
	private ArrayList<AuxillaryOperator> auxOps;
	
	
	public PropBuilder(LogicType chosenType, WindowController controller) {
		logicType = chosenType;
		windowController = controller;
		
		
		propositionList = new ArrayList<AtomicProposition>(Arrays.asList(new AtomicProposition(new ArrayList<AuxillaryOperator>(), "p"), 
				new AtomicProposition(new ArrayList<AuxillaryOperator>(), "q"), 
				new AtomicProposition(new ArrayList<AuxillaryOperator>(), "r"), 
				new AtomicProposition(new ArrayList<AuxillaryOperator>(), "a"), 
				new AtomicProposition(new ArrayList<AuxillaryOperator>(), "b"), 
				new AtomicProposition(new ArrayList<AuxillaryOperator>(), "c")
				));
				
		//propositionList = new ArrayList<>(Arrays.asList("p", "q", "r", "a", "b", "c"));
		auxOps = new ArrayList<AuxillaryOperator>();
		
		window = new Stage();
		window.setTitle("Proposition Builder");
		
		grid = new GridPane();
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setPadding(new Insets(10, 10, 10, 10));
		//grid.setGridLinesVisible(true);
		
		
		
		auxOpsString = "";
		negationSymbol = "~";
		possibilitySymbol = "\u25C7";
		necessitySymbol = "\u2610";
		universalSymbol = "\u2200";
		existentialSymbol = "\u2203";
		
		
		/*
		logicTypeLabel = new Label("Select logic type");
		GridPane.setConstraints(logicTypeLabel, 0, 0);
		
		logicTypeGroup = new ToggleGroup();
		logicTypeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> observed, Toggle oldVal, Toggle newVal) {
				if (logicTypeGroup.getSelectedToggle() == classicalRb) {
					logicType = LogicType.CLASSICAL;
				} else if (logicTypeGroup.getSelectedToggle() == predicateRb) {
					logicType = LogicType.PREDICATE;
				} else if (logicTypeGroup.getSelectedToggle() == modalRb) {
					logicType = LogicType.MODAL;
				} else {
					System.out.println("Some error");
				}
			}
		});
		
		classicalRb = new RadioButton("Classical");
		classicalRb.setToggleGroup(logicTypeGroup);
		
		predicateRb = new RadioButton("Predicate");
		predicateRb.setToggleGroup(logicTypeGroup);
		
		modalRb = new RadioButton("Modal");
		modalRb.setToggleGroup(logicTypeGroup);
		
		logicTypeVBox = new VBox(7.5);
		logicTypeVBox.getChildren().addAll(classicalRb, predicateRb, modalRb);
		GridPane.setConstraints(logicTypeVBox, 0, 1);
		
		*/
		
		// ====================================== LABELS
		
		
		
		auxOpsLabel = new Label("Auxillary operator");
		auxOpsLabel.setAlignment(Pos.CENTER);
		auxOpsLabel.setPrefWidth(130);
		GridPane.setConstraints(auxOpsLabel, 0, 0);
		
		atomicLabel = new Label("Atomic");
		atomicLabel.setAlignment(Pos.CENTER);
		atomicLabel.setPrefWidth(80);
		GridPane.setConstraints(atomicLabel, 3, 0);
		
		firstOperandLabel = new Label("First operand");
		firstOperandLabel.setAlignment(Pos.CENTER);
		firstOperandLabel.setPrefWidth(100);
		GridPane.setConstraints(firstOperandLabel, 4, 0);
		
		rightOperandLabel = new Label("Second operand");
		rightOperandLabel.setAlignment(Pos.CENTER);
		rightOperandLabel.setPrefWidth(100);
		GridPane.setConstraints(rightOperandLabel, 6, 0);
		
		operatorLabel = new Label("Operator");
		operatorLabel.setAlignment(Pos.CENTER);
		operatorLabel.setPrefWidth(100);
		GridPane.setConstraints(operatorLabel, 5, 0);
		
		stagingLabel = new Label("Staged proposition");
		stagingLabel.setAlignment(Pos.CENTER);
		GridPane.setConstraints(stagingLabel, 0, 3, 8, 1, HPos.CENTER, VPos.CENTER);
		
		chainWithOperatorLabel = new Label("Chain with operator");
		chainWithOperatorLabel.setAlignment(Pos.CENTER);
		
		asLabel = new Label("as");
		asLabel.setAlignment(Pos.CENTER);
		
		currentChainLabel = new Label("Current chain");
		currentChainLabel.setAlignment(Pos.CENTER);
		GridPane.setConstraints(currentChainLabel, 0, 5, 8, 1, HPos.CENTER, VPos.CENTER);
		
		// =============== RADIO BUTTONS
		
		operandToggleGroup = new ToggleGroup();
		
		asFirstOperandCheck = new RadioButton("First operand");
		asFirstOperandCheck.setToggleGroup(operandToggleGroup);
		asSecondOperandCheck = new RadioButton("Second operand");
		asSecondOperandCheck.setToggleGroup(operandToggleGroup);
		
		
		
		// ============= CHECKBOXES
		
		atomicCheck = new CheckBox();
		atomicCheck.setAlignment(Pos.CENTER);
		atomicCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observed, Boolean oldVal, Boolean newVal) {
				if (atomicCheck.isSelected()) {
					stagingOperatorChoice.setDisable(true);
					secondOperandChoice.setDisable(true);
				}else {
					stagingOperatorChoice.setDisable(false);
					secondOperandChoice.setDisable(false);
				}
				
			}
		});
		StackPane stackForAtomicCheck = new StackPane();
		stackForAtomicCheck.getChildren().add(atomicCheck);
		GridPane.setConstraints(stackForAtomicCheck, 3, 1);
		
		// ============== COMBOBOX ITEMS
		
		ArrayList<String> modalOps = new ArrayList<String>(Arrays.asList("POSSIBILITY", "NECESSITY"));
		ArrayList<String> predicateOps = new ArrayList<String>(Arrays.asList("UNIVERSAL", "EXISTENTIAL"));
		String negationOp = "NEGATION";
		
		// =============== COMBOBOXES
		
		auxOpsChoice = new ComboBox<String>();
		auxOpsChoice.getItems().addAll(negationOp);
		if (logicType == LogicType.CLASSICAL) {
			
		}else if (logicType == LogicType.PREDICATE) {
			auxOpsChoice.getItems().addAll(predicateOps);
		}else if (logicType == LogicType.MODAL) {
			auxOpsChoice.getItems().addAll(modalOps);
		}
		GridPane.setConstraints(auxOpsChoice, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		

		firstOperandChoice = new ComboBox<AtomicProposition>();
		firstOperandChoice.getItems().addAll(propositionList);
		GridPane.setConstraints(firstOperandChoice, 4, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		secondOperandChoice = new ComboBox<AtomicProposition>();
		secondOperandChoice.getItems().addAll(propositionList);
		GridPane.setConstraints(secondOperandChoice, 6, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		stagingOperatorChoice = new ComboBox<Operator>();
		stagingOperatorChoice.getItems().addAll(new Operator(OperatorType.OR), 
				new Operator(OperatorType.AND), 
				new Operator(OperatorType.IF), 
				new Operator(OperatorType.EQUALS));
		GridPane.setConstraints(stagingOperatorChoice, 5, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		chainWithOperatorChoice = new ComboBox<Operator>();
		chainWithOperatorChoice.getItems().addAll(new Operator(OperatorType.OR), 
				new Operator(OperatorType.AND), 
				new Operator(OperatorType.IF), 
				new Operator(OperatorType.EQUALS));
		chainWithOperatorChoice.setDisable(true);
		
		chainWithOperandChoice = new ComboBox<String>();
		chainWithOperandChoice.getItems().addAll("First operand", "Second operand");
		chainWithOperandChoice.setDisable(true);
		
		
		// ========== BUTTONS
		
		addAuxOp = new Button("+");
		addAuxOp.setOnAction(e -> {
			addAuxOp();
		});
		GridPane.setConstraints(addAuxOp, 1, 1);
		
		addPropToStaging = new Button("Add proposition to staging");
		addPropToStaging.setPrefWidth(250);
		addPropToStaging.setOnAction(e -> {
			addNewPropToStaging();
		});
		GridPane.setConstraints(addPropToStaging, 7, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		addPropToChain = new Button("Add proposition to chain");
		addPropToChain.setPrefWidth(250);
		addPropToChain.setDisable(true);
		addPropToChain.setOnAction(e -> {
			addPropToChain();
		});
		GridPane.setConstraints(addPropToChain, 7, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		
		completeProp = new Button("Complete proposition");
		completeProp.setPrefWidth(250);
		completeProp.setDisable(true);
		completeProp.setOnAction(e -> {
			completeProposition();
		});
		GridPane.setConstraints(completeProp, 7, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		
		clearAll = new Button("Clear all");
		clearAll.setPrefWidth(200);
		clearAll.setOnAction(e -> {
			clearAllPropositions();
		});
		GridPane.setConstraints(clearAll, 0, 7, 8, 1, HPos.CENTER, VPos.CENTER);
		
		
		// ================ TEXTFIELDS
		
		auxOpsChainField = new TextField();
		auxOpsChainField.setEditable(false);
		GridPane.setConstraints(auxOpsChainField, 2, 1);
		
		stagedPropField = new TextField();
		stagedPropField.setEditable(false);
		GridPane.setConstraints(stagedPropField, 0, 4, 7, 1);
		
		currentChainField = new TextField();
		currentChainField.setEditable(false);
		GridPane.setConstraints(currentChainField, 0, 6, 7, 1);
		
		
		// ============ SEPARATORS
		
		Separator sep = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(sep, 0, 2, 8, 1);
		
		// ================== H and V BOXES
		
		operandRadioButtons = new VBox(10);
		operandRadioButtons.setAlignment(Pos.CENTER_LEFT);
		operandRadioButtons.getChildren().addAll(asFirstOperandCheck, asSecondOperandCheck);
		//GridPane.setConstraints(operandRadioButtons, 0, 7, 7, 1, HPos.CENTER, VPos.CENTER);
		
		chainWithRow = new HBox(10);
		chainWithRow.setAlignment(Pos.CENTER);
		chainWithRow.getChildren().addAll(chainWithOperatorLabel, chainWithOperatorChoice, asLabel, chainWithOperandChoice);
		GridPane.setConstraints(chainWithRow, 7, 3);
		
		// ============ ADD TO GRID
		
		grid.getChildren().addAll(auxOpsLabel, atomicLabel, firstOperandLabel, rightOperandLabel, operatorLabel, stagingLabel, currentChainLabel, 
				auxOpsChoice, firstOperandChoice, secondOperandChoice, stagingOperatorChoice,
				auxOpsChainField, stagedPropField, currentChainField, 
				addAuxOp, addPropToStaging, addPropToChain, completeProp, clearAll, 
				sep, chainWithRow, stackForAtomicCheck);
		
		scene = new Scene(grid);
		window.setScene(scene);
		window.showAndWait();
	}
	
	public void addAuxOp() {
		if (auxOpsChoice.getSelectionModel().isEmpty()) {
			WarningModal warning = new WarningModal("Please choose an auxillary operator");
		}else {
			AuxillaryOperator auxOp = new AuxillaryOperator();
			if (auxOpsChoice.getSelectionModel().getSelectedItem().equals("NEGATION")) {
				auxOpsString = negationSymbol + auxOpsString;
				auxOp.setAuxOp(AuxillaryOperatorType.NEGATION); 
			}else if (auxOpsChoice.getSelectionModel().getSelectedItem().equals("POSSIBILITY")) {
				auxOpsString = possibilitySymbol + " " + auxOpsString;
				auxOp.setAuxOp(AuxillaryOperatorType.POSSIBILITY);
			}else if (auxOpsChoice.getSelectionModel().getSelectedItem().equals("NECESSITY")) {
				auxOpsString = necessitySymbol + " " + auxOpsString;
				auxOp.setAuxOp(AuxillaryOperatorType.NECESSITY);
			}else if (auxOpsChoice.getSelectionModel().getSelectedItem().equals("UNIVERSAL")){
				auxOpsString = universalSymbol + " " + auxOpsString;
				auxOp.setAuxOp(AuxillaryOperatorType.UNIVERSAL);
			}else if (auxOpsChoice.getSelectionModel().getSelectedItem().equals("EXISTENTIAL")) {
				auxOpsString = existentialSymbol + " " + auxOpsString;
				auxOp.setAuxOp(AuxillaryOperatorType.EXISTENTIAL);
			}else {

			}
			auxOps.add(0, auxOp);
			auxOpsChainField.setText(auxOpsString);
		}
		
	}

	
	public void addNewPropToStaging() {
		if ((firstOperandChoice.getSelectionModel().isEmpty() & atomicCheck.isSelected() == true) | 
				((firstOperandChoice.getSelectionModel().isEmpty() |
						stagingOperatorChoice.getSelectionModel().isEmpty() | 
						secondOperandChoice.getSelectionModel().isEmpty()) & atomicCheck.isSelected() == false)) {
			WarningModal warning = new WarningModal("Please choose a valid proposition");
		}else {
			String variable = firstOperandChoice.getSelectionModel().getSelectedItem().getVariableName();
			AtomicProposition firstAtom = firstOperandChoice.getSelectionModel().getSelectedItem();
			AtomicProposition secondAtom = secondOperandChoice.getSelectionModel().getSelectedItem();
			Operator op = stagingOperatorChoice.getSelectionModel().getSelectedItem();
			
			if (atomicCheck.isSelected() == false) {
				stagedProposition = new CompoundProposition(firstAtom, 
						secondAtom, 
						op, 
						auxOps);
			}else {
				stagedProposition = new AtomicProposition(auxOps, variable);
				
			}
			stagedProposition = stagedProposition.copy();
			stagedPropField.setText(stagedProposition.toString());
			
			auxOps = new ArrayList<AuxillaryOperator>();
			
			auxOpsChoice.setDisable(true);
			addAuxOp.setDisable(true);
			atomicCheck.setDisable(true);
			firstOperandChoice.setDisable(true);
			secondOperandChoice.setDisable(true);
			stagingOperatorChoice.setDisable(true);
			addPropToStaging.setDisable(true);
			
			if (rootProposition != null) {
				chainWithOperatorChoice.setDisable(false);
				chainWithOperandChoice.setDisable(false);
			}
			
			addPropToChain.setDisable(false);
		}
		
		
		
		
		
		


	}
	
	public void addPropToChain() {
		if (chainWithOperatorChoice.getSelectionModel().isEmpty() & rootProposition != null) {
			WarningModal warning = new WarningModal("Please choose an operator");
		}else {
			if (rootProposition != null) {
				if (chainWithOperandChoice.getSelectionModel().getSelectedItem().equals("First operand")) {
					rootProposition = new CompoundProposition(stagedProposition, 
							rootProposition, 
							chainWithOperatorChoice.getSelectionModel().getSelectedItem(), 
							auxOps);
				}else if (chainWithOperandChoice.getSelectionModel().getSelectedItem().equals("Second operand")) {
					rootProposition = new CompoundProposition(rootProposition, 
							stagedProposition, 
							chainWithOperatorChoice.getSelectionModel().getSelectedItem(), 
							auxOps);
				}
				
			}else {
				rootProposition = stagedProposition;
			}
			rootProposition = rootProposition.copy();
			
			currentChainField.setText(rootProposition.toString());
			
			
			auxOpsChoice.setDisable(false);
			addAuxOp.setDisable(false);
			atomicCheck.setDisable(false);
			firstOperandChoice.setDisable(false);
			if (atomicCheck.isSelected() == false) {
				secondOperandChoice.setDisable(false);
				stagingOperatorChoice.setDisable(false);
			}
			
			addPropToStaging.setDisable(false);
			
			completeProp.setDisable(false);
			addPropToChain.setDisable(true);
			chainWithOperatorChoice.setDisable(true);
			chainWithOperandChoice.setDisable(true);
			
			stagedPropField.clear();
			auxOps = new ArrayList<AuxillaryOperator>();
			auxOpsChainField.clear();
		}
		
	}
	
	public void completeProposition() {
		windowController.setStoredProp(rootProposition);
		window.close();
	}
	
	public void clearAllPropositions() {
		stagedProposition = new CompoundProposition();
		rootProposition = new CompoundProposition();
		auxOps = new ArrayList<AuxillaryOperator>();
		stagedPropField.clear();
		currentChainField.clear();
		chainWithOperatorChoice.setDisable(true);
		chainWithOperandChoice.setDisable(true);
		completeProp.setDisable(true);
	}
	
	@Override
	public void handle(ActionEvent e) {
		
	}
}
