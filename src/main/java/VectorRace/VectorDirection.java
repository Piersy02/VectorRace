package VectorRace;

public class VectorDirection {
    private Position current;
    private Position next;

    public enum CardinalDirection {
        N, NE, E, SE, S, SW, W, NW;
    }

    public VectorDirection(Position current, Position next) {
        this.current = current;
        this.next = next;
    }

    public double directionRadians() {
        return Math.atan2(next.getY() - current.getY(), next.getX() - current.getX());
    }

    public double directionDegrees() {
        return Math.toDegrees(directionRadians());
    }

    public CardinalDirection getCardinalDirection() {
        double angle = directionDegrees();
        if (angle < 0) { angle += 360; }
        if (angle >= 337.5 || angle < 22.5) {
            return CardinalDirection.E;
        } else if (angle >= 22.5 && angle < 67.5) {
            return CardinalDirection.NE;
        } else if (angle >= 67.5 && angle < 112.5) {
            return CardinalDirection.N;
        } else if (angle >= 112.5 && angle < 157.5) {
            return CardinalDirection.NW;
        } else if (angle >= 157.5 && angle < 202.5) {
            return CardinalDirection.W;
        } else if (angle >= 202.5 && angle < 247.5) {
            return CardinalDirection.SW;
        } else if (angle >= 247.5 && angle < 292.5) {
            return CardinalDirection.S;
        } else if (angle >= 292.5 && angle < 337.5) {
            return CardinalDirection.SE;
        }
        return null;
    }
}

