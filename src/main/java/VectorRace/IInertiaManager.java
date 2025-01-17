package VectorRace;

import java.util.List;

/**
 * Interfaccia che definisce le regole di "inerzia" per i movimenti dei giocatori.
 * <p>
 * In molti giochi, la direzione attuale e la velocit� influenzano le direzioni
 * che il giocatore pu� intraprendere nel turno successivo. IInertiaManager si
 * occupa proprio di determinare quali direzioni siano ammesse date queste condizioni.
 *
 * Implementazioni diverse di IInertiaManager potrebbero introdurre meccaniche
 * personalizzate, ad esempio restrizioni pi� o meno severe in base alla velocit�.
 */
public interface IInertiaManager {

    /**
     * Restituisce l'elenco di direzioni cardinale consentite per il giocatore,
     * data la sua velocit� corrente e la direzione che stava seguendo nel turno precedente.
     *
     * @param currentVelocity      Velocit� attuale del giocatore.
     * @param previousDirection    Direzione seguita al turno precedente.
     * @return Lista di direzioni ammesse per il turno in corso.
     */
    List<VectorDirection.CardinalDirection> allowedDirections(int currentVelocity,
                                                              VectorDirection.CardinalDirection previousDirection);
}
