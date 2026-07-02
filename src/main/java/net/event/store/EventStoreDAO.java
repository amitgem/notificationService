package net.event.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.event.Event;
import net.event.EventLog;
import net.event.EventRef;
import net.event.EventState;

public class EventStoreDAO {
	private final AtomicLong eventIds = new AtomicLong();
	private final Map<Long, Event> events = new ConcurrentHashMap<Long, Event>();
	private final Map<Long, EventRef> refs = new ConcurrentHashMap<Long, EventRef>();

	public synchronized long addEvent(Event event) {
		long eventId = eventIds.incrementAndGet();
		Event storedEvent = copyEvent(event);
		storedEvent.setEventId(eventId);
		events.put(Long.valueOf(eventId), storedEvent);

		EventRef ref = new EventRef();
		ref.setEventId(eventId);
		ref.setState(EventState.NEW);
		ref.setLog(Collections.synchronizedList(new ArrayList<EventLog>()));
		ref.setPublishedChannelCount(0);
		refs.put(Long.valueOf(eventId), ref);
		return eventId;
	}

	public synchronized EventRef updateEvent(long eventId, EventRef ref) {
		if (!refs.containsKey(Long.valueOf(eventId))) {
			return null;
		}

		EventRef stored = copyEventRef(ref);
		stored.setEventId(eventId);
		refs.put(Long.valueOf(eventId), stored);
		return copyEventRef(stored);
	}

	public synchronized Event getEvent(long eventId) {
		Event event = events.get(Long.valueOf(eventId));
		return event == null ? null : copyEvent(event);
	}

	public synchronized EventRef getEventRef(long eventId) {
		EventRef ref = refs.get(Long.valueOf(eventId));
		return ref == null ? null : copyEventRef(ref);
	}

	public synchronized boolean startPublication(long eventId) {
		EventRef ref = refs.get(Long.valueOf(eventId));
		if (ref == null || ref.getState() != EventState.NEW) {
			return false;
		}

		ref.setState(EventState.PUBLISHING);
		return true;
	}

	public synchronized EventRef setEventState(long eventId, EventState state) {
		EventRef ref = refs.get(Long.valueOf(eventId));
		if (ref == null) {
			return null;
		}

		ref.setState(state);
		return copyEventRef(ref);
	}

	public synchronized EventRef incrementPublishedChannelCount(long eventId) {
		EventRef ref = refs.get(Long.valueOf(eventId));
		if (ref == null) {
			return null;
		}

		ref.setPublishedChannelCount(ref.getPublishedChannelCount() + 1);
		ref.setState(EventState.PARTIALLY_PUBLISHED);
		return copyEventRef(ref);
	}

	public synchronized EventRef addEventLog(long eventId, String logEntry) {
		EventRef ref = refs.get(Long.valueOf(eventId));
		if (ref == null) {
			return null;
		}

		List<EventLog> logs = ref.getLog();
		if (logs == null) {
			logs = Collections.synchronizedList(new ArrayList<EventLog>());
			ref.setLog(logs);
		}

		EventLog log = new EventLog();
		log.setLogEntry(logEntry);
		log.setLogTime(new Date());
		logs.add(log);
		return copyEventRef(ref);
	}

	private Event copyEvent(Event source) {
		Event copy = new Event();
		copy.setEventId(source.getEventId());
		copy.setEventType(source.getEventType());
		copy.setSource(source.getSource());
		copy.setPriority(source.getPriority());
		copy.setMessage(source.getMessage());
		copy.setDestination(source.getDestination());
		copy.setExpireInMinutes(source.getExpireInMinutes());
		return copy;
	}

	private EventRef copyEventRef(EventRef source) {
		EventRef copy = new EventRef();
		copy.setEventId(source.getEventId());
		copy.setState(source.getState());
		copy.setPublishedChannelCount(source.getPublishedChannelCount());
		copy.setLog(copyLogs(source.getLog()));
		return copy;
	}

	private List<EventLog> copyLogs(List<EventLog> sourceLogs) {
		List<EventLog> copies = new ArrayList<EventLog>();
		if (sourceLogs == null) {
			return copies;
		}

		synchronized (sourceLogs) {
			for (int i = 0; i < sourceLogs.size(); i++) {
				EventLog sourceLog = sourceLogs.get(i);
				EventLog copyLog = new EventLog();
				copyLog.setLogEntry(sourceLog.getLogEntry());
				copyLog.setLogTime(sourceLog.getLogTime());
				copies.add(copyLog);
			}
		}
		return copies;
	}
}
