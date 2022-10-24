package org.team498.lib.control;

import org.team498.lib.math.Rotation2;
import org.team498.lib.math.Vector2;

public abstract class PathSegment {
    public State getStart() {
        return calculate(0.0);
    }

    public State getEnd() {
        return calculate(getLength());
    }
    
    public abstract State calculate(double distance);

    public abstract double getLength();

    public class State {
        private Vector2 position;
        private Rotation2 heading;
        private double curvature;

        public State(Vector2 position, Rotation2 heading, double curvature) {
            this.position = position;
            this.heading = heading;
            this.curvature = curvature;
        }

        public Vector2 getPosition() {
            return position;
        }

        public Rotation2 getHeading() {
            return heading;
        }

        public double getCurvature() {
            return curvature;
        }
    }
}
