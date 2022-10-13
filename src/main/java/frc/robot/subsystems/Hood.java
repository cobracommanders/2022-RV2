package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

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
	// private final SparkMaxPIDController PID1;
	private ControlMode currentControlMode = ControlMode.PID;

	public Hood() {
		motor = new CANSparkMax(36, MotorType.kBrushless);
		motor.restoreFactoryDefaults();
		encoder = motor.getEncoder();
		motor.setSmartCurrentLimit(20);
		encoder.setPosition(0);
		PID = new PIDController(0.0075, 0, 0.0000005);
		// PID.setTolerance(1);
		limit = new DigitalInput(9);
		motor.setIdleMode(IdleMode.kBrake);
		// PID1 = motor.getPIDController();
		// PID1.setP(0.0075);
		// PID1.setI(0);
		// PID1.setD(0.0000005);
		// PID1.setOutputRange(-1, 1);

		// motor.setSoftLimit(SoftLimitDirection.kReverse, 1.4F);
		// motor.enableSoftLimit(SoftLimitDirection.kForward, true);

		// motor.setSoftLimit(SoftLimitDirection.kForward, 0F);
		// motor.enableSoftLimit(SoftLimitDirection.kReverse, true);

		// SmartDashboard.putNumber("Hood Angle", 0);
	}

	public enum ControlMode {
		PID,
		MANUAL
	}

	public void setAngle(double angle) {
		PID.setSetpoint(angle * -20);
		// PID1.setReference(angle, ControlType.kPosition);
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
		SmartDashboard.putData(this);
		SmartDashboard.putNumber("hood encoder", encoder.getPosition());
		SmartDashboard.putNumber("hood error", PID.getPositionError());
		if (currentControlMode == ControlMode.PID)
			// if (PID.getPositionError() > 0.5)
				motor.set(MathUtil.clamp(PID.calculate(encoder.getPosition()), -0.5, 0.5));
			// else {
			// 	motor.set(0);
			// }
	}
}
