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
	private String firstAuxOpsStr, secondAuxOpsStr, currentPropositionStr, placeholderStr;
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
	private Button addMainAuxOps, addAuxOpToFirst, addAuxOpToSecond, addToPropLists, clearAll, completeProp;
	private LogicType logicType;
	private WindowController windowController;
	private Proposition chosenFirstOperand, chosenSecondOperand, firstOperandForString, secondOperandForString;
	private Operator chosenOperator;
	private ArrayList<Proposition> firstPropositionList, secondPropositionList;
	private ArrayList<AuxillaryOperator> mainAuxOps, firstAuxOps, secondAuxOps;
	private Tooltip auxOpPopup;
	private PropComponentString propStr;
	
	public PropBuilder(LogicType chosenType, WindowController controller) {
		logicType = chosenType;
		windowController = controller;
		
		
		initDefaults();
		
		
		
		window = new Stage();
		window.setTitle("Proposition Builder");
		
		//currentPropositionStr = "( []-_[]- )";
		
		layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20, 20, 20, 20));
		
		

		
		
		
		
		
		
		
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
				}else {
					addMainAuxOps.setDisable(false);
					operatorChoice.setDisable(false);
					addAuxOpToSecond.setDisable(false);
					secondOperandChoice.setDisable(false);
				}
				
			}
		});
		GridPane.setConstraints(singlePropCheck, 2, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
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
		
		currentPropositionLabel = new Label("Current proposition");
		currentPropositionLabel.setAlignment(Pos.CENTER);
		currentPropositionLabel.setPrefWidth(150);

		
		firstOperandChoice = new ComboBox<Proposition>();
		firstOperandChoice.setPrefWidth(200);
		firstOperandChoice.getItems().addAll(firstPropositionList);
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
		GridPane.setConstraints(firstOperandChoice, 2, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		secondOperandChoice = new ComboBox<Proposition>();
		secondOperandChoice.setPrefWidth(200);
		secondOperandChoice.getItems().addAll(secondPropositionList);
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
		GridPane.setConstraints(secondOperandChoice, 2, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		
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
		GridPane.setConstraints(operatorChoice, 2, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// ====== BUTTONS
		

		addMainAuxOps = new Button("+");
		addMainAuxOps.setOnAction(e -> {
			mainAuxOps = addAuxOps();
			if (mainAuxOps != null) {
				//replaceAuxOps(true);
				propStr.setMainAuxOps(mainAuxOps);
				currentPropositionField.setText(propStr.toString());
			}
			
		});
		GridPane.setConstraints(addMainAuxOps, 1, 2);
		
		addAuxOpToFirst = new Button("+");
		addAuxOpToFirst.setOnAction(e -> {
			firstAuxOps = addAuxOps();
			if (firstAuxOps != null) {
				//replaceAuxOps(true);
				if (chosenFirstOperand != null) {
					chosenFirstOperand.setAuxOps(firstAuxOps);
					
				}
				propStr.setFirstOperandAuxOps(firstAuxOps);
				currentPropositionField.setText(propStr.toString());
			}
			
		});
		GridPane.setConstraints(addAuxOpToFirst, 1, 1);
		
		addAuxOpToSecond = new Button("+");
		addAuxOpToSecond.setOnAction(e -> {
			secondAuxOps = addAuxOps();
			if (secondAuxOps != null) {
				//replaceAuxOps(false);
				if (chosenSecondOperand != null) {
					chosenSecondOperand.setAuxOps(secondAuxOps);
					
				}
				propStr.setSecondOperandAuxOps(secondAuxOps);
				currentPropositionField.setText(propStr.toString());
			}
			
		});
		GridPane.setConstraints(addAuxOpToSecond, 1, 3);
		
		addToPropLists = new Button("Add to operand lists");
		addToPropLists.setPrefWidth(180);
		addToPropLists.setOnAction(e -> {
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
		propositionGrid.setVgap(20);
		propositionGrid.setHgap(20);
		propositionGrid.getChildren().addAll(singlePropCheck, singlePropLabel, firstOperandLabel, rightOperandLabel, operatorLabel, 
				firstOperandChoice, secondOperandChoice, operatorChoice, 
				addMainAuxOps, addAuxOpToFirst, addAuxOpToSecond);
		
		completionButtonsRow = new HBox(10);
		completionButtonsRow.setAlignment(Pos.CENTER); 
		completionButtonsRow.getChildren().addAll(addToPropLists, completeProp);
		//GridPane.setConstraints(completionButtonsRow, 0, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		
		layout.getChildren().addAll(propositionGrid, sep1, currentPropositionLabel, currentPropositionField, completionButtonsRow, sep2, clearAll);
		
		
		
		scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
	
	public void resetTextField() {
		propStr = new PropComponentString();
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
	
	public Proposition constructProposition() {
		AtomicProposition atomicToAdd = null;
		CompoundProposition compoundToAdd = null;

		if (singlePropCheck.isSelected()) {
			if (firstAuxOps == null) {
				// popUp error window
				WarningModal warning = new WarningModal("Proposition already exists");
			}else {
				
				if (chosenFirstOperand instanceof AtomicProposition) {
					//chosenFirstOperand = (AtomicProposition) chosenFirstOperand;
					
					atomicToAdd = (AtomicProposition) chosenFirstOperand;
				}else {
					compoundToAdd = (CompoundProposition) chosenFirstOperand;
				}
			}
			
			
		}else {
			compoundToAdd = new CompoundProposition(chosenFirstOperand, chosenSecondOperand, chosenOperator, mainAuxOps);
		}
		
		
		if (atomicToAdd != null) {
			return atomicToAdd;


		}else if (compoundToAdd != null) {
			return compoundToAdd;


		}else {
			return null;
		}
	}
	
	
	public void addPropToLists() {
		if (isValidProposition()) {
			

			Proposition toAdd = constructProposition();
			
			
			if (toAdd != null) {
				firstPropositionList.add(toAdd.copy());
				secondPropositionList.add(toAdd.copy());

			}
			
			firstOperandChoice.getItems().clear();
			secondOperandChoice.getItems().clear();
			
			firstOperandChoice.getItems().addAll(firstPropositionList);
			secondOperandChoice.getItems().addAll(secondPropositionList);
			
			mainAuxOps = null;
			firstAuxOps = null;
			secondAuxOps = null;

			
			clearUi();
		}else {
			WarningModal warning = new WarningModal("Please choose a valid proposition");
		}

	}
	
	public void completeProposition() {
		Proposition toReturn = constructProposition();
		windowController.setStoredProp(toReturn);
		window.close();
	}
	
	public void initDefaults() {
		propStr = new PropComponentString();
		
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
		
		
		mainAuxOps = null;
		firstAuxOps = null;
		secondAuxOps = null;
		
		
		
		
		
	}
	
	public void clearUi() {

		resetTextField();
		
		firstOperandChoice.getItems().clear();
		secondOperandChoice.getItems().clear();
		operatorChoice.getItems().clear();
		
		firstOperandChoice.getItems().addAll(firstPropositionList);
		secondOperandChoice.getItems().addAll(secondPropositionList);
		operatorChoice.getItems().addAll(new Operator(OperatorType.OR), 
				new Operator(OperatorType.AND), 
				new Operator(OperatorType.IF), 
				new Operator(OperatorType.EQUALS));
		

	}
	
	public ArrayList<AuxillaryOperator> addAuxOps(){
		WindowController controller = new WindowController();
		AuxOpBuilder auxOpBuilder = new AuxOpBuilder(logicType, controller);
		ArrayList<AuxillaryOperator> returnedAuxOps = controller.getStoredAuxOps();
		return returnedAuxOps;
	}
	
	
	// ============ STRING MANIPULATION AND REPLACEMENT
	
	
	
	public void replaceAuxOps(boolean replaceFirst) {
		int indexOfFirstOpeningBracket = currentPropositionStr.indexOf('[');
		int indexOfFirstClosingBracket = currentPropositionStr.indexOf(']');
		int indexOfSecondOpeningBracket = currentPropositionStr.indexOf('[', indexOfFirstOpeningBracket + 1);
		int indexOfSecondClosingBracket = currentPropositionStr.indexOf(']', indexOfFirstClosingBracket + 1);
		String firstHalf;
		String insertion;
		String secondHalf;

		if (replaceFirst) {
			insertion = buildAuxOpStr(firstAuxOps);
			currentPropositionStr = insertItemInString(0, indexOfFirstOpeningBracket + 1, insertion, indexOfFirstClosingBracket);
		}else {
			insertion = buildAuxOpStr(secondAuxOps);
			currentPropositionStr = insertItemInString(0, indexOfSecondOpeningBracket + 1, insertion, indexOfSecondClosingBracket);
		} 
		
	}
	
	public void replaceOperator() {
		int indexOfOperator = findIndexOfOperator();
		Operator chosenOperator = operatorChoice.getSelectionModel().getSelectedItem();
		currentPropositionStr = insertItemInString(0, indexOfOperator, chosenOperator.toString(), indexOfOperator + 1);
		
	}
	
	public void replaceOperand(boolean replaceFirst) {
		

		if (replaceFirst) {
			Proposition firstOperand = firstOperandChoice.getSelectionModel().getSelectedItem();

			int[] indexesOfFirstOperand = findIndexesOfOperand(true);
			String firstOperandStr = firstOperand.toString();
			currentPropositionStr = insertItemInString(0, indexesOfFirstOperand[0], firstOperandStr, indexesOfFirstOperand[1]);

			
		}else {
			Proposition secondOperand = secondOperandChoice.getSelectionModel().getSelectedItem();

			int[] indexesOfSecondOperand = findIndexesOfOperand(false);
			String secondOperandStr = secondOperand.toString();
			currentPropositionStr = insertItemInString(0, indexesOfSecondOperand[0], secondOperandStr, indexesOfSecondOperand[1]);

			
		}
	}
	
	
	// ============ INDEX FINDING 
	
	public int findIndexOfOperator() {
		int absoluteOpeningBracket = currentPropositionStr.indexOf('(');
		int absoluteClosingBracket = currentPropositionStr.lastIndexOf(')');
		ArrayList<Character> operators = new ArrayList<Character>(Arrays.asList('_', '&', 'V', '\u2283', '='));
		for (int i = 0; i < currentPropositionStr.length() - 1; i ++) {
			if (operators.indexOf(currentPropositionStr.charAt(i)) != -1) {
				int[] indexesOfBrackets = findSurroundingBrackets(currentPropositionStr, i);
				if (indexesOfBrackets[0] == absoluteOpeningBracket & indexesOfBrackets[1] == absoluteClosingBracket) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public int[] findIndexesOfOperand(boolean findFirst) {
		int operatorIndex = findIndexOfOperator();
		
		String firstHalfStr = currentPropositionStr.substring(0, operatorIndex);
		String secondHalfStr = currentPropositionStr.substring(operatorIndex + 1);
		
		int indexOfAbsoluteOpen = currentPropositionStr.indexOf('(');
		int indexOfAbsoluteClose = currentPropositionStr.lastIndexOf(')');
		
		
		int indexOfFirstDash;
		int indexOfSecondDash;
		
		int indexOfOperandStart;
		int indexOfOperandEnd;
		if (findFirst) {
			indexOfFirstDash = firstHalfStr.indexOf('-');
			if (indexOfFirstDash != -1) {
				return new int[] {indexOfFirstDash, indexOfFirstDash + 1};
			}else {
				for (Proposition propInList : firstPropositionList) {
					indexOfOperandStart = firstHalfStr.indexOf(propInList.toString());
					if (indexOfOperandStart != -1) {
						indexOfOperandEnd = indexOfOperandStart + propInList.toString().length();
						return new int[] {indexOfOperandStart, indexOfOperandEnd};
					}
				}
			}
			
		}else {
			indexOfSecondDash = secondHalfStr.indexOf('-');
			if (indexOfSecondDash != -1) {
				indexOfSecondDash += firstHalfStr.length() + 1;
				return new int[] {indexOfSecondDash, indexOfSecondDash + 1};
			}else {
				//int indexOfOpeningBracketWithinMainProp = currentPropositionStr.indexOf('(', indexOfOpeningBracket);
				for (Proposition propInList : secondPropositionList) {
					indexOfOperandStart = secondHalfStr.indexOf(propInList.toString());
					if (indexOfOperandStart != -1) {
						indexOfOperandStart = indexOfOperandStart + firstHalfStr.length() + 1;
						indexOfOperandEnd = indexOfOperandStart + propInList.toString().length();
						return new int[] {indexOfOperandStart, indexOfOperandEnd};
					}
				}
			}
			
		}
		return new int[] {};
		
	}
	
	
	// ============= UTIL STRING MANIPULATION
	
	public int[] findSurroundingBrackets(String stringToSearch, int initialIndex) {
		int propsToTheRight = 0;
		int propsToTheLeft = 0;
		
		int indexOfOpeningBracket = -1;
		int indexOfClosingBracket = -1;
		
		boolean foundOpening = false;
		boolean foundClosing = false;
		
		int i = initialIndex;
		int j = initialIndex;
		
		while (foundOpening == false) {
			if (stringToSearch.charAt(i) == ')') {
				propsToTheLeft += 1;
			}else if (stringToSearch.charAt(i) == '(') {
				propsToTheLeft -= 1;
			}
			if (propsToTheLeft == -1) {
				indexOfOpeningBracket = i;
				foundOpening = true;
			}
			i -= 1;
		}
		
		while (foundClosing == false) {
			if (stringToSearch.charAt(j) == '(') {
				propsToTheRight += 1;
			}else if (stringToSearch.charAt(j) == ')') {
				propsToTheRight -= 1;
			}
			if (propsToTheRight == -1) {
				indexOfClosingBracket = j;
				foundClosing = true;
			}
			j += 1;
		}
		
		
		
		return new int[] {indexOfOpeningBracket, indexOfClosingBracket};
	}
	
	
	public String buildAuxOpStr(ArrayList<AuxillaryOperator> auxOps) {
		String str = "";
		for (int i = auxOps.size() - 1; i >= 0; i -= 1) {
			AuxillaryOperator auxOp = auxOps.get(i);
			str = auxOp + str;
		}
		return str;
	}
	
	
	
	public String insertItemInString(int firstHalfStart, int firstHalfEnd, String insertion, int secondHalfStart) {
		String firstHalf = currentPropositionStr.substring(firstHalfStart, firstHalfEnd);
		String secondHalf = currentPropositionStr.substring(secondHalfStart);
		return firstHalf + insertion + secondHalf;
	}
	
	@Override
	public void handle(ActionEvent e) {
		
	}
}
