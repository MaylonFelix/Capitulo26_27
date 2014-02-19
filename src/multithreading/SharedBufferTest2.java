package multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SharedBufferTest2 {

	public static void main(String[] args) {
		ExecutorService application = Executors.newCachedThreadPool();
		Buffer sharedLocation = new SynchronizedBuffer();
		 application.execute(new Producer(sharedLocation));
		 application.execute(new Consumer(sharedLocation));
		 
		 application.shutdown();

	}

}
