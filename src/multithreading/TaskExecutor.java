package multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {

	public static void main(String[] args) {
		//Executáveis
		PrintTask task1 = new PrintTask("task1");
		PrintTask task2 = new PrintTask("task2");
		PrintTask task3 = new PrintTask("task3");
	
		System.out.println("Starting Executor");
		
		//Gerenciador de threads
		ExecutorService threadExecutor = Executors.newCachedThreadPool();

		//Inicia as threads e coloca no estado executável
		threadExecutor.execute(task1);
		threadExecutor.execute(task2);
		threadExecutor.execute(task3);
		
		//Encerra as threads trabalhadores quando suas tarefas terminarem
		threadExecutor.shutdown();
		
		System.out.println("Task started, main ends.\n ");
	}

}
