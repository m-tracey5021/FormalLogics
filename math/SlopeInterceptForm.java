package math;

public class SlopeInterceptForm {
	private String form = "y = mx + b";
	private double y, m, x, b;
	
	public SlopeInterceptForm(double m, double b) {
		this.m = m;
		this.b = b;
	}
	
	@Override
	public String toString() {
		return "y = " + m + "x + " + b;
	}
}
