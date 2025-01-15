package VectorRace;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Track track = new Track();
        track.loadFromFile("track.txt");

        char[][] grid = track.getGrid();
        for (char[] row : grid) {
            System.out.println(new String(row));
        }

        System.out.println("Start Position: (" + track.getStart().getX() + ", " + track.getStart().getY() + ")");
        System.out.println("Finish Position: (" + track.getFinish().getX() + ", " + track.getFinish().getY() + ")");
    }
}