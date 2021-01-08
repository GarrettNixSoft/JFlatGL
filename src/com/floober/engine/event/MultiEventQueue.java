package com.floober.engine.event;

import com.floober.engine.util.Logger;
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
		if (lastEventAdded != null && (!lastEventAdded.isBlocking() ||
				(lastEventAdded.isBlocking()) && lastEventAdded.isComplete())) {
			while (!events.isEmpty() && runningEvents.size() < maxSimultaneousEvents &&
					!lastEventAdded.isBlocking() || (lastEventAdded.isBlocking()) && lastEventAdded.isComplete()) {
				E nextEvent = events.poll();
				lastEventAdded = nextEvent;
				runningEvents.add(nextEvent);
				nextEvent.onStart();
			}
		}
		// update the currently running events
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

}