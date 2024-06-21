package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import GhoulBehaviours.*;
import Model.Direction;
import Model.Ghoul;
import Model.PacMan;
import Model.Pellet;
import pacmanbehaviors.*;

public class GameLogic {
    private int mazeWidth, mazeHeight;
    private int[][] mazeLayout;
    private PacMan pacMan;
    private List<Ghoul> ghouls;
    private List<Pellet> pellets;
    private int score;
    private int lives;
    private boolean gameOver;
    private boolean gameWon;
    private Timer scoreDecrementTimer;
    private boolean isAIControlled;

    public GameLogic(int mazeWidth, int mazeHeight, int[][] mazeLayout, int pacManStartX, int pacManStartY, List<int[]> ghoulPositions) {
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;
        this.mazeLayout = mazeLayout;

        // Initialize PacMan with the starting position from the maze
        pacMan = new PacMan(pacManStartX, pacManStartY, Direction.RIGHT, new AStarPacManBehavior());

        // Initialize Ghouls with the starting positions from the maze
        this.ghouls = new ArrayList<>();
        for (int[] pos : ghoulPositions) {
            ghouls.add(new Ghoul(pos[0], pos[1], new DijkstraGhoulBehavior(), 1));
        }

        this.pellets = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.gameOver = false;
        this.gameWon = false;
        this.isAIControlled = false; // Initially AI controlled
        initializePellets();
        startScoreDecrementTimer();
    }

    private void initializePellets() {
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                if (mazeLayout[y][x] == 0) {
                    pellets.add(new Pellet(x, y, false));
                } else if (mazeLayout[y][x] == 2) {
                    pellets.add(new Pellet(x, y, true)); // Power pellet
                }
            }
        }
    }

    private void startScoreDecrementTimer() {
        scoreDecrementTimer = new Timer(1000, e -> decreaseScore(10));
        scoreDecrementTimer.start();
    }

    public void updateGame() {
        if (gameOver || gameWon) return;

        if (isAIControlled) {
            // Move PacMan using the behavior
            pacMan.updatePosition(pellets, mazeLayout, mazeWidth, mazeHeight);
        } else {
            // Move PacMan manually based on direction (handled in PacManGame's KeyListener)
            pacMan.move(pacMan.getDirection().getDx(), pacMan.getDirection().getDy(), mazeLayout);
        }

        // Check collisions with pellets
        pellets.removeIf(pellet -> {
            if (pellet.getX() == pacMan.getX() && pellet.getY() == pacMan.getY()) {
                score += pellet.isPowerPellet() ? 50 : 10;
                return true;
            }
            return false;
        });

        // Check if all pellets are eaten
        if (pellets.isEmpty()) {
            gameWon = true;
            scoreDecrementTimer.stop(); // Stop the timer when the game is won
        }

        // Move Ghouls
        for (Ghoul ghoul : ghouls) {
            ghoul.move(pacMan.getX(), pacMan.getY(), mazeWidth, mazeHeight, mazeLayout);
        }

        // Check collisions with ghouls
        for (Ghoul ghoul : ghouls) {
            if (ghoul.getX() == pacMan.getX() && ghoul.getY() == pacMan.getY()) {
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                } else {
                    pacMan.resetPosition(1, 1); // Reset PacMan's position to the starting position
                    for (Ghoul g : ghouls) {
                        g.resetPosition(9, 9); // Reset ghouls' positions to the starting positions
                    }
                }
                break;
            }
        }
    }

    public void changePacManDirection(Direction direction) {
        pacMan.setDirection(direction);
    }

    public void toggleControlMode() {
        isAIControlled = !isAIControlled;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void decreaseScore(int amount) {
        score -= amount;
        if (score < 0) score = 0; // Ensure score doesn't go negative
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < mazeWidth && y >= 0 && y < mazeHeight && mazeLayout[y][x] != 1;
    }

    public void draw(Graphics g, int cellSize) {
        drawMaze(g, cellSize);
        drawPellets(g, cellSize);
        drawPacMan(g, cellSize);
        drawGhouls(g, cellSize); // Draw ghouls
        drawLives(g, cellSize);
        drawScore(g, cellSize); // Draw the score
        if (gameOver) {
            drawGameOverScreen(g, cellSize);
        } else if (gameWon) {
            drawGameWonScreen(g, cellSize);
        }
    }

    private void drawMaze(Graphics g, int cellSize) {
        g.setColor(Color.BLUE);
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                if (mazeLayout[y][x] == 1) {
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    private void drawPellets(Graphics g, int cellSize) {
        for (Pellet pellet : pellets) {
            pellet.draw(g, cellSize);
        }
    }

    private void drawPacMan(Graphics g, int cellSize) {
        pacMan.draw(g, cellSize);
    }

    private void drawGhouls(Graphics g, int cellSize) {
        Color[] ghoulColors = {Color.RED, Color.GREEN, Color.PINK, Color.ORANGE};
        for (int i = 0; i < ghouls.size(); i++) {
            ghouls.get(i).draw(g, cellSize, ghoulColors[i % ghoulColors.length]);
        }
    }

    private void drawLives(Graphics g, int cellSize) {
        g.setColor(Color.YELLOW);
        for (int i = 0; i < lives; i++) {
            g.fillArc(i * cellSize, mazeHeight * cellSize + 10, cellSize, cellSize, 30, 300);
        }
    }

    private void drawScore(Graphics g, int cellSize) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20); // Display the score at the top-left corner
    }

    private void drawGameOverScreen(Graphics g, int cellSize) {
        g.setColor(Color.RED);
        g.drawString("Game Over", mazeWidth * cellSize / 2 - 30, mazeHeight * cellSize / 2);
    }

    private void drawGameWonScreen(Graphics g, int cellSize) {
        g.setColor(Color.GREEN);
        g.drawString("You Win!", mazeWidth * cellSize / 2 - 30, mazeHeight * cellSize / 2);
    }

    public void animatePacManMouth() {
        pacMan.animateMouth();
    }
}
