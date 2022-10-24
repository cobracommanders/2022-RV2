package org.team498.lib.math;

import java.text.DecimalFormat;
import java.util.Objects;

import org.team498.lib.util.Interpolable;

import edu.wpi.first.math.geometry.Translation2d;

public class Vector2 implements Interpolable<Vector2> {

    public final double length;

    public final double x;
    public final double y;

    public static final Vector2 ZERO = new Vector2(0.0, 0.0);

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
        this.length = Math.hypot(x, y);
    }

    /**
     * Calculates the cross product of this vector and another vector in 3d space and returns the length.
     *
     * @param other The other vector to calculate the cross product with
     * @return The length of the calculated vector
     */
    public double cross(Vector2 vector) {
        return x * vector.y - y * vector.x;
    }

	public static Translation2d translation2d(Vector2 vector) {
		return new Translation2d(
			vector.length, 
			Rotation2.toRotation2d(vector.getAngle()));
	}

	/**
	 * Calculates the dot product of this vector and another vector.
     *
	 * @param other The other vector to calculate the dot product with
	 * @return The dot product of the two vectors
	 */
    public double dot(Vector2 vector) {
        return x * vector.x + y * vector.y;
    }

    /**
     * Adds this vector and another vector together.
     *
     * @param vector The vector to add
     * @return A vector with the result of the addition
     */
    public Vector2 add(Vector2 other){
        return add(other.x, other.y);
    }

    /**
     * Adds two scalar values to this vector.
     *
     * @param x The value to add to the x-coordinate
     * @param y The value to add to the y-coordinate
     * @return A vector with the result of the addition
     */
    public Vector2 add(double x, double y) {
        return new Vector2(this.x + x, this.y + y);
    }

	/**
     * Subtracts a vector from this vector.
     *
     * @param vector The vector to subtract from this vector
     * @return A vector with the result of the subtraction
	 */
    public Vector2 subtract(Vector2 other){
        return subtract(other.x, other.y);
    }

    /**
     * Subtracts two scalar values from this vector.
     *
     * @param x The value to subtract from the x-coordinate
     * @param y The value to subtract from the y-coordinate
     * @return A vector with the result of the subtraction
     */
    public Vector2 subtract(double x, double y) {
        return new Vector2(this.x - x, this.y - y);
    }

	/**
	 * Multiplies each component of the vector by a scalar value.
	 * @param scalar The scalar to multiply each component by.
	 * @return The vector scaled by the scalar.
	 */
    public Vector2 scale(double scalar) {
        return multiply(scalar, scalar);
    }
    /**
     * Preforms a component-wise multiplication on this vector with another vector.
     *
     * @param vector The vector to multiply by
     * @return A vector with the result of the multiplication
     */
    public Vector2 multiply(Vector2 other) {
        return multiply(other.x, other.y);
    }
    /**
     * Multiplies the components of this vector by two scalar values.
     *
     * @param x A scalar to multiply the x-coordinate by
     * @param y A scalar to multiply the y-coordinate by
     * @return A vector with the result of the multiplication
     * @since 0.1
     */
    public Vector2 multiply(double x, double y) {
        return new Vector2(this.x * x, this.y * y);
    }

    /**
     * Calculates the inverse of this vector
     * 
     * @return inverse vector
     */
    public Vector2 inverse() {
        return new Vector2(-x, -y);
    }

	/**
	 * Creates a unit vector from a rotation.
     *
	 * @param rotation The rotation to create the vector from
	 * @return A unit vector with the specified angle.
	 */
	public static Vector2 fromAngle(Rotation2 rotation) {
		return new Vector2(rotation.cos, rotation.sin);
	}

	/**
	 * Calculates the angle between two vectors.
	 *
	 * @param a The vector that the angle begins at.
	 * @param b The vector that the angle ends at.
	 * @return The angle between the two vectors.
	 */
	public static Rotation2 getAngleBetween(Vector2 a, Vector2 b) {
		double cos = a.dot(b) / (a.length * b.length);
		if (Double.isNaN(cos)) {
			return Rotation2.ZERO;
		}

		return Rotation2.fromRadians(Math.acos(MathUtils.clamp(cos, -1.0, 1.0)));
	}

    /**
     * Get the angle of the vector
     * 
     * @return a rotation representation of this vector
     */
    public Rotation2 getAngle() {
        return new Rotation2(x, y, true);
    }

    /**
	 * Rotates this vector by the specified rotation.
     *
	 * @param rotation How much the vector should be rotated.
	 * @return A vector rotated by the specified amount
	 */
	public Vector2 rotateBy(Rotation2 rotation) {
		return new Vector2(x * rotation.cos - y * rotation.sin, x * rotation.sin + y * rotation.cos);
	}

    /**
     * Calculates the normalized form of this vector.
     *
     * @return A unit vector with the same angle as this vector
     */
	public Vector2 normal() {
		return new Vector2(x / length, y / length);
	}

    @Override
    public Vector2 interpolate(Vector2 other, double t) {
        if (t <= 0.0) {
            return this;
        }
        else if (t >= 1.0) {
            return other;
        }
        return null;
    }
    
    public Vector2 extrapolate(Vector2 other, double t) {
        Vector2 delta = other.subtract(this);
        return this.add(delta.scale(t));
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector2)) {
			return false;
		}

		return equals((Vector2) obj, MathUtils.EPSILON);
	}

	public boolean equals(Vector2 other, double allowableError) {
		return MathUtils.epsilonEquals(x, other.x, allowableError) &&
				MathUtils.epsilonEquals(y, other.y, allowableError);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public String toString() {
		DecimalFormat fmt = new DecimalFormat("#0.000");
		return '(' + fmt.format(x) + ", " + fmt.format(y) + ')';
	}
}
