package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hood extends SubsystemBase {
	private final CANSparkMax motor;
	private final RelativeEncoder encoder;
	private final PIDController PID;
	private final DigitalInput limit;
	private ControlMode currentControlMode = ControlMode.PID;

	public Hood() {
		motor = new CANSparkMax(36, MotorType.kBrushless);
		encoder = motor.getEncoder();
		motor.setSmartCurrentLimit(20);
		encoder.setPosition(0);
		PID = new PIDController(0.0075, 0, 0.0000005);
		PID.setTolerance(1);
		limit = new DigitalInput(9);
		motor.setIdleMode(IdleMode.kBrake);

		// SmartDashboard.putNumber("Hood Angle", 0);
	}

	public enum ControlMode {
		PID,
		MANUAL
	}

	public void setAngle(double angle) {
		PID.setSetpoint(angle * -20);
	}

	public boolean getLimit() {
		return !limit.get();
	}

	public void setMotor(double speed) {
		motor.set(-speed);
	}

	public void resetEncoder() {
		encoder.setPosition(0);
	}

	public boolean atSetpoint() {
		return PID.atSetpoint();
	}

	public void setState(ControlMode state) {
		currentControlMode = state;
	}

	@Override
	public void periodic() {
		SmartDashboard.putBoolean("hood limit", getLimit());
		// SmartDashboard.putData(this);
		if (currentControlMode == ControlMode.PID)
			motor.set(MathUtil.clamp(PID.calculate(encoder.getPosition()), -0.5, 0.5));
	}
}
