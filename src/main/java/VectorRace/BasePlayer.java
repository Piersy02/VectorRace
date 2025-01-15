package VectorRace;

public abstract class BasePlayer implements IPlayer {
    protected String name;
    protected Position currentPosition;
    protected int velocity;

    public BasePlayer(String name, Position start) {
        this.name = name;
        this.currentPosition = start;
        this.velocity = 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public Position getCurrentPosition() { return currentPosition; }

    @Override
    public void setCurrentPosition(Position position) { this.currentPosition = position; }

    @Override
    public int getVelocity() { return velocity; }

    @Override
    public void setVelocity(int velocity) {
        if (velocity < 0) velocity = 0;
        else if (velocity > 3) velocity = 3;
        this.velocity = velocity;
    }
}


