package org.team498.lib.control;

import org.team498.lib.math.MathUtils;

public class PIDController {
    private final PIDConstants constants;

    private double setpoint;
    private boolean continuous = false;
    private double inputRange = Double.POSITIVE_INFINITY;
    private double minOutput = Double.NEGATIVE_INFINITY;
    private double maxOutput = Double.POSITIVE_INFINITY;

    private double error = 0;
    private double epsilon = MathUtils.EPSILON;

    private double lastError = Double.NaN;
    private double integralAccum = 0.0;
    private double integralRange = Double.POSITIVE_INFINITY;
    private boolean shouldClearIntegralOnErrorSignChange = false;

    public PIDController(PIDConstants constants) {
        this.constants = new PIDConstants(constants.getP(), constants.getI(), constants.getD());
    }
    public PIDController(double kP, double kI, double kD) {
        this.constants = new PIDConstants(kP, kI, kD);
    }
    public double calculate(double measurement, double dt) {
        double error = setpoint - measurement;
        if (continuous) {
            error %= inputRange;
            if (Math.abs(error) > inputRange / 2.0) {
                if (error > 0.0) {
                    error -= inputRange;
                } else {
                    error += inputRange;
                }
            }
        }

        double derivative = Double.isFinite(lastError) ? (error - lastError) / dt : 0.0;
        if (shouldClearIntegralOnErrorSignChange && !MathUtils.epsilonEquals(error, Math.copySign(error, integralAccum))) {
            integralAccum = 0.0;
        }
        lastError = error;

        double integral = (Math.abs(error) < integralRange / 2.0) ? integralAccum + error * dt : 0.0;
        integralAccum = integral;

        return MathUtils.clamp((constants.getP() * error) + (integral / constants.getI()) + (derivative * constants.getD() / dt), minOutput, maxOutput);
    }
    public void reset() {
        integralAccum = 0.0;
        lastError = Double.NaN;
    }
    public double getSetpoint() {
        return setpoint;
    }
    public boolean atSetpoint() {
        return MathUtils.epsilonEquals(error, 0.0, epsilon);
    }
    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }
    public void setInputRange(double min, double max) {
        this.inputRange = Math.abs(max - min);
    }
    public void setIntegralRange(double integralRange) {
        this.integralRange = integralRange;
    }
    public void setOutputRange(double min, double max) {
        if (max < min) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        this.minOutput = min;
        this.maxOutput = max;
    }
    public void setShouldClearIntegralOnErrorSignChange(boolean shouldClearIntegralOnErrorSignChange) {
        this.shouldClearIntegralOnErrorSignChange = shouldClearIntegralOnErrorSignChange;
    }
}