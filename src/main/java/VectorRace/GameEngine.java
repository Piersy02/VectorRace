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
        board.display(players, previousDirections);

        boolean raceFinished = false;
        int turn = 0;
        while (!raceFinished && turn < maxTurns) {
            System.out.println("Turno: " + (turn + 1));
            turn++;

            boolean anySuccessfulMove = false;

            // Utilizzo di un iterator per poter rimuovere giocatori in sicurezza
            Iterator<IPlayer> iterator = players.iterator();
            while (iterator.hasNext()) {
                IPlayer player = iterator.next();

                // Salta il giocatore se è stato rimosso in precedenza da altre logiche
                if (player == null) continue;

                VectorDirection.CardinalDirection previousDirection = previousDirections.get(player);
                List<VectorDirection.CardinalDirection> allowed = inertiaManager.allowedDirections(player.getVelocity(), previousDirection);
                VectorDirection.CardinalDirection chosenDirection = player.chooseDirection(allowed);

                // Se non esiste una direzione sicura, passa al prossimo giocatore
                if (chosenDirection == null) {
                    System.out.println(((BasePlayer)player).getName() + " non ha direzioni sicure per muoversi.");
                    continue;
                }

                previousDirections.put(player, chosenDirection);
                int acceleration = player.chooseAcceleration();
                player.setVelocity(player.getVelocity() + acceleration);

                Position currentPos = player.getCurrentPosition();
                Position newPos = calculateNewPosition(currentPos, chosenDirection, player.getVelocity());

                if (!board.isFree(newPos)) {
                    if (board.isObstacle(newPos)) {
                        System.out.println(((BasePlayer)player).getName() + " ha colpito un ostacolo ed è eliminato dal gioco!");
                        // Rimuove il giocatore corrente dalla lista
                        iterator.remove();
                        // Rimuove l'entrata dalla mappa delle direzioni precedenti
                        previousDirections.remove(player);
                        // Passa al prossimo giocatore
                        continue;
                    } else {
                        System.out.println("Posizione occupata da un altro giocatore. " +
                                ((BasePlayer)player).getName() + " riprova la mossa...");
                        continue;
                    }
                }

                if (board.isFinish(newPos)) {
                    System.out.println(((BasePlayer)player).getName() + " ha raggiunto il traguardo!");
                    raceFinished = true;
                    anySuccessfulMove = true;
                    // Aggiorna la posizione e visualizza, poi interrompe se la gara è finita
                    board.updatePlayerPosition(player, newPos);
                    player.setCurrentPosition(newPos);
                    board.display(players, previousDirections);
                    break;
                }

                board.updatePlayerPosition(player, newPos);
                player.setCurrentPosition(newPos);
                board.display(players, previousDirections);
                anySuccessfulMove = true;
            }

            if (!anySuccessfulMove) {
                System.out.println("Nessun giocatore è riuscito a muoversi in questo turno.");
            }

            // Se non ci sono più giocatori attivi, termina la partita
            if (players.isEmpty()) {
                System.out.println("Tutti i giocatori sono stati eliminati. La partita finisce.");
                break;
            }
        }

        if (!raceFinished) {
            System.out.println("Limite di " + maxTurns + " turni raggiunto. La partita termina.");
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
