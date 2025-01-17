package VectorRace;

import java.util.*;

/**
 * GameEngine si occupa di gestire il flusso di gioco:
 * - Tiene traccia dei turni e di un limite massimo.
 * - Coordina i giocatori, gestisce spostamenti, controlla ostacoli e condizioni di vittoria.
 * - Visualizza lo stato del gioco dopo ogni azione.
 */
public class GameEngine {

    /**
     * Numero massimo di turni consentiti prima che la partita finisca automaticamente.
     */
    private final int maxTurns;

    /**
     * Riferimento alla "plancia" di gioco, che contiene le informazioni sul tracciato
     * e la gestione delle posizioni dei giocatori.
     */
    private GameBoard board;

    /**
     * IVelocityCalculator (per calcolare velocit� pi� complesse).
     */
    private IVelocityCalculator velocityCalculator;

    /**
     * IInertiaManager si occupa di determinare le possibili direzioni consentite
     * a un giocatore in base alla sua velocit� e direzione precedente.
     */
    private IInertiaManager inertiaManager;

    /**
     * Lista dei giocatori in partita (IPlayer).
     */
    List<IPlayer> players;

    /**
     * Mappa per ricordare la direzione precedente di ogni giocatore,
     * utile per calcolare le direzioni ammesse al turno successivo.
     */
    private Map<IPlayer, VectorDirection.CardinalDirection> previousDirections;

    /**
     * Costruttore di GameEngine.
     *
     * @param board             GameBoard per gestire posizioni e tracciato.
     * @param velocityCalculator Oggetto per calcoli di velocit� (se necessario).
     * @param inertiaManager     Oggetto per determinare le direzioni ammesse in base a velocit� e direzione precedente.
     * @param maxTurns          Limite massimo di turni.
     */
    public GameEngine(GameBoard board, IVelocityCalculator velocityCalculator,
                      IInertiaManager inertiaManager, int maxTurns) {
        this.board = board;
        this.velocityCalculator = velocityCalculator;
        this.inertiaManager = inertiaManager;
        this.players = new ArrayList<>();
        this.previousDirections = new HashMap<>();
        this.maxTurns = maxTurns;
    }

    /**
     * Aggiunge un nuovo giocatore alla partita, assegnandogli
     * una posizione di partenza tramite GameBoard e memorizzandolo
     * nella lista interna di players.
     *
     * @param player Il giocatore da aggiungere.
     */
    public void addPlayer(IPlayer player) {
        players.add(player);
        board.addPlayer(player);
        // Imposta la direzione iniziale come Est (E) per default
        previousDirections.put(player, VectorDirection.CardinalDirection.E);
    }

    /**
     * Avvia la corsa, iterando sui turni fino al raggiungimento del maxTurns
     * o finch� non si stabilisce la fine della gara (es. tutti eliminati o uno ha vinto).
     */
    public void startRace() {
        boolean raceFinished = false;
        int turn = 0;

        // Continua finch� la gara non � finita o non superiamo i turni massimi.
        while (!raceFinished && turn < maxTurns) {
            turn++;
            processTurn(turn);

            // Verifica se tutti i giocatori sono stati eliminati, condizione di fine gara.
            if (players.isEmpty()) {
                System.out.println("Tutti i giocatori sono stati eliminati. La partita finisce.");
                break;
            }
        }

        // Se non si � conclusa entro maxTurns, terminiamo la gara.
        if (!raceFinished) {
            System.out.println("Limite di " + maxTurns + " turni raggiunto. La partita termina.");
        }
    }

