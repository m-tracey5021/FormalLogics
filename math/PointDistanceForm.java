package math;

import javafx.geometry.Point2D;

public class PointDistanceForm {
	private String form = "sqrt((x1 - x2)^2 + (y1 - y2)^2) = d";
	private double x1, x2, y1, y2, d;
	
	public PointDistanceForm(double x1, double x2, double y1, double y2, double d) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.d = d;
	}
	/*
	public double solveForP1AndLength() {
					
	}
	
	public Point2D solveForLength(double AC, Point2D A, double BC, Point2D B) { // C is unknown point
		double sqrdAC = AC * AC;
		double sqrdBC = BC * BC;
		
		
		
		double acMinusBc = sqrdAC - sqrdBC;
		
		
		
		
	}
	*/
}
