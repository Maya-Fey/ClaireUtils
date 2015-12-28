package claire.util.standards.genetic;

public abstract class SCompetitor implements ICompetitor {
	
	private boolean dead = false;
	private int age = 0;

	public void setDead()
	{
		this.dead = true;
	}

	public boolean isDead()
	{
		return this.dead;
	}
	
	public void age()
	{
		age++;
	}
	
	public long getAge()
	{
		return this.age;
	}

}
