package VectorRace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Track implements ITrack{

    private char[][] grid;
    private Position start;
    private Position finish;
    @Override
    public void loadFromFile(String filename) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int rows = 0;
            int cols = 0;

            // First, read the file to determine the dimensions of the grid
            while ((line = br.readLine()) != null) {
                cols = Math.max(cols, line.length());
                rows++;
            }

            // Initialize the grid with the dimensions
            grid = new char[rows][cols];

            // Reset the BufferedReader to read the file again
            inputStream.close();

            try (InputStream inputStream2 = getClass().getClassLoader().getResourceAsStream(filename);
                 BufferedReader br2 = new BufferedReader(new InputStreamReader(inputStream2))) {
                int row = 0;
                while ((line = br2.readLine()) != null) {
                    for (int col = 0; col < line.length(); col++) {
                        char currentChar = line.charAt(col);
                        grid[row][col] = currentChar;

                        if (currentChar == 'S') {
                            start = new Position(row, col);
                        } else if (currentChar == 'F') {
                            finish = new Position(row, col);
                        } else if (currentChar == '#') {
                            // '#' is considered an obstacle
                        } else {
                            grid[row][col] = '.';
                        }
                    }
                    // Fill the rest of the row with empty spaces if line is shorter than max cols
                    for (int col = line.length(); col < cols; col++) {
                        grid[row][col] = '.';
                    }
                    row++;
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    public char[][] getGrid() {
        return grid;
    }

    public Position getStart() {
        return start;
    }

    public Position getFinish() {
        return finish;
    }

}


