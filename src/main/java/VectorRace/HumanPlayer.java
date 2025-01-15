package VectorRace;

import java.util.Scanner;

public class HumanPlayer extends BasePlayer {
    private Scanner scanner = new Scanner(System.in);

    public HumanPlayer(String name, Position start) {
        super(name, start);
    }

    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        System.out.println("Scegli una direzione tra: " + allowedDirections);
        String input = scanner.nextLine().toUpperCase();
        try {
            VectorDirection.CardinalDirection chosen = VectorDirection.CardinalDirection.valueOf(input);
            for (VectorDirection.CardinalDirection dir : allowedDirections) {
                if (dir == chosen) {
                    return chosen;
                }
            }
        } catch (IllegalArgumentException e) {}
        System.out.println("Direzione non valida. Riprova.");
        return chooseDirection(allowedDirections);
    }

    @Override
    public int chooseAcceleration() {
        System.out.println("Inserisci decisione: -1 per decelerare, 0 per costante, +1 per accelerare");
        int decision = scanner.nextInt();
        scanner.nextLine(); // Consuma newline
        if (decision < -1 || decision > 1) {
            System.out.println("Input non valido. Riprova.");
            return chooseAcceleration();
        }
        return decision;
    }
}


