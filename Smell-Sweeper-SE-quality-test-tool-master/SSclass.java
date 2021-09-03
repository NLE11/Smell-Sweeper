package smellsweeper;

public class SSclass {
	int FieldCount;
	int MethodCount;
	String name;
	String Parent;
	String Path;
	boolean visited;
	
    public SSclass(int FieldCount,int MethodCount,String name,String Parent, String Path) 
    { 
        this.FieldCount = FieldCount; 
        this.MethodCount = MethodCount;
        this.name = name; 
        this.Parent = Parent;
        this.Path = Path;
    }
	
    
	
}
