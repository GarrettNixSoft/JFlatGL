package com.gnix.jflatgl.core.util.math;

import com.gnix.jflatgl.core.entity.Collider;
import com.gnix.jflatgl.core.entity.Entity;
import com.gnix.jflatgl.core.util.configuration.Config;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Collisions {

	/**
	 * Create a Vector4f representation of an entity collision box, in absolute coordinates.
	 * @param x the x position (center)
	 * @param y the y position (center)
	 * @param width the width of the box
	 * @param height the height of the box
	 * @return a Vector4f representing {x1,y1,x2,y2} (NOT {x,y,w,h})
	 */
	public static Vector4f createCollisionBox(float x, float y, float width, float height) {
		return new Vector4f(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
	}

	/**
	 * Create a Vector4f representation of an entity collision box, in absolute coordinates.
	 * @param position the entity position
	 * @param size the entity size
	 * @return a Vector4f representing {x1,y1,x2,y2} (NOT {x,y,w,h})
	 */
	public static Vector4f createCollisionBox(Vector2f position, Vector2f size) {
		return new Vector4f(position.x - size.x / 2, position.y - size.y / 2, position.x + size.x / 2, position.y + size.y / 2);
	}

	/**
	 * Create a Vector4f representation of an entity collision box, in absolute coordinates.
	 * @param position the entity position (center)
	 * @param size the entity size
	 * @return a Vector4f representing {x1,y1,x2,y2} (NOT {x,y,w,h})
	 */
	public static Vector4f createCollisionBox(Vector2f position, Vector2f size, float scale) {
		Vector2f scaledSize = new Vector2f(size.x * scale, size.y * scale);
		return new Vector4f(position.x - scaledSize.x / 2, position.y - scaledSize.y / 2, position.x + scaledSize.x / 2, position.y + scaledSize.y / 2);
	}

	/**
	 * Create a Vector4f representation of an entity collision box, in absolute coordinates.
	 * @param position the entity position
	 * @param size the entity size
	 * @return a Vector4f representing {x1,y1,x2,y2} (NOT {x,y,w,h})
	 */
	public static Vector4f createCollisionBox(Vector3f position, Vector2f size) {
		return new Vector4f(position.x - size.x / 2, position.y - size.y / 2, position.x + size.x / 2, position.y + size.y / 2);
	}

	public static boolean contains(Vector2f location, Vector2f size, Vector2f point) {
		return point.x >= location.x - size.x / 2 &&
				point.x <= location.x + size.x / 2 &&
				point.y >= location.y - size.y / 2 &&
				point.y <= location.y + size.y / 2;
	}

	public static boolean contains(Vector2f location, Vector2f size, Vector2i point) {
		return point.x >= location.x - size.x / 2 &&
				point.x <= location.x + size.x / 2 &&
				point.y >= location.y - size.y / 2 &&
				point.y <= location.y + size.y / 2;
	}

	public static boolean contains(Vector4f bounds, Vector2f point) {
		return point.x >= bounds.x && point.x <= bounds.z &&
				point.y >= bounds.y && point.y <= bounds.w;
	}

	public static boolean contains(Vector4f bounds, Vector2i point) {
		return point.x >= bounds.x && point.x <= bounds.z &&
				point.y >= bounds.y && point.y <= bounds.w;
	}

	public static boolean contains(Vector4f bounds1, Vector4f bounds2) {
		return bounds1.x <= bounds2.x && bounds1.y <= bounds2.y &&
				bounds1.z >= bounds2.z && bounds1.w >= bounds2.w;
	}

	public static boolean intersects(Vector2f location1, Vector2f size1, Vector2f location2, Vector2f size2) {
		Vector4f bounds1 = createCollisionBox(location1, size1);
		Vector4f bounds2 = createCollisionBox(location2, size2);
		return intersects(bounds1, bounds2);
	}

	public static boolean intersects(Vector4f bounds1, Vector4f bounds2) {
		Vector2f[] corners1 = new Vector2f[4];
		corners1[0] = new Vector2f(bounds1.x, bounds1.y);
		corners1[1] = new Vector2f(bounds1.z, bounds1.y);
		corners1[2] = new Vector2f(bounds1.z, bounds1.w);
		corners1[3] = new Vector2f(bounds1.x, bounds1.w);
		Vector2f[] corners2 = new Vector2f[4];
		corners2[0] = new Vector2f(bounds2.x, bounds2.y);
		corners2[1] = new Vector2f(bounds2.z, bounds2.y);
		corners2[2] = new Vector2f(bounds2.z, bounds2.w);
		corners2[3] = new Vector2f(bounds2.x, bounds2.w);
		return checkIntersectionOneWay(bounds1, corners2) || checkIntersectionOneWay(bounds2, corners1);
	}

	private static boolean checkIntersectionOneWay(Vector4f bounds, Vector2f[] corners) {
		if (Config.DEBUG_MODE) {
			if (contains(bounds, corners[0])) {
//				Logger.log("Top left corner contained");
				return true;
			} else if (contains(bounds, corners[1])) {
//				Logger.log("Top right corner contained");
				return true;
			} else if (contains(bounds, corners[2])) {
//				Logger.log("Bottom right corner contained");
				return true;
			} else if (contains(bounds, corners[3])) {
//				Logger.log("Bottom left corner contained");
				return true;
			} else return false;
		}
		else {
			return  contains(bounds, corners[0]) ||
					contains(bounds, corners[1]) ||
					contains(bounds, corners[2]) ||
					contains(bounds, corners[3]);
		}
	}

	/**
	 * Determine what proportion of {@code rect1}'s area
	 * overlaps with {@code rect2}'s area.
	 * @param rect1 the rect to find the proportion of
	 * @param rect2 the rect that is intersected
	 * @return a float in the range [0..1]
	 */
	public static float overlapProportion(Vector4f rect1, Vector4f rect2) {
		// there is no overlap if they don't intersect
		if (!intersects(rect1, rect2)) return 0;
		// find the area of rect1
		float rectArea = (rect1.z - rect1.x) * (rect1.w - rect1.y);
//		Logger.log("Rect area = " + rectArea);
		// find the rectangle representing the overlapping area
		float l = Math.max(rect1.x, rect2.x);
		float r = Math.min(rect1.z, rect2.z);
		float t = Math.max(rect1.y, rect2.y);
		float b = Math.min(rect1.w, rect2.w);
		// calculate its area
		float overlapArea = (r - l) * (b - t);
//		Logger.log("overlap area = " + overlapArea);
		// return the proportion
		return overlapArea / rectArea;
	}

	/**
	 * Determine if a line segment intersects a rectangle.
	 * @param bounds the bounds of the rectangle
	 * @param point1 line point 1
	 * @param point2 line point 2
	 * @return {@code true} if the line segment intersects any side of the rectangle or is contained within the rectangle
	 */
	public static boolean lineIntersects(Vector4f bounds, Vector2f point1, Vector2f point2) {
		// check bounds contain whole segment
		if (contains(bounds, point1) && contains(bounds, point2)) return true;
		// generate Vec2s for each point; check segments along the way to potentially save time
		Vector2f tl = new Vector2f(bounds.x, bounds.y);
		Vector2f tr = new Vector2f(bounds.z, bounds.y);
		if (linesIntersect(tl, tr, point1, point2)) return true;
		Vector2f br = new Vector2f(bounds.z, bounds.w);
		if (linesIntersect(tr, br, point1, point2)) return true;
		Vector2f bl = new Vector2f(bounds.x, bounds.w);
		if (linesIntersect(br, bl, point1, point2)) return true;
		// this is the last check, so just return whether it succeeds or fails
		return linesIntersect(tl, bl, point1, point2);
	}

	/**
	 * Determine if two line segments intersect.
	 * @param l1p1 line 1 point 1
	 * @param l1p2 line 1 point 2
	 * @param l2p1 line 2 point 1
	 * @param l2p2 line 2 point 2
	 * @return {@code true} if the line segments intersect
	 * (thanks to @Callum Rogers on StackOverflow)
	 */
	public static boolean linesIntersect(Vector2f l1p1, Vector2f l1p2, Vector2f l2p1, Vector2f l2p2) {
		// first check dot product; if parallel, no intersection is possible
		Vector2f l1 = new Vector2f(l1p2).sub(l1p1);
		Vector2f l2 = new Vector2f(l2p2).sub(l2p1);
		float dot = l1.x * l2.y - l1.y * l2.x;
		if (dot == 0) return false;
		// if not parallel, check segment intersection
		Vector2f c = new Vector2f(l2p1).sub(l1p1);
		float t = (c.x * l2.y - c.y * l2.x) / dot;
		if (t < 0 || t > 1) return false;
		// no idea what that ^ does, nor this either:
		float u = (c.x * l1.y - c.y * l1.x) / dot;
		if (u < 0 || u > 1) return false;
		// bonus: calculate the intersection point
		// intersection = new Vector2f(l1p1).add((new Vector2f(l1)).mul(t));
		return true;
	}

	/**
	 * Check for a collision between a Collider and an arbitrary rectangle.
	 * @param collider The Entity
	 * @param bounds The Rectangle
	 * @return True if the collider's hitbox intersects with the rectangle.
	 */
	public static boolean collision(Collider collider, Vector4f bounds) {
		return intersects(collider.getHitbox(), bounds);
	}

	/**
	 * Check for a collision between two Colliders.
	 * @param a Entity A
	 * @param b Entity B
	 * @return True if the colliders are unique and their hitboxes intersect.
	 */
	public static boolean collision(Collider a, Collider b) {
		// the results of this check appear to be dependent on parameter order;
		// in order to ensure collisions are detected regardless of order,
		// collisions are checked using both arrangements of parameters
		return a != b && (intersects(a.getHitbox(), b.getHitbox()) || intersects(b.getHitbox(), a.getHitbox()));
	}

}
