package VectorRace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AggressiveBot extends BasePlayer {
    private Random random = new Random();

    public AggressiveBot(String name, Position start) {
        super(name, start);
    }

    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        List<VectorDirection.CardinalDirection> options = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            options.add(dir);
        }
        return options.get(random.nextInt(options.size()));
    }

    @Override
    public int chooseAcceleration() {
        return (this.getVelocity() < 3) ? 1 : 0;
    }
}

