package VectorRace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        List<IPlayer> playersList = new ArrayList<>();

        //IPlayer human = new HumanPlayer("h", track.getStartPosition());
        IPlayer aggressiveBot = new AggressiveBot("a", track.getStartPosition());
        //IPlayer defensiveBot = new DefensiveBot("DefBot", track.getStartPosition(), track);
        IPlayer greedyBot = new GreedyBot("g", track.getStartPosition(), track);
        //IPlayer speedBot = new SpeedControlBot("s", track.getStartPosition(), track);
        IPlayer awareBot = new AwareBot("c", track.getStartPosition(), track, playersList);

        //engine.addPlayer(human);
        engine.addPlayer(aggressiveBot);
        //engine.addPlayer(defensiveBot);
        engine.addPlayer(greedyBot);
        //engine.addPlayer(speedBot);
        engine.addPlayer(awareBot);

        for (IPlayer p : playersList) {
            engine.addPlayer(p);
        }

        //board.display(engine.players);  // Visualizza lo stato iniziale se desiderato

        engine.startRace();
    }
}