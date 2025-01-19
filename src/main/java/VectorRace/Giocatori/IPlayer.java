package VectorRace.Giocatori;

import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

/**
 * L'interfaccia IPlayer definisce le operazioni di base richieste
 * a un "giocatore" (umano o bot) all'interno del gioco VectorRace.
 * <p>
 * Un'implementazione tipica includer�:
 * - gestione della posizione corrente;
 * - gestione della velocit�;
 * - logica di scelta della direzione e dell'accelerazione.
 */
public interface IPlayer {

    /**
     * Restituisce la posizione corrente del giocatore.
     *
     * @return Oggetto {@link Position} che rappresenta la posizione attuale.
     */
    Position getCurrentPosition();

    /**
     * Imposta la posizione corrente del giocatore.
     *
     * @param position La nuova {@link Position} da assegnare.
     */
    void setCurrentPosition(Position position);

    /**
     * Restituisce la velocit� attuale del giocatore.
     *
     * @return Valore intero della velocit�.
     */
    int getVelocity();

    /**
     * Imposta la velocit� del giocatore.
     *
     * @param velocity Nuovo valore di velocit�.
     */
    void setVelocity(int velocity);

    /**
     * Sceglie una direzione tra quelle possibili.
     * La logica di scelta varia a seconda dell'implementazione (bot, umano, ecc.).
     *
     * @param allowedDirections Insieme di direzioni consentite in questo turno.
     * @return La direzione selezionata per il turno.
     */
    VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections);

    /**
     * Sceglie l'accelerazione (variazione di velocit�).
     * Il contratto prevede tre valori possibili:
     * -1 (decelerazione), 0 (mantieni la velocit�), +1 (aumenta la velocit�).
     *
     * @return Un intero compreso tra -1 e +1.
     */
    int chooseAcceleration();
}
