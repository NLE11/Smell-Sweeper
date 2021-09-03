package smellsweeper;

import java.util.Scanner;

public class Classprocessor {
	
	public void WMC() {
		char inchoice = 'y';
    	Scanner console2 = new Scanner(System.in);
    	while (inchoice=='Y'|| inchoice=='y') {
    		findMAXWMC(); 
    	System.out.println("\n Find the next Class with Large WMC List? (y/n)");		    	
    	inchoice = console2.next().charAt(0);
    	}
	}
	
	public  void findMAXWMC() {
		int maxtemp = 0;
		for (SSclass obj :Smellsweeper.Classlist) {			
			if (obj.visited == false && obj.MethodCount >= maxtemp)
				maxtemp = obj.MethodCount;	
				}
		for (SSclass obj :Smellsweeper.Classlist) {			
			if (maxtemp == obj.MethodCount) {
				System.out.println("The next Class with Large WMC is: "+ obj.name +"\n Having: " + obj.MethodCount + " methods \n" + "This method belongs to: " + obj.Parent);
				System.out.println("File Location: "+ obj.Path);
				obj.visited = true;
	    	}
	}
	}
	
	public  void Lazyclass() {				
		for (SSclass obj :Smellsweeper.Classlist) 		
			if (obj.MethodCount == 0) {
				System.out.println("Data Class/Lazy Class with Zero Methods: "+ obj.name  + "\n From Superclass: " + obj.Parent);
				System.out.println("File Location: "+ obj.Path);}
	
	}
	
	public  void GenerateReport() {
		int totalfield = 0;
		int totalmethod = 0;
		int count = Smellsweeper.Filelist.size();				

		for (SSclass obj :Smellsweeper.Classlist) {
			totalfield += obj.FieldCount; 
			totalmethod += obj.MethodCount;
		}
		System.out.println("\n ######################################################### ");
		System.out.println("\n CLASS INFORMATION SUMMARY ");
		System.out.println("\n ######################################################### ");
		System.out.println("\n Number of Classes in this Package: " + count);
		System.out.println("\n Total Fields: " + totalfield);
		System.out.println("\n Total Methods: " + totalmethod);
		System.out.println("\n Average Fields Per Class: " + totalfield/count);
		System.out.println("\n Average Methods Per Class: " + totalmethod/count);
		System.out.println("\n ######################################################### ");
	}
	
	
}
