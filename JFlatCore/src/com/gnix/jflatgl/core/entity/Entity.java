package com.gnix.jflatgl.core.entity;

import com.gnix.jflatgl.animation.Animation;
import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.camera.Camera;
import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.util.Layers;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.math.Collisions;
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
	protected boolean facingRight;

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

	// attaching this entity to another
//	protected EntityAttachableTo entityAttachedTo;

	// ******************************** CONSTRUCTORS ********************************
	public Entity() {
		this.x = 0;
		this.y = 0;
		this.layer = Layers.DEFAULT_LAYER;
		// init a null Texture that can be set/moved later
		textureElement = new TextureElement(Game.getTexture("default"), 0, 0, 0, 0, 0, true);
	}

	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		this.layer = Layers.DEFAULT_LAYER;
		// init a null Texture that can be set/moved later
		textureElement = new TextureElement(Game.getTexture("default"), 0, 0, 0, 0, 0, true);
	}

	public Entity(float x, float y, int layer) {
		this.x = x;
		this.y = y;
		this.layer = layer;
		// init a null Texture that can be set/moved later
		textureElement = new TextureElement(Game.getTexture("default"), 0, 0, 0, 0, 0, true);
	}

	// ******************************** GETTERS ********************************
	public String getId() {
		return entityID;
	}
	public TextureElement getTextureElement() {
		return textureElement;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public int getLayer() {
		return layer;
	}
	public float getDX() {
		return dx;
	}
	public float getDY() {
		return dy;
	}
	public Vector2f getPosition() {
		return new Vector2f(x, y);
	}
	public Vector3f getPosition3() {
		return new Vector3f(x, y, layer);
	}
	public float getMoveSpeed() {
		return moveSpeed;
	}
	public float getMaxSpeed() {
		return maxSpeed;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public float getHealth() {
		return health;
	}
	public float getCWidth() {
		return cwidth;
	}
	public float getCHeight() {
		return cheight;
	}
	public float getMaxHealth() {
		return maxHealth;
	}
	public boolean isDead() {
		return dead;
	}

	public boolean remove() {
		return remove;
	}

	public Vector4f getHitbox() {
		return Collisions.createCollisionBox(getPosition(), new Vector2f(cwidth, cheight));
	}

	// ******************************** SETTERS ********************************
	public void setId(String entityID) {
		this.entityID = entityID;
	}

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

	// ******************************** SIMULATION ********************************
	public abstract void update();

	public abstract void render(Camera camera);

	protected void handleAttachments() {}

	protected void updateTextureElement() {
		textureElement.setTexture(animation.getCurrentFrame());
		textureElement.setPosition(x, y, layer);
		textureElement.setSize(width, height);
		textureElement.transform();
	}

	// ******************************** INTERACTION ********************************
	public void damage(float damage) {}

	public void kill() {
		Logger.log("ENTITY " + entityID + " KILLED");
		damage(maxHealth);
	}

	// ******************************** WORLD BOUNDS ********************************
	protected void fixBounds() {
		if (x < 0) x = 0;
		if (y < 0) y = 0;
	}

	// screen bounds
	public boolean notOnScreen(Camera camera) {

		float screenX = camera.worldXToScreenX(x);
		float screenY = camera.worldYToScreenY(y);
		float screenWidth = width * camera.getScale();
		float screenHeight = height * camera.getScale();

		return screenX + screenWidth / 2 < 0 ||
				screenX - screenWidth / 2 > Game.width() ||
				screenY + screenHeight / 2 < 0 ||
				screenY - screenHeight / 2 > Game.height();
	}

	public boolean onScreen(Camera camera) { // for ease of reading
		return !notOnScreen(camera);
	}

}
