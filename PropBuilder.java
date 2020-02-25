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
	private GridPane grid, initialPropGrid;
	private VBox logicTypeVBox;
	private String auxOpsString;
	private HBox chainWithRow, completionButtonsRow;
	private VBox operandRadioButtons;
	//private StackPane leftLabelStack, rightLabelStack, operatorLabelStack, auxOpsLabelStack, leftOperandChoiceStack, rightOperandChoiceStack, operatorChoiceStack;
	private Label logicTypeLabel, initialPropositionLabel, atomicLabel, firstOperandLabel, rightOperandLabel, operatorLabel, 
		auxOpsLabel, usingAuxOpsLabel, stagingLabel, chainWithOperatorLabel, asLabel, currentChainLabel;
	private ComboBox<AtomicProposition> firstOperandChoice, secondOperandChoice; 
	private ComboBox<Operator> stagingOperatorChoice, chainWithOperatorChoice;
	private ComboBox<String> chainWithOperandChoice;
	private ComboBox<AuxillaryOperator> stagingAuxOpsChoice, chainWithAuxOpsChoice;
	private CheckBox atomicCheck;
	private TextField auxOpsStagingTextField, auxOpsChainingTextField, stagedPropField, currentChainField;
	private Button stagingAddAuxOp, chainingAddAuxOp, addPropToStaging, addPropToChain, completeProp, clearAll;
	private RadioButton asFirstOperandCheck, asSecondOperandCheck;
	private ToggleGroup operandToggleGroup, logicTypeGroup;
	private LogicType logicType;
	private WindowController windowController;
	private Proposition rootProposition, stagedProposition;
	private ArrayList<AtomicProposition> propositionList;
	private ArrayList<AuxillaryOperator> auxOpsForStaging, auxOpsForChaining;
	
	
	public PropBuilder(LogicType chosenType, WindowController controller) {
		logicType = chosenType;
		windowController = controller;
		
		
		propositionList = new ArrayList<AtomicProposition>(Arrays.asList(new AtomicProposition(null, "p"), 
				new AtomicProposition(null, "q"), 
				new AtomicProposition(null, "r"), 
				new AtomicProposition(null, "a"), 
				new AtomicProposition(null, "b"), 
				new AtomicProposition(null, "c")
				));
				
		//propositionList = new ArrayList<>(Arrays.asList("p", "q", "r", "a", "b", "c"));
		auxOpsForStaging = new ArrayList<AuxillaryOperator>();
		auxOpsForChaining = new ArrayList<AuxillaryOperator>();
		
		auxOpsString = "";
		
		window = new Stage();
		window.setTitle("Proposition Builder");
		
		grid = new GridPane();
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setPadding(new Insets(20, 20, 30, 20));
		//grid.setGridLinesVisible(true);
		
		
	
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
		
		initialPropositionLabel = new Label("Initial proposition");
		initialPropositionLabel.setAlignment(Pos.CENTER);
		initialPropositionLabel.setPrefWidth(130);
		GridPane.setConstraints(initialPropositionLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
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
		GridPane.setConstraints(stagingLabel, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		
		chainWithOperatorLabel = new Label("Chain with operator");
		chainWithOperatorLabel.setAlignment(Pos.CENTER);
		
		asLabel = new Label("as");
		asLabel.setAlignment(Pos.CENTER);
		
		currentChainLabel = new Label("Current chain");
		currentChainLabel.setAlignment(Pos.CENTER);
		GridPane.setConstraints(currentChainLabel, 0, 8, 1, 1, HPos.CENTER, VPos.CENTER);
		
		usingAuxOpsLabel = new Label("Using auxillary operators");
		usingAuxOpsLabel.setAlignment(Pos.CENTER);
		
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
		
		ArrayList<AuxillaryOperator> modalOps = new ArrayList<AuxillaryOperator>(Arrays.asList(new AuxillaryOperator(AuxillaryOperatorType.POSSIBLE), 
				new AuxillaryOperator(AuxillaryOperatorType.NECESSARY),
				new AuxillaryOperator(AuxillaryOperatorType.NOTPOSSIBLE),
				new AuxillaryOperator(AuxillaryOperatorType.NOTNECESSARY)));
		ArrayList<AuxillaryOperator> predicateOps = new ArrayList<AuxillaryOperator>(Arrays.asList(new AuxillaryOperator(AuxillaryOperatorType.UNIVERSAL), 
				new AuxillaryOperator(AuxillaryOperatorType.EXISTENTIAL), 
				new AuxillaryOperator(AuxillaryOperatorType.NOTUNIVERSAL), 
				new AuxillaryOperator(AuxillaryOperatorType.NOTEXISTENTIAL)));
		AuxillaryOperator negationOp = new AuxillaryOperator(AuxillaryOperatorType.NEGATION);
		
		// =============== COMBOBOXES
		
		stagingAuxOpsChoice = new ComboBox<AuxillaryOperator>();
		stagingAuxOpsChoice.getItems().addAll(negationOp);
		if (logicType == LogicType.CLASSICAL) {
			
		}else if (logicType == LogicType.PREDICATE) {
			stagingAuxOpsChoice.getItems().addAll(predicateOps);
		}else if (logicType == LogicType.MODAL) {
			stagingAuxOpsChoice.getItems().addAll(modalOps);
		}
		GridPane.setConstraints(stagingAuxOpsChoice, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		
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
		
		chainWithAuxOpsChoice = new ComboBox<AuxillaryOperator>();
		chainWithAuxOpsChoice.getItems().addAll(negationOp);
		if (logicType == LogicType.CLASSICAL) {
			
		}else if (logicType == LogicType.PREDICATE) {
			chainWithAuxOpsChoice.getItems().addAll(predicateOps);
		}else if (logicType == LogicType.MODAL) {
			chainWithAuxOpsChoice.getItems().addAll(modalOps);
		}
		chainWithAuxOpsChoice.setDisable(true);
		
		
		// ========== BUTTONS
		
		stagingAddAuxOp = new Button("+");
		stagingAddAuxOp.setOnAction(e -> {
			addAuxOp(stagingAuxOpsChoice, auxOpsForStaging, auxOpsStagingTextField);
		});
		GridPane.setConstraints(stagingAddAuxOp, 1, 1);
		

		chainingAddAuxOp = new Button("+");
		chainingAddAuxOp.setOnAction(e -> {
			addAuxOp(chainWithAuxOpsChoice, auxOpsForChaining, auxOpsChainingTextField);
		});
		chainingAddAuxOp.setDisable(true);

		
		addPropToStaging = new Button("Add initial proposition to staging");
		addPropToStaging.setPrefWidth(250);
		addPropToStaging.setOnAction(e -> {
			addNewPropToStaging();
		});
		GridPane.setConstraints(addPropToStaging, 7, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		addPropToChain = new Button("Add staged proposition to chain");
		addPropToChain.setPrefWidth(250);
		addPropToChain.setDisable(true);
		addPropToChain.setOnAction(e -> {
			addPropToChain();
		});
		GridPane.setConstraints(addPropToChain, 7, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		
		completeProp = new Button("Complete proposition");
		completeProp.setPrefWidth(250);
		completeProp.setDisable(true);
		completeProp.setOnAction(e -> {
			completeProposition();
		});
		//GridPane.setConstraints(completeProp, 7, 8, 1, 1, HPos.CENTER, VPos.CENTER);
		
		clearAll = new Button("Clear all");
		clearAll.setPrefWidth(250);
		clearAll.setOnAction(e -> {
			clearAllPropositions();
		});
		//GridPane.setConstraints(clearAll, 0, 9, 8, 1, HPos.CENTER, VPos.CENTER);
		
		
		// ================ TEXTFIELDS
		
		auxOpsStagingTextField = new TextField();
		auxOpsStagingTextField.setEditable(false);
		GridPane.setConstraints(auxOpsStagingTextField, 2, 1);
		
		auxOpsChainingTextField = new TextField();
		auxOpsChainingTextField.setEditable(false);

		
		stagedPropField = new TextField();
		stagedPropField.setEditable(false);
		GridPane.setConstraints(stagedPropField, 0, 5, 1, 1);
		
		currentChainField = new TextField();
		currentChainField.setEditable(false);
		GridPane.setConstraints(currentChainField, 0, 9, 1, 1);
		
		
		// ============ SEPARATORS
		
		Separator sep1 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(sep1, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS, new Insets(20, 0, 20, 0));
		
		Separator sep2 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(sep2, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS, new Insets(20, 0, 20, 0));
		
		// ================== H and V BOXES, GRIDS
		
		operandRadioButtons = new VBox(10);
		operandRadioButtons.setAlignment(Pos.CENTER_LEFT);
		operandRadioButtons.getChildren().addAll(asFirstOperandCheck, asSecondOperandCheck);
		//GridPane.setConstraints(operandRadioButtons, 0, 7, 7, 1, HPos.CENTER, VPos.CENTER);
		
		initialPropGrid = new GridPane();
		initialPropGrid.setVgap(10);
		initialPropGrid.setHgap(10);
		initialPropGrid.getChildren().addAll(auxOpsLabel, atomicLabel, firstOperandLabel, rightOperandLabel, operatorLabel, 
				stagingAuxOpsChoice, firstOperandChoice, secondOperandChoice, auxOpsStagingTextField, stagingAddAuxOp, 
				stagingOperatorChoice, stackForAtomicCheck, addPropToStaging);
		GridPane.setConstraints(initialPropGrid, 0, 1);
		
		chainWithRow = new HBox(10);
		chainWithRow.setAlignment(Pos.CENTER);
		chainWithRow.getChildren().addAll(usingAuxOpsLabel, chainWithAuxOpsChoice, chainingAddAuxOp, auxOpsChainingTextField, chainWithOperatorLabel, 
				chainWithOperatorChoice, asLabel, chainWithOperandChoice, addPropToChain);
		GridPane.setConstraints(chainWithRow, 0, 6);
		
		completionButtonsRow = new HBox(10);
		completionButtonsRow.setAlignment(Pos.CENTER); 
		completionButtonsRow.getChildren().addAll(clearAll, completeProp);
		GridPane.setConstraints(completionButtonsRow, 0, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// ============ ADD TO GRID
		
		grid.getChildren().addAll(initialPropositionLabel, initialPropGrid, stagingLabel, currentChainLabel, 
				stagedPropField, currentChainField, completionButtonsRow,
				sep1, sep2, chainWithRow);
		
		scene = new Scene(grid);
		window.setScene(scene);
		window.showAndWait();
	}
	
	public void addAuxOp(ComboBox<AuxillaryOperator> choice, ArrayList<AuxillaryOperator> auxOpList, TextField auxOpField) {
		if (choice.getSelectionModel().isEmpty()) {
			WarningModal warning = new WarningModal("Please choose an auxillary operator");
		}else {
			AuxillaryOperator auxOp = choice.getSelectionModel().getSelectedItem().copy();
			auxOpsString = auxOp.toString() + auxOpsString;
			auxOpList.add(0, auxOp);
			auxOpField.setText(auxOpsString);
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
			if (auxOpsForStaging.size() == 0) {
				auxOpsForStaging = null;
			}

			if (atomicCheck.isSelected() == false) {
				stagedProposition = new CompoundProposition(firstAtom, 
						secondAtom, 
						op, 
						auxOpsForStaging);
			}else {
				stagedProposition = new AtomicProposition(auxOpsForStaging, variable);
				
			}
			stagedProposition = stagedProposition.copy();
			stagedPropField.setText(stagedProposition.toString());
			
			auxOpsForStaging = new ArrayList<AuxillaryOperator>();
			auxOpsString = "";
			auxOpsStagingTextField.clear();
			
			stagingAuxOpsChoice.setDisable(true);
			stagingAddAuxOp.setDisable(true);
			atomicCheck.setDisable(true);
			firstOperandChoice.setDisable(true);
			secondOperandChoice.setDisable(true);
			stagingOperatorChoice.setDisable(true);
			addPropToStaging.setDisable(true);
			
			if (rootProposition != null) {
				chainWithOperatorChoice.setDisable(false);
				chainWithOperandChoice.setDisable(false);
				chainWithAuxOpsChoice.setDisable(false);
				chainingAddAuxOp.setDisable(false);
			}
			
			addPropToChain.setDisable(false);
		}
		
		
		
		
		
		


	}
	
	public void addPropToChain() {
		if (chainWithOperatorChoice.getSelectionModel().isEmpty() & rootProposition != null) {
			WarningModal warning = new WarningModal("Please choose an operator");
		}else {
			if (auxOpsForChaining.size() == 0) {
				auxOpsForChaining = null;
			}
			if (rootProposition != null) {
				if (chainWithOperandChoice.getSelectionModel().getSelectedItem().equals("First operand")) {
					rootProposition = new CompoundProposition(stagedProposition, 
							rootProposition, 
							chainWithOperatorChoice.getSelectionModel().getSelectedItem(), 
							auxOpsForChaining);
				}else if (chainWithOperandChoice.getSelectionModel().getSelectedItem().equals("Second operand")) {
					rootProposition = new CompoundProposition(rootProposition, 
							stagedProposition, 
							chainWithOperatorChoice.getSelectionModel().getSelectedItem(), 
							auxOpsForChaining);
				}
				
			}else {
				rootProposition = stagedProposition;
			}
			rootProposition = rootProposition.copy();
			
			currentChainField.setText(rootProposition.toString());
			
			
			stagingAuxOpsChoice.setDisable(false);
			
			stagingAddAuxOp.setDisable(false);
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
			chainWithAuxOpsChoice.setDisable(true);
			
			stagedPropField.clear();
			auxOpsForChaining = new ArrayList<AuxillaryOperator>();
			auxOpsString = "";
			auxOpsChainingTextField.clear();
		}
		
	}
	
	public void completeProposition() {
		windowController.setStoredProp(rootProposition);
		window.close();
	}
	
	public void clearAllPropositions() {
		stagedProposition = null;
		rootProposition = null;
		auxOpsForStaging = new ArrayList<AuxillaryOperator>();
		auxOpsForChaining = new ArrayList<AuxillaryOperator>();
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
