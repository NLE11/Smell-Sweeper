package smellsweeper;

import java.util.Scanner;



public class Methodprocessor {

	public void LPL() {
		char inchoice = 'y';
    	Scanner console2 = new Scanner(System.in);
    	while (inchoice=='Y'|| inchoice=='y') {
    		findMAXParameterList(); 
    	System.out.println("\n Find the next potential Long Parameter List? (y/n)");		    	
    	inchoice = console2.next().charAt(0);
    	}
	}
	
	public  void findMAXParameterList() {
		int maxtemp = 0;
		for (SSmethod obj :Smellsweeper.Methodlist) {			
			if (obj.visited == false && obj.parameters >= maxtemp)
				maxtemp = obj.parameters;	
				}
		for (SSmethod obj :Smellsweeper.Methodlist) {			
			if (maxtemp == obj.parameters) {
				System.out.println("The next method of Long Parameter List is: "+ obj.name +"\n With: " + obj.parameters + " parameters \n" + "This method belongs to: " + obj.name);
				System.out.println("File Location: "+ obj.Path);
				obj.visited = true;
	    	}
	}
	}
	public  void GenerateReport() {
		int totalparameters = 0;
		int count = Smellsweeper.Methodlist.size();				

		for (SSmethod obj :Smellsweeper.Methodlist) {
			totalparameters += obj.parameters; 
		}
		System.out.println("\n METHOD INFORMATION SUMMARY ");
		System.out.println("\n ######################################################### ");
		System.out.println("\n Number of Methods in this Package: " + count);
		System.out.println("\n Total Parameters from methods: " + totalparameters);
		System.out.println("\n Average Parameters Per method: " + totalparameters/count);
		System.out.println("\n ######################################################### ");
	}	
		
		
		

}
