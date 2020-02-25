
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
			PropBuilder propBuilder = new PropBuilder(logicType, controller);
			if (controller.getStoredProp() != null) {
				Proposition returnedProp = controller.getStoredProp();
				System.out.println("");
				returnedProp = returnedProp.copy();
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
		});
		clearPropositions.setDisable(true);

		
		generateModel = new Button("Generate model");
		generateModel.setOnAction(e -> {
			if (logicType == LogicType.MODAL || logicType == LogicType.CLASSICAL) {
				modelPane.getChildren().clear();
				TernaryNode treeStart = setupInitialTree();
				RootNode rootNode = new RootNode(treeStart);
				Universe rootUniverse = new Universe(false, false, false, false);
				World rootWorld = new World(rootUniverse, rootNode);
				rootWorld.expandWithinUniverse();

				double xPos = 200.0;
				for(World world : rootUniverse.getWorlds()) {
					System.out.println(world);
					world.getRootNode().getTreeStart().setupGeometry(xPos, 50.0, 45.0, 20.0, modelPane);
					xPos  += 100;
				}
				scroll.setContent(modelPane);
				resetModel.setDisable(false);

			}else if (logicType == LogicType.PREDICATE) {
				
			}
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
		//modelPane.setPrefSize(400, 400);

		
		modelPaneCenter = new StackPane();
		modelPaneCenter.setPrefSize(400, 400);
		modelPaneCenter.getChildren().add(modelPane);
		
		
		
		
		scroll = new ScrollPane();
		scroll.setPrefSize(400, 400);
		scroll.setContent(modelPane);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
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
	
	public TernaryNode setupInitialTree() {
		ArrayList<TernaryNode> initialNodes = new ArrayList<TernaryNode>();
		TernaryNode nodeToChain = new TernaryNode(propositions.get(0));
		
		for (int i = 0; i < propositions.size() - 1; i ++) {
			nodeToChain.lane(propositions.get(i + 1), false);
			nodeToChain = nodeToChain.getCenterNode();
		}
		
		return nodeToChain.getRoot();
	}
	
	// ================ FILE IO
	
	public String serializePropositions() {
		//JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		//ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PropositionContainer container = new PropositionContainer(logicType, propositions);

		try {
			//JsonGenerator generator = factory.createGenerator(outputStream, JsonEncoding.UTF8);
			
			String propJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(container);
			
			/*
			int propCount = 0;
			generator.writeStartObject();
			generator.writeArrayFieldStart("propositions");
			for (Proposition propToSerialize : propositions) {
				String propJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(propToSerialize);
				generator.writeStringField("propsition " + propCount, propJson);
			}
			generator.writeEndArray();
			generator.writeEndObject();
			generator.close();
			
			String outputStr = new String(outputStream.toByteArray());
			System.out.println(outputStr);
			return outputStr;
			
			*/
			return propJson;
		}catch (IOException ex) {
			ex.printStackTrace();
			return "error in serialization";
		}
		
		
	}
	
	public PropositionContainer deserializePropositions(String jsonStr) { // WILL NOT WORK WITH RECURSIVE OBJECTS, MAKE YOUR OWN FUNCTION
		//ArrayList<Proposition> importedProps = new ArrayList<Proposition>();
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
					importedProps.add(importedProp);
				}
			}
			container = new PropositionContainer(returnedLogicType, importedProps);
			//importedProps = mapper.readValue(jsonStr, Proposition[].class);
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		
		
		/*
		PropositionDeserializer propDeserializer = new PropositionDeserializer();
		Proposition deserializedProp = propDeserializer.deserialize(jsonStr);
		
		
		
		importedProps.add(deserializedProp);
		
		*/
		return container;
		
	}
	
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
