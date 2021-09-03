package smellsweeper;

public class SSfile {
	int LineOfCode;
	String name;
	int CommentCount;
	boolean visited = false;
	boolean hasswitch;
	String Path;
	
    public SSfile(String name, int[] LineOfCode, boolean hasswitch,String Path) 
    { 

    	this.CommentCount = LineOfCode[1];
    	this.LineOfCode = LineOfCode[0]; 
        this.name = name;	
        this.hasswitch = hasswitch;
        this.Path = Path;
    }
        
}
