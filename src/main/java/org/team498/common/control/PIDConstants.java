package org.team498.common.control;

public class PIDConstants {
    private final double kP;
    private final double kI;
    private final double kD;

    public PIDConstants(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public double getP() {
        return kP;
    }

    public double getI() {
        return kI;
    }
    
    public double getD() {
        return kD;
    }
}
