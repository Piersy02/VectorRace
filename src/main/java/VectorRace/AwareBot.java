package VectorRace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AwareBot extends BasePlayer {
    private Random random = new Random();
    private ITrack track;
    private List<IPlayer> players; // Riferimento alla lista di tutti i giocatori

    public AwareBot(String name, Position start, ITrack track, List<IPlayer> players) {
        super(name, start);
        this.track = track;
        this.players = players;
    }

    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        List<VectorDirection.CardinalDirection> safeDirections = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);
            boolean occupied = false;
            // Verifica se un altro giocatore occupa la posizione successiva
            for (IPlayer p : players) {
                if (p != this && p.getCurrentPosition().equals(nextPos)) {
                    occupied = true;
                    break;
                }
            }
            // Aggiunge la direzione alla lista se la cella successiva è libera e non occupata
            if (track.isFree(nextPos) && !occupied) {
                safeDirections.add(dir);
            }
        }

        if (!safeDirections.isEmpty()) {
            // Se ci sono direzioni sicure, sceglie casualmente una di esse
            return safeDirections.get(random.nextInt(safeDirections.size()));
        }
        // Se nessuna direzione è sicura, non si muove
        return null;
    }

    @Override
    public int chooseAcceleration() {
        // Definisce una soglia di distanza per considerare un giocatore "vicino"
        final int THRESHOLD_DISTANCE = 3;

        // Controlla se ci sono altri giocatori entro la soglia di distanza
        boolean someoneClose = false;
        for (IPlayer other : players) {
            if (other == this) continue;
            int distance = manhattanDistance(this.currentPosition, other.getCurrentPosition());
            if (distance <= THRESHOLD_DISTANCE) {
                someoneClose = true;
                break;
            }
        }

        // Se qualcuno è vicino, decelera; altrimenti accelera
        if (someoneClose) {
            return (this.getVelocity() > 0) ? -1 : 0;
        } else {
            return 1;
        }
    }

    private int manhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
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