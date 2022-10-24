package frc.robot.commands.drivetrain;

import frc.robot.subsystems.Drivetrain;
import org.team498.common.control.Trajectory;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class FollowTrajectory extends CommandBase {
    private final Drivetrain drivetrain;
    private final Trajectory trajectory;

    public FollowTrajectory(Drivetrain drivetrain, Trajectory trajectory) {
        this.drivetrain = drivetrain;
        this.trajectory = trajectory;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.getFollower().follow(trajectory);
    }

    @Override
    public void end(boolean interuptable) {
        drivetrain.getFollower().cancel();
    }
    
    @Override
    public boolean isFinished() {
        return drivetrain.getFollower().getCurrentTrajectory().isEmpty();
    }
}