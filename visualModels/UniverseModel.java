package visualModels;

import java.awt.Point;
import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import universeComponents.Relation;
import universeComponents.Universe;
import universeComponents.World;

public class UniverseModel {
	private ArrayList<WorldModel> worldModels;
	private ArrayList<RelationModel> relationModels;
	private boolean popupUniverse;
	double universeHeight, universeWidth;

	
	public UniverseModel(ArrayList<WorldModel> worldModels, ArrayList<RelationModel> relationModels, boolean popupUniverse) {
		this.worldModels = worldModels;
		this.relationModels = relationModels;
		this.popupUniverse = popupUniverse;
		initUniverseSize();
	}
	
	// ======== GET
	
	public double getUniverseWidth() {
		return this.universeWidth;
	}
	
	public double getUniverseHeight() {
		return this.universeHeight;
	}
	
	// ========== SET
	
	

	
	
	// ========== 

	
	public void initUniverseSize() {
		double minX = worldModels.get(0).getLocation().getX() - worldModels.get(0).getWorldWidth() / 2;
		double maxX = worldModels.get(0).getLocation().getX() + worldModels.get(0).getWorldWidth() / 2;
		double minY = worldModels.get(0).getLocation().getY() - worldModels.get(0).getWorldHeight() / 2;
		double maxY = worldModels.get(0).getLocation().getY() + worldModels.get(0).getWorldHeight() / 2;
		for (WorldModel worldModel : worldModels) {
			if (worldModel.getLocation().getX() + (worldModel.getWorldWidth() / 2) > maxX) {
				maxX = worldModel.getLocation().getX() + (worldModel.getWorldWidth() / 2);
			}else if (worldModel.getLocation().getX() - (worldModel.getWorldWidth() / 2) < minX) {
				minX = worldModel.getLocation().getX() - (worldModel.getWorldWidth() / 2);
			}
			if (worldModel.getLocation().getY() + (worldModel.getWorldHeight() / 2) > maxY) {
				maxY = worldModel.getLocation().getY() + (worldModel.getWorldHeight() / 2);
			}else if (worldModel.getLocation().getY() - (worldModel.getWorldHeight() / 2) < minY){
				minY = worldModel.getLocation().getY() - (worldModel.getWorldHeight() / 2);
			}
		}
		universeHeight = maxY - minY;
		universeWidth = maxX - minX;
	}
	
	public void relocateUniverse(double translateX, double translateY) {
		for (WorldModel worldModel : worldModels) {
			worldModel.translateWorld(translateX, translateY, popupUniverse);
		}
		for (RelationModel relationModel : relationModels) {
			relationModel.relocateRelation(translateX, translateY);
		}
	}
	
	public void drawUniverse(Stage rootWindow, Scene scene, Pane modelPane) {
		for (WorldModel worldModel : worldModels) {
			if (popupUniverse) {
				worldModel.drawWorld(modelPane, false);
				worldModel.makeLabelClickable(rootWindow, scene, modelPane);
			}else {
				worldModel.drawWorld(modelPane, true);
				
			}
			
		}
		for (RelationModel relationModel : relationModels) {
			relationModel.drawRelation(modelPane);
		}
	}
}
