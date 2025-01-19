package VectorRace.Posizione;

import VectorRace.Posizione.Position;

import java.io.IOException;

/**
 * L'interfaccia ITrack definisce i metodi fondamentali per interagire
 * con un tracciato di gioco (mappa) in VectorRace.
 * <p>
 * Un'implementazione di ITrack pu� rappresentare il tracciato in vari modi
 * (array bidimensionale, lista di liste, file, ecc.),
 * purch� fornisca le operazioni richieste.
 */
public interface ITrack {

    /**
     * Carica i dati del tracciato da un file esterno.
     * Il formato del file e le modalit� di parsing
     * possono variare nelle diverse implementazioni concrete.
     *
     * @param filename Nome o percorso del file da cui caricare il tracciato.
     * @throws IOException Se si verifica un errore di lettura del file.
     */
    void loadFromFile(String filename) throws IOException;

    /**
     * Restituisce il carattere che rappresenta la cella
     * in una determinata posizione (es. '.', '#', ecc.).
     *
     * @param position Posizione per cui si vuole il carattere.
     * @return Il carattere corrispondente a quella cella.
     */
    char getCell(Position position);

    /**
     * Indica se la cella a una certa posizione � libera
     * (cio� non contiene ostacoli n� altri elementi bloccanti).
     *
     * @param position Posizione da verificare.
     * @return true se la cella � libera, false altrimenti.
     */
    boolean isFree(Position position);

    /**
     * Indica se la cella a una certa posizione � un ostacolo.
     *
     * @param position Posizione da verificare.
     * @return true se la cella corrisponde a un ostacolo, false altrimenti.
     */
    boolean isObstacle(Position position);

    /**
     * Indica se la cella a una certa posizione corrisponde al traguardo.
     *
     * @param position Posizione da verificare.
     * @return true se la cella � parte del traguardo, false altrimenti.
     */
    boolean isFinish(Position position);

    /**
     * Restituisce una posizione di partenza valida sul tracciato.
     * Nelle implementazioni che prevedono pi� posizioni di start,
     * si pu� restituire la prima posizione disponibile,
     * oppure una posizione predefinita.
     *
     * @return Una {@link Position} di start.
     */
    Position getStartPosition();

    /**
     * Restituisce una posizione di traguardo se presente,
     * altrimenti pu� restituire null o sollevare un'eccezione.
     *
     * @return Una {@link Position} di finish (se presente),
     *         altrimenti null.
     */
    Position getFinishPosition();

    /**
     * Restituisce la larghezza (numero di colonne) del tracciato.
     *
     * @return Valore intero che rappresenta la larghezza.
     */
    int getWidth();

    /**
     * Restituisce l'altezza (numero di righe) del tracciato.
     *
     * @return Valore intero che rappresenta l'altezza.
     */
    int getHeight();
}

