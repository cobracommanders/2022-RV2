package frc.robot.commands.hood;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hood.ControlMode;

public class SetHood extends InstantCommand {
	private Hood hood;
	private double angle;

	public SetHood(Hood hood, double angle) {
		this.hood = hood;
		this.angle = angle;
		addRequirements(this.hood);
	}

	@Override
	public void initialize() {
		hood.setState(ControlMode.PID);
		hood.setAngle(angle);
	}
}
