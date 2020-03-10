package uiComponents;

import visualModels.TernaryTreeModel;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

public class PopupTreeModel {
	private PopOver popOver;
	private TreeModelView treeModelView;

	public PopupTreeModel(TernaryTreeModel treeModel) {
		popOver = new PopOver();
		treeModelView = new TreeModelView(treeModel, 40, 40, true);
		popOver.setContentNode(treeModelView);
		popOver.setArrowLocation(ArrowLocation.LEFT_CENTER);
		popOver.setArrowSize(10);
		popOver.setCornerRadius(4);
		popOver.setTitle("Tree info");
		
		
	}
	
	// ===== GET
	
	public PopOver getPopOver() {
		return this.popOver;
	}
}
