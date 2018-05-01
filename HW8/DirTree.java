import java.awt.Desktop;
import java.io.*;
import java.util.*;

public class DirTree {
	static String lineBreak = System.lineSeparator();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String defaultDir = "The url is invalid, use working directory.";
		File path;
		
		if(args.length == 0) {
			System.out.println(defaultDir);
			path = new File(System.getProperty("user.dir"));
		}
		else {
			path = new File(args[0]);
		}
		
		if(path.isFile()) {
			try {
			path = new File(path.getCanonicalPath());
			}
			catch (Exception ex){
				path = new File(path.getAbsolutePath());
			}
			path = new File(path.getParent());
		}
		else if(!path.isDirectory()) {
			System.out.println(defaultDir);
			path = new File(System.getProperty("user.dir"));
		}		
		printFolder(path);
		
	}
	
	public static void printFolder(File path) {
		
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(path.getCanonicalPath());
		}
		catch (Exception ex){
			sb.append(path.getName());
		}
		sb.append(":" + lineBreak);
		printFolder(path,sb,new StringBuilder());
		
		try {
			File file = new File("DirTree.txt");
			PrintWriter pw = new PrintWriter(file,"UTF-8");
			pw.print(sb);
			pw.close();
			
			if(!Desktop.isDesktopSupported()){
	            System.out.println("Desktop is not supported");
	        }
			else {
		        Desktop desktop = Desktop.getDesktop();
		        if(file.exists())
		        	desktop.open(file);
		        else
		        	throw new FileNotFoundException();
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("File system error.");
			System.out.println(sb);
		}
		catch(UnsupportedEncodingException ex) {
			System.out.println("File encoding error.");
			System.out.println(sb);
		}
		catch(IOException ex) {
			System.out.println("Please open 'DirTree.txt' to see your DirTree.");
		}
	}
	
	private static void printFolder(File path,StringBuilder sb, StringBuilder indent) {
		Queue<File> folder = new LinkedList<>();
		Queue<File> file = new LinkedList<>();
		StringBuilder fileIn = indent;
		File subFolder;
		try {
			for(File name: path.listFiles()) {
				if(name.isFile()) 
					file.add(name);
				
				else 
					folder.add(name);
			}
		}
		catch(NullPointerException ex) {
			sb.append(fileIn + "'FOLDER CAN'T BE ACCESSED!'" + lineBreak);
		}
		
		if(folder.size()>0)
			fileIn = new StringBuilder(indent).append("\u2502  ");
		
		while(!file.isEmpty()) {
			sb.append(fileIn + file.poll().getName() + lineBreak);
		}
		
		while(!folder.isEmpty()) {
			subFolder = folder.poll();
			sb.append(fileIn + lineBreak + indent);
			
			if(folder.size()==0) {
				sb.append("\u2514\u2500\u2500" + subFolder.getName() + ":" + lineBreak);
				printFolder(subFolder,sb,new StringBuilder(indent).append("   \t"));
			}
			
			else {
				sb.append("\u251C\u2500\u2500" + subFolder.getName() + ":" + lineBreak);
				printFolder(subFolder,sb,new StringBuilder(fileIn).append("   \t"));
			}
		}

	}

}