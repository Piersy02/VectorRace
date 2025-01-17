package VectorRace;

/**
 * L'interfaccia IVelocityCalculator definisce il metodo per calcolare
 * una velocit� in base a due posizioni.
 * <p>
 * Questo pu� essere utile in vari scenari, ad esempio:
 * - Interpretare la distanza tra due punti come velocit� (o viceversa).
 * - Tradurre la differenza di posizioni in un valore di velocit� da assegnare a un giocatore.
 * <p>
 * L�implementazione concreta di IVelocityCalculator decider� come usare
 * le coordinate (e.g. distanza Euclidea, Manhattan, ecc.) per restituire
 * un valore di velocit�.
 */
public interface IVelocityCalculator {

    /**
     * Calcola la velocit� (un valore intero) a partire da due posizioni.
     * In base all'implementazione, potrebbe trattarsi di:
     * - Distanza Euclidea arrotondata.
     * - Distanza Manhattan.
     * - Differenze di coordinate sommate/pesate.
     *
     * @param pos1 Prima posizione.
     * @param pos2 Seconda posizione.
     * @return Un valore intero di velocit�, calcolato in base a pos1 e pos2.
     */
    int velocity(Position pos1, Position pos2);
}
