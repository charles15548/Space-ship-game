package math;

public class Vector2d {

	private double x,y;
	
	public Vector2d(double x, double y)
	{
		this.x=x;
		this.y=y;
	}

	public Vector2d()
	{
		x=0;
		y=0;
	}
	
	public Vector2d add(Vector2d v) 
	{
		return new Vector2d(x+v.getX(),y+v.getY());
	}
	public Vector2d subtrac(Vector2d v) 
	{
		return new Vector2d(x-v.getX(),y-v.getY());
	}
	
	public Vector2d scale(double value)
	{
		return new Vector2d(x*value,y*value);
	}
	
	public Vector2d limit(double value)
	{
		if( getMagnitude()> value)
		{
			return this.normalize().scale(value);
		}
		return this;
	}
	public Vector2d normalize()
	{
		double magnitude = getMagnitude();
		
		return new Vector2d(x/magnitude,y/magnitude);
	}

	public double getMagnitude() {
		return Math.sqrt(x*x+y*y);
	}
	
	public Vector2d setDireccion (double angle) {
		double magnitude = getMagnitude();
		
		return new Vector2d(Math.cos(angle)*magnitude,
				Math.sin(angle)*magnitude);
	}
	
	public double getAngle() {
		return Math.asin(y/getMagnitude());
		}
	
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
