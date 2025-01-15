package VectorRace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefensiveBot extends BasePlayer {
    private Random random = new Random();
    private ITrack track;

    public DefensiveBot(String name, Position start, ITrack track) {
        super(name, start);
        this.track = track;
    }

    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        // Trova direzioni sicure: quelle che non portano immediatamente a un ostacolo
        List<VectorDirection.CardinalDirection> safeDirections = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);
            if (track.isFree(nextPos)) {
                safeDirections.add(dir);
            }
        }

        // Se ci sono direzioni sicure, sceglie una casualmente
        if (!safeDirections.isEmpty()) {
            return safeDirections.get(random.nextInt(safeDirections.size()));
        }

        // Se non ci sono direzioni sicure, ritorna una direzione casuale tra quelle consentite
        // oppure può scegliere di non muoversi (implementazione opzionale)
        List<VectorDirection.CardinalDirection> options = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            options.add(dir);
        }
        return options.isEmpty() ? null : options.get(random.nextInt(options.size()));
    }

    @Override
    public int chooseAcceleration() {
        // Strategia difensiva: decelera per sicurezza
        return (this.getVelocity() > 0) ? -1 : 0;
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