package VectorRace;

import java.util.List;

public interface IInertiaManager {
    List<VectorDirection.CardinalDirection> allowedDirections(int currentVelocity, VectorDirection.CardinalDirection previousDirection);
}
