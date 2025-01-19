package VectorRace.Giocatori;

import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DefensiveBot implementa una strategia difensiva:
 * - Tenta di evitare ostacoli scegliendo direzioni "sicure".
 * - Tende a decelerare piuttosto che accelerare,
 *   per minimizzare il rischio di collisioni o di uscita dal tracciato.
 */
public class DefensiveBot extends BasePlayer {

    /**
     * Generatore di numeri casuali per scegliere
     * in modo aleatorio tra più opzioni.
     */
    private Random random = new Random();

    /**
     * Riferimento al tracciato di gioco, usato per
     * verificare se una cella è libera o occupata/ostacolo.
     */
    private ITrack track;

    /**
     * Costruttore di DefensiveBot.
     *
     * @param name  Nome del bot.
     * @param start Posizione di partenza sul tracciato.
     * @param track Riferimento al tracciato di gioco.
     */
    public DefensiveBot(String name, Position start, ITrack track) {
        // Richiama il costruttore della superclasse.
        super(name, start);
        this.track = track;
    }

    /**
     * Sceglie la direzione in modo difensivo:
     * - Cerca innanzitutto tra le direzioni consentite
     *   quelle che portano in una cella libera (senza ostacoli).
     * - Se ne trova, ne seleziona una in modo casuale.
     * - Altrimenti, sceglie in modo casuale tra tutte le direzioni
     *   consentite, o eventualmente non si muove (null).
     *
     * @param allowedDirections Insieme di direzioni possibili in questo turno.
     * @return La direzione selezionata o null (se decide di non muoversi).
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        // Trova le direzioni "sicure" (che portano a una cella libera).
        List<VectorDirection.CardinalDirection> safeDirections = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            // Calcola la prossima posizione in base alla direzione.
            Position nextPos = getNextPosition(this.currentPosition, dir);
            // Se la cella è libera, aggiunge la direzione alla lista delle sicure.
            if (track.isFree(nextPos)) {
                safeDirections.add(dir);
            }
        }

        // Se ci sono direzioni sicure, scegline una casualmente.
        if (!safeDirections.isEmpty()) {
            return safeDirections.get(random.nextInt(safeDirections.size()));
        }

        // Se non ci sono direzioni sicure,
        // o si sceglie a caso tra quelle consentite,
        // o si può decidere di non muoversi (null).
        List<VectorDirection.CardinalDirection> options = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            options.add(dir);
        }

        // Restituisce null se non ci sono direzioni disponibili,
        // altrimenti sceglie casualmente tra quelle rimaste.
        return options.isEmpty() ? null : options.get(random.nextInt(options.size()));
    }

    /**
     * Sceglie l'accelerazione con una strategia difensiva:
     * - Se la velocità è superiore a 0, decelera di 1.
     * - Se la velocità è già a 0, non accelera (0).
     *
     * @return -1 se la velocità > 0, altrimenti 0.
     */
    @Override
    public int chooseAcceleration() {
        // Se la velocità è positiva, decelera di 1; altrimenti rimane invariata.
        return (this.getVelocity() > 0) ? -1 : 0;
    }

    /**
     * Calcola la prossima posizione sulla base della direzione specificata.
     *
     * @param current   Posizione attuale.
     * @param direction Direzione cardinale (N, NE, E, SE, S, SW, W, NW).
     * @return La nuova posizione (x,y) dopo essersi spostati
     *         di una cella nella direzione indicata.
     */
    private Position getNextPosition(Position current, VectorDirection.CardinalDirection direction) {
        int x = current.getX();
        int y = current.getY();

        // Aggiorna (x, y) secondo la direzione.
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

        // Ritorna la nuova posizione.
        return new Position(x, y);
    }
}
