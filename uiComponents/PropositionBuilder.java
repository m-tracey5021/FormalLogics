package uiComponents;
import java.util.*;

import enums.InstanceVariableType;
import enums.LogicType;
import enums.OperatorType;
import enums.PlaceholderVariableType;
import enums.PropositionalVariableType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import propositions.AtomicProposition;
import propositions.AuxillaryOperator;
import propositions.CompoundProposition;
import propositions.Operator;
import propositions.PredicatedProposition;
import propositions.Proposition;
import propositions.PropositionComponentString;
import propositions.PropositionalVariable;
import propositions.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import org.controlsfx.control.*;

public class PropositionBuilder implements EventHandler<ActionEvent> {
	
	private Stage window;
	private Scene scene;
	private GridPane grid, propositionGrid;
	private VBox logicTypeVBox;
	private String firstAuxOpsStr, secondAuxOpsStr, currentPropositionStr, placeholderStr;
	private HBox completionButtonsRow;
	private VBox layout;
	private Label singlePropLabel, firstOperandLabel, rightOperandLabel, operatorLabel, currentPropositionLabel;
	private ComboBox<Proposition> firstOperandChoice, secondOperandChoice; 
	private ComboBox<Operator> operatorChoice;
	private CheckBox singlePropCheck, firstOperandPredicated, secondOperandPredicated;
	private TextField currentPropositionField;
	private Button addMainAuxOps, addAuxOpToFirst, addAuxOpToSecond, addToCustomOperands, clearAll, completeProp;
	private SegmentedButton propositionType1, propositionType2;
	private Spinner<String> spinner1, spinner2;
	private LogicType logicType;
	private WindowController windowController;
	private Proposition chosenFirstOperand, chosenSecondOperand, firstOperandForString, secondOperandForString;
	private Operator chosenOperator;
	private ArrayList<Proposition> firstAtomicPropositions, secondAtomicPropositions, 
		firstPredicatePropositions, secondPredicatePropositions, 
		firstCustomPropositions, secondCustomPropositions, 
		firstRelationalPropositions, secondRelationalPropositions;
	private ArrayList<AuxillaryOperator> mainAuxOps, firstAuxOps, secondAuxOps;
	private PropositionComponentString propStr;
	
