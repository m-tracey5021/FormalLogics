package visualModels;

import org.controlsfx.control.PopOver;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import uiComponents.PopupTreeModel;
import universeComponents.World;

public class WorldModel {
	//private World world;
	private Point2D modelLocation;
	private TernaryTreeModel treeModel;
	private PopupTreeModel popupTree;
	private Label worldLabel;
	private double worldWidth, worldHeight;

	
	/*
	public WorldModel(World world, Point2D modelLocation, TernaryTreeModel treeModel) {
		//this.world = world;
		this.modelLocation = modelLocation;
		this.treeModel = treeModel;
		this.worldLabel = new Label(world.toString());
		calculateWorldSize();
		setupGeometry();
	}
	*/
	
	public WorldModel(Point2D modelLocation, Label worldLabel, TernaryTreeModel treeModel) {
		this.modelLocation = modelLocation;
		this.treeModel = treeModel;
		this.worldLabel = worldLabel;
		initWorldSize();
		centerLabel();
	}
	
	/*
	public WorldModel(World world, Point2D location) {
		//this.world = world;
		world.getTernaryTree().generateModel();
		this.modelLocation = location;
		this.treeModel = world.getTernaryTree().getTreeModel();
		this.worldLabel = new Label(world.toString());
		calculateWorldSize();
		setupGeometry();
	}
	
	*/
	
	
	
	// ========== GET
	
	//public World getWorld() {
	//	return this.world;
	//}

	public Point2D getLocation() {
		return this.modelLocation;
	}
	
	public TernaryTreeModel getTreeModel() {
		return this.treeModel;
	}
	
	public Label getLabel() {
		return this.worldLabel;
	}
	
	public double getWorldWidth() {
		return this.worldWidth;
	}
	public double getWorldHeight() {
		return this.worldHeight;
	}
	
	// ==============
	
	public void setLocation(Point2D point) {
		this.modelLocation = point;
	}
	
	public void initWorldSize() {
		Pane dummyPane = new Pane();
		Scene dummyScene = new Scene(dummyPane);
		dummyPane.getChildren().add(worldLabel);
		dummyPane.applyCss();
		dummyPane.layout();

		worldWidth = worldLabel.getWidth();
		worldHeight = worldLabel.getHeight();
	}
	
	public void centerLabel() {
		worldLabel.relocate(modelLocation.getX() - (worldWidth / 2), modelLocation.getY() - (worldHeight / 2));
	}
	
	public void makeLabelClickable(Stage rootWindow, Scene scene, Pane modelPane) {
		PopupTreeModel popupTree = new PopupTreeModel(treeModel);
		//Pane newPane = new Pane();
		//newPane.getChildren().add(new Label("POOP"));
		//newPane.setPrefSize(200, 200);
		//PopOver test = new PopOver();
		//test.setContentNode(newPane);
		worldLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				scene.setCursor(Cursor.HAND);
				popupTree.getPopOver().show(worldLabel);
				//test.show(worldLabel);
			}
		});
		worldLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				scene.setCursor(Cursor.DEFAULT);
				popupTree.getPopOver().hide();
				//test.hide();
			}
		});
		/*
		worldLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				PopupTreeModel popupTree = new PopupTreeModel(treeModel);
				popupTree.getPopOver().show(worldLabel);
			}
		});
		*/
			
		
	}
	
	public void drawWorld(Pane modelPane, boolean drawTree) {
		modelPane.getChildren().add(worldLabel);
		if (drawTree) {
			treeModel.drawTree(modelPane);

		}
		
	}
	
	public void translateWorld(double translateX, double translateY, boolean popupUniverse) {
		modelLocation = new Point2D(modelLocation.getX() + translateX, modelLocation.getY() + translateY);
		worldLabel.relocate(worldLabel.getLayoutX() + translateX, worldLabel.getLayoutY() + translateY);
		if (!popupUniverse) {
			treeModel.translateTree(translateX, translateY + worldHeight + 10);
		}
		
		

		
	}
}
