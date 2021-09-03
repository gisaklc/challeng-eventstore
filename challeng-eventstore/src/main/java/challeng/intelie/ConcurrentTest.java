package challeng.intelie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * We use the ConcurrentHashMap competition collection as it is secure and
 * allows parallel read access across multiple threads without synchronizing the
 * entire map.In addition to providing an Iterator that does not require
 * synchronization. It is more scalable and its performance in this case is
 * better because it allows competition between multiple threads that access the
 * map simultaneously.
 *
 * To store the collection of events and accessible to threads during
 * simultaneous execution, ConcurrentSkipListSet is a safe option. It has the
 * most basic operations in the log (n). Allows you to safely add / remove and
 * read simultaneously by several threads. To have a navigable ordered list of
 * elements used in not-too-large sets, this was a good alternative. The sorting
 * used for this collection was sorted in an ascending manner by the "timestamp"
 * so that your iterators were faster.
 *
 **/

public class ConcurrentTest {

	public static final EventStoreImpl store = new EventStoreImpl();

	public static void main(String[] args) {

		ConcurrentTest concurrent = new ConcurrentTest();
		ExecutorService executor = Executors.newFixedThreadPool(4);

		// Producer thread
		executor.execute(concurrent.new ConProducer("type1", 10));
		executor.execute(concurrent.new ConProducer("type2", 2));

		// Consumer thread
		executor.execute(concurrent.new ConConsumer("type1", 10));
		executor.execute(concurrent.new ConConsumer("type2", 10));
		// executor.execute(concurrent.new ConConsumer("type2", 2));

		executor.shutdown();

	}

	// Producer
	class ConProducer implements Runnable {

		private final long numMaxProducer;
		private final String type;

		ConProducer(String type, long numMaxProducer) {
			this.type = type;
			this.numMaxProducer = numMaxProducer;

		}

		@Override
		public void run() {
			// Producer produces a continuous stream of numbers for every 200 milliseconds
			for (int i = 0; i < numMaxProducer; i++) {
				System.out.println("Thread Name -" + Thread.currentThread().getName() + " Adding to map-" + i);
				ConcurrentTest.store.insert(new Event(type, i));
			}
		}
	}

	// Consumer
	class ConConsumer implements Runnable {

		private EventIterator iterator;
		private final String type;
		private final long numMaxConsumer;

		ConConsumer(String type, long numMaxConsumer) {
			this.type = type;
			this.numMaxConsumer = numMaxConsumer;
		}

		@Override
		public void run() {

			iterator = ConcurrentTest.store.query(type, 5l, numMaxConsumer +1); // start e end
		
			while (iterator != null && iterator.moveNext()) {
				Event currentEvent = iterator.current();
				System.out.println("Thread Name -" + Thread.currentThread().getName() + " Event[type: "
						+ currentEvent.type() + ", timestamp: " + currentEvent.timestamp() + "]");
			}

		}

	}

}
