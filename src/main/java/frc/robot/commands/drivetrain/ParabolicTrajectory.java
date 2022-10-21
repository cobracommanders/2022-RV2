package frc.robot.commands.drivetrain;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class ParabolicTrajectory extends CommandBase {
    private final Drivetrain drivetrain;
    private double t;
    private double t1;
    // private double x;
    // private double y;
    // private double r;
    private double t2;	
    private double dx;
    private double dy;
    private double dr;
    // private double dt;
    private double xScalar;
    private double yScalar;
    private double rps;
    private Timer timer = new Timer();
    /** 
     * @apiNote t is zero at x = 0 and y = 0
     * @apiNote all units in meters
     * @param drivetrain drivetrain
     * @param xScalar coefficient of x in parabolic equation
     * @param yScalar coefficient of y in parabolic equation
     * @param rps rotations per second
     * @param t1 initial value of t
     * @param t2 final value of t
    */
	public ParabolicTrajectory(Drivetrain drivetrain, double xScalar, double yScalar, double rps, double t1, double t2) {
		this.drivetrain = drivetrain;
                this.xScalar = xScalar;
                this.yScalar = yScalar;
                this.rps = rps;
                this.t1 = t1;
                this.t2 = t2;
                addRequirements(this.drivetrain);
	}

	@Override
	public void initialize() {
		timer.reset();
        timer.start();
	}

	@Override
	public void execute() {
                t = timer.get() + t1;
                // y = t * t;
                // x = 1.0;
                // r = t;
                dy = 2.0 * t * yScalar;
                dx = 1.0 * xScalar;
                dr = 1.0 * Math.PI * rps;
                drivetrain.drive(new ChassisSpeeds(dx, dy, dr));
                // dt = timer.get() + t1 - t;
	}

	@Override
	public void end(boolean interrupted) {
                drivetrain.drive(new ChassisSpeeds());
				timer.stop();
				timer.reset();
	}

	@Override
	public boolean isFinished() {
	    return t >= t2 - t1;
	}
}