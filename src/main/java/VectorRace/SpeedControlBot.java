package VectorRace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpeedControlBot extends BasePlayer {
    private Random random = new Random();
    private ITrack track;

    public SpeedControlBot(String name, Position start, ITrack track) {
        super(name, start);
        this.track = track;
    }

    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        // Strategia semplice: sceglie casualmente una direzione sicura
        List<VectorDirection.CardinalDirection> safeDirs = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);

            if (track.isFree(nextPos)) {
                safeDirs.add(dir);
            }
        }
        return safeDirs.isEmpty() ? null : safeDirs.get(random.nextInt(safeDirs.size()));
    }

    @Override
    public int chooseAcceleration() {
        // Se la velocità è inferiore a 3, accelera; altrimenti decelera
        if (this.getVelocity() < 3) {
            return 1;  // accelerazione
        } else {
            return -1; // decelerazione
        }
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

