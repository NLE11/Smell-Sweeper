package smellsweeper;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class User {
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {

		System.out.println("PLEASE ENTER A PATH FOR YOUR PACKAGE:");
        Scanner scanner = new Scanner(System.in);
        String filepath = scanner.nextLine();
        File checkFilePath = new File(filepath);
        while(!checkFilePath.exists())
        {
        	System.out.println("Invalid Path. ");
        	System.out.println("PLEASE ENTER A PATH FOR YOUR PACKAGE:");
        	filepath = scanner.nextLine();
        	checkFilePath = new File(filepath);
        }
        
        Smellsweeper obj = new Smellsweeper();
        obj.mainmenu(filepath);
        
				    			    		
    }	 
}
