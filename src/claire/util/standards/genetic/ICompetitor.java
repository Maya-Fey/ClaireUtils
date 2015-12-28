package claire.util.standards.genetic;

public interface ICompetitor {
	
	public long getScore();
	public ICompetitor getOffspring();

	public void setDead();
	public boolean isDead();
	
	public void age();
	public long getAge();
	
	public void print();
	
}
