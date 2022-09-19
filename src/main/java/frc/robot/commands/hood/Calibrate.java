package frc.robot.commands.hood;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;

public class Calibrate extends CommandBase {
	private Hood hood;

	public Calibrate(Hood hood) {
		this.hood = hood;
		addRequirements(this.hood);
	}

	@Override
	public void initialize() {
		hood.setMotor(-0.1);
	}

	@Override
	public void end(boolean interrupted) {
		hood.resetEncoder();
		hood.setMotor(0);
	}

	@Override
	public boolean isFinished() {
		return hood.getLimit();
	}
}
