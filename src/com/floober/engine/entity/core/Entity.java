package com.floober.engine.entity.core;

import com.floober.engine.animation.Animation;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.entity.attachments.EntityAttachableTo;
import com.floober.engine.entity.projectile.Projectile;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.renderEngine.util.Layers;
import com.floober.engine.util.Logger;
import com.floober.engine.util.math.Collisions;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Entity {

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

	public Entity() {
		this.x = 0;
		this.y = 0;
		this.layer = Layers.DEFAULT_LAYER;
		// init a null Texture that can be set/moved later
		textureElement = new TextureElement(null, 0, 0, 0, 0, 0, true);
	}

	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		this.layer = Layers.DEFAULT_LAYER;
		// init a null Texture that can be set/moved later
		textureElement = new TextureElement(null, 0, 0, 0, 0, 0, true);
	}

	public Entity(float x, float y, int layer) {
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
	public int getLayer() { return layer; }
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

	public void damage(Projectile projectile) {}

	public void kill() {
		Logger.log("ENTITY " + entityID + " KILLED");
		damage(maxHealth);
	}

	protected void fixBounds() {
		if (x < 0) x = 0;
		if (y < 0) y = 0;
	}

	// screen bounds
	public boolean notOnScreen() {
		return x + width / 2 < 0 ||
				x - width / 2 > DisplayManager.getPrimaryGameWindow().getWidth() ||
				y + height / 2 < 0 ||
				y - height / 2 > DisplayManager.getPrimaryGameWindow().getHeight();
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