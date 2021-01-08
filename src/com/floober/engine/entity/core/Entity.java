package com.floober.engine.entity.core;

import com.floober.engine.animation.Animation;
import com.floober.engine.display.Display;
import com.floober.engine.entity.attachments.EntityAttachableTo;
import com.floober.engine.entity.util.EntityHandler;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.util.Collisions;
import com.floober.engine.util.Logger;
import com.floober.engine.util.configuration.Config;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Entity {

	// game access
	protected EntityHandler entityHandler;

	// retrieving this entity
	protected String entityID;

	// position in world
	protected float x, y;
	protected int layer;

	// size in pixels
	protected float width, height;

	// collision box
	protected float cwidth, cheight;

	// motion
	protected float dx, dy;
	protected float moveSpeed, maxSpeed;

	// health
	protected float health, maxHealth;
	protected boolean dead;

	// deletion
	protected boolean remove;

	// appearance
	protected Animation animation;
	protected TextureElement textureElement;

	// sync time for update methods
	protected float time;

	// attaching this entity to another
	protected EntityAttachableTo entityAttachedTo;

	public Entity(EntityHandler entityHandler) {
		this.entityHandler = entityHandler;
		this.x = 0;
		this.y = 0;
		this.layer = MasterRenderer.NUM_LAYERS / 2;
		// init a null Texture that can be set/moved later
		textureElement = new TextureElement(null, 0, 0, 0, 0, 0, true);
	}

	public Entity(EntityHandler entityHandler, float x, float y) {
		this.entityHandler = entityHandler;
		this.x = x;
		this.y = y;
		this.layer = MasterRenderer.NUM_LAYERS / 2;
		// init a null Texture that can be set/moved later
		textureElement = new TextureElement(null, 0, 0, 0, 0, 0, true);
	}

	public Entity(EntityHandler entityHandler, float x, float y, int layer) {
		this.entityHandler = entityHandler;
		this.x = x;
		this.y = y;
		this.layer = layer;
		// init a null Texture that can be set/moved later
		textureElement = new TextureElement(null, 0, 0, 0, 0, 0, true);
	}

	// GETTERS
	public String getId() { return entityID; }
	public TextureElement getTextureElement() { return textureElement; }
	public float getX() { return x; }
	public float getY() { return y; }
	public float getLayer() { return layer; }
	public float getDX() { return dx; }
	public float getDY() { return dy; }
	public Vector2f getPosition() { return new Vector2f(x, y); }
	public Vector3f getPosition3() { return new Vector3f(x, y, layer); }
	public float getMoveSpeed() { return moveSpeed; }
	public float getMaxSpeed() { return maxSpeed; }
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	public float getHealth() {
		return health;
	}
	public float getCWidth() { return cwidth; }
	public float getCHeight() { return cheight; }
	public float getMaxHealth() {
		return maxHealth;
	}
	public boolean isDead() {
		return dead;
	}
	public EntityHandler getEntityHandler() { return entityHandler; }

	public boolean remove() { return remove; }

	public Vector4f getHitbox() {
		return Collisions.createCollisionBox(getPosition(), new Vector2f(cwidth, cheight));
	}

	// SETTERS
	public void setId(String entityID) { this.entityID = entityID; }

	// simulating the entity
	public abstract void update();
	public abstract void render();
	protected void handleAttachments() {}

	protected void updateTextureElement() {
		textureElement.setTexture(animation.getCurrentFrame());
		textureElement.setPosition(x, y, layer);
		textureElement.setSize(width, height);
		textureElement.transform();
	}

	// interacting with the entity
	public void damage(float damage) {}

	public void kill() {
		Logger.log("ENTITY " + entityID + " KILLED");
		damage(maxHealth);
	}

	protected void fixBounds() {
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		// implement upper bounds
	}

	// screen bounds
	public boolean notOnScreen() {
		return x + width / 2 < 0 ||
				x - width / 2 > Display.WIDTH ||
				y + height / 2 < 0 ||
				y - height / 2 > Display.HEIGHT;
	}

	public boolean onScreen() { // for ease of reading
		return !notOnScreen();
	}

	// moving the entity
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(float x, float y, int layer) {
		this.x = x;
		this.y = y;
		this.layer = layer;
	}

	public void setPosition(Vector2f position) {
		setPosition(position.x(), position.y());
	}

	public void setPosition(Vector3f position) {
		setPosition(position.x(), position.y(), (int) position.z());
	}

	public void setPositionAndResetVelocity(float x, float y) {
		setPosition(x, y);
		dx = dy = 0;
	}

	public void setPositionAndResetVelocity(float x, float y, int z) {
		setPosition(x, y, z);
		dx = dy = 0;
	}

	public void setPositionAndResetVelocity(Vector2f position) {
		setPositionAndResetVelocity(position.x(), position.y());
	}

	public void setPositionAndResetVelocity(Vector3f position) {
		setPositionAndResetVelocity(position.x(), position.y(), (int) position.z());
	}

	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public void setLayer(int layer) { this.layer = layer; }

	public void setXVel(float dx) { this.dx = dx; }
	public void setYVel(float dy) { this.dy = dy; }

	public void addXVel(float dx) { this.dx += dx; }
	public void addYVel(float dy) { this.dy += dy; }

	public void move(float dx, float dy) {
		x += dx;
		y += dy;
	}

}