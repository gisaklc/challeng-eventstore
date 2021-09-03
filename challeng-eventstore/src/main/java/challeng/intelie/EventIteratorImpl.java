package challeng.intelie;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

public class EventIteratorImpl implements EventIterator {

	private ConcurrentSkipListSet<Event> list = new ConcurrentSkipListSet<>();

	private final Iterator<Event> iterator;

	private Event current = null;

	/**
	 * the constructor method receives as a parameter a list of events ordered for iteration
	/**
	 */
	public EventIteratorImpl(ConcurrentSkipListSet<Event> map) {
		this.list = map;

		if (this.list != null) {
			iterator = this.list.iterator();
		} else {
			iterator = null;
		}
	}

	/**
	 * Method to close this resource. Override from AutoCloseable.class
	 * 
	 * The close() method of an AutoCloseable object is called automatically when
	 * exiting a try-with-resources block for which the object has been declared in
	 * the resource specification header.
	 */
	public void close() throws Exception {
		current = null;
	}

	/**
	 * @return false if the iterator has reached the end, true otherwise.
	 **/
	/**
	 * Move the iterator to the next event, if any.
	 */
	public boolean moveNext() {
		// if list no equals null and list does have next, take next event
		if (this.list != null && iterator.hasNext()) {
			current = iterator.next();
			return true;
		}
		return false;
	}

	/**
	 * @return IllegalStateException if current event is null, if not the current event.
	/**
	 */
	public Event current() {
		// if key is null, then it does not have event current*
		if (current == null) {
			throw new IllegalStateException();
		}
		// else return the current event from key
		return current;
	}

	/**
	 * @return IllegalStateException if current event is null, if not remove current event.
	/**
	 */
	public void remove() {
		if (current == null) {
			throw new IllegalStateException();
		}
		// else, remove event
		this.list.remove(current);
		current = null;
	}

}
