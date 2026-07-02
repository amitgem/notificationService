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
	private final EventStore store;
	
	public EventProcessorService(int poolSize) throws IOException {
		pool = Executors.newFixedThreadPool(poolSize);
		store = new EventStore();
	}

	public void run() { // run the service
		try {
			for (;;) {
				pool.execute(new EventProcessor(store, store.readEvent()));
			}
		} catch (InterruptedException ex) {
			shutdown();
		}
	}
	
	public void shutdown(){
		pool.shutdown();
	}
}
