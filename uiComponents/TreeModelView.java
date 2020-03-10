package uiComponents;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import visualModels.TernaryTreeModel;


public class TreeModelView extends Pane{
	//private Pane modelPane;
	private TernaryTreeModel treeModel;
	private double viewWidth, viewHeight;
	private boolean border;
	
	public TreeModelView(TernaryTreeModel treeModel, double paddingX, double paddingY, boolean border) {
		this.treeModel = treeModel;
		//this.getStyleClass().add("treeModelView");
		
		double modelWidth = treeModel.getTreeWidth();
		double modelHeight = treeModel.getTreeHeight();
		
		this.viewWidth = modelWidth + paddingX;
		this.viewHeight = modelHeight + paddingY;
		this.border = border;
		//double modelMidHeight = (modelHeight + 40) / 2;
		double modelMidWidth = modelWidth / 2;
		double modelMidHeight = modelHeight / 2;
		
		
		double translateX = modelMidWidth + (paddingX / 2);
		double translateY = (paddingY / 2) + (treeModel.getNodeModels().get(0).getNodeHeight() / 2);
		
		this.setPrefSize(viewWidth, viewHeight);
		/*
		if (modelWidth > 400) {
			this.setPrefWidth(modelWidth + 40);
			translateX = (modelWidth + 40) / 2;
		}else {
			translateX = 200;
		}
		if (modelHeight > 400) {
			this.setPrefHeight(modelHeight + 40);
			translateY = (modelHeight + 40) / 2;
		}else {
			translateY = 200;
		}
		*/
		
		treeModel.translateTree(translateX, translateY);

		treeModel.drawTree(this);
		double borderSpacingX = paddingX / 4;
		double borderSpacingY = paddingY / 4;
		//double rightBorderSpacing = modelWidth - leftBorderSpacing;
		//double bottomBorderSpacing = modelHeight - topBorderSpacing;
		if (border) {
			this.getChildren().add(new Line(borderSpacingX, borderSpacingY, modelWidth + (paddingX / 2) + borderSpacingX, borderSpacingY));
			this.getChildren().add(new Line(borderSpacingX, borderSpacingY, borderSpacingX, modelHeight + (paddingY / 2) + borderSpacingY));
			this.getChildren().add(new Line(borderSpacingX, modelHeight + (paddingY / 2) + borderSpacingY, modelWidth + (paddingX / 2) + borderSpacingX, modelHeight + (paddingY / 2) + borderSpacingY));
			this.getChildren().add(new Line(modelWidth + (paddingX / 2) + borderSpacingX, borderSpacingY, modelWidth + (paddingX / 2) + borderSpacingX, modelHeight + (paddingY / 2) + borderSpacingY));
		}
		
	}
	
	public TernaryTreeModel getTreeModel() {
		return this.treeModel;
	}
	
	public double getModelViewWidth() {
		return this.viewWidth;
	}
	
	public double getModelViewHeight() {
		return this.viewHeight;
	}
}
