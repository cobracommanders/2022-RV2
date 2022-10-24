package org.team498.lib.util;

import org.team498.lib.math.Rotation2;
import org.team498.lib.math.Vector2;

public class HolonomicDriveSignal {
    private final Vector2 translation;
    private final Rotation2 rotation;
    private final boolean isFieldOriented;
    public HolonomicDriveSignal(Vector2 translation, Rotation2 rotation, boolean isFieldOriented) {
        this.translation = translation;
        this.rotation = rotation;
        this.isFieldOriented = isFieldOriented;
    }
    public HolonomicDriveSignal(Vector2 translation, double rotation, boolean isFieldOriented) {
        this.translation = translation;
        this.rotation = Rotation2.fromRadians(rotation);
        this.isFieldOriented = isFieldOriented;
    }

    public Vector2 getTranslation() {
        return translation;
    }
    public Rotation2 getRotation() {
        return rotation;
    }
    public boolean isFieldOriented() {
        return isFieldOriented;
    }
}
