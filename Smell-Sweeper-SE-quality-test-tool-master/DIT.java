package smellsweeper;

import java.util.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;


import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;

enum classNullStatus
{
	NOT_NULL, IS_NULL;
}

public class DIT {
	
	String path;
	List<File> fileList = new ArrayList<File>();
	Hashtable<String, depthInfoNode>depthInfoHash = new Hashtable<String, depthInfoNode>();
   
	public DIT(List<File> fileList)
	{
		this.fileList = fileList;
	}
	
	public DIT(String filePath)
	{
		File folder = new File(filePath); 
        File[] files = folder.listFiles();
        for (File file : files)
        {
            if (file.isFile() && file.getName().endsWith(".java"))
            {
            	this.fileList.add(file);
            }
            else if (file.isDirectory())
            {
                filereader(file.getAbsolutePath());
            }
        }
	}
	
	private void filereader(String filePath)
	{
		File folder = new File(filePath); 
        File[] files = folder.listFiles();
        for (File file : files)
        {
            if (file.isFile() && file.getName().endsWith(".java"))
            {
            	fileList.add(file);
            }
            else if (file.isDirectory())
            {
                filereader(file.getAbsolutePath());
            }
        }
	}
	
        
	static class depthInfoNode
	{
		int numChild = 0;
		int depth = 0;
		depthInfoNode parent;
	}
	
	private String adjustJavaFileName(String fileName)
	{
		
		return fileName.replace(".java", "");
	}
	
	private boolean hashContainsNode(String currentJavaFile)
	{
		return depthInfoHash.contains(currentJavaFile);
	}
	
	public void getNewDIT(File currentFile)throws IOException, InstantiationException, 
	IllegalAccessException, ClassNotFoundException
	{

		File currentFile1 = currentFile;
		String fileLocation = currentFile1.getAbsolutePath();
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
		for(JavaFileObject jfo: fileManager.list(StandardLocation.CLASS_OUTPUT,"",Collections.singleton(JavaFileObject.Kind.CLASS),true)) 
		{
			Class<?> clazz = null;
			
			String s = tempDirectory.toUri().relativize(jfo.toUri()).toString();
			s = s.substring(0, s.length()-6).replace('/', '.');
			clazz = cl.loadClass(s);
			
			addToDitHash(clazz);
		
		}
			
	
	}
	private void addToDitHash(Class<?> addClass)
	    {
	    	
	    	String className = addClass.getSimpleName();
	    	//System.out.println("HERE " + className);
	    	String parentClassName;
	    	classNullStatus classStatus;
	    	try
	    	{
	    		parentClassName = addClass.getSuperclass().getSimpleName();
	    		classStatus = classNullStatus.NOT_NULL;
	    	}
	    	catch(Exception e)
	    	{
	    		parentClassName = "NONE";
	    		classStatus = classNullStatus.IS_NULL;
	    	}
	    	
	    	if (classStatus == classNullStatus.NOT_NULL)
	    	{
	    		//System.out.println("HERE " + parentClassName);

		    	depthInfoNode newNode = new depthInfoNode();
		    	depthInfoNode parentNode = new depthInfoNode();

		    	
		    	if (!depthInfoHash.containsKey(parentClassName))
		    	{
		    		if(!parentClassName.equals("Object"))
		        	{
		    			//go to the root of the tree
		    			System.out.println("1");
		        		addToDitHash(addClass.getSuperclass());
		        	}
		 	
		        	
		    		if(parentClassName.equals("Object"))
		        	{
		    			System.out.println("2");
		        		newNode.depth = 0;
		            	newNode.numChild = 0;
		            	newNode.parent = parentNode;
		            	parentNode.depth = -1;
		            	parentNode.numChild = -1;
		            	parentNode.parent = null;	
		            	
		            	depthInfoHash.put(className, newNode);
		            	depthInfoHash.put("Object", parentNode);
		        	}
		        	else
		        	{
		        		parentNode = depthInfoHash.get(parentClassName);
		        		newNode.depth = parentNode.depth + 1;
		        		
		        		newNode.numChild = 0;
		        		newNode.parent = parentNode;
		        		parentNode.numChild = parentNode.numChild + 1;
		        		
		        		depthInfoHash.put(parentClassName, parentNode);
		        		depthInfoHash.put(className, newNode);
		        	}
		    	}
		    	else
		    	{
		    		parentNode = depthInfoHash.get(parentClassName);
		    		newNode.parent = parentNode;
		    		newNode.depth = parentNode.depth + 1;
		    		parentNode.numChild = parentNode.numChild + 1;
		    		
		    		depthInfoHash.put(parentClassName, parentNode);
		    		depthInfoHash.put(className, newNode);
		    		
		    	}
	    		
	    	}
	    		
	    	
	    }
	    	

	public void startFillHashDit() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		depthInfoNode tempNode = new depthInfoNode();
		String adjJavaName = new String();
		
		for (File currentFile: fileList)
		{
			adjJavaName = adjustJavaFileName(currentFile.getName());
			
			if(!hashContainsNode(adjJavaName))
			{
				
				getNewDIT(currentFile);
			}
		}
	}
	
	

	public void DITmain(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

		DIT testDit = new DIT(path);
		
		for (File file: testDit.fileList)
		{
			//System.out.println(file.getName());
		}
		testDit.startFillHashDit();
		Set<String> keys = testDit.depthInfoHash.keySet();
		for (String key: keys)
		{
			System.out.print("Class name is " + key);
			System.out.println("Value Depth " + testDit.depthInfoHash.get(key).depth);
			System.out.println("Number of children " + testDit.depthInfoHash.get(key).numChild);
			depthInfoNode tempParent = testDit.depthInfoHash.get(key).parent;
			
			String parentName = " ";
			for (String keyParent: keys)
			{
				if (testDit.depthInfoHash.get(keyParent).equals(tempParent))
				{
					parentName = keyParent;
					break;
				}
			}
			
			System.out.println("Parent is " + parentName);
		}
		
	}

}
