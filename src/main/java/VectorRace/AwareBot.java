package VectorRace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AwareBot è un bot "consapevole" del contesto circostante.
 * Tiene conto della presenza degli altri giocatori e di eventuali ostacoli sul tracciato
 * per decidere direzione e accelerazione.
 *
 * - Verifica se la cella verso cui si muoverà è libera e non occupata da altri giocatori.
 * - Se un avversario è vicino, tende a rallentare o fermarsi per evitare collisioni.
 */
public class AwareBot extends BasePlayer {

    // Usato per scegliere casualmente tra più opzioni disponibili.
    private Random random = new Random();

    // Tracciato di gioco, necessario per controllare se una cella è libera.
    private ITrack track;

    // Riferimento alla lista di tutti i giocatori (incluso se stesso).
    private List<IPlayer> players;

    /**
     * Costruttore di AwareBot.
     * @param name  Nome del bot.
     * @param start Posizione di partenza del bot.
     * @param track Istanza di ITrack per verificare lo stato del percorso.
     * @param players Lista di tutti i giocatori in partita.
     */
    public AwareBot(String name, Position start, ITrack track, List<IPlayer> players) {
        // Richiama il costruttore della superclasse BasePlayer per inizializzare nome e posizione.
        super(name, start);
        this.track = track;
        this.players = players;
    }

    /**
     * Sceglie la direzione da intraprendere in base alle celle libere e non occupate
     * dai giocatori avversari. Se esistono direzioni sicure, ne sceglie una a caso.
     * Se nessuna direzione è sicura, restituisce null (indicando che non si muoverà).
     *
     * @param allowedDirections Direzioni consentite in questo turno.
     * @return Una direzione sicura scelta casualmente, oppure null se non ce ne sono.
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        // Lista delle direzioni che risultano "sicure" (cella libera e non occupata).
        List<VectorDirection.CardinalDirection> safeDirections = new ArrayList<>();

        // Verifica per ogni direzione ammessa se la prossima cella è libera e non occupata.
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            // Calcola la posizione futura in base alla direzione.
            Position nextPos = getNextPosition(this.currentPosition, dir);
            boolean occupied = false;

            // Controlla se un altro giocatore occupa già quella posizione.
            for (IPlayer p : players) {
                // Evita di confrontare il bot con se stesso.
                if (p != this && p.getCurrentPosition().equals(nextPos)) {
                    occupied = true;
                    break;
                }
            }

            // Se la cella è libera sul tracciato e non occupata da altri giocatori, è sicura.
            if (track.isFree(nextPos) && !occupied) {
                safeDirections.add(dir);
            }
        }

        // Se ci sono direzioni sicure, scegline una casualmente.
        if (!safeDirections.isEmpty()) {
            return safeDirections.get(random.nextInt(safeDirections.size()));
        }

        // Se non ci sono direzioni sicure, non ci si muove (restituisce null).
        return null;
    }

    /**
     * Sceglie il valore di accelerazione in base alla vicinanza di altri giocatori.
     * Utilizza una distanza di soglia per verificare se altri bot sono "troppo vicini".
     * - Se qualcuno è a distanza ? 3 (Manhattan), riduce la velocità (fino a -1).
     * - Altrimenti, accelera di 1.
     *
     * @return Il valore di accelerazione (1 per accelerare, 0 per mantenere, -1 per decelerare).
     */
    @Override
    public int chooseAcceleration() {
        // Distanza di soglia per considerare un giocatore "vicino".
        final int THRESHOLD_DISTANCE = 3;

        // Flag che indica se almeno un altro giocatore è entro la distanza di soglia.
        boolean someoneClose = false;

        // Verifica tutti gli altri giocatori.
        for (IPlayer other : players) {
            // Ignora se stesso.
            if (other == this) continue;

            // Calcola la distanza Manhattan tra il bot e l'altro giocatore.
            int distance = manhattanDistance(this.currentPosition, other.getCurrentPosition());
            if (distance <= THRESHOLD_DISTANCE) {
                someoneClose = true;
                break;
            }
        }

        // Se c'è un giocatore vicino, decelera (se la velocità > 0), altrimenti accelera.
        if (someoneClose) {
            return (this.getVelocity() > 0) ? -1 : 0;
        } else {
            return 1;
        }
    }

    /**
     * Calcola la distanza Manhattan tra due posizioni.
     * La distanza Manhattan è la somma dei valori assoluti delle differenze
     * delle coordinate X e Y.
     *
     * @param p1 Prima posizione
     * @param p2 Seconda posizione
     * @return Distanza Manhattan tra p1 e p2
     */
    private int manhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    /**
     * Restituisce la prossima posizione tenendo conto della direzione cardinale.
     *
     * @param current Posizione attuale.
     * @param direction Direzione cardinale (N, NE, E, SE, S, SW, W, NW).
     * @return La nuova posizione calcolata a partire da 'current' e 'direction'.
     */
    private Position getNextPosition(Position current, VectorDirection.CardinalDirection direction) {
        int x = current.getX();
        int y = current.getY();

        // Aggiorna le coordinate in base alla direzione.
        switch (direction) {
            case N:
                y -= 1;
                break;
            case NE:
                x += 1;
                y -= 1;
                break;
            case E:
                x += 1;
                break;
            case SE:
                x += 1;
                y += 1;
                break;
            case S:
                y += 1;
                break;
            case SW:
                x -= 1;
                y += 1;
                break;
            case W:
                x -= 1;
                break;
            case NW:
                x -= 1;
                y -= 1;
                break;
        }

        // Ritorna la nuova posizione.
        return new Position(x, y);
    }
}