package com.floober.engine.util;

import com.floober.engine.entity.core.Entity;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Collisions {

	public static Vector4f createCollisionBox(Vector2f position, Vector2f size) {
		return new Vector4f(position.x - size.x / 2, position.y - size.y / 2, position.x + size.x / 2, position.y + size.y / 2);
	}

	public static Vector4f createCollisionBox(Vector3f position, Vector2f size) {
		return new Vector4f(position.x - size.x / 2, position.y - size.y / 2, position.x + size.x / 2, position.y + size.y / 2);
	}

	public static boolean contains(Vector2f location, Vector2f size, Vector2f point) {
		return point.x >= location.x - size.x / 2 &&
				point.x <= location.x + size.x / 2 &&
				point.y >= location.y - size.y / 2 &&
				point.y <= location.y + size.y / 2;
	}

	public static boolean contains(Vector4f bounds, Vector2f point) {
		return point.x >= bounds.x && point.x <= bounds.z &&
				point.y >= bounds.y && point.y <= bounds.w;
	}

	public static boolean contains(Vector4f bounds1, Vector4f bounds2) {
		return bounds1.x < bounds2.x && bounds1.y < bounds2.y &&
				bounds1.z > bounds2.z && bounds1.w > bounds2.w;
	}

	public static boolean intersects(Vector2f location1, Vector2f size1, Vector2f location2, Vector2f size2) {
		Vector4f bounds = new Vector4f(location1.x, location1.y, location2.x, location2.y);
		Vector2f point1 = new Vector2f(location2).sub(new Vector2f(size2).div(2));
		Vector2f point2 = new Vector2f(location2.x + size2.x / 2, location2.y - size2.y / 2);
		Vector2f point3 = new Vector2f(location2).add(new Vector2f(size2).div(2));
		Vector2f point4 = new Vector2f(location2.x - size2.x / 2, location2.y + size2.y / 2);
		return contains(bounds, point1) || contains(bounds, point2) || contains(bounds, point3) || contains(bounds, point4);
	}

	public static boolean intersects(Vector4f bounds1, Vector4f bounds2) {
		Vector2f point1 = new Vector2f(bounds2.x, bounds2.y);
		Vector2f point2 = new Vector2f(bounds2.z, bounds2.y);
		Vector2f point3 = new Vector2f(bounds2.z, bounds2.w);
		Vector2f point4 = new Vector2f(bounds2.x, bounds2.w);
		return contains(bounds1, point1) || contains(bounds1, point2) || contains(bounds1, point3) || contains(bounds1, point4);
	}

	/**
	 * Check for a collision between an Entity and an arbitrary rectangle.
	 * @param entity The Entity
	 * @param bounds The Rectangle
	 * @return True if the entity's hitbox intersects with the rectangle.
	 */
	public static boolean collision(Entity entity, Vector4f bounds) {
		return intersects(entity.getHitbox(), bounds);
	}

	/**
	 * Check for a collision between two Entities.
	 * @param a Entity A
	 * @param b Entity B
	 * @return True if the entities are unique and their hitboxes intersect.
	 */
	public static boolean collision(Entity a, Entity b) {
		return a != b && intersects(a.getHitbox(), b.getHitbox());
	}

}