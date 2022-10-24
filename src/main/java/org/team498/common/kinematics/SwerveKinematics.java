package org.team498.common.kinematics;

import java.util.Arrays;

import org.ejml.simple.SimpleMatrix;
import org.team498.common.math.Vector2;
import org.team498.common.robot.ChassisVelocity;

public class SwerveKinematics {
    private final Vector2[] moduleOffsets;

    private final SimpleMatrix inverseKinematics;
    private final SimpleMatrix forwardKinematics;

    public SwerveKinematics(Vector2... moduleOffsets) {
        if (moduleOffsets.length < 1) {
            throw new IllegalArgumentException("Must have at least 1 module");
        }

        this.moduleOffsets = Arrays.copyOf(moduleOffsets, moduleOffsets.length);

        inverseKinematics = new SimpleMatrix(moduleOffsets.length * 2, 3);
        for (int i = 0; i < moduleOffsets.length; i++) {
            inverseKinematics.setRow(i * 2 + 0, 0, 1.0, 0.0, -moduleOffsets[i].y);
            inverseKinematics.setRow(i * 2 + 1, 0, 0.0, 1.0, moduleOffsets[i].x);
        }
        forwardKinematics = inverseKinematics.pseudoInverse();
    }

    /**
     * Performs inverse kinematics to convert a desired chassis velocity into a set of swerve module velocities.
     *
     * @param velocity The desired overall robot velocity.
     * @return An array containing the module velocities required to reach the desired robot velocity. These velocities
     * may be outside the acceptable range of the modules. Use the
     * {@link #normalizeModuleVelocities(Vector2[], double) normalizeModuleVelocities} method to resolve this issue.
     */
    public Vector2[] toModuleVelocities(ChassisVelocity velocity) {
        SimpleMatrix chassisVelocityVector = new SimpleMatrix(3, 1);
        chassisVelocityVector.setColumn(0, 0,
                velocity.getTranslationalVelocity().x,
                velocity.getTranslationalVelocity().y,
                velocity.getAngularVelocity());

        SimpleMatrix moduleVelocitiesMatrix = inverseKinematics.mult(chassisVelocityVector);
        Vector2[] moduleVelocities = new Vector2[moduleOffsets.length];

        for (int i = 0; i < moduleOffsets.length; i++) {
            moduleVelocities[i] = new Vector2(
                    moduleVelocitiesMatrix.get(i * 2 + 0),
                    moduleVelocitiesMatrix.get(i * 2 + 1)
            );
        }

        return moduleVelocities;
    }

    /**
     * Performs forward kinematics to convert a set of swerve module velocities into a chassis velocity.
     *
     * @param moduleVelocities The velocities of the modules w.r.t the robot.
     * @return The chassis velocity that would result from the module velocities.
     */
    public ChassisVelocity toChassisVelocity(Vector2... moduleVelocities) {
        if (moduleVelocities.length != moduleOffsets.length) {
            throw new IllegalArgumentException("Amount of module velocities given does not match the amount of modules specified in the constructor");
        }

        SimpleMatrix moduleVelocitiesMatrix = new SimpleMatrix(moduleOffsets.length * 2, 1);
        for (int i = 0; i < moduleOffsets.length; i++) {
            moduleVelocitiesMatrix.setColumn(0, i * 2,
                    moduleVelocities[i].x,
                    moduleVelocities[i].y
            );
        }

        SimpleMatrix chassisVelocityVector = forwardKinematics.mult(moduleVelocitiesMatrix);
        return new ChassisVelocity(
                new Vector2(
                        chassisVelocityVector.get(0),
                        chassisVelocityVector.get(1)
                ),
                chassisVelocityVector.get(2)
        );
    }

    /**
     * Normalizes the module velocities using some maximum velocity.
     * <p>
     * Sometimes, the required velocity from one or more modules may be above the modules maximum attainable velocity.
     * To fix this issue, all the module velocities can be "normalized" so that all module velocities are below the
     * maximum attainable velocity while still keeping the ratio of velocities between the modules.
     *
     * @param moduleVelocities An array containing the module velocities. This array will be mutated to contain the
     *                         normalized velocities.
     * @param maximumVelocity  The absolute maximum velocity that a module can reach.
     */
    public static void normalizeModuleVelocities(Vector2[] moduleVelocities, double maximumVelocity) {
        double realMaxVelocity = Arrays.stream(moduleVelocities).mapToDouble(m -> m.length).max().orElseThrow();
        if (realMaxVelocity > maximumVelocity) {
            for (int i = 0; i < moduleVelocities.length; i++) {
                moduleVelocities[i] = moduleVelocities[i].scale(maximumVelocity / realMaxVelocity);
            }
        }
    }
}