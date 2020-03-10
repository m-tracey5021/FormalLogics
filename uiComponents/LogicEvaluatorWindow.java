package uiComponents;

import java.util.*;
import com.google.gson.*;
import com.fasterxml.*;
import com.fasterxml.jackson.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;

import enums.LogicType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import propositions.Proposition;
import propositions.PropositionContainer;
import propositions.PropositionDeserializer;
import treeComponents.TernaryTree;
import treeComponents.TernaryNode;
import universeComponents.Relation;
import universeComponents.Universe;
import universeComponents.World;
import visualModels.TernaryTreeModel;
import visualModels.UniverseModel;
import visualModels.WorldModel;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.geometry.*;

public class LogicEvaluatorWindow {
	
	private Stage window;
	private Scene scene;
	private GridPane grid;
	private Label logicTypeLabel, modelLabel;
	private VBox logicTypeVBox, lockButtonsVBox, propositionList;
	private HBox selectionHBox, importOrCreatePropositions, saveOrClearPropositions;
	private ScrollPane scroll;
	private Pane modelPane;
	private StackPane modelPaneCenter;
	private RadioButton classicalRb, predicateRb, modalRb;
	private ToggleGroup logicTypeGroup;
	private Button lockLogicType, unlockLogicType, createNewProp, importPropositions, savePropositions, clearPropositions, generateModel, resetModel;
	private ArrayList<Proposition> propositions;
	private ArrayList<PropositionContainer> containers; // put the propositions in here so that can sort out Ids
	private LogicType logicType;
	private WindowController controller;


	
	public LogicEvaluatorWindow() {
		
		controller = new WindowController();
		
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
			importPropositions.setDisable(false);
			
		});
		
		unlockLogicType = new Button("Unlock selection");
		unlockLogicType.setPrefWidth(130);
		unlockLogicType.setOnAction(e -> {

			ConfirmationModal confirm = new ConfirmationModal("All current propositions will be lost", controller);
			if (controller.getBoolRet() == true) {
				lockLogicType.setDisable(false);
				classicalRb.setDisable(false);
				predicateRb.setDisable(false);
				modalRb.setDisable(false);
				
				unlockLogicType.setDisable(true);
				createNewProp.setDisable(true);
				importPropositions.setDisable(true);
				generateModel.setDisable(true);
				savePropositions.setDisable(true);
				clearPropositions.setDisable(true);
				
				propositions = new ArrayList<Proposition>();
				propositionList.getChildren().clear();
				
				modelPane = new Pane();
				modelPane.setPrefSize(400, 400);
				scroll.setContent(modelPane);
			}
			
			
		});
		unlockLogicType.setDisable(true);
		
		createNewProp = new Button("Create new proposition");
		createNewProp.setPrefWidth(180);
		createNewProp.setOnAction(e -> {
			WindowController controller = new WindowController();
			PropositionBuilder propBuilder = new PropositionBuilder(logicType, controller);
			if (controller.getStoredProp() != null) {
				Proposition returnedProp = controller.getStoredProp();
				System.out.println("");
				//returnedProp = returnedProp.copy();
				//returnedProp.startAssignment();
				propositions.add(returnedProp);
				Label newPropLabel = new Label(returnedProp.toString());
				propositionList.getChildren().addAll(newPropLabel);
				generateModel.setDisable(false);
				savePropositions.setDisable(false);
				clearPropositions.setDisable(false);
			}
			
		});
		createNewProp.setDisable(true);

		
		importPropositions = new Button("Import propositions");
		importPropositions.setPrefWidth(180);
		importPropositions.setOnAction(e -> {
			String jsonImported = importFile();
			PropositionContainer importedPropsContainer = deserializePropositions(jsonImported);
			if (importedPropsContainer != null) {
				LogicType returnedLogicType = importedPropsContainer.getLogicType();
				if (returnedLogicType != logicType) {
					ConfirmationModal confirm = new ConfirmationModal("Incompatible logic types, current selection will be overridden", controller);
					if (controller.getBoolRet() == true) {
						if (returnedLogicType == LogicType.PREDICATE) {
							predicateRb.setSelected(true);
						}else if (returnedLogicType == LogicType.MODAL) {
							modalRb.setSelected(true);
						}else {
							classicalRb.setSelected(true);
						}
						addImportedProps(importedPropsContainer);

					}else {
						
					}
				}else {
					addImportedProps(importedPropsContainer);
				}
			}
			
			
		});
		importPropositions.setDisable(true);
		
		savePropositions = new Button("Save propositions");
		savePropositions.setPrefWidth(180);
		savePropositions.setOnAction(e -> {
			String jsonToSave = serializePropositions();
			saveToFile(jsonToSave);
			
		});
		savePropositions.setDisable(true);
		
		
		clearPropositions = new Button("Clear propositions");
		clearPropositions.setPrefWidth(180);
		clearPropositions.setOnAction(e -> {
			propositions = new ArrayList<Proposition>();
			propositionList.getChildren().clear();
			generateModel.setDisable(true);
			savePropositions.setDisable(true);
			clearPropositions.setDisable(true);
			resetModel.setDisable(true);
			modelPane = new Pane();
			scroll.setContent(modelPane);
		});
		clearPropositions.setDisable(true);

		
		generateModel = new Button("Generate model");
		generateModel.setOnAction(e -> {
			generateModel();
		});
		generateModel.setDisable(true);
		GridPane.setConstraints(generateModel, 0, 8, 1, 1, HPos.CENTER, VPos.CENTER);
		
		resetModel = new Button("Reset model");
		resetModel.setOnAction(e -> {
			resetModel.setDisable(true);
			modelPane = new Pane();
			scroll.setContent(modelPane);
		});
		resetModel.setDisable(true);
		GridPane.setConstraints(resetModel, 1, 8, 1, 1, HPos.CENTER, VPos.CENTER);
		
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
		
		
		importOrCreatePropositions = new HBox(20);
		importOrCreatePropositions.setAlignment(Pos.CENTER);
		importOrCreatePropositions.getChildren().addAll(importPropositions, createNewProp);
		GridPane.setConstraints(importOrCreatePropositions, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		
		saveOrClearPropositions = new HBox(20);
		saveOrClearPropositions.setAlignment(Pos.CENTER);
		saveOrClearPropositions.getChildren().addAll(savePropositions, clearPropositions);
		GridPane.setConstraints(saveOrClearPropositions, 0, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// ========== PANES
		
		modelPane = new Pane();
		modelPane.setPrefSize(4000, 4000);

		
		scroll = new ScrollPane();
		scroll.setPrefSize(400, 400);
		scroll.setContent(modelPane);
		scroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setPannable(true);
		GridPane.setConstraints(scroll, 1, 1, 1, 7);
		
		// ========== SEPARATORS
		
		Separator selectionSep1 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(selectionSep1, 0, 1, 1, 1);
		
		Separator selectionSep2 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(selectionSep2, 0, 3, 1, 1);
		
		Separator selectionSep3 = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(selectionSep3, 0, 7, 1, 1, HPos.CENTER, VPos.BOTTOM);
		
		
		// ==================================================
		

		grid.getChildren().addAll(logicTypeLabel, modelLabel, 
				selectionSep1, selectionSep2, selectionSep3,
				selectionHBox, propositionList, importOrCreatePropositions, saveOrClearPropositions, 
				generateModel, resetModel,  
				scroll);
		
		scene = new Scene(grid);
		window.setScene(scene);
		window.show();
		
	}
	
	// ============== MODEL GENERATION
	
	public void generateModel() {
		if (logicType == LogicType.CLASSICAL) {
			modelPane.getChildren().clear();
			TernaryNode treeStart = setupInitialTree();
			TernaryTree rootNode = new TernaryTree(treeStart);
			
			rootNode.expandWithinWorld(null);
			
			rootNode.generateTreeModel();
			TernaryTreeModel treeModel = rootNode.getTreeModel();
			
			double widthOfTree = treeModel.getTreeWidth();
			double heightOfTree = treeModel.getTreeHeight();
			
			TreeModelView treeModelView = new TreeModelView(treeModel, 50, 50, false);

			// STILL NEED TO CHANGE VIEWPORT
			
			
			
			scroll.setContent(treeModelView);
			scroll.setVmax(treeModelView.getPrefHeight());
			scroll.setHmax(treeModelView.getPrefWidth());

	        scroll.setVvalue(treeModelView.getPrefHeight() / 2);
	        scroll.setHvalue(treeModelView.getPrefWidth() / 2);
			resetModel.setDisable(false);

		}else if (logicType == LogicType.MODAL) {
			modelPane.getChildren().clear();
			TernaryNode treeStart = setupInitialTree();
			TernaryTree rootNode = new TernaryTree(treeStart);
			Universe rootUniverse = new Universe(false, false, false, false, true);
			World rootWorld = new World(rootUniverse, rootNode);
			rootWorld.expandWithinUniverse();
			
			
			// STILL NEED TO CHANGE VIEWPORT
			
			rootUniverse.generateUniverseModel();
			UniverseModel universeModel = rootUniverse.getUniverseModel();
			double modelWidth = universeModel.getUniverseWidth();
			double modelHeight = universeModel.getUniverseHeight();
			
			double translateX;
			double translateY;
			
			if (modelWidth > 400) {
				modelPane.setPrefWidth(modelWidth + 40);
				translateX = (modelWidth + 40) / 2;
			}else {
				translateX = 200;
			}
			if (modelHeight > 400) {
				modelPane.setPrefHeight(modelHeight + 40);
				translateY = (modelHeight + 40) / 2;
			}else {
				translateY = 200;
			}

			
			universeModel.relocateUniverse(translateX, translateY);
			universeModel.drawUniverse(window, scene, modelPane);
			
			scroll.setContent(modelPane);
			resetModel.setDisable(false);
		}else if (logicType == LogicType.PREDICATE) {
			modelPane.getChildren().clear();
			TernaryNode treeStart = setupInitialTree();
			TernaryTree rootNode = new TernaryTree(treeStart);
			Universe rootUniverse = new Universe(false, false, false, false, true);
			World rootWorld = new World(rootUniverse, rootNode);
			rootWorld.expandWithinUniverse();
			
			//rootNode.expandWithinWorld(null);
			
			rootNode.generateTreeModel();
			TernaryTreeModel treeModel = rootNode.getTreeModel();
			
			double widthOfTree = treeModel.getTreeWidth();
			double heightOfTree = treeModel.getTreeHeight();
			
			TreeModelView treeModelView = new TreeModelView(treeModel, 50, 50, false);

			// STILL NEED TO CHANGE VIEWPORT
			
			
			
			scroll.setContent(treeModelView);
			scroll.setVmax(treeModelView.getPrefHeight());
			scroll.setHmax(treeModelView.getPrefWidth());

	        scroll.setVvalue(treeModelView.getPrefHeight() / 2);
	        scroll.setHvalue(treeModelView.getPrefWidth() / 2);
			resetModel.setDisable(false);
		}
	}
	
	public TernaryNode setupInitialTree() {
		ArrayList<TernaryNode> initialNodes = new ArrayList<TernaryNode>();
		TernaryNode nodeToChain = new TernaryNode(propositions.get(0));
		
		for (int i = 0; i < propositions.size() - 1; i ++) {
			nodeToChain.laneNode(propositions.get(i + 1));
			nodeToChain = nodeToChain.getCenterNode();
		}
		
		return nodeToChain.getTreeStart();
	}
	
	
	// ================ SERIALIZATION
	
	
	public String serializePropositions() {

		ObjectMapper mapper = new ObjectMapper();
		PropositionContainer container = new PropositionContainer(logicType, propositions);

		try {
			String propJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(container);

			return propJson;
		}catch (IOException ex) {
			ex.printStackTrace();
			return "error in serialization";
		}
		
		
	}
	
	public PropositionContainer deserializePropositions(String jsonStr) { 
		ObjectMapper mapper = new ObjectMapper();
		PropositionDeserializer propDeserializer = new PropositionDeserializer();
		
		PropositionContainer container = null;
		ArrayList<Proposition> importedProps = new ArrayList<Proposition>();
		
		try {
			JsonNode rootNode = mapper.readTree(jsonStr);
			String returnedLogicTypeStr = rootNode.get("logicType").toString();
			String trimmed = returnedLogicTypeStr.substring(1, returnedLogicTypeStr.length() - 1);
			LogicType returnedLogicType = getValueOfLogicType(trimmed);
			JsonNode propsNode = rootNode.get("propositions");
			if (propsNode.isArray()) {
				ArrayNode propsArr = (ArrayNode) propsNode;
				for (int i = 0; i < propsArr.size(); i ++) {
					JsonNode propositionObject = propsArr.get(i);
					String propositionString = propositionObject.toString();
					Proposition importedProp = propDeserializer.deserialize(propositionString);
					importedProp.assignVariablesForAll();
					importedProps.add(importedProp);
				}
			}
			container = new PropositionContainer(returnedLogicType, importedProps);
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		
		
		return container;
		
	}
	
	
	// ============ FILE IO
	
	public String importFile() {
		
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(window);
		if (file != null) {
			try {
				String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
				return content;
			}catch(IOException ex) {
				ex.printStackTrace();
				return "";
			}
		}else {
			return "";
		}
		
		
	}
	
	public void saveToFile(String content) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(window);
		
		if (file != null) {
			try {
				FileWriter writer = new FileWriter(file);
				writer.write(content);
				writer.close();
				System.out.println("Wrote to file: " + file.getAbsolutePath());
			}catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
	}
	
	// ============ MISC
	
	public void addImportedProps(PropositionContainer importedPropsContainer) {
		propositions = importedPropsContainer.getPropositions();
		propositionList.getChildren().clear();
		for (Proposition importedProp : importedPropsContainer.getPropositions()) {
			propositionList.getChildren().add(new Label(importedProp.toString()));
		}
		generateModel.setDisable(false);
		savePropositions.setDisable(false);
		clearPropositions.setDisable(false);
	}
	
	public LogicType getValueOfLogicType(String logicTypeStr) {
		if (logicTypeStr.equals("PREDICATE")) {
			return LogicType.PREDICATE;
		}else if (logicTypeStr.equals("MODAL")) {
			return LogicType.MODAL;
		}else {
			return LogicType.CLASSICAL;
		}
	}
	
	
	
}
