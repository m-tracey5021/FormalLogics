package uiComponents;
import java.util.ArrayList;
import java.util.Arrays;

import enums.AuxillaryOperatorType;
import enums.LogicType;
import enums.PlaceholderVariableType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import propositions.AuxillaryOperator;
import propositions.InstanceVariable;
import propositions.Operator;
import propositions.PredicatedProposition;
import propositions.Proposition;
import propositions.Quantifier;

public class AuxOpBuilder {
	
	private Stage window;
	private Scene scene;
	private VBox layout;
	private HBox mainRow, buttons;
	private Label chosenPropositionLabel;
	private Button addAuxOp, confirm, cancel;
	private TextField chosenAuxOpsField;
	private ComboBox<AuxillaryOperator> auxOpsChoice;
	private ComboBox<InstanceVariable> applicableInstanceVariables;
	private ArrayList<InstanceVariable> instanceVariables;
	private ArrayList<AuxillaryOperator> chosenAuxOps, modalOps, predicateOps;
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
		
		modalOps = new ArrayList<AuxillaryOperator>(Arrays.asList(new AuxillaryOperator(AuxillaryOperatorType.POSSIBLE), 
				new AuxillaryOperator(AuxillaryOperatorType.NECESSARY),
				new AuxillaryOperator(AuxillaryOperatorType.NOTPOSSIBLE),
				new AuxillaryOperator(AuxillaryOperatorType.NOTNECESSARY)));
		predicateOps = new ArrayList<AuxillaryOperator>(Arrays.asList(new Quantifier(AuxillaryOperatorType.UNIVERSAL, null), 
				new Quantifier(AuxillaryOperatorType.EXISTENTIAL, null), 
				new Quantifier(AuxillaryOperatorType.NOTUNIVERSAL, null), 
				new Quantifier(AuxillaryOperatorType.NOTEXISTENTIAL, null)));
		AuxillaryOperator negationOp = new AuxillaryOperator(AuxillaryOperatorType.NEGATION);
		
		instanceVariables = new ArrayList<InstanceVariable>(Arrays.asList(new InstanceVariable(PlaceholderVariableType.X), 
				new InstanceVariable(PlaceholderVariableType.Y), 
				new InstanceVariable(PlaceholderVariableType.Z)
				));
		
		// ========= COMBO BOXES
		
		auxOpsChoice = new ComboBox<AuxillaryOperator>();
		auxOpsChoice.getItems().addAll(negationOp);
		if (logicType == LogicType.PREDICATE) {
			auxOpsChoice.getItems().addAll(predicateOps);
		}else if (logicType == LogicType.MODAL) {
			auxOpsChoice.getItems().addAll(modalOps);
		}
		auxOpsChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AuxillaryOperator> () {
			@Override
			public void changed(ObservableValue<? extends AuxillaryOperator> auxOp, AuxillaryOperator oldVal, AuxillaryOperator newVal) {
				if (newVal != null) {
					if (oldVal != newVal) {
						if (logicType == LogicType.PREDICATE) {
							if (newVal.getAuxOpType() == AuxillaryOperatorType.NEGATION) {
								applicableInstanceVariables.setDisable(true);
							}else {
								applicableInstanceVariables.setDisable(false);
							}
						}
						
					}
				}
				
				
			}
		});
		
		applicableInstanceVariables = new ComboBox<InstanceVariable>();
		if (logicType != LogicType.PREDICATE) {
			applicableInstanceVariables.setDisable(true);
		}else {
			applicableInstanceVariables.getItems().addAll(instanceVariables);
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
		mainRow.getChildren().addAll(auxOpsChoice, applicableInstanceVariables, addAuxOp, chosenAuxOpsField);
		
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
		}else if (logicType == LogicType.PREDICATE) {
			if (auxOpsChoice.getSelectionModel().getSelectedItem().getAuxOpType() != AuxillaryOperatorType.NEGATION) {
				if (applicableInstanceVariables.getSelectionModel().isEmpty()) {
					WarningModal warning = new WarningModal("Please choose an applicable instance variable");
				}else {
					appendQuantifier();
				}
			}else {
				appendRegularAuxOp();
			}
			
		}else {

			appendRegularAuxOp();
		}
		
	}
	
	public void appendRegularAuxOp() {
		AuxillaryOperator auxOp = auxOpsChoice.getSelectionModel().getSelectedItem().copy();
		chosenAuxOpsString = auxOp.toString() + chosenAuxOpsString;
		chosenAuxOps.add(0, auxOp);
		chosenAuxOpsField.setText(chosenAuxOpsString);
	}
	
	public void appendQuantifier() {
		Quantifier quantifier = (Quantifier) auxOpsChoice.getSelectionModel().getSelectedItem().copy();
		InstanceVariable chosenVar = applicableInstanceVariables.getSelectionModel().getSelectedItem();
		InstanceVariable instanceVar = chosenVar.copy();
		quantifier.setInstanceVariable(instanceVar);
		chosenAuxOpsString = quantifier.toString() + chosenAuxOpsString;
		chosenAuxOps.add(0, quantifier);
		chosenAuxOpsField.setText(chosenAuxOpsString);
		instanceVariables.remove(chosenVar);
		resetApplicableInstanceVars();
	
	}
	
	public void resetApplicableInstanceVars() {
		applicableInstanceVariables.getItems().clear();
		applicableInstanceVariables.getItems().addAll(instanceVariables);
	}
}
