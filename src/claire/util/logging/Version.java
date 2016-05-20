package claire.util.logging;

public class Version {
	
	public int TREE;
	public int MAJOR;
	public int MINOR;
	public int BUILD;
	
	public String TITLE;
	
	public Version(int tree, int major, int minor, int build, String title)
	{
		this.TREE = tree;
		this.MAJOR = major;
		this.MINOR = minor;
		this.BUILD = build;
		this.TITLE = title;
	}
	
	public String toString()
	{
		return TREE + "." + MAJOR + "." + MINOR + "." + BUILD + "-" + TITLE;
	}
	
	public long getID()
	{
		return  (BUILD & 0xFFFFFFFFL) +
			   ((MINOR & 0xFFFFFFFFL) * 1000) + 
			   ((MAJOR & 0xFFFFFFFFL) * 1000 * 1000) +
			   ((TREE  & 0xFFFFFFFFL) * 1000 * 1000 * 1000);
	}

}
