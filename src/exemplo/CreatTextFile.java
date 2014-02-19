package exemplo;

import java.io.FileNotFoundException;
import java.util.Formatter;

public class CreatTextFile {

	 private Formatter output;
	 
	 public void openFile(){
		 try {
			output = new Formatter("clients.txt");//abre o arquivo
		} 
		 catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Erro ao abrir ou criar arquivo.");
			System.exit(1);
		}
		 catch(SecurityException securityException){
			 System.err.println("Você não tem permissão para acessar o arquivo.");
			 System.exit(1);
		}
	 }
}
