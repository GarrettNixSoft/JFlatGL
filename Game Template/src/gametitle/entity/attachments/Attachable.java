package gametitle.entity.attachments;

import com.gnix.jflatgl.core.entity.Entity;

public interface Attachable {

	void attachTo(EntityAttachableTo target);
	void detach();

	boolean canAttach();

	Entity getAsEntity();

}
