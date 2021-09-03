package challeng.intelie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Before;
import org.junit.Test;

public class EventStoreImplTest {

	public EventStoreImpl store;
	public EventIterator iterator;
	public ConcurrentSkipListSet<Event> eventList;
	public Event event;
	
	@Before
	public void init() {
		event = new Event("type1", 10L);
		store = new EventStoreImpl();

	}
	//test checks the event has been inserted
	@Test
	public void insertEventSameType() {

		Event event1 = new Event("type1", 10l);
		Event event2 = new Event("type1", 20l);

		assertFalse(store.listEvents.containsKey("type1"));

		store.insert(event1);

		assertTrue(store.listEvents.containsKey("type1"));

		assertEquals(1, store.listEvents.size());

	}
	// test returns null when there is no type on the map
	@Test
	public void queryNoExistTypeEvent() {
		store = new EventStoreImpl();
		store.listEvents.clear();
		assertEquals(null, store.query("type1", 10l, 30l));
	}

	// test removes all events by type
	@Test
	public void removeTest() {
		store = new EventStoreImpl();

		Event event1 = new Event("type1", 10l);
		Event event2 = new Event("type2", 20l);

		store.insert(event1);
		store.insert(event2);

		store.removeAll("type1");
		assertFalse(store.listEvents.containsKey("type1"));

		store.removeAll("type2");
		assertFalse(store.listEvents.containsKey("type2"));
	}

	// removes all of the same selected type
	@Test
	public void removeAllTheSameTypeTest() {
		store = new EventStoreImpl();

		Event event1 = new Event("type1", 10l);
		Event event2 = new Event("type1", 20l);

		store.insert(event1);
		store.insert(event2);

		store.removeAll("type1");
		assertFalse(store.listEvents.containsKey("type1"));

	}

	// test returns events by type
	@Test
	public void QueryTest() {
		store = new EventStoreImpl();
		store.insert(event);

		iterator = (EventIterator) store.query("type1", 5L, 15L);
		iterator.moveNext();
		iterator.current();

		assertEquals(event, iterator.current());
	}

}
