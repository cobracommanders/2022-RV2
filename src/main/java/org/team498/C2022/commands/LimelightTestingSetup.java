package org.team498.C2022.commands;

import org.team498.C2022.subsystems.Hood;
import org.team498.C2022.subsystems.Shooter;
import org.team498.lib.drivers.Limelight;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class LimelightTestingSetup extends CommandBase {
	private final Shooter shooter;
	private final Hood hood;
	private final Limelight limelight;

	public LimelightTestingSetup(Shooter shooter, Hood hood, Limelight limelight) {
		this.shooter = shooter;
		this.hood = hood;
		this.limelight = limelight;

		addRequirements(shooter, hood);
	}

	@Override
	public void initialize() {
		SmartDashboard.putNumber("T-Shooter RPM", (SmartDashboard.getNumber("T-Shooter RPM", 0)));
		SmartDashboard.putNumber("T-Hood angle", (SmartDashboard.getNumber("T-Hood angle", 0)));
	}

	@Override
	public void execute() {
		shooter.set(SmartDashboard.getNumber("T-Shooter RPM", 0));
		hood.setAngle(SmartDashboard.getNumber("T-Hood angle", 0));
		SmartDashboard.putNumber("T-limelight distance", limelight.getDistance());
	}

	@Override
	public void end(boolean interrupted) {
		shooter.set(0);
	}
}
