package Model;
import GhoulBehaviours.GhoulBehavior;

import java.awt.Color;
import java.awt.Graphics;

public class Ghoul {
    private int x, y;
    private GhoulBehavior behavior;
    private int moveCounter;
    private int moveDelay;

    public Ghoul(int x, int y, GhoulBehavior behavior, int moveDelay) {
        this.x = x;
        this.y = y;
        this.behavior = behavior;
        this.moveDelay = moveDelay;
        this.moveCounter = 0;
    }

    public void move(int pacManX, int pacManY, int mazeWidth, int mazeHeight, int[][] mazeLayout) {
        if (moveCounter >= moveDelay) {
            behavior.move(this, pacManX, pacManY, mazeWidth, mazeHeight, mazeLayout);
            moveCounter = 0; // Reset the counter after moving
        } else {
            moveCounter++; // Increment the counter on each update
        }
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g, int cellSize, Color color) {
        g.setColor(color);
        g.fillOval(x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public void resetPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
