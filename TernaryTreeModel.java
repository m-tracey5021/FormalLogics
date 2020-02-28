import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class TernaryTreeModel {
	private Pane modelPane;
	private RootNode rootNode;
	private ArrayList<TernaryNode> nodes;
	private ArrayList<Line> lines;
	private double startX, startY, horizontalSpacing, verticalSpacing, treeHeight, treeWidth;
	
	public TernaryTreeModel(Pane modelPane, RootNode rootNode, double startX, double startY) {
		this.modelPane = modelPane;
		this.rootNode = rootNode;
		this.startX = startX;
		this.startY = startY;
		this.nodes = rootNode.getAllNodes();
		this.lines = new ArrayList<Line>();
		initGeometry();

	}
	
	public double getTreeHeight() {
		return this.treeHeight;
	}
	
	public double getTreeWidth() {
		return this.treeWidth;
	}
	
	public void initGeometry() {
		rootNode.getTreeStart().setupNodeModels();
		initSpacing();
		rootNode.getTreeStart().setupGeometry(startX, startY, horizontalSpacing, verticalSpacing, lines);
		initTreeSize();
		
	}
	
	
	public void initSpacing() {
		double maxWidth = 0.0;
		for (TernaryNode node : nodes) {
			if (node.getNodeModel().getNodeWidth() >= maxWidth) {
				maxWidth = node.getNodeModel().getNodeWidth();
			}
		}
		this.horizontalSpacing = (maxWidth / 2) + 10;
		this.verticalSpacing = 20.0; //(maxWidth / 8);
	}
	
	public void initTreeSize() {
		
		// ====== NOT TESTED
		/*  
		int layerCount = -1;
		rootNode.getTreeStart().getTreeLayers(layerCount);
		
		TernaryNodeModel leftMostNodeModel = rootNode.getLeftMostNode().getNodeModel();
		TernaryNodeModel rightMostNodeModel = rootNode.getRightMostNode().getNodeModel();
		double maxX = rightMostNodeModel.getLocation().getX() + rightMostNodeModel.getNodeWidth() / 2;
		double minX = leftMostNodeModel.getLocation().getX() - leftMostNodeModel.getNodeWidth() / 2;
		
		treeHeight = layerCount * verticalSpacing;
		treeWidth = maxX - minX;
		
		*/
		
		
		
		
		
		
		
		double minX = rootNode.getTreeStart().getNodeModel().getLocation().getX() - rootNode.getTreeStart().getNodeModel().getNodeWidth() / 2;
		double maxX = rootNode.getTreeStart().getNodeModel().getLocation().getX() + rootNode.getTreeStart().getNodeModel().getNodeWidth() / 2;
		double minY = rootNode.getTreeStart().getNodeModel().getLocation().getY() - rootNode.getTreeStart().getNodeModel().getNodeHeight() / 2;
		double maxY = rootNode.getTreeStart().getNodeModel().getLocation().getY() + rootNode.getTreeStart().getNodeModel().getNodeHeight() / 2;
		
		for (TernaryNode node : nodes) {
			TernaryNodeModel model = node.getNodeModel();
			if (model.getLocation().getX() + (model.getNodeWidth() / 2) > maxX) {
				maxX = model.getLocation().getX() + (model.getNodeWidth() / 2);
			}else if (model.getLocation().getX() - (model.getNodeWidth() / 2) < minX) {
				minX = model.getLocation().getX() - (model.getNodeWidth() / 2);
			}
			if (model.getLocation().getY() + (model.getNodeHeight() / 2) > maxY) {
				maxY = model.getLocation().getY() + (model.getNodeHeight() / 2);
			}else if (model.getLocation().getY() - (model.getNodeHeight() / 2) < minY){
				minY = model.getLocation().getY() - (model.getNodeHeight() / 2);
			}
		}
		treeHeight = maxY - minY;
		treeWidth = maxX - minX;
	}
	
	public void relocateTree() {
		
	}

	
	public void drawLabels() {
		for (TernaryNode node : nodes) {
			modelPane.getChildren().add(node.getNodeModel().getPropositionLabel());
			
		}
	}
	
	public void drawLines() {
		for (Line line : lines) {
			modelPane.getChildren().add(line);
		}
	}
	
	
	
	public void drawTree() {
		drawLabels();
		drawLines();
	}
	
	

}
