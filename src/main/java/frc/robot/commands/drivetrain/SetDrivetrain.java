package frc.robot.commands.drivetrain;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class SetDrivetrain extends CommandBase {
	private final Drivetrain drivetrainSubsystem;

	private double translationX;
	private double translationY;
	private double rotation;

	public SetDrivetrain(Drivetrain drivetrainSubsystem,
			double translationX,
			double translationY,
			double rotation) {
		this.drivetrainSubsystem = drivetrainSubsystem;
		this.translationX = translationX;
		this.translationY = translationY;
		this.rotation = rotation;

		addRequirements(drivetrainSubsystem);
	}

	@Override
	public void execute() {
		drivetrainSubsystem.drive(
				new ChassisSpeeds(translationX, translationY, rotation));
	}

	@Override
	public void end(boolean interrupted) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}