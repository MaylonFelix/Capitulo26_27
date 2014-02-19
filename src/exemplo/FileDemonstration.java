package exemplo;

import java.io.File;
import java.util.Scanner;

public class FileDemonstration {

	public static void main(String[] args) {
		Scanner input =  new Scanner(System.in);
		
		System.out.print("Enter file or directory name: ");
		analysePath(input.nextLine());

	}
	
	public static void analysePath(String path){
		File name =  new File(path);
		if(name.exists())
		{
			System.out.printf("%s %s\n%s", name.getName(), "exists",(name.isFile()?"is a file":"is not a file"));
		}
	}
}
