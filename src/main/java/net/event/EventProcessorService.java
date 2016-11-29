package net.event;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.event.store.EventStore;

/**
 * Service Class to create a pool of EventProcessors
 * 
 * @author Amit Singh
 *
 */
public class EventProcessorService implements Runnable {

	private final ExecutorService pool;
	private EventStore store;
	
	public EventProcessorService(int poolSize) throws IOException {
		pool = Executors.newFixedThreadPool(poolSize);
	}

	public void run() { // run the service
		try {
			for (;;) {
				pool.execute(new EventProcessor(store.readEvent()));
			}
		} catch (InterruptedException ex) {
			shutdown();
		}
	}
	
	public void shutdown(){
		pool.shutdown();
	}
}
