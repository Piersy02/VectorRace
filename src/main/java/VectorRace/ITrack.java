package VectorRace;

import java.io.IOException;

public interface ITrack {
    void loadFromFile(String filename) throws IOException;
    char getCell(Position position);
    boolean isFree(Position position);
    boolean isObstacle(Position position);
    boolean isFinish(Position position);
    Position getStartPosition();
    Position getFinishPosition();
    int getWidth();
    int getHeight();
}
