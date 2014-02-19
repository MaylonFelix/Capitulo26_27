package multithreading;

public class ThreadCreator {

	public static void main(String[] args) {
		System.out.println("Creating threads");
		
		Thread thread1 =  new Thread(new PrintTask("taskl1"));
		Thread thread2 =  new Thread(new PrintTask("taskl2"));
		Thread thread3 =  new Thread(new PrintTask("taskl3"));
		
		System.out.println("Threads created, starting task");
		
		thread1.start();
		thread2.start();
		thread3.start();
		
		System.out.println("Task started, main, ends.\n");

	}

}
