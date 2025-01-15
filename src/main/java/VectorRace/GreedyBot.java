package VectorRace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GreedyBot extends BasePlayer {
    private Random random = new Random();
    private ITrack track;

    public GreedyBot(String name, Position start, ITrack track) {
        super(name, start);
        this.track = track;
    }

    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        Position finish = track.getFinishPosition();
        if (finish == null) {
            // Se non c'è traguardo definito, si comporta come un bot casuale sicuro.
            return randomSafeDirection(allowedDirections);
        }

        VectorDirection.CardinalDirection bestDirection = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);
            if (!track.isFree(nextPos)) {
                continue; // Salta le direzioni non sicure
            }

            // Calcola la distanza Manhattan dal traguardo
            int distanceToFinish = manhattanDistance(nextPos, finish);
            // Calcola la distanza fino al prossimo ostacolo in questa direzione
            int distanceToObstacle = distanceToNextObstacle(nextPos, dir);

            // Combina i due criteri: si desidera una piccola distanza al traguardo
            // e una grande distanza dall'ostacolo.
            // Il parametro alfa controlla il peso relativo di avvicinamento vs. sicurezza.
            double alfa = 1.0;
            double score = (-alfa * distanceToFinish) + distanceToObstacle;

            if (score > bestScore) {
                bestScore = score;
                bestDirection = dir;
            }
        }

        // Se nessuna direzione è risultata valida, non si muove.
        return bestDirection != null ? bestDirection : null;
    }

    @Override
    public int chooseAcceleration() {
        // Strategia semplice: accelerare se possibile
        return 1;
    }

    private VectorDirection.CardinalDirection randomSafeDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        List<VectorDirection.CardinalDirection> safeDirs = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);
            if (track.isFree(nextPos)) {
                safeDirs.add(dir);
            }
        }
        return safeDirs.isEmpty() ? null : safeDirs.get(random.nextInt(safeDirs.size()));
    }

    private int manhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    private int distanceToNextObstacle(Position start, VectorDirection.CardinalDirection direction) {
        int distance = 0;
        Position current = start;
        while (true) {
            Position next = getNextPosition(current, direction);
            // Controllo manuale dei confini
            boolean outOfBounds = next.getX() < 0 || next.getX() >= track.getWidth()
                    || next.getY() < 0 || next.getY() >= track.getHeight();
            if (outOfBounds || !track.isFree(next)) {
                break;
            }
            distance++;
            current = next;
        }
        return distance;
    }


    private Position getNextPosition(Position current, VectorDirection.CardinalDirection direction) {
        int x = current.getX();
        int y = current.getY();

        switch (direction) {
            case N:  y -= 1; break;
            case NE: x += 1; y -= 1; break;
            case E:  x += 1; break;
            case SE: x += 1; y += 1; break;
            case S:  y += 1; break;
            case SW: x -= 1; y += 1; break;
            case W:  x -= 1; break;
            case NW: x -= 1; y -= 1; break;
        }

        return new Position(x, y);
    }
}

