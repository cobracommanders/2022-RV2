package org.team498.common.math;

import java.util.Objects;

import org.team498.common.util.Interpolable;

import edu.wpi.first.math.geometry.Pose2d;

public final class RigidTransform2 implements Interpolable<RigidTransform2> {
    public static final RigidTransform2 ZERO = new RigidTransform2(Vector2.ZERO, Rotation2.ZERO);

    /**
     * The translation of the transform
     */
    public final Vector2 translation;

    /**
     * The rotation of the transform
     */
    public final Rotation2 rotation;

    /**
     * Create a new rigid transform from a translation and a rotation.
     *
     * @param translation The translation
     * @param rotation    The rotation
     */
    public RigidTransform2(Vector2 translation, Rotation2 rotation) {
        this.translation = translation;
        this.rotation = rotation;
    }

	public Pose2d toPose2d(RigidTransform2 pose) {
		return new Pose2d(
			Vector2.translation2d(pose.translation),
			Rotation2.toRotation2d(pose.rotation)
		);
	}
	public static RigidTransform2 fromPose2d(Pose2d pose) {
		return new RigidTransform2(
			new Vector2(pose.getX(), pose.getY()),
			Rotation2.fromRadians(pose.getRotation().getRadians())
		);
	}

    private static Vector2 intersectionInternal(RigidTransform2 a, RigidTransform2 b) {
        final double t = ((a.translation.x - b.translation.x) * b.rotation.tan + b.translation.y - a.translation.y) /
                (a.rotation.sin - a.rotation.cos * b.rotation.tan);
        return a.translation.add(Vector2.fromAngle(a.rotation).scale(t));
    }

    /**
     * Adds the effects of this rigid transform and another rigid transform together.
     *
     * @param other The rigid transform to apply
     * @return A rigid transform with the effects of both this and another rigid transform
     */
    public RigidTransform2 transformBy(RigidTransform2 other) {
        return new RigidTransform2(translation.add(other.translation.rotateBy(rotation)), rotation.rotateBy(other.rotation));
    }

    /**
     * Gets the rigid transform that would undo the effects of this transform.
     *
     * @return The inverse of this transform
     */
    public RigidTransform2 inverse() {
        Rotation2 inverseRotation = rotation.inverse();
        return new RigidTransform2(translation.inverse().rotateBy(inverseRotation), inverseRotation);
    }

    /**
     * Gets the point of intersection between this rigid transform and another.
     *
     * @param other The other rigid transform
     * @return The point of intersection between the two transforms
     */
    public Vector2 intersection(RigidTransform2 other) {
        if (rotation.isParallel(other.rotation)) {
            return new Vector2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        if (Math.abs(rotation.cos) < Math.abs(other.rotation.cos)) {
            return intersectionInternal(this, other);
        } else {
            return intersectionInternal(other, this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{T: " + translation + ", R: " + rotation + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RigidTransform2)) {
            return false;
        }

        RigidTransform2 other = (RigidTransform2) obj;

        return translation.equals(other.translation) && rotation.equals(other.rotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translation, rotation);
    }

    @Override
    public RigidTransform2 interpolate(RigidTransform2 other, double t) {
        return new RigidTransform2(translation.interpolate(other.translation, t), rotation.interpolate(other.rotation, t));
    }
}
