package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MazeLoader {
    public static class MazeInfo {
        public int[][] maze;
        public int pacManStartX;
        public int pacManStartY;
        public List<int[]> ghoulPositions;

        public MazeInfo(int[][] maze, int pacManStartX, int pacManStartY, List<int[]> ghoulPositions) {
            this.maze = maze;
            this.pacManStartX = pacManStartX;
            this.pacManStartY = pacManStartY;
            this.ghoulPositions = ghoulPositions;
        }
    }

    public static MazeInfo loadMaze(String filePath) throws IOException {
        InputStream is = MazeLoader.class.getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        int width = 0;
        int height = 0;
        int pacManStartX = -1;
        int pacManStartY = -1;
        List<int[]> ghoulPositions = new ArrayList<>();

        // First pass to determine the dimensions of the maze
        while ((line = reader.readLine()) != null) {
            width = Math.max(width, line.length());
            height++;
        }
        reader.close();

        int[][] maze = new int[height][width];
        is = MazeLoader.class.getResourceAsStream(filePath);
        reader = new BufferedReader(new InputStreamReader(is));
        int y = 0;

        // Second pass to fill the maze array and find PacMan's and Ghouls' starting positions
        while ((line = reader.readLine()) != null) {
            for (int x = 0; x < line.length(); x++) {
                char currentChar = line.charAt(x);
                if (currentChar == '%') {
                    maze[y][x] = 1;
                } else if (currentChar == 'P') {
                    maze[y][x] = 0;  // Treat 'P' as an empty space
                    pacManStartX = x;
                    pacManStartY = y;
                } else if (currentChar == 'G') {
                    maze[y][x] = 0;  // Treat 'G' as an empty space
                    ghoulPositions.add(new int[]{x, y});
                } else {
                    maze[y][x] = 0;
                }
            }
            y++;
        }
        reader.close();

        return new MazeInfo(maze, pacManStartX, pacManStartY, ghoulPositions);
    }
}
