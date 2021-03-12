package com.floober.engine.event;

import com.floober.engine.util.data.Queue;

import java.util.ArrayList;
import java.util.List;

public class MultiEventQueue<E extends QueuedEvent> extends EventQueue<E> {

	private final List<E> runningEvents;
	private final Queue<E> eventsToRemove;
	private QueuedEvent lastEventAdded;
	private int maxSimultaneousEvents = 10;

	public MultiEventQueue() {
		super();
		runningEvents = new ArrayList<>();
		eventsToRemove = new Queue<>();
	}

	public MultiEventQueue(int capacity) {
		super();
		maxSimultaneousEvents = capacity;
		runningEvents = new ArrayList<>();
		eventsToRemove = new Queue<>();
	}

	/**
	 * Add an event to the queue.
	 * @param e the event to enqueue
	 */
	public void queueEvent(E e) {
		super.queueEvent(e);
		lastEventAdded = e;
	}

	@Override
	public void update() {
		// check the queue, add events if possible
		if (lastEventAdded != null && 													// if an event has been added at some point, and
				(!lastEventAdded.isBlocking() || 										// 1. it is not a blocking event, or
				(lastEventAdded.isBlocking()) && lastEventAdded.isComplete())) { 		// 2. it is a blocking event but it's over now, then
			while (!events.isEmpty() && 												// while there are more events in the queue, and
					runningEvents.size() < maxSimultaneousEvents && 					// there is room for more simultaneous events, and
					(!lastEventAdded.isBlocking() || 									// the last event added was not a blocking event, or
					(lastEventAdded.isBlocking()) && lastEventAdded.isComplete())) { 	// it was, but is now complete, then start the next event and continue
				E nextEvent = events.poll();
				if (nextEvent.mustWait()) { 											// unless the next event has to wait, in which case,
					if (!runningEvents.isEmpty()) break; 								// we're done here until the running events are gone
				}
				lastEventAdded = nextEvent;
				runningEvents.add(nextEvent);
				nextEvent.onStart();
			}
		}
		// update the currently running events
//		if (!runningEvents.isEmpty()) Logger.log(concurrentEventsToString());
		for (E event : runningEvents) {
			event.update();
			if (event.isComplete()) {
				event.onFinish();
				eventsToRemove.push(event);
			}
		}
		// remove all finished events
		while (!eventsToRemove.isEmpty()) {
			runningEvents.remove(eventsToRemove.poll());
		}
	}

//	private String concurrentEventsToString() {
//		StringBuilder sb = new StringBuilder("MultiEventQueue:");
//		sb.append('[');
//		for (E event : runningEvents) {
//			sb.append(event).append(", ");
//		}
//		if (!runningEvents.isEmpty()) sb.setLength(sb.length() - 2);
//		sb.append(']');
//		return sb.toString();
//	}

}