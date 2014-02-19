package multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SharedArrayTest {

	public static void main(String[] args) {
		SimpleArray shareSimpleArray = new SimpleArray(9);
		
		ArrayWriter writer1 = new ArrayWriter(1, shareSimpleArray);
		ArrayWriter writer2 = new ArrayWriter(11, shareSimpleArray);
		ArrayWriter writer3 = new ArrayWriter(20, shareSimpleArray);
		
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(writer1);
		executor.execute(writer2);
		executor.execute(writer3);
		
		executor.shutdown();
		
		
		boolean taskEnded;
		try {
			taskEnded = executor.awaitTermination(1, TimeUnit.MINUTES);
			if(taskEnded){
				System.out.println(shareSimpleArray);
			}
			else
				System.out.println("Timed out while waiting for tasks to finish.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

}
