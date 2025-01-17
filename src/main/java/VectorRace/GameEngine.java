package VectorRace;

import java.util.*;

public class GameEngine {
    private final int maxTurns;  // Limite di turni impostabile dal Main
    private GameBoard board;
    private IVelocityCalculator velocityCalculator;
    private IInertiaManager inertiaManager;
    List<IPlayer> players;
    private Map<IPlayer, VectorDirection.CardinalDirection> previousDirections;

    public GameEngine(GameBoard board, IVelocityCalculator velocityCalculator,
                      IInertiaManager inertiaManager, int maxTurns) {
        this.board = board;
        this.velocityCalculator = velocityCalculator;
        this.inertiaManager = inertiaManager;
        this.players = new ArrayList<>();
        this.previousDirections = new HashMap<>();
        this.maxTurns = maxTurns;
    }

    public void addPlayer(IPlayer player) {
        players.add(player);
        board.addPlayer(player);
        previousDirections.put(player, VectorDirection.CardinalDirection.E);
    }

    public void startRace() {

        boolean raceFinished = false;
        int turn = 0;
        while (!raceFinished && turn < maxTurns) {
            turn++;
            processTurn(turn);

            // Verifica eventuali condizioni di fine gara,
            // ad esempio, se non ci sono più giocatori o se è stato raggiunto il traguardo.
            if (players.isEmpty()) {
                System.out.println("Tutti i giocatori sono stati eliminati. La partita finisce.");
                break;
            }
        }

        if (!raceFinished) {
            System.out.println("Limite di " + maxTurns + " turni raggiunto. La partita termina.");
        }
    }


    private void processTurn(int turn) {

        System.out.println("Turno: " + turn);

        Iterator<IPlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            IPlayer player = iterator.next();
            processPlayerTurn(player, iterator);
        }


    }

    private void processPlayerTurn(IPlayer player, Iterator<IPlayer> iterator) {
        VectorDirection.CardinalDirection previousDirection = previousDirections.get(player);
        List<VectorDirection.CardinalDirection> allowed = inertiaManager.allowedDirections(player.getVelocity(), previousDirection);
        VectorDirection.CardinalDirection chosenDirection = player.chooseDirection(allowed);

        if (chosenDirection == null) {
            System.out.println(((BasePlayer)player).getName() + " non ha direzioni sicure per muoversi.");
            return;
        }

        previousDirections.put(player, chosenDirection);
        int acceleration = player.chooseAcceleration();
        player.setVelocity(player.getVelocity() + acceleration);

        Position currentPos = player.getCurrentPosition();
        Position newPos = calculateNewPosition(currentPos, chosenDirection, player.getVelocity());

        if (!board.isFree(newPos)) {
            handleCollision(player, newPos, iterator);
        } else {
            if (board.isFinish(newPos)) {
                System.out.println(((BasePlayer)player).getName() + " ha raggiunto il traguardo!");
                // Gestione della vittoria e uscita dal turno
                return;
            }
            board.updatePlayerPosition(player, newPos);
            player.setCurrentPosition(newPos);
            board.display(players, previousDirections);
        }
    }

    private void handleCollision(IPlayer player, Position newPos, Iterator<IPlayer> iterator) {
        if (board.isObstacle(newPos)) {
            System.out.println(((BasePlayer)player).getName() + " ha colpito un ostacolo ed è eliminato dal gioco!");
            iterator.remove();
            previousDirections.remove(player);
        } else {
            System.out.println("Posizione occupata da un altro giocatore. " +
                    ((BasePlayer)player).getName() + " riprova la mossa...");
            // Possibile logica per gestire la ripetizione o il salto della mossa
        }
    }


    private Position calculateNewPosition(Position current,
                                          VectorDirection.CardinalDirection direction,
                                          int velocity) {
        int step = Math.min(velocity, 3);
        int x = current.getX();
        int y = current.getY();

        switch (direction) {
            case N:  y -= step; break;
            case NE: x += step; y -= step; break;
            case E:  x += step; break;
            case SE: x += step; y += step; break;
            case S:  y += step; break;
            case SW: x -= step; y += step; break;
            case W:  x -= step; break;
            case NW: x -= step; y -= step; break;
        }
        return new Position(x, y);
    }
}
