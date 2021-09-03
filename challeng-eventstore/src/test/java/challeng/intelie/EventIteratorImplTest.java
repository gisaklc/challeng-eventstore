package challeng.intelie;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.ConcurrentSkipListSet;
import org.junit.Before;
import org.junit.Test;

public class EventIteratorImplTest {

	public EventStoreImpl store;
	public EventIterator iterator;
	public ConcurrentSkipListSet<Event> eventList;
	public Event event;

	@Before
	public void init() {
		eventList = new ConcurrentSkipListSet<Event>();
		store = new EventStoreImpl();
	}

	@Test
	public void testMoveNext() {
		store = new EventStoreImpl();
		
		createEvent();
		iterator = (EventIterator) store.query("type1", 5L, 15L);
		
		assertTrue(iterator.moveNext());
	
		iterator.moveNext();
		assertFalse(iterator.moveNext());
	}

	@Test
	public void testCurrent() {

		createEvent();
		iterator = (EventIterator) store.query("type1", 5L, 15L);

		iterator.moveNext();
		iterator.moveNext();

		assertTrue(isEquals(new Event("typeA", 12L), iterator.current()));
	}

	@Test(expected = IllegalStateException.class)
	public void testCurrentException() {

		createEvent();
		iterator = (EventIterator) store.query("type1", 5L, 15L);
		iterator.current();

	}

	@Test
	public void testRemove() {

		createEvent();

		iterator = (EventIterator) store.query("type1", 5L, 15L);

		iterator.moveNext();
		iterator.moveNext();
		iterator.remove();

		eventList = store.listEvents.get("type1");
		assertFalse(eventList.contains(new Event("type1", 11L)));
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveException() {

		createEvent();

		iterator = (EventIterator) store.query("type1", 5L, 15L);

		iterator.remove();

	}

	private boolean isEquals(Event a, Event b) {

		if ((a.type() == a.type()) && (a.timestamp() == b.timestamp())) {
			return true;
		}

		return false;
	}

	private void createEvent() {

		store.insert(new Event("type1", 12L));
		store.insert(new Event("type2", 6L));
		store.insert(new Event("type3", 25L));

	}
}
