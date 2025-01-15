package VectorRace;

import java.util.*;

public class DefaultInertiaManager implements IInertiaManager {
    @Override
    public List<VectorDirection.CardinalDirection> allowedDirections(int currentVelocity, VectorDirection.CardinalDirection previousDirection) {
        List<VectorDirection.CardinalDirection> directions = new ArrayList<>();
        // Se velocità 0 o 1: tutte le direzioni possibili
        if (currentVelocity <= 1) {
            directions.addAll(Arrays.asList(VectorDirection.CardinalDirection.values()));
        } else if (currentVelocity == 2) {
            // Limita a direzione precedente ±90°
            directions.addAll(getDirectionsWithinAngle(previousDirection, 90));
        } else if (currentVelocity >= 3) {
            // Limita a direzione precedente ±45°
            directions.addAll(getDirectionsWithinAngle(previousDirection, 45));
        }
        return directions;
    }

    private List<VectorDirection.CardinalDirection> getDirectionsWithinAngle(VectorDirection.CardinalDirection baseDir, int angleRange) {
        List<VectorDirection.CardinalDirection> nearby = new ArrayList<>();
        // Mappa le direzioni a angoli
        Map<VectorDirection.CardinalDirection, Double> directionAngles = new HashMap<>();
        for (VectorDirection.CardinalDirection dir : VectorDirection.CardinalDirection.values()) {
            directionAngles.put(dir, getAngle(dir));
        }

        double baseAngle = directionAngles.get(baseDir);
        for (VectorDirection.CardinalDirection dir : VectorDirection.CardinalDirection.values()) {
            double angle = directionAngles.get(dir);
            double diff = Math.abs(baseAngle - angle);
            diff = Math.min(diff, 360 - diff); // Minimizza l'angolo differenza
            if (diff <= angleRange) {
                nearby.add(dir);
            }
        }
        return nearby;
    }

    private double getAngle(VectorDirection.CardinalDirection dir) {
        switch (dir) {
            case N: return 90;
            case NE: return 45;
            case E: return 0;
            case SE: return 315;
            case S: return 270;
            case SW: return 225;
            case W: return 180;
            case NW: return 135;
            default: return 0;
        }
    }
}