    /**
     * Esegue la logica di un singolo turno di gioco.
     *
     * @param turn Numero del turno corrente.
     */
    private void processTurn(int turn) {
        System.out.println("Turno: " + turn);

        // Utilizziamo un iterator per poter rimuovere i giocatori eliminati durante il ciclo.
        Iterator<IPlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            IPlayer player = iterator.next();
            processPlayerTurn(player, iterator);
        }
    }

    /**
     *   Esegue le azioni di un singolo giocatore in un turno:
     * - Calcola le direzioni consentite in base alla velocit� e direzione precedente.
     * - Fa scegliere al giocatore la direzione.
     * - Calcola l'accelerazione e aggiorna la velocit�.
     * - Determina la nuova posizione e verifica collisioni, ostacoli e traguardo.
     *
     * @param player   Il giocatore da processare.
     * @param iterator L�iterator sui giocatori, usato per eventuale rimozione (eliminazione).
     */
    private void processPlayerTurn(IPlayer player, Iterator<IPlayer> iterator) {
        // Recupera la direzione precedente da previousDirections.
        VectorDirection.CardinalDirection previousDirection = previousDirections.get(player);

        // Calcola quali direzioni sono permesse in base all'inertiaManager.
        List<VectorDirection.CardinalDirection> allowed =
                inertiaManager.allowedDirections(player.getVelocity(), previousDirection);

        // Chiede al giocatore di scegliere una direzione tra quelle consentite.
        VectorDirection.CardinalDirection chosenDirection = player.chooseDirection(allowed);

        // Se il giocatore non pu� o non vuole muoversi, stampa avviso e termina qui il suo turno.
        if (chosenDirection == null) {
            System.out.println(((BasePlayer)player).getName() + " non ha direzioni sicure per muoversi.");
            return;
        }

        // Aggiorna la direzione precedente con quella scelta dal giocatore.
        previousDirections.put(player, chosenDirection);

        // Fa scegliere l'accelerazione e aggiorna la velocit� del giocatore.
        int acceleration = player.chooseAcceleration();
        player.setVelocity(player.getVelocity() + acceleration);

        // Calcola la nuova posizione in base alla direzione e alla velocit�.
        Position currentPos = player.getCurrentPosition();
        Position newPos = calculateNewPosition(currentPos, chosenDirection, player.getVelocity());

        // Verifica se la nuova posizione � libera o se si � verificata una collisione/ostacolo.
        if (!board.isFree(newPos)) {
            handleCollision(player, newPos, iterator);
        } else {
            // Se la nuova posizione � un traguardo, il giocatore ha vinto.
            if (board.isFinish(newPos)) {
                System.out.println(((BasePlayer)player).getName() + " ha raggiunto il traguardo!");

                return;
            }
            // Aggiornamento della posizione sul board e sullo stato del giocatore.
            board.updatePlayerPosition(player, newPos);
            player.setCurrentPosition(newPos);

            // Mostra la situazione aggiornata del gioco (facoltativo).
            board.display(players, previousDirections);
        }
    }

    /**
     * Gestisce la collisione di un giocatore con un ostacolo o con un�altra posizione occupata.
     * Se � un ostacolo, elimina (rimuove) il giocatore dal gioco.
     * Se � occupata da un altro giocatore, gestisce l�evento (es. riprova mossa o salta mossa).
     *
     * @param player   Giocatore che ha subito la collisione.
     * @param newPos   Posizione in cui avviene la collisione.
     * @param iterator Iterator su players, per rimuovere eventualmente il giocatore.
     */
    private void handleCollision(IPlayer player, Position newPos, Iterator<IPlayer> iterator) {
        // Se la posizione � un ostacolo, il giocatore viene eliminato dal gioco.
        if (board.isObstacle(newPos)) {
            System.out.println(((BasePlayer)player).getName() + " ha colpito un ostacolo ed � eliminato dal gioco!");
            iterator.remove();
            previousDirections.remove(player);
        } else {
            // Se la posizione � occupata da un altro giocatore.
            System.out.println("Posizione occupata da un altro giocatore. " +
                    ((BasePlayer)player).getName() + " riprova la mossa...");
        }
    }

    /**
     * Calcola la nuova posizione di un giocatore in base a una direzione e a una velocit�.
     * Limita lo spostamento a un massimo di 3 celle per turno (Math.min(velocity, 3)).
     *
     * @param current   Posizione attuale del giocatore.
     * @param direction Direzione cardinale selezionata.
     * @param velocity  Velocit� corrente del giocatore (passo di movimento).
     * @return          La nuova posizione calcolata.
     */
    private Position calculateNewPosition(Position current,
                                          VectorDirection.CardinalDirection direction,
                                          int velocity) {
        // Limitiamo lo spostamento a max 3 celle per non sforare il tracciato.
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
