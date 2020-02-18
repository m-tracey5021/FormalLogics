
import java.util.*;
import java.util.Date;

import enums.LogicType;

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
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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

public class LogicEvaluatorWindow {
	
	private Stage window;
	private Scene scene;
	private GridPane grid;
	private Label logicTypeLabel, modelLabel;
	private VBox logicTypeVBox, lockButtonsVBox, propositionList;
	private HBox selectionHBox;
	private ScrollPane scroll;
	private Pane modelPane;
	private RadioButton classicalRb, predicateRb, modalRb;
	private ToggleGroup logicTypeGroup;
	private Button lockLogicType, unlockLogicType, createNewProp, generateModel, resetModel;
	private ArrayList<Proposition> propositions;
	private ArrayList<PropositionContainer> containers; // put the propositions in here so that can sort out Ids
	private LogicType logicType;
	
	public LogicEvaluatorWindow() {
		
		propositions = new ArrayList<Proposition>();
		
		window = new Stage();
		
		grid = new GridPane();
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setPadding(new Insets(10, 10, 10, 10));
		//grid.setGridLinesVisible(true);
		
		// ========== LABELS 
		
		logicTypeLabel = new Label("Select logic type");
		GridPane.setConstraints(logicTypeLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		modelLabel = new Label("Model");
		GridPane.setConstraints(modelLabel, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// ========== BUTTONS
		
		lockLogicType = new Button("Lock selection");
		lockLogicType.setPrefWidth(130);
		lockLogicType.setOnAction(e -> {
			lockLogicType.setDisable(true);
			classicalRb.setDisable(true);
			predicateRb.setDisable(true);
			modalRb.setDisable(true);
			
			unlockLogicType.setDisable(false);
			createNewProp.setDisable(false);
			
		});
		
		unlockLogicType = new Button("Unlock selection");
		unlockLogicType.setPrefWidth(130);
		unlockLogicType.setOnAction(e -> {
			WindowController controller = new WindowController();
			ConfirmationModal confirm = new ConfirmationModal("All current propositions will be lost", controller);
			if (controller.getBoolRet() == true) {
				lockLogicType.setDisable(false);
				classicalRb.setDisable(false);
				predicateRb.setDisable(false);
				modalRb.setDisable(false);
				
				unlockLogicType.setDisable(true);
				createNewProp.setDisable(true);
				generateModel.setDisable(true);
				
				propositions = new ArrayList<Proposition>();
				propositionList.getChildren().clear();
				
				modelPane = new Pane();
				modelPane.setPrefSize(400, 400);
				scroll.setContent(modelPane);
			}
			
			
		});
		unlockLogicType.setDisable(true);
		
		createNewProp = new Button("Create new proposition");
		createNewProp.setOnAction(e -> {
			WindowController controller = new WindowController();
			PropBuilder propBuilder = new PropBuilder(logicType, controller);
			if (controller.getStoredProp() != null) {
				Proposition returnedProp = controller.getStoredProp();
				System.out.println("");
				returnedProp = returnedProp.copy();
				propositions.add(returnedProp);
				Label newPropLabel = new Label(returnedProp.toString());
				propositionList.getChildren().addAll(newPropLabel);
				generateModel.setDisable(false);
			}
			
		});
		createNewProp.setDisable(true);
		GridPane.setConstraints(createNewProp, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		
		generateModel = new Button("Generate model");
		generateModel.setOnAction(e -> {
			if (logicType == LogicType.CLASSICAL) {
				modelPane.getChildren().clear();
				TernaryNode rootNode = new TernaryNode(propositions);
				rootNode.expand();
				rootNode.setupGeometry(200.0, 50.0, 30.0, 20.0, modelPane);
				scroll.setContent(modelPane);
				resetModel.setDisable(false);
				ArrayList<TernaryNode> emptyNodes = rootNode.initEmptyNodes();
				ArrayList<Proposition> allProps = rootNode.initAllPropositions();
				System.out.println(emptyNodes);
				System.out.println(allProps);
			}
		});
		generateModel.setDisable(true);
		GridPane.setConstraints(generateModel, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		
		resetModel = new Button("Reset model");
		resetModel.setOnAction(e -> {
			resetModel.setDisable(true);
			modelPane = new Pane();
			modelPane.setPrefSize(400, 400);
			scroll.setContent(modelPane);
		});
		resetModel.setDisable(true);
		GridPane.setConstraints(resetModel, 1, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// ========== RADIO BUTTONS
		
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
				classicalRb.setPrefHeight(20);
				classicalRb.setSelected(true);
				classicalRb.setToggleGroup(logicTypeGroup);
				
				predicateRb = new RadioButton("Predicate");
				predicateRb.setPrefHeight(20);
				predicateRb.setToggleGroup(logicTypeGroup);
				
				modalRb = new RadioButton("Modal");
				modalRb.setPrefHeight(20);
				modalRb.setToggleGroup(logicTypeGroup);// ========== RADIO BUTTONS
				
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
				classicalRb.setPrefHeight(20);
				classicalRb.setSelected(true);
				classicalRb.setToggleGroup(logicTypeGroup);
				
				predicateRb = new RadioButton("Predicate");
				predicateRb.setPrefHeight(20);
				predicateRb.setToggleGroup(logicTypeGroup);
				
				modalRb = new RadioButton("Modal");
				modalRb.setPrefHeight(20);
				modalRb.setToggleGroup(logicTypeGroup);
		
		// ========== H AND V BOXES
		
		logicTypeVBox = new VBox(7.5);
		logicTypeVBox.getChildren().addAll(classicalRb, predicateRb, modalRb);
		
		lockButtonsVBox = new VBox(10);
		lockButtonsVBox.setAlignment(Pos.CENTER);
		lockButtonsVBox.getChildren().addAll(lockLogicType, unlockLogicType);
		
		selectionHBox = new HBox(20);
		selectionHBox.setAlignment(Pos.CENTER);
		selectionHBox.getChildren().addAll(logicTypeVBox, lockButtonsVBox);
		GridPane.setConstraints(selectionHBox, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		
		propositionList = new VBox();
		propositionList.setPrefSize(400, 150);
		propositionList.setAlignment(Pos.CENTER);
		propositionList.getStyleClass().add("");
		GridPane.setConstraints(propositionList, 0, 5, 1, 1);
		
		// ========== PANES
		
		modelPane = new Pane();
		modelPane.setPrefSize(400, 400);

		
		
		
		
		
		
		scroll = new ScrollPane();
		scroll.setContent(modelPane);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setPannable(true);
		GridPane.setConstraints(scroll, 1, 1, 1, 6);
		
		// ========== SEPARATORS
		
		Separator selectionSep1 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(selectionSep1, 0, 1, 1, 1);
		
		Separator selectionSep2 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(selectionSep2, 0, 3, 1, 1);
		
		Separator selectionSep3 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(selectionSep3, 0, 6, 1, 1, HPos.CENTER, VPos.BOTTOM);
		
		
		// ==================================================
		

		grid.getChildren().addAll(logicTypeLabel, modelLabel, 
				selectionSep1, selectionSep2, selectionSep3,
				selectionHBox, propositionList, 
				createNewProp, generateModel, resetModel,  
				scroll);
		
		scene = new Scene(grid);
		window.setScene(scene);
		window.show();
		
	}
	
}
