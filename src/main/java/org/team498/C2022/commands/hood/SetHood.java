package org.team498.C2022.commands.hood;

import org.team498.C2022.subsystems.Hood;
import org.team498.C2022.subsystems.Hood.ControlMode;

import edu.wpi.first.wpilibj2.command.InstantCommand;

// Sets the hood to a given position
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
