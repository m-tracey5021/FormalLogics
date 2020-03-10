package math;

public class PointSlopeForm {
	private String form = "y - y1 = m(x - x1)";
	private double y, y1, m, x, x1;
	
	public PointSlopeForm(double y1, double m, double x1) {
		this.y1 = y1;
		this.m = m;
		this.x1 = x1;
		
	}
	
	public SlopeInterceptForm convertToSlopeInterceptForm() {
		double b = (m * x1) + y1;
		return new SlopeInterceptForm(m, b);
	}
	
	@Override
	public String toString() {
		return "y - " + y1 + " = " + m + "(x - " + x1 + ")";
	}
}
