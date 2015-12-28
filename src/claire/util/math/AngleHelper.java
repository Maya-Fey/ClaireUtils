package claire.util.math;

public class AngleHelper {
	
	public static double cos(double d)
	{
		return Math.cos(Math.toRadians(d));
	}
	
	public static double sin(double d)
	{
		return Math.sin(Math.toRadians(d));
	}
	
	public static double tan(double d)
	{
		return Math.tan(Math.toRadians(d));
	}
	
	public static double acos(double d)
	{
		return Math.toDegrees(Math.acos(d));
	}
	
	public static double asin(double d)
	{
		return Math.asin(d);
	}
	
	public static double atan(double d)
	{
		return Math.atan(d);
	}

	public static double getSideFromHeightWidth(double height, double width)
	{
		height = MathHelper.absolute(height);
		width = MathHelper.absolute(width);
		height *= height;
		width *= width;
		return Math.sqrt(height + width);
	}
	
}
