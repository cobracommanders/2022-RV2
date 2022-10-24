package org.team498.lib.robot;

import org.team498.lib.math.Rotation2;
import org.team498.lib.math.Vector2;

public class ChassisVelocity {
    public final double x;
    public final double y;
    public final double r;
    /**
     * Creates a Chassis Velocity
     * @param x x velocity
     * @param y y velocity
     * @param r rotation in radians
     */
    public ChassisVelocity(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
    public ChassisVelocity(Vector2 vector, Rotation2 rotation) {
        x = vector.x;
        y = vector.y;
        r = rotation.toRadians();
    }
    public ChassisVelocity(Vector2 vector, double rotation) {
        x = vector.x;
        y = vector.y;
        r = rotation;
    }
    public Vector2 getTranslationalVelocity() {
        return null;
    }
    public double getAngularVelocity() {
        return 0;
    }
}
