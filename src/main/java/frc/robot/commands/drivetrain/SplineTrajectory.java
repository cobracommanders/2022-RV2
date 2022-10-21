package frc.robot.commands.drivetrain;

import java.util.List;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.spline.PoseWithCurvature;
import edu.wpi.first.math.spline.QuinticHermiteSpline;
import edu.wpi.first.math.spline.SplineParameterizer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class SplineTrajectory extends CommandBase {
	private final Drivetrain drivetrain;
    private double[] x1;
    private double[] y1;
    private double[] x2;
    private double[] y2;
    private double y3;
    private double y4;
    private double y5;
    private double x3;
    private double x4;
    private double x5;
    private double dx;
    private double dy;
    private double dr;    
    private int t;
    private double maxVelocity;
    private double maxTime;
    private int tIndex;
    private int tIncrement;
    QuinticHermiteSpline spline;
    private List<PoseWithCurvature> parameterizedSpline;
    private Timer timer = new Timer();
    private double streamRateLimiter;
    /** 
     * @apiNote all units in meters
     * @param drivetrain drivetrain
     * @param x1 double array containing initial x[position, velocity, acceleration] (velocity and acceleration are typically zero)
     * @param y1 double array containing initial y[position, velocity, acceleration] (velocity and acceleration are typically zero)
     * @param x2 double array containing final x[position, velocity, acceleration] (velocity and acceleration are typically zero)
     * @param y2 double array containing final y[position, velocity, acceleration] (velocity and acceleration are typically zero)
     * @param maxVelocity maximum robot speed in meters
    */
	public SplineTrajectory(Drivetrain drivetrain, double[] x1, double[] y1, double[] x2, double[] y2, double maxVelocity) {
		this.drivetrain = drivetrain;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.maxVelocity = maxVelocity;
		addRequirements(this.drivetrain);
	}

	@Override
	public void initialize() {
        timer.start();
        spline = new QuinticHermiteSpline(x1, x2, y1, y2);
        parameterizedSpline = SplineParameterizer.parameterize(spline, 0.0, 1.0);
        tIncrement = 1; // smaller = more accurate dx and dy estimates; larger might be neccesary though

        int numSteps = parameterizedSpline.size();
        int numMs = numSteps * 10;// ms
        maxTime = numMs / 10.0; // increments of 10ms
        double maxMetersPerMs = 0.127 * 10;// 1.27 m/ms
        double maxMetersPerSec = maxMetersPerMs / 1000.0; // 0.00127 m/s
        //double totalChange = numMs * changePerMs;// meters
        //outputScalar = 1;//maxVelocity / changePerSec; // should scale the output to real meters
        //TODO: test without outputScalar first
        //but it will likely be VERY slow
        streamRateLimiter = (maxMetersPerSec / maxVelocity); // .00127
	}
    // taken from parameterizer class
    // indicates maximum velocity per variable per step
    // private static final double kMaxDx = 0.127;
    // private static final double kMaxDy = 0.00127;
    // private static final double kMaxDtheta = 0.0872;
	@Override
	public void execute() {
        t = (int)(timer.get() * 100.0); //update in increments of 10ms
        tIndex = t * (int)streamRateLimiter; //tIndex = parameterizedSpline.size() * (int)(t / maxTime); //integer percent of path complete
        y3 = parameterizedSpline.get(tIndex).poseMeters.getY();
        //parameterizedSpline.stream().
        x3 = parameterizedSpline.get(tIndex).poseMeters.getY();
        //r3 = parameterizedSpline.get(tIndex).curvatureRadPerMeter;
        y4 = parameterizedSpline.get(tIndex + tIncrement).poseMeters.getY();
        x4 = parameterizedSpline.get(tIndex + tIncrement).poseMeters.getX();
        //r4 = parameterizedSpline.get(tIndex + 1).curvatureRadPerMeter;
        y5 = (tIndex >= 1) ? parameterizedSpline.get(tIndex - tIncrement).poseMeters.getY() : parameterizedSpline.get(tIndex).poseMeters.getY();
        x5 = (tIndex >= 1) ? parameterizedSpline.get(tIndex - tIncrement).poseMeters.getX() : parameterizedSpline.get(tIndex).poseMeters.getX();
        dy = (((y4 - y3) + (y5 - y4)) / 2.0);//* outputScalar;
        dx = (((x4 - x3) + (x5 - x4)) / 2.0);//* outputScalar;
        dr = parameterizedSpline.get(tIndex).curvatureRadPerMeter;
        drivetrain.drive(new ChassisSpeeds(dx, dy, 0)); 
        //TODO: plug in dr
        //but we should verify dx and dy first
	}

	@Override
	public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds());
	}

	@Override
	public boolean isFinished() {
		return t >= maxTime;
	}
}