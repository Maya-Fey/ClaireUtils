package claire.util.vector._2D;

import claire.util.math.MathHelper;
import claire.util.memory.array.CArray;

public class Shape2D {
	
	protected final CArray<Vector2D> points;
	
	public Shape2D(Vector2D ... points) {
		this.points = new CArray<Vector2D>(points);
	}
	
	public double getHeight() 
	{
		double[] vals = new double[points.size()];
		for(int i = 0; i < points.size(); i++)
			vals[i] = points.get(i).getX();
		return MathHelper.ceiling(vals) + MathHelper.floor(vals);
	}

}
