package com.floober.engine.entity.attachments;

import com.floober.engine.entity.core.Entity;

public interface Attachable {

	void attachTo(EntityAttachableTo target);
	void detach();

	boolean canAttach();

	Entity getAsEntity();

}