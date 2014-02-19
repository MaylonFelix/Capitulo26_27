package multithreading;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionsSyncronizedBuffer implements Buffer {

	private final Lock accessLock = new ReentrantLock();

	private final Condition canWrite = accessLock.newCondition();
	private final Condition canRead = accessLock.newCondition();

	private int buffer = -1;
	private boolean occupied = false;

	@Override
	public void set(int value) throws InterruptedException {
		accessLock.lock();// bloqueia esse objeto
		// envia informações de thread e as informações de buffer para saída,
		// então espera
		try {
			while (occupied) {
				System.out.println("Buffer tries to write.");
				displayState("Buffer full. Producer waits.");
				canWrite.await();// libera imediatamente o Lock associado e
									// coloca a threa em estado de espera dessa
									// Contition
				// espera até que o buffer esteja vazio
			}
			buffer = value;

			// indica que a produtora não pode armazenar outro valor
			// até a consumidora recuperar o valor atual de buffer
			occupied = true;

			displayState("Producer writes " + buffer);

			// sinaliza a thread que está esperando para ler a partir do buffer
			canRead.signal();
		} finally {
			accessLock.unlock();
		}

	}

	@Override
	public int get() throws InterruptedException {
		int readValue = 0;
		accessLock.lock();
		try {
			while (!occupied) {
				System.out.println("Consumer tries to read.");
				displayState("Buffer empty. Consumer waits.");
				canRead.await();
			}
			occupied = false;
			readValue = buffer;

			displayState("Consumer reads " + readValue);
			canWrite.signal();
		} finally {
			accessLock.unlock();
		}
		return readValue;
	}

	private void displayState(String operation) {
		System.out.printf("%-40s%d\t\t%b\n\n", operation, buffer, occupied);

	}

}
