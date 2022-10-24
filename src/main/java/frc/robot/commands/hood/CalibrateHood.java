package frc.robot.commands.hood;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hood.ControlMode;

// Lowers the hood until it triggers the limit switch, then set the encoder to zero
public class CalibrateHood extends CommandBase {
	private Hood hood;

	public CalibrateHood(Hood hood) {
		this.hood = hood;
		addRequirements(this.hood);
	}

	@Override
	public void initialize() {
		hood.setState(ControlMode.MANUAL);
		hood.setAngle(0);
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