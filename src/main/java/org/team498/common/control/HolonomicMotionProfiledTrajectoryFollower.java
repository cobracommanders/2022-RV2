package org.team498.common.control;

import org.team498.common.math.RigidTransform2;
import org.team498.common.math.Rotation2;
import org.team498.common.math.Vector2;
import org.team498.common.util.HolonomicDriveSignal;
import org.team498.common.util.HolonomicFeedforward;

public class HolonomicMotionProfiledTrajectoryFollower extends TrajectoryFollower<HolonomicDriveSignal> {
    private PIDController forwardController;
    private PIDController strafeController;
    private PIDController rotationController;

    private HolonomicFeedforward feedforward;

    private Trajectory.State lastState = null;

    private boolean finished = false;

    public HolonomicMotionProfiledTrajectoryFollower(PIDConstants translationConstants, PIDConstants rotationConstants,
                                                     HolonomicFeedforward feedforward) {
        this.forwardController = new PIDController(translationConstants);
        this.strafeController = new PIDController(translationConstants);
        this.rotationController = new PIDController(rotationConstants);
        this.rotationController.setContinuous(true);
        this.rotationController.setInputRange(0.0, 2.0 * Math.PI);

        this.feedforward = feedforward;
    }

    @Override
    protected HolonomicDriveSignal calculateDriveSignal(RigidTransform2 currentPose, Vector2 velocity,
                                               double rotationalVelocity, Trajectory trajectory, double time,
                                               double dt) {
        if (time > trajectory.getDuration()) {
            finished = true;
            return new HolonomicDriveSignal(Vector2.ZERO, Rotation2.ZERO, false);
        }

        lastState = trajectory.calculate(time);

        Vector2 segmentVelocity = Vector2.fromAngle(lastState.getPathState().getHeading()).scale(lastState.getVelocity());
        Vector2 segmentAcceleration = Vector2.fromAngle(lastState.getPathState().getHeading()).scale(lastState.getAcceleration());

        Vector2 feedforwardVector = feedforward.calculateFeedforward(segmentVelocity, segmentAcceleration);

        forwardController.setSetpoint(lastState.getPathState().getPosition().x);
        strafeController.setSetpoint(lastState.getPathState().getPosition().y);
        rotationController.setSetpoint(lastState.getPathState().getRotation().toRadians());

        return new HolonomicDriveSignal(
                new Vector2(
                        forwardController.calculate(currentPose.translation.x, dt) + feedforwardVector.x,
                        strafeController.calculate(currentPose.translation.y, dt) + feedforwardVector.y
                ),
                Rotation2.fromRadians(rotationController.calculate(currentPose.rotation.toRadians(), dt)),
                true
        );
    }

    public Trajectory.State getLastState() {
        return lastState;
    }

    @Override
    protected boolean isFinished() {
        return finished;
    }

    @Override
    protected void reset() {
        forwardController.reset();
        strafeController.reset();
        rotationController.reset();

        finished = false;
    }
}