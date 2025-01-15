package VectorRace;

public class SimpleVelocityCalculator implements IVelocityCalculator {
    @Override
    public int velocity(Position pos1, Position pos2) {
        int diffX = pos2.getX() - pos1.getX();
        int diffY = pos2.getY() - pos1.getY();
        return Math.max(Math.abs(diffX), Math.abs(diffY));
    }
}
