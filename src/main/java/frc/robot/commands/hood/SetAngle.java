package frc.robot.commands.hood;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;

public class SetAngle extends CommandBase {
	private Hood hood;
	private double angle;

	public SetAngle(Hood hood, double angle) {
		this.hood = hood;
		this.angle = angle;
		addRequirements(this.hood);
	}

	@Override
	public void execute() {
		hood.setAngle(angle);
	}

	@Override
	public boolean isFinished() {
		return hood.atSetpoint();
	}
}
