package VectorRace;

public interface IPlayer {
    Position getCurrentPosition();
    void setCurrentPosition(Position position);
    int getVelocity();
    void setVelocity(int velocity);
    VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections);
    int chooseAcceleration(); // -1 per decelerare, 0 per costante, +1 per accelerare
}
