package VectorRace;

import VectorRace.Fisica.DefaultInertiaManager;
import VectorRace.Fisica.IInertiaManager;
import VectorRace.Fisica.IVelocityCalculator;
import VectorRace.Fisica.SimpleVelocityCalculator;
import VectorRace.Giocatori.AggressiveBot;
import VectorRace.Giocatori.GreedyBot;
import VectorRace.Giocatori.IPlayer;
import VectorRace.Motore.GameBoard;
import VectorRace.Motore.GameEngine;
import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        IPlayer aggressiveBot = new AggressiveBot("a", track.getStartPosition());;
        //IPlayer defensiveBot = new DefensiveBot("DefBot", track.getStartPosition(), track);
        IPlayer greedyBot = new GreedyBot("g", track.getStartPosition(), track);
        //IPlayer speedBot = new SpeedControlBot("s", track.getStartPosition(), track);

        //engine.addPlayer(human);
        engine.addPlayer(aggressiveBot);
        //engine.addPlayer(defensiveBot);
        engine.addPlayer(greedyBot);

        engine.startRace();
    }
}