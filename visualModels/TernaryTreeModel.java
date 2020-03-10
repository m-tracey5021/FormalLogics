package visualModels;

import java.util.ArrayList;
import java.util.HashMap;
import treeComponents.*;


import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class TernaryTreeModel {
	private ArrayList<TernaryNodeModel> nodeModels;
	private ArrayList<Line> lines;
	private double startX, startY, horizontalSpacing, verticalSpacing, treeHeight, treeWidth;
	
	public TernaryTreeModel(ArrayList<TernaryNodeModel> nodeModels, ArrayList<Line> lines) {
		this.nodeModels = nodeModels;
		this.lines = lines;
		initTreeSize();
	}
	
	// =========== GET
	
	public ArrayList<TernaryNodeModel> getNodeModels(){
		return this.nodeModels;
	}
	
	public ArrayList<Line> getLines(){
		return this.lines;
	}
	
	public double getStartX() {
		return this.startX;
	}
	
	public double getStartY() {
		return this.startY;
	}
	
	public double getHorizontalSpacing() {
		return this.horizontalSpacing;
	}
	
	public double getVerticalSpacing() {
		return this.verticalSpacing;
	}
	
	public double getTreeHeight() {
		return this.treeHeight;
	}
	
	public double getTreeWidth() {
		return this.treeWidth;
	}
	
	// ===========
	
	public void initTreeSize() {
		
		double minX = nodeModels.get(0).getLocation().getX() - nodeModels.get(0).getNodeWidth() / 2;
		double maxX = nodeModels.get(0).getLocation().getX() + nodeModels.get(0).getNodeWidth() / 2;
		double minY = nodeModels.get(0).getLocation().getY() - nodeModels.get(0).getNodeHeight() / 2;
		double maxY = nodeModels.get(0).getLocation().getY() + nodeModels.get(0).getNodeHeight() / 2;
		
		for (TernaryNodeModel nodeModel : nodeModels) {

			if (nodeModel.getLocation().getX() + (nodeModel.getNodeWidth() / 2) > maxX) {
				maxX = nodeModel.getLocation().getX() + (nodeModel.getNodeWidth() / 2);
			}else if (nodeModel.getLocation().getX() - (nodeModel.getNodeWidth() / 2) < minX) {
				minX = nodeModel.getLocation().getX() - (nodeModel.getNodeWidth() / 2);
			}
			if (nodeModel.getLocation().getY() + (nodeModel.getNodeHeight() / 2) > maxY) {
				maxY = nodeModel.getLocation().getY() + (nodeModel.getNodeHeight() / 2);
			}else if (nodeModel.getLocation().getY() - (nodeModel.getNodeHeight() / 2) < minY){
				minY = nodeModel.getLocation().getY() - (nodeModel.getNodeHeight() / 2);
			}
		}
		treeHeight = maxY - minY;
		treeWidth = maxX - minX;
	}
	
	public void translateTree(double translateX, double translateY) {
		for (TernaryNodeModel nodeModel : nodeModels) {
			nodeModel.translateNodeModel(translateX, translateY);
		}
		for (Line line : lines) {
			line.setEndX(line.getEndX() + translateX);
			line.setEndY(line.getEndY() + translateY);
			line.setStartX(line.getStartX() + translateX);
			line.setStartY(line.getStartY() + translateY);
			
			
		}
		startX += translateX;
		startY += translateY;
	}

	
	public void drawLabels(Pane modelPane) {
		for (TernaryNodeModel nodeModel : nodeModels) {
			modelPane.getChildren().add(nodeModel.getPropositionLabel());
			
		}
	}
	
	public void drawLines(Pane modelPane) {
		for (Line line : lines) {
			modelPane.getChildren().add(line);
		}
	}
	
	
	
	public void drawTree(Pane modelPane) {
		drawLabels(modelPane);
		drawLines(modelPane);
	}
	
	

}
