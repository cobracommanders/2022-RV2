package org.team498.lib.control;

import java.util.Map;

import org.team498.lib.math.Rotation2;
import org.team498.lib.math.Vector2;
import org.team498.lib.util.InterpolatingDouble;
import org.team498.lib.util.InterpolatingTreeMap;

public class Path {
    private final PathSegment[] segments;
    private final InterpolatingTreeMap<InterpolatingDouble, Rotation2> rotationMap = new InterpolatingTreeMap<>();
    private final double[] distancesFromStart;

    private final double length;

    public Path(PathSegment[] segments, Map<Double, Rotation2> rotationMap) {
        this.segments = segments;
        for (Map.Entry<Double, Rotation2> rotationEntry : rotationMap.entrySet()) {
            this.rotationMap.put(new InterpolatingDouble(rotationEntry.getKey()), rotationEntry.getValue());
        }
        distancesFromStart = new double[segments.length];
        double length = 0.0;
        for (int i = 0; i < segments.length; ++i) {
            distancesFromStart[i] = length;
            length += segments[i].getLength();
        }
        this.length = length;
    }

    public double getDistanceToSegmentStart(int segment) {
        return distancesFromStart[segment];
    }

    public double getDistanceToSegmentEnd(int segment) {
        return segments[segment].getLength() + distancesFromStart[segment];
    }

    private int getSegmentAtDistance(double distance) {
        int start = 0;
        int end = segments.length - 1;
        int mid = -1;
        while (start <= end) {
            mid = (end - start) / 2;
            if (distance < getDistanceToSegmentEnd(mid)) {
                end  = mid - 1;
            } else if (distance > getDistanceToSegmentStart(mid)) {
                start = mid + 1;
            } else {
                break;
            }
        }
        return mid;
    }

    public double getLength() {
        return length;
    }

    public State calculate(double distance) {
        int currSegment = getSegmentAtDistance(distance);
        PathSegment segment = segments[currSegment];
        double segmentDistance = getDistanceToSegmentStart(currSegment);
        
        PathSegment.State state = segment.calculate(segmentDistance);

        return new State(state, distance, rotationMap.getInterpolated(new InterpolatingDouble(distance)));
    }
    public class State {
        private final double distance;
        private final Vector2 position;
        private final Rotation2 heading;
        private final Rotation2 rotation;
        private final double curvature;

        public State(double distance, Vector2 position, Rotation2 heading, Rotation2 rotation, double curvature) {
            this.distance = distance;
            this.position = position;
            this.heading = heading;
            this.rotation = rotation;
            this.curvature = curvature;
        }
        public State(PathSegment.State segmentState, double distance, Rotation2 rotation) {
            this.distance = distance;
            this.position = segmentState.getPosition();
            this.heading = segmentState.getHeading();
            this.rotation = rotation;
            this.curvature = segmentState.getCurvature();
        }
        public double getDistance() {
            return distance;
        }
        public Vector2 getPosition() {
            return position;
        }
        public Rotation2 getHeading() {
            return heading;
        }
        public Rotation2 getRotation() {
            return rotation;
        }
        public double getCurvature() {
            return curvature;
        }
    }
}
