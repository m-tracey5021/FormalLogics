package uiComponents;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import visualModels.TernaryTreeModel;


public class TreeModelView extends Pane{
	private TernaryTreeModel treeModel;
	private double viewWidth, viewHeight;
	private boolean border;
	
	public TreeModelView(TernaryTreeModel treeModel, double paddingX, double paddingY, boolean border) {
		this.treeModel = treeModel;

		
		double modelWidth = treeModel.getTreeWidth();
		double modelHeight = treeModel.getTreeHeight();
		
		this.viewWidth = modelWidth + paddingX;
		this.viewHeight = modelHeight + paddingY;
		this.border = border;

		double modelMidWidth = modelWidth / 2;
		double modelMidHeight = modelHeight / 2;
		
		
		double translateX = modelMidWidth + (paddingX / 2);
		double translateY = (paddingY / 2) + (treeModel.getNodeModels().get(0).getNodeHeight() / 2);
		
		this.setPrefSize(viewWidth, viewHeight);

		treeModel.translateTree(translateX, translateY);

		treeModel.drawTree(this);
		double borderSpacingX = paddingX / 4;
		double borderSpacingY = paddingY / 4;

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
