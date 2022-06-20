package com.floober.engine.core.assets.loaders.event;

import com.floober.engine.core.audio.event.*;
import com.floober.engine.event.DelayEvent;
import com.floober.engine.event.QueuedEvent;
import com.floober.engine.event.TriggerEventQueue;
import com.floober.engine.core.util.file.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Load Audio Events from a JSON file.
 */
public class AudioEventLoader extends EventLoader<QueuedEvent> {

	public void load(String path) {

		// create queue for storing events
		eventQueue = new TriggerEventQueue<>();

		// load JSON
		JSONObject json = FileUtil.getResourceDataJSON(path);
		JSONArray events = json.getJSONArray("events");

		// parse events
		int numEvents = events.length();
		for (int i = 0; i < numEvents; ++i) {
			JSONObject eventObject = events.getJSONObject(i);

			// check object type
			if (eventObject.has("music") || eventObject.has("audio")) {
				PlayEvent event = parsePlayEvent(eventObject);
				eventQueue.queueEvent(event);
			}
			else if (eventObject.has("delay")) {
				DelayEvent event = parseDelayEvent(eventObject);
				eventQueue.queueEvent(event);
			}
			else if (eventObject.has("fade_music")) {
				FadeMusicEvent event = parseFadeEvent(eventObject);
				eventQueue.queueEvent(event);
			}
			else if (eventObject.has("stop_music")) {
				StopMusicEvent event = parseStopMusicEvent(eventObject);
				eventQueue.queueEvent(event);
			}
			else if (eventObject.has("music_volume")) {
				MusicVolumeEvent event = parseVolumeEvent(eventObject);
				eventQueue.queueEvent(event);
			}
		}
	}

	// EVENT BUILDERS

	private PlayEvent parsePlayEvent(JSONObject object) {
		AudioEvent.Type type;
		String audioID;
		if (object.has("music")) {
			type = AudioEvent.Type.MUSIC;
			audioID = object.getString("music");
		}
		else if (object.has("audio")) {
			type = AudioEvent.Type.SFX;
			audioID = object.getString("audio");
		}
		else {// default
			type = AudioEvent.Type.SFX;
			audioID = "sample";
		}
		float startTime = object.getFloat("playfrom");
		float duration = object.getFloat("playto");
		boolean loop = object.optBoolean("loop", false);
		// if not play through, then play for difference in time
		if (duration != -1) duration = duration - startTime;

		// choose mode based on settings
		PlayEvent.PlayMode mode;
		if (loop) mode = PlayEvent.PlayMode.LOOP;
		else if (duration == -1) mode = PlayEvent.PlayMode.FULL;
		else mode = PlayEvent.PlayMode.PARTIAL;

		return new PlayEvent(audioID, type, mode, startTime, duration);
	}

	private FadeMusicEvent parseFadeEvent(JSONObject object) {
		JSONObject fade = object.getJSONObject("fade_music");
		float targetVol = fade.getFloat("target");
		float fadeTime = fade.getFloat("time");
		return new FadeMusicEvent(targetVol, fadeTime);
	}

	private StopMusicEvent parseStopMusicEvent(JSONObject object) {
		String audioID = object.optString("music", "ALL");
		return new StopMusicEvent(audioID);
	}

	private MusicVolumeEvent parseVolumeEvent(JSONObject object) {
		float volume = object.getFloat("music_volume");
		return new MusicVolumeEvent(volume);
	}

}
