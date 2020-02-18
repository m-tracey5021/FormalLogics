import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class TernaryTreeModel {
	private TernaryNode rootNode;
	private Point2D points;
	
	public TernaryTreeModel(TernaryNode rootNode) {
		this.rootNode = rootNode;
		setupGeometry(rootNode, 0.0, 0.0);
	}
	
	public void setupGeometry(TernaryNode node, double tmpX, double tmpY) {
		double x1 = tmpX;
		double y1 = tmpY;
		
		double x2 = tmpX;
		double y2 = tmpY;
		
		double x3 = tmpX;
		double y3 = tmpY;
		
		points.add(new Point2D(tmpX, tmpY));
		
		if (node.getLeftNode() != null) {
			setupGeometry(node.getLeftNode(), x1 - 10, y1 + 10);
		}
		if (node.getRightNode() != null) {
			setupGeometry(node.getRightNode(), x2 + 10, y2 + 10);
		}
		if (node.getCenterNode() != null) {
			setupGeometry(node.getCenterNode(), x2, y2 + 10);
		}
	}
	
	public void drawTernaryTree(Pane modelPane) {
		
	}

}
