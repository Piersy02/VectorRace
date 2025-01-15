package VectorRace;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        ITrack track = new Track();
        try {
            track.loadFromFile("track.txt");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento del tracciato: " + e.getMessage());
            return;
        }

        GameBoard board = new GameBoard(track);
        IVelocityCalculator velocityCalc = new SimpleVelocityCalculator();
        IInertiaManager inertiaMgr = new DefaultInertiaManager();
        int maxTurns = 20;  // Imposta qui il limite desiderato
        GameEngine engine = new GameEngine(board, velocityCalc, inertiaMgr, maxTurns);

        //IPlayer human = new HumanPlayer("Saverio", track.getStartPosition());
        IPlayer aggressiveBot = new AggressiveBot("AggroBot", track.getStartPosition());
        IPlayer defensiveBot = new DefensiveBot("DefBot", track.getStartPosition(), track);

        //engine.addPlayer(human);
        engine.addPlayer(aggressiveBot);
        engine.addPlayer(defensiveBot);

        //board.display(engine.players);  // Visualizza lo stato iniziale se desiderato

        engine.startRace();
    }
}