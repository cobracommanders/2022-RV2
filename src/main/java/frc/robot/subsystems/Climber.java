package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
	private final CANSparkMax leftClimber = new CANSparkMax(32, MotorType.kBrushless);
	private final CANSparkMax rightClimber = new CANSparkMax(35, MotorType.kBrushless);
	private final RelativeEncoder leftEncoder = leftClimber.getEncoder();

	private double leftSpeed;
	private double rightSpeed;

	public Climber() {
		leftClimber.setIdleMode(IdleMode.kBrake);
		rightClimber.setIdleMode(IdleMode.kBrake);
		leftClimber.setInverted(false);
		rightClimber.setInverted(true);
	}

	@Override
	public void periodic() {
		leftClimber.set(-leftSpeed);
		rightClimber.set(-rightSpeed);

		// SmartDashboard.putNumber("climber encoder", leftEncoder.getPosition());
	}

	public void setSpeedLeft(double speed) {
		leftSpeed = speed;
	}

	public void setSpeedRight(double speed) {
		rightSpeed = speed;
	}

	public double getEncoder() {
		return leftEncoder.getPosition();
	}

	public void resetEncoder() {
		leftEncoder.setPosition(0);
	}

}