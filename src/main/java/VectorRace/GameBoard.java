package VectorRace;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameBoard {
    private ITrack track;
    private Map<IPlayer, Position> playerPositions;
    private int nextStartIndex = 0; // Indice per la prossima posizione di partenza disponibile

    public GameBoard(ITrack track) {
        this.track = track;
        this.playerPositions = new HashMap<>();
    }

    public void addPlayer(IPlayer player) {
        Position start;
        // Se il track è un'istanza di Track, usiamo getAllStartPositions per assegnare posizioni uniche
        if (track instanceof Track) {
            List<Position> starts = ((Track) track).getAllStartPositions();
            // Se ci sono ancora posizioni di partenza disponibili
            if (nextStartIndex < starts.size()) {
                start = starts.get(nextStartIndex++);
            } else {
                // Se esauriamo le posizioni di partenza, riciclo l'ultima o gestisco errore
                start = starts.get(starts.size() - 1);
            }
        } else {
            // Fallback per interfaccia generica
            start = track.getStartPosition();
        }
        player.setCurrentPosition(start);
        playerPositions.put(player, start);
    }

    public boolean isFree(Position pos) {
        return track.isFree(pos) && !isOccupied(pos);
    }

    public boolean isObstacle(Position pos) {
        return track.isObstacle(pos);
    }

    public boolean isFinish(Position pos) {
        return track.isFinish(pos);
    }

    private boolean isOccupied(Position pos) {
        return playerPositions.values().stream().anyMatch(p -> p.equals(pos));
    }

    public void updatePlayerPosition(IPlayer player, Position newPos) {
        playerPositions.put(player, newPos);
    }

    public ITrack getTrack() {
        return track;
    }

    public void display(List<IPlayer> players, Map<IPlayer, VectorDirection.CardinalDirection> previousDirections) {
        for (int y = 0; y < track.getHeight(); y++) {
            for (int x = 0; x < track.getWidth(); x++) {
                Position pos = new Position(x, y);
                char cell;

                if (track instanceof Track) {
                    Track t = (Track) track;
                    if (t.getAllStartPositions().contains(pos)) {
                        cell = 'S';
                    } else if (t.getAllFinishPositions().contains(pos)) {
                        cell = 'F';
                    } else {
                        cell = t.getCell(pos);
                    }
                } else {
                    cell = track.getCell(pos);
                }

                String playerSymbol = null;
                for (IPlayer player : players) {
                    if (player.getCurrentPosition().equals(pos)) {
                        playerSymbol = player instanceof BasePlayer ?
                                ((BasePlayer) player).getName().substring(0, 1) : "P";
                        break;
                    }
                }

                System.out.print(playerSymbol != null ? playerSymbol : cell);
            }
            System.out.println();
        }

        System.out.println("\nStato dei giocatori:");
        for (IPlayer player : players) {
            String playerName = player instanceof BasePlayer ? ((BasePlayer) player).getName() : "Giocatore";
            Position pos = player.getCurrentPosition();
            int velocity = player.getVelocity();
            VectorDirection.CardinalDirection direction = previousDirections.get(player);
            String directionStr = (direction != null) ? direction.toString() : "N/D";

            System.out.printf("%s - Velocità: %d, Posizione: (%d, %d), Direzione: %s%n",
                    playerName, velocity, pos.getX(), pos.getY(), directionStr);
        }
        System.out.println("--------------------------------------------------");
    }

}

