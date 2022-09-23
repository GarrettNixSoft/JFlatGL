package gametitle.entity.attachments;

import com.gnix.jflatgl.core.entity.Entity;
import com.gnix.jflatgl.core.entity.util.EntityHandler;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityAttachableTo extends Entity {

	protected EntityHandler entityHandler;

	// attaching entities to one another
	protected static class Attachment {
		public Attachable attachedEntity;
		public float xOffset;
		public Attachment(Attachable attachedEntity, float xOffset) {
			this.attachedEntity = attachedEntity;
			this.xOffset = xOffset;
		}
	}

	protected final List<Attachment> attachments = new ArrayList<>();

	// settings
	private boolean allowsJumping;

	public EntityAttachableTo(EntityHandler entityHandler, float x, float y, int layer) {
		super(x, y, layer);
		this.entityHandler = entityHandler;
		allowsJumping = true;
	}

	public void adjustOffset(Attachable attached, float xOffset) {
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < attachments.size(); i++) {
			Attachment attachment = attachments.get(i);
			if (attachment.attachedEntity.equals(attached)) {
				attachment.xOffset += xOffset;
				// check if this is too far to remain attached
				if (!offsetInRange(attachment)) {
					attached.detach();
				}
				break;
			}
		}
	}

	public void disallowJumping() {
		allowsJumping = false;
	}
	public boolean allowsJumping() {
		return allowsJumping;
	}

	protected abstract boolean offsetInRange(Attachment attachment);
	public abstract boolean checkPositioning(Attachable attachable);

	// ATTACHING ENTITIES
	public boolean attach(Attachable attachment) {
		if (checkPositioning(attachment)) {
			attachments.add(new Attachment(attachment, attachment.getAsEntity().getX() - x));
			return true;
		}
		else return false;
	}

	public void detach(Attachable other) {
		for (int i = 0; i < attachments.size(); i++) {
			if (attachments.get(i).attachedEntity.equals(other)) {
				attachments.remove(i);
				Logger.log("Attachment removed!");
				break;
			}
		}
	}
}
