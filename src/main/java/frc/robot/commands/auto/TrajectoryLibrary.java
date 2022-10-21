package frc.robot.commands.auto;

import frc.robot.subsystems.Drivetrain;
import org.team498.common.control.SimplePathBuilder;
import org.team498.common.control.Trajectory;
import org.team498.common.math.Rotation2;
import org.team498.common.math.Vector2;

public class TrajectoryLibrary {
    public final static Trajectory forward_one = new Trajectory(
        new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO).lineTo(new Vector2(0, 5)).build(), 
        Drivetrain.TRAJECTORY_CONSTRAINTS, 
        0.1);
}
