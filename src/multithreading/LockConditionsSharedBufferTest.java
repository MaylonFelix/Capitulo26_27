package multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LockConditionsSharedBufferTest {

	public static void main(String[] args) {
		ExecutorService application = Executors.newCachedThreadPool();
		Buffer sharedLocation = new LockConditionsSyncronizedBuffer();
		
		application.execute(new Producer(sharedLocation));
		application.execute(new Consumer(sharedLocation));
		
		application.shutdown(); 

	}

}
