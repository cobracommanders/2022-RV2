package org.team498.lib.math;

import java.text.DecimalFormat;
import java.util.Objects;

import org.team498.lib.util.Interpolable;

import edu.wpi.first.math.geometry.Rotation2d;

public class Rotation2 implements Interpolable<Rotation2> {
    public final double cos;
    public final double sin;
    public final double tan;

    public static final Rotation2 ZERO = new Rotation2(1.0, 0.0, false);

    public Rotation2(double x, double y, boolean normalize) {
        if (normalize) {
            double length = Math.sqrt(x * x + y * y);

            if(length > MathUtils.EPSILON) {
                x /= length;
                y /= length;
            }
            else {
                //return 0 if angle is indeterminable or zero
                x = 1;
                y = 0;
            }
        }
        this.cos = x;
        this.sin = y;
        if(MathUtils.epsilonEquals(cos, 0)) {
            if (sin >= 0) {
                this.tan = Double.POSITIVE_INFINITY;
            } else {
                this.tan = Double.NEGATIVE_INFINITY;
            }
        } else {
            this.tan = sin / cos;
        }
    }

	public static Rotation2d toRotation2d(Rotation2 rotation) {
		return new Rotation2d(rotation.toRadians());
	}

    /**
     * Create a new rotation from an angle in degrees.
     *
     * @param angle The angle the rotation should be in degrees.
     * @return The rotation.
     */
    public static Rotation2 fromDegrees(double angle) {
        return fromRadians(Math.toRadians(angle));
    }

    public static Rotation2 fromRadians(double angle) {
        return new Rotation2(Math.cos(angle), Math.sin(angle), false);
    }

    /**
     * Get the angle of this rotation in degrees.
     *
     * @return The angle in degrees.
     */
    public double toDegrees() {
        return Math.toDegrees(toRadians());
    }

    /**
     * Get the angle of this rotation in radians.
     *
     * @return The angle in radians.
     */
    public double toRadians() {
        double angle = Math.atan2(sin, cos);

        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle > 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }

        return angle;
    }
    /**
     * Rotate this rotation by another by adding the effects together.
     *
     * @param other How much to rotate this rotation by.
     * @return The rotation rotated by the other rotation.
     */
    public Rotation2 rotateBy(Rotation2 other) {
        // This is implemented as a rotation matrix. The rotation "this" is rotated by a rotation matrix created by the
        // rotation "other".
        // See https://en.wikipedia.org/wiki/Rotation_matrix for more information on rotation matrices.
        return new Rotation2(cos * other.cos - sin * other.sin,
                cos * other.sin + sin * other.cos, true);
    }

    /**
     * Calculate the normal this rotation.
     * The normal is perpendicular to this rotation.
     *
     * @return The normal of this rotation.
     */
    public Rotation2 normal() {
        return new Rotation2(-sin, cos, false);
    }

    /**
     * Calculate the rotation which would "undo" this rotation.
     *
     * @return The inverse of this rotation.
     */
    public Rotation2 inverse() {
        return new Rotation2(cos, -sin, false);
    }

    /**
     * Check whether this rotation is parallel to another rotation.
     * <p>
     * This is different from {@link #equals} because it also takes into account rotations that are facing the opposite
     * direction.
     *
     * @param other The rotation to check if it is parallel with.
     * @return If the rotations are parallel.
     */
    public boolean isParallel(Rotation2 other) {
        return MathUtils.epsilonEquals(Vector2.fromAngle(this).cross(Vector2.fromAngle(other)), 0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rotation2 interpolate(Rotation2 other, double t) {
        if (t <= 0.0) {
            return this;
        } else if (t >= 1.0) {
            return other;
        }

        double from = toRadians();
        double to = other.toRadians();

        double diff = Math.abs(from - to);
        if (diff > Math.PI) {
            if (from < to) {
                from += 2 * Math.PI;
            } else {
                to += 2 * Math.PI;
            }
        }

        return Rotation2.fromRadians(from + ((to - from) * t));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rotation2)) {
            return false;
        }

        Rotation2 other = (Rotation2) obj;

        return MathUtils.epsilonEquals(cos, other.cos) && MathUtils.epsilonEquals(sin, other.sin);
    }

    public boolean equals(Rotation2 other, double maxError) {
        return MathUtils.epsilonEquals(cos, other.cos, Math.abs(Math.cos(maxError))) &&
                MathUtils.epsilonEquals(sin, other.sin, Math.abs(Math.sin(maxError)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(cos, sin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        DecimalFormat fmt = new DecimalFormat("#0.000");
        return fmt.format(toDegrees()) + '\u00b0';
    }
}
