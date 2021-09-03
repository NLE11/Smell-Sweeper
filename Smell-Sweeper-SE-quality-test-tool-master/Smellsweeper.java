package smellsweeper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Smellsweeper {

public static List<Javafiles> Javafilelist = new ArrayList<Javafiles>();	
public static List<SSfile> Filelist = new ArrayList<SSfile>();
public static List<SSmethod> Methodlist = new ArrayList<SSmethod>();
public static List<SSclass> Classlist = new ArrayList<SSclass>();

	public void filereader(String path)
    {	File folder = new File(path); 
        File[] files = folder.listFiles();
        for (File file : files)
        {
            if (file.isFile() && file.getName().endsWith(".java"))
            {
            	Javafilelist.add(new Javafiles(file.getAbsolutePath(),file.getName()));
            	//System.out.println(file.getAbsolutePath());
            }
            else if (file.isDirectory())
            {
                filereader(file.getAbsolutePath());
            }
        }
    }

	public void mainmenu(String input) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		System.out.println("Welcome to Smell Sweeper - SE Quality Testing Tool! "); 		
		System.out.print("Initializing......"); 
		filereader(input);
		System.out.print(".......");
    	LOCinit();
    	System.out.println("........");
    	RFCFIELDinit();
    	System.out.println("----------------------------------");
		System.out.println("Package Information:");
		
		System.out.println("Total Files Processed: "  + Javafilelist.size());
		System.out.println("Total Classes: "  + Classlist.size());
		System.out.println("Total Methods: "  + Methodlist.size());

		String choice1;

		do {
		System.out.println("\n Please select which type of anti-pattern you wish to identify: \n" + "1. Generate Overall Reports \n" + "2. Blob \n" + "3. Long Methods(Coming Soon)"); 
		System.out.println("4. Long Parameter List\n" + "5. Class with Large WMC\n" + "6. Data Class/Lazy Class \n" + "7. Switch Statement \n" + "8. Depth of Inheritance Info (testing)\n" + "\n q. EXIT");
        Scanner scan1 = new Scanner(System.in);
        choice1 = scan1.nextLine();
		switch(choice1) {
			case "1":		
		    	Classprocessor proc0c = new Classprocessor();
		    	proc0c.GenerateReport();				
				Methodprocessor proc0m = new Methodprocessor();
		    	proc0m.GenerateReport();
				Fileprocessor proc0f = new Fileprocessor();
				proc0f.GenerateReport();
				break;
		
			case "2":		
		    	Fileprocessor proc1 = new Fileprocessor();
		    	proc1.nextBlobfinder();
		    	break;
		    case "3":
				System.out.println("Coming Soon");
				break;	
		    case "4":		
		    	Methodprocessor proc3 = new Methodprocessor();
		    	proc3.LPL();
		    	break;
		    case "5":		
		    	Classprocessor procwmc = new Classprocessor();
		    	procwmc.WMC();
		    	break;
		    case "6":		
		    	Classprocessor proc4 = new Classprocessor();
		    	proc4.Lazyclass();
		    	break;	
		    case "7":		
		    	Fileprocessor proc5 = new Fileprocessor();
		    	proc5.findswitch();
		    	break;
		    case "8":		
		    	DIT new1 = new DIT(input);
		    	new1.DITmain(input);
		    	break;
		    	
		    	
		    	
		    case "q":
		    	System.out.println("EXITING..."); 
		    	break;
		    default: 
			System.out.println("INVALID INPUT");   
	}
        }	while (!choice1.equals("q"));
	}
	public void LOCinit() throws IOException{
		for (Javafiles obj :Javafilelist) {			

			Filelist.add(new SSfile(obj.name,obj.getLOC(),obj.ifswitch,obj.path));}
	}
	

	public void RFCFIELDinit() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
		for (Javafiles obj :Javafilelist) {			
			obj.getMethodandField();
			Methodlist.addAll(obj.Methodofthis);
			Classlist.addAll(obj.Classofthis);
			}
	}
	
	

}



	