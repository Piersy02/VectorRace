package VectorRace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Track implements ITrack {
    private char[][] grid;
    private List<Position> startPositions = new ArrayList<>();
    private List<Position> finishPositions = new ArrayList<>();
    private int width;
    private int height;

    @Override
    public void loadFromFile(String filename) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new IOException("File non trovato: " + filename);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
                width = Math.max(width, line.length());
                height++;
            }

            grid = new char[height][width];
            for (int y = 0; y < height; y++) {
                String currentLine = lines.get(y);
                for (int x = 0; x < width; x++) {
                    char currentChar = (x < currentLine.length()) ? currentLine.charAt(x) : '.';
                    switch (currentChar) {
                        case 'S':
                            startPositions.add(new Position(x, y));
                            grid[y][x] = '.'; // Considera la cella come libera, ma registra la posizione di start
                            break;
                        case 'F':
                            finishPositions.add(new Position(x, y));
                            grid[y][x] = '.'; // Analogamente per la fine
                            break;
                        case '#':
                            grid[y][x] = '#';
                            break;
                        default:
                            grid[y][x] = '.';
                            break;
                    }
                }
            }
        }
    }

    @Override
    public char getCell(Position position) {
        if (isWithinBounds(position)) {
            return grid[position.getY()][position.getX()];
        }
        return '#'; // Fuori dai limiti considerato ostacolo
    }

    @Override
    public boolean isFree(Position position) {
        return getCell(position) == '.';
    }

    @Override
    public boolean isObstacle(Position position) {
        return getCell(position) == '#';
    }

    @Override
    public boolean isFinish(Position position) {
        // Verifica se la posizione è uno dei punti di arrivo
        return finishPositions.contains(position);
    }

    @Override
    public Position getStartPosition() {
        // Ritorna la prima posizione di partenza se disponibile
        return startPositions.isEmpty() ? null : startPositions.get(0);
    }

    @Override
    public Position getFinishPosition() {
        // Ritorna la prima posizione di arrivo se disponibile
        return finishPositions.isEmpty() ? null : finishPositions.get(0);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public List<Position> getAllStartPositions() {
        return startPositions;
    }

    public List<Position> getAllFinishPositions() {
        return finishPositions;
    }

    private boolean isWithinBounds(Position position) {
        return position.getX() >= 0 && position.getX() < width &&
                position.getY() >= 0 && position.getY() < height;
    }
}
