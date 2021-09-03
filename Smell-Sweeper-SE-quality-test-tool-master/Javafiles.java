package smellsweeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

public class Javafiles {

	String path;
	String name;
	boolean ifswitch = false;

	public List<SSmethod> Methodofthis = new ArrayList<SSmethod>();
	public List<SSclass> Classofthis = new ArrayList<SSclass>();
	
	
    public Javafiles(String path,String name) 
    { 
        this.path = path; 
        this.name = name;
    }


    
    


    
public int[] getLOC() throws IOException    {
    int line_count = 0;
    int comment_count = 0;
	BufferedReader reader = null;
	String line;
	reader = new BufferedReader(new FileReader(this.path));
	while ((line = reader.readLine()) != null) {
		line_count++; 
		if (line.contains("//")) {
			comment_count++;
        } else if (line.startsWith("/*")) {
        	comment_count++;
            while (!(line = reader.readLine()).endsWith("'*\'")) {
            comment_count++;
            break;
            };
        }
        else if (line.contains("switch"))
        	ifswitch = true;
	}
	reader.close();
	//System.out.println("Processing:  " + this.name);
	
	return new int[] {(line_count - comment_count), comment_count};
}



public void getMethodandField() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

	
	String fileLocation = this.path;
	//System.out.println(fileLocation);

	//create a compiler and a known place where class is compiled to
	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	DiagnosticCollector diagnosticsCollector = new DiagnosticCollector();
	StandardJavaFileManager fileManager =compiler.getStandardFileManager(diagnosticsCollector,null,null);
	Path tempDirectory =Files.createTempDirectory("compile-test-");
	fileManager.setLocation(StandardLocation.CLASS_OUTPUT,Collections.singleton(tempDirectory.toFile()));
	
	//add java file Location to array
	List<File> sourceFileList = new ArrayList <File> ();
	sourceFileList.add (new File (fileLocation));
	
	//get an iterable object for the java files
	Iterable<? extends JavaFileObject> compilationUnits =fileManager.getJavaFileObjectsFromFiles (sourceFileList);
	
	//create class
	CompilationTask task = compiler.getTask (null,fileManager, diagnosticsCollector, null, null, compilationUnits);
    task.call();

    //load the class
    ClassLoader classLoader = Javafiles.class.getClassLoader();
    URLClassLoader cl = new URLClassLoader(new URL[]{tempDirectory.toUri().toURL()});
    
    
    //create a class from a java file object and find its superclasses
    for(JavaFileObject jfo: fileManager.list(StandardLocation.CLASS_OUTPUT,"",Collections.singleton(JavaFileObject.Kind.CLASS),true)) {
    	int fieldcount;
    	int methodcount;
    	String Parent;
    	String SelfName;
    	
    	
    	Class<?> clazz = null;
    	
    	String s = tempDirectory.toUri().relativize(jfo.toUri()).toString();
    	s = s.substring(0, s.length()-6).replace('/', '.');
    	//System.out.println("HERE!!!!  "  + s);

    	clazz = cl.loadClass(s);
    	
    	Method[] clazzMethod = clazz.getDeclaredMethods();
    	Field[] clazzField = clazz.getDeclaredFields(); //I got a list of fields
    	Class<?> clazzSuper = clazz.getSuperclass();
    	
    	SelfName = clazz.getName();    	
    	//System.out.println("\nCLAZZ NAME: " + SelfName);

    	//System.out.println("METHODS OF THE CLASS");
    	int count = 0;
    	for (int i = 0; i < clazzMethod.length; i++)
    	{
    		//System.out.println(clazzMethod[i]);
    		//System.out.println("count of para: " + clazzMethod[i].getParameterCount());
    		
    		Methodofthis.add(new SSmethod(clazzMethod[i].getName(),SelfName,clazzMethod[i].getParameterCount(),this.path));
    		
    		count++;
    		
    	}
    	//System.out.println("Total methods in this class are: " + count);
    	methodcount = count;
    	
    	//System.out.println("\nCLAZZ NAME: " + clazz.getName());
    	//System.out.println("FIELDS OF THE CLASS");
    	int count1 = 0;
    	for (int i = 0; i < clazzField.length; i++)
    	{
    		//System.out.println(clazzField[i]);
    		count1++;
    	}
    	//System.out.println("Total fields in this class are: " + count1);
    	fieldcount = count1;
    	String superClassName;

    	
    	try
    	{
    		superClassName = clazzSuper.getSimpleName();

    	}
    	catch (Exception e)
    	{
    		superClassName = "None";
    	}
    	Parent = superClassName;
    	//System.out.println("My superclass is " + superClassName+ "\n");  
    	
    	Classofthis.add(new SSclass(fieldcount,methodcount,SelfName,Parent,this.path));
    	
    	
    }
}




}