	public PropositionBuilder(LogicType chosenType, WindowController controller) {
		logicType = chosenType;
		windowController = controller;
		
		
		initDefaults();
		
		
		
		window = new Stage();
		window.setTitle("Proposition Builder");
		
		
		layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20, 20, 20, 20));
		
		

		
		
		
		// ============= CHECKBOXES
		
		
		
		singlePropCheck = new CheckBox();
		singlePropCheck.setAlignment(Pos.CENTER);
		singlePropCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observed, Boolean oldVal, Boolean newVal) {
				clearUi();
				if (singlePropCheck.isSelected()) {
					addMainAuxOps.setDisable(true);
					mainAuxOps = null;
					operatorChoice.setDisable(true);
					addAuxOpToSecond.setDisable(true);
					secondAuxOps = null;
					secondOperandChoice.setDisable(true);
					spinner2.setDisable(true);
					rightOperandLabel.setDisable(true);
					operatorLabel.setDisable(true);
				}else {
					addMainAuxOps.setDisable(false);
					operatorChoice.setDisable(false);
					addAuxOpToSecond.setDisable(false);
					secondOperandChoice.setDisable(false);
					spinner2.setDisable(false);
					rightOperandLabel.setDisable(false);
					operatorLabel.setDisable(false);
				}
				
			}
		});
		GridPane.setConstraints(singlePropCheck, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		
		
		// ============= LABELS
		
		
		singlePropLabel = new Label("Single Proposition");
		singlePropLabel.setAlignment(Pos.CENTER);
		singlePropLabel.setPrefWidth(100);
		GridPane.setConstraints(singlePropLabel, 0, 0);
		
		firstOperandLabel = new Label("First operand");
		firstOperandLabel.setAlignment(Pos.CENTER);
		firstOperandLabel.setPrefWidth(100);
		GridPane.setConstraints(firstOperandLabel, 0, 1);
		
		rightOperandLabel = new Label("Second operand");
		rightOperandLabel.setAlignment(Pos.CENTER);
		rightOperandLabel.setPrefWidth(100);
		GridPane.setConstraints(rightOperandLabel, 0, 3);
		
		operatorLabel = new Label("Operator");
		operatorLabel.setAlignment(Pos.CENTER);
		operatorLabel.setPrefWidth(100);
		GridPane.setConstraints(operatorLabel, 0, 2);
		//GridPane.setMargin(operatorLabel, new Insets(40, 0, 0, 0));
		
		
		currentPropositionLabel = new Label("Current proposition");
		currentPropositionLabel.setAlignment(Pos.CENTER);
		currentPropositionLabel.setPrefWidth(150);

		
		
		// ============== COMBOBOXES
		
		firstOperandChoice = new ComboBox<Proposition>();
		firstOperandChoice.setPrefWidth(200);
		firstOperandChoice.getItems().addAll(firstAtomicPropositions);
		firstOperandChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Proposition> () {
			@Override
			public void changed(ObservableValue<? extends Proposition> proposition, Proposition oldVal, Proposition newVal) {
				if (newVal != null) {
					if (oldVal != newVal) {
						chosenFirstOperand = newVal.copy();
						firstOperandForString = newVal.copy();
						if (firstAuxOps != null) {
							chosenFirstOperand.setAuxOps(firstAuxOps);
						}
						propStr.setFirstOperand(firstOperandForString);
						currentPropositionField.setText(propStr.toString());
					}
				}
				
			}
		});
		GridPane.setConstraints(firstOperandChoice, 3, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		
		secondOperandChoice = new ComboBox<Proposition>();
		secondOperandChoice.setPrefWidth(200);
		secondOperandChoice.getItems().addAll(secondAtomicPropositions);
		secondOperandChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Proposition> () {
			@Override
			public void changed(ObservableValue<? extends Proposition> proposition, Proposition oldVal, Proposition newVal) {
				if (newVal != null) {
					if (oldVal != newVal) {
						chosenSecondOperand = newVal.copy();
						secondOperandForString = newVal.copy();
						if (secondAuxOps != null) {
							chosenSecondOperand.setAuxOps(secondAuxOps);
						}
						propStr.setSecondOperand(secondOperandForString);
						currentPropositionField.setText(propStr.toString());
					}
				}
			}
		});
		GridPane.setConstraints(secondOperandChoice, 3, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		
		
		operatorChoice = new ComboBox<Operator>();
		operatorChoice.getItems().addAll(new Operator(OperatorType.OR), 
				new Operator(OperatorType.AND), 
				new Operator(OperatorType.IF), 
				new Operator(OperatorType.EQUALS));
		operatorChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Operator> () {
			@Override
			public void changed(ObservableValue<? extends Operator> operator, Operator oldVal, Operator newVal) {
				if (newVal != null) {
					if (oldVal != newVal) {
						chosenOperator = newVal.copy();
						propStr.setOperator(chosenOperator);
						currentPropositionField.setText(propStr.toString());
					}
				}
				
				
			}
		});
		GridPane.setConstraints(operatorChoice, 3, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		//GridPane.setMargin(operatorChoice, new Insets(40, 0, 0, 0));
		
		// ============= SPINNERS
		
		ObservableList<String> propTypes;
		
		if (logicType == LogicType.CLASSICAL || logicType == LogicType.MODAL) {
			propTypes = FXCollections.observableArrayList("Atomic", "Custom");
		}else {
			propTypes = FXCollections.observableArrayList("Atomic", "Predicated", "Relational", "Custom");
		}
		
		/*
		SpinnerValueFactory<String> factory1 = new SpinnerValueFactory<String>() {
			@Override
			public void decrement(int steps) {
				String currentSelection = this.getValue();
			}
			
			@Override
			public void increment(int steps) {
				String currentSelection = this.getValue();
			}
		};
		*/
		
		spinner1 = new Spinner<String>();
		spinner1.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<String>(propTypes));
		spinner1.getValueFactory().setWrapAround(true);
		spinner1.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldVal, String newVal) {
				if (newVal.equals("Atomic")) {
					setChoiceToAtomic(firstOperandChoice);
				}else if (newVal.equals("Predicated")) {
					setChoiceToPredicate(firstOperandChoice);
				} else if (newVal.equals("Relational")) {
					setChoiceToRelational(firstOperandChoice);
				}else {
					setChoiceToCustom(firstOperandChoice);
				}
			}
		});
		spinner1.getStyleClass().add("Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL");

		
		
		spinner2 = new Spinner<String>();
		spinner2.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<String>(propTypes));
		spinner2.getValueFactory().setWrapAround(true);
		spinner2.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldVal, String newVal) {
				if (newVal.equals("Atomic")) {
					setChoiceToAtomic(secondOperandChoice);
				}else if (newVal.equals("Predicated")) {
					setChoiceToPredicate(secondOperandChoice);
				} else if (newVal.equals("Relational")) {
					setChoiceToRelational(secondOperandChoice);
				}else {
					setChoiceToCustom(secondOperandChoice);
				}
			}
		});
		spinner2.getStyleClass().add("Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL");
		
		GridPane.setConstraints(spinner1, 2, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(spinner2, 2, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// ====== BUTTONS
		
		
		/*
		
		ToggleButton b1 = new ToggleButton("Atomic");
		b1.setOnAction(e -> {
			firstOperandChoice.getItems().clear();
			firstOperandChoice.getItems().addAll(firstAtomicPropositions);
		});
		ToggleButton b2 = new ToggleButton("Predicate");
		b2.setOnAction(e -> {
			firstOperandChoice.getItems().clear();
			firstOperandChoice.getItems().addAll(firstPredicatePropositions);
		});
		ToggleButton b3 = new ToggleButton("Custom");
		b3.setOnAction(e -> {
			firstOperandChoice.getItems().clear();
			firstOperandChoice.getItems().addAll(firstCustomPropositions);
		});
		
		
		
		
		ToggleButton b4 = new ToggleButton("Atomic");
		b4.setOnAction(e -> {
			secondOperandChoice.getItems().clear();
			secondOperandChoice.getItems().addAll(secondAtomicPropositions);
		});
		ToggleButton b5 = new ToggleButton("Predicate");
		b5.setOnAction(e -> {
			secondOperandChoice.getItems().clear();
			secondOperandChoice.getItems().addAll(secondPredicatePropositions);
		});
		ToggleButton b6 = new ToggleButton("Custom");
		b6.setOnAction(e -> {
			secondOperandChoice.getItems().clear();
			secondOperandChoice.getItems().addAll(secondCustomPropositions);
		});
		
		if (logicType == LogicType.CLASSICAL || logicType == LogicType.MODAL) {
			propositionType1 = new SegmentedButton(b1, b3);
			propositionType2 = new SegmentedButton(b4, b6);
		}else {
			propositionType1 = new SegmentedButton(b1, b2, b3);
			propositionType2 = new SegmentedButton(b4, b5, b6);
		}
		
		*/
		
		
		

		addMainAuxOps = new Button("+");
		addMainAuxOps.setTooltip(new Tooltip("Add axuillary operators to proposition"));
		addMainAuxOps.setOnAction(e -> {
			mainAuxOps = addAuxOps();
			if (mainAuxOps != null) {
				propStr.setMainAuxOps(mainAuxOps);
				currentPropositionField.setText(propStr.toString());
			}
			
		});
		GridPane.setConstraints(addMainAuxOps, 1, 2);
		//GridPane.setMargin(addMainAuxOps, new Insets(40, 0, 0, 0));
		
		addAuxOpToFirst = new Button("+");
		addAuxOpToFirst.setTooltip(new Tooltip("Add axuillary operators to first operand"));
		addAuxOpToFirst.setOnAction(e -> {
			firstAuxOps = addAuxOps();
			if (firstAuxOps != null) {
				if (chosenFirstOperand != null) {
					chosenFirstOperand.setAuxOps(firstAuxOps);
					
				}
				propStr.setFirstOperandAuxOps(firstAuxOps);
				currentPropositionField.setText(propStr.toString());
			}
			
		});
		GridPane.setConstraints(addAuxOpToFirst, 1, 1);
		
		addAuxOpToSecond = new Button("+");
		addAuxOpToSecond.setTooltip(new Tooltip("Add axuillary operators to second operand"));
		addAuxOpToSecond.setOnAction(e -> {
			secondAuxOps = addAuxOps();
			if (secondAuxOps != null) {
				if (chosenSecondOperand != null) {
					chosenSecondOperand.setAuxOps(secondAuxOps);
					
				}
				propStr.setSecondOperandAuxOps(secondAuxOps);
				currentPropositionField.setText(propStr.toString());
			}
			
		});
		GridPane.setConstraints(addAuxOpToSecond, 1, 3);
		
		addToCustomOperands = new Button("Add to custom operands");
		addToCustomOperands.setPrefWidth(180);
		addToCustomOperands.setOnAction(e -> {
			addPropToLists();
		});
		//GridPane.setConstraints(addToPropLists, 3, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		clearAll = new Button("Clear all");
		clearAll.setPrefWidth(180);
		clearAll.setOnAction(e -> {
			initDefaults();
			clearUi();
			
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
		currentPropositionField.setAlignment(Pos.CENTER);
		currentPropositionField.setText(propStr.toString());
		currentPropositionField.setEditable(false);
		
		// =========================== LAYOUTS
		
		propositionGrid = new GridPane();
		//propositionGrid.setGridLinesVisible(true);
		propositionGrid.setVgap(20);
		propositionGrid.setHgap(20);
		propositionGrid.getChildren().addAll(singlePropCheck,  
				singlePropLabel, firstOperandLabel, rightOperandLabel, operatorLabel, 
				firstOperandChoice, secondOperandChoice, operatorChoice, 
				spinner1, spinner2, 
				addMainAuxOps, addAuxOpToFirst, addAuxOpToSecond);
		
		completionButtonsRow = new HBox(10);
		completionButtonsRow.setAlignment(Pos.CENTER); 
		completionButtonsRow.getChildren().addAll(addToCustomOperands, completeProp);
		//GridPane.setConstraints(completionButtonsRow, 0, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		
		layout.getChildren().addAll(propositionGrid, sep1, currentPropositionLabel, currentPropositionField, completionButtonsRow, sep2, clearAll);
		
		
		
		scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
	
	public void resetTextField() {
		propStr = new PropositionComponentString();
		currentPropositionField.setText(propStr.toString());
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
	
	public Proposition constructProposition(String action) {
		Proposition singlePropToAdd = null;
		CompoundProposition compoundToAdd = null;

		if (singlePropCheck.isSelected()) {
			if (firstAuxOps == null & action.equals("add")) {

				WarningModal warning = new WarningModal("Proposition already exists");
			}else {
				singlePropToAdd = chosenFirstOperand;

			}
			
			
		}else {
			compoundToAdd = new CompoundProposition(chosenFirstOperand, chosenSecondOperand, chosenOperator, mainAuxOps);
			chosenFirstOperand.setParentProp(compoundToAdd);
			chosenSecondOperand.setParentProp(compoundToAdd);
		}
		
		
		if (singlePropToAdd != null) {
			ArrayList<Proposition> allPredicates = singlePropToAdd.initAllPredicateProps();
			for (Proposition pred : allPredicates) {
				pred.startAssignment();
			}
			singlePropToAdd.assignAuxOps();
			return singlePropToAdd;


		}else if (compoundToAdd != null) {
			ArrayList<Proposition> allPredicates = compoundToAdd.initAllPredicateProps();
			for (Proposition pred : allPredicates) {
				pred.startAssignment();
			}
			compoundToAdd.assignAuxOps();
			return compoundToAdd;


		}else {
			return null;
		}
	}
	
	
	public void addPropToLists() {
		if (isValidProposition()) {
			

			Proposition toAdd = constructProposition("add");
			
			
			if (toAdd != null) {
				firstCustomPropositions.add(toAdd.copy());
				secondCustomPropositions.add(toAdd.copy());

			}
			
			//firstOperandChoice.getItems().clear();
			//secondOperandChoice.getItems().clear();
			
			//firstOperandChoice.getItems().addAll(firstAtomicPropositions);
			//secondOperandChoice.getItems().addAll(secondAtomicPropositions);
			
			mainAuxOps = null;
			firstAuxOps = null;
			secondAuxOps = null;

			
			clearUi();
		}else {
			WarningModal warning = new WarningModal("Please choose a valid proposition");
		}

	}
	
	public void completeProposition() {
		if (isValidProposition()) {
			Proposition toReturn = constructProposition("complete");
			windowController.setStoredProp(toReturn);
			window.close();
		}else {
			WarningModal warning = new WarningModal("Please choose a valid proposition");
		}
		
	}
	
	public void initDefaults() {
		propStr = new PropositionComponentString();
		
		firstAtomicPropositions = new ArrayList<Proposition>(Arrays.asList(new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.P)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.Q)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.R)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.S)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.T)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.U)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.V)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.W))
				));
		
		secondAtomicPropositions = new ArrayList<Proposition>(Arrays.asList(new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.P)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.Q)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.R)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.S)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.T)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.U)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.V)), 
				new AtomicProposition(null, new PropositionalVariable(PropositionalVariableType.W))
				));
		
		
		
		firstRelationalPropositions = new ArrayList<Proposition>(Arrays.asList(new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.X), new InstanceVariable(PlaceholderVariableType.X)), 
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.X), new InstanceVariable(PlaceholderVariableType.Y)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.X), new InstanceVariable(PlaceholderVariableType.Z)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Y), new InstanceVariable(PlaceholderVariableType.X)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Y), new InstanceVariable(PlaceholderVariableType.Y)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Y), new InstanceVariable(PlaceholderVariableType.Z)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Z), new InstanceVariable(PlaceholderVariableType.X)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Z), new InstanceVariable(PlaceholderVariableType.Y)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Z), new InstanceVariable(PlaceholderVariableType.Z))
				));
		secondRelationalPropositions = new ArrayList<Proposition>(Arrays.asList(new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.X), new InstanceVariable(PlaceholderVariableType.X)), 
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.X), new InstanceVariable(PlaceholderVariableType.Y)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.X), new InstanceVariable(PlaceholderVariableType.Z)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Y), new InstanceVariable(PlaceholderVariableType.X)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Y), new InstanceVariable(PlaceholderVariableType.Y)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Y), new InstanceVariable(PlaceholderVariableType.Z)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Z), new InstanceVariable(PlaceholderVariableType.X)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Z), new InstanceVariable(PlaceholderVariableType.Y)),
				new RelationalProposition(null, null, null, new InstanceVariable(PlaceholderVariableType.Z), new InstanceVariable(PlaceholderVariableType.Z))
				));
		
		firstCustomPropositions = new ArrayList<Proposition>();
		secondCustomPropositions = new ArrayList<Proposition>();
		
		firstPredicatePropositions = new ArrayList<Proposition>();
		secondPredicatePropositions = new ArrayList<Proposition>();
		
		String[] defaultPredicates = new String[] {"F", "G", "H", "J", "K", "L"};
		for (int i = 0; i < defaultPredicates.length; i ++) {
			addToPredicatePropositions(defaultPredicates[i]);
		}
		
		mainAuxOps = null;
		firstAuxOps = null;
		secondAuxOps = null;
		
		
		
		
		
	}
	
	public void clearUi() {

		resetTextField();
		
		setChoiceToAtomic(firstOperandChoice);
		setChoiceToAtomic(secondOperandChoice);
		
		operatorChoice.getItems().clear();
		operatorChoice.getItems().addAll(new Operator(OperatorType.OR), 
				new Operator(OperatorType.AND), 
				new Operator(OperatorType.IF), 
				new Operator(OperatorType.EQUALS));
		

	}
	
	public void setChoiceToAtomic(ComboBox<Proposition> combo) {
		if (combo == firstOperandChoice) {
			combo.getItems().clear();
			combo.getItems().addAll(firstAtomicPropositions);
		}else {
			combo.getItems().clear();
			combo.getItems().addAll(secondAtomicPropositions);
		}
		
	}
	
	public void setChoiceToPredicate(ComboBox<Proposition> combo) {
		if (combo == firstOperandChoice) {
			combo.getItems().clear();
			combo.getItems().addAll(firstPredicatePropositions);
		}else {
			combo.getItems().clear();
			combo.getItems().addAll(secondPredicatePropositions);
		}
	}
	
	public void setChoiceToRelational(ComboBox<Proposition> combo) {
		if (combo == firstOperandChoice) {
			combo.getItems().clear();
			combo.getItems().addAll(firstRelationalPropositions);
		}else {
			combo.getItems().clear();
			combo.getItems().addAll(secondRelationalPropositions);
		}
	}
	
	public void setChoiceToCustom(ComboBox<Proposition> combo) {
		if (combo == firstOperandChoice) {
			combo.getItems().clear();
			combo.getItems().addAll(firstCustomPropositions);
		}else {
			combo.getItems().clear();
			combo.getItems().addAll(secondCustomPropositions);
		}
	}
	
	public void addToPredicatePropositions(String predicate) {
		ArrayList<Proposition> tmpPredicates1 = new ArrayList<Proposition>(Arrays.asList(new PredicatedProposition(null, null, predicate, new InstanceVariable(PlaceholderVariableType.X)), 
				new PredicatedProposition(null, null, predicate, new InstanceVariable(PlaceholderVariableType.Y)), 
				new PredicatedProposition(null, null, predicate, new InstanceVariable(PlaceholderVariableType.Z))
				));
		ArrayList<Proposition> tmpPredicates2 = new ArrayList<Proposition>(Arrays.asList(new PredicatedProposition(null, null, predicate, new InstanceVariable(PlaceholderVariableType.X)), 
				new PredicatedProposition(null, null, predicate, new InstanceVariable(PlaceholderVariableType.Y)), 
				new PredicatedProposition(null, null, predicate, new InstanceVariable(PlaceholderVariableType.Z))
				));
		firstPredicatePropositions.addAll(tmpPredicates1);
		secondPredicatePropositions.addAll(tmpPredicates2);

	}
	
	public ArrayList<AuxillaryOperator> addAuxOps(){
		WindowController controller = new WindowController();
		AuxOpBuilder auxOpBuilder = new AuxOpBuilder(logicType, controller);
		ArrayList<AuxillaryOperator> returnedAuxOps = controller.getStoredAuxOps();
		return returnedAuxOps;
	}
	
	
	
	
	
	
	
	public String buildAuxOpStr(ArrayList<AuxillaryOperator> auxOps) {
		String str = "";
		for (int i = auxOps.size() - 1; i >= 0; i -= 1) {
			AuxillaryOperator auxOp = auxOps.get(i);
			str = auxOp + str;
		}
		return str;
	}
	
	
	@Override
	public void handle(ActionEvent e) {
		
	}
}
