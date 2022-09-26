package com.gnix.jflatgl.core.assets.loaders.event;
import com.gnix.jflatgl.event.DelayEvent;
import com.gnix.jflatgl.event.QueuedEvent;
import com.gnix.jflatgl.event.TriggerEventQueue;
import org.json.JSONObject;

/*
	EventLoaders store the path to the target file
	and will load the data from it when triggered.

	The load() method is overloaded by the different
	EventLoader subclasses to handle different sorts
	of event data (Audio, UI animation, cutscene, etc.)
 */
public abstract class EventLoader<T extends QueuedEvent> {

	protected TriggerEventQueue<T> eventQueue;

	public TriggerEventQueue<T> getEventQueue() {
		return eventQueue;
	}

	// universal events
	protected DelayEvent parseDelayEvent(JSONObject object) {
		float duration = object.getFloat("delay");
		return new DelayEvent(duration);
	}

}
