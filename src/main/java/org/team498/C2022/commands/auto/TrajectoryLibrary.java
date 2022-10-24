package org.team498.C2022.commands.auto;

import org.team498.C2022.subsystems.Drivetrain;
import org.team498.lib.control.SimplePathBuilder;
import org.team498.lib.control.Trajectory;
import org.team498.lib.math.Rotation2;
import org.team498.lib.math.Vector2;

public class TrajectoryLibrary {
    public final static Trajectory forward_one = new Trajectory(
        new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO).lineTo(new Vector2(0, 5)).build(), 
        Drivetrain.TRAJECTORY_CONSTRAINTS, 
        0.1);
}
