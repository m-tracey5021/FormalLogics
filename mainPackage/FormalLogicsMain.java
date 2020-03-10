package mainPackage;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import uiComponents.LogicEvaluatorWindow;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.geometry.*;


public class FormalLogicsMain extends Application implements EventHandler<ActionEvent>{

	public static void main(String[] args) {
		launch(args);

		
    	
		

	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		LogicEvaluatorWindow logicEval = new LogicEvaluatorWindow();
		


	}
	
	@Override 
	public void handle(ActionEvent e) {
		
	}

}
