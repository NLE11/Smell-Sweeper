package smellsweeper;

public class SSmethod {
	String name;
	String Parent;
	int parameters;
	boolean visited = false;
	String Path;
	
	public SSmethod(String name, String Parent, int parameters, String Path) { 
		this.name = name; 
		this.Parent = Parent;
		this.parameters = parameters;
		this.Path = Path;
	    }
	
}
