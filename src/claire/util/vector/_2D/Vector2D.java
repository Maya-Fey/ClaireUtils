package claire.util.vector._2D;

import claire.util.math.AngleHelper;
import claire.util.math.MathHelper;
import claire.util.standards.IDeepClonable;

public class Vector2D implements IDeepClonable<Vector2D> {
	
	public static final Vector2D CENTER = new Vector2D(0, 0);

	protected double X;
	protected double Y;
	
	public Vector2D()
	{
		
	}
	
	public Vector2D(double X, double Y)
	{
		this.Y = Y;
		this.X = X;
	}

	public double getX()
	{
		return this.X;
	}
	
	public double getY()
	{
		return this.Y;
	}
	
	public Vector2D setX(double X)
	{
		this.X = X;
		return this;
	}
	
	public Vector2D setY(double Y)
	{
		this.Y = Y;
		return this;
	}
	
	public Vector2D shiftY(double shift)
	{
		this.Y += shift;
		return this;
	}
	
	public Vector2D shiftX(double shift)
	{
		this.X += shift;
		return this;
	}
	
	public double distanceBetween(Vector2D v)
	{
		Vector2D n = this.clone();
		n.subtract(v);
		return AngleHelper.getSideFromHeightWidth(n.Y, n.X);
	}
	
	public void moveFromPoint(Vector2D point, double distance)
	{
		this.subtract(point);
		double curDist = AngleHelper.getSideFromHeightWidth(this.Y, this.X);
		curDist += distance;
		X = curDist;
		Y = 0.0;
		
	}
	
	public Vector2D rotateAroundPoint(Vector2D point, double degrees)
	{
		this.subtract(point);
		double X = this.X * AngleHelper.cos(degrees) - (this.Y * AngleHelper.sin(degrees));
		double Y = (this.X * AngleHelper.sin(degrees)) + this.Y * AngleHelper.cos(degrees);
		//if(degrees > 180) Y *= -1;
		//if(degrees > 90 && degrees < 270) X *= -1;
		Vector2D n = new Vector2D(X, Y).add(point);
		this.set(n);
		return this;
	}
	
	protected void set(Vector2D v)
	{
		X = v.X;
		Y = v.Y;
	}
	
	public Vector2D add(Vector2D v)
	{
		this.X += v.X;
		this.Y += v.Y;
		return this;
	}
	
	public Vector2D subtract(Vector2D v)
	{
		this.X -= v.X;
		this.Y -= v.Y;
		return this;
	}
	
	public Vector2D clone()
	{
		return new Vector2D(this.X,this.Y);
	}

	public boolean equals(Vector2D vector)
	{
		double diffX = MathHelper.absolute(this.X) - MathHelper.absolute(vector.X);
		double diffY = MathHelper.absolute(this.Y) - MathHelper.absolute(vector.Y);
		if(diffX < 0.001 && diffY < 0.001) return true;
		return false;
	}
	
	public String toString()
	{
		return "(X:" + this.X + ",Y: " + this.Y + ")";
	}

	public Vector2D createDeepClone()
	{
		return this.clone();
	}

}
