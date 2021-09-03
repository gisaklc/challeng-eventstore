package challeng.intelie;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class EventStoreImpl implements EventStore {

	ConcurrentHashMap<String, ConcurrentSkipListSet<Event>> listEvents = new ConcurrentHashMap<>();

	/**
	 * Only new events are inserted in the map. otherwise it takes the event and
	 * adds it to the internal list.
	 **/
	public void insert(Event event) {

		ConcurrentSkipListSet<Event> listType = null;

		String type = event.type();

		if (!exist(type)) {
			listType = new ConcurrentSkipListSet<Event>(Comparator.comparing(Event::timestamp));
			listType.add(event);
			listEvents.put(type, listType);// add new list for type

		} else {
			listType = listEvents.get(type);
			listType.add(event);

		}

	}

	/**
	 * Removes all events of type.
	 *
	 * @param type
	 */
	public void removeAll(String type) {

		if (exist(type)) {
			listEvents.remove(type);
		}
	}

	/**
	 * @param type
	 * @param startime
	 * @param endTime
	 *            Returns the events whose subset elements are within the range
	 *            startTime (inclusive) and endTime (exclusive) and type of the
	 *            informed event
	 * 
	 */
	public EventIterator query(String type, long startTime, long endTime) {

		if (type != null && exist(type)) {
			Event eventoStartTime = new Event(type, startTime);
			Event eventoEndTime = new Event(type, endTime);

			ConcurrentSkipListSet<Event> list = (ConcurrentSkipListSet<Event>) listEvents.get(type)
					.subSet(eventoStartTime, eventoEndTime);

			return new EventIteratorImpl(list);

		}

		return null;

	}

	/**
	 * check if the event exists by the key if yes returns true, if not false.
	 *
	 * @param type
	 */
	private boolean exist(String type) {
		if (listEvents.containsKey(type)) {
			return true;
		}

		return false;
	}

}
