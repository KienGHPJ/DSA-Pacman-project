package Model;

import pacmanbehaviors.PacManBehavior;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class PacMan {
    private int x, y;
    private Direction direction;
    private boolean mouthOpen;
    private PacManBehavior behavior;

    public PacMan(int x, int y, Direction direction, PacManBehavior behavior) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.mouthOpen = true;
        this.behavior = behavior;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void move(int dx, int dy, int[][] mazeLayout) {
        int newX = x + dx;
        int newY = y + dy;

        if (isValidMove(newX, newY, mazeLayout)) {
            x = newX;
            y = newY;

            if (dx > 0) {
                direction = Direction.RIGHT;
            } else if (dx < 0) {
                direction = Direction.LEFT;
            } else if (dy > 0) {
                direction = Direction.DOWN;
            } else if (dy < 0) {
                direction = Direction.UP;
            }
        }
    }

    private boolean isValidMove(int newX, int newY, int[][] mazeLayout) {
        return newX >= 0 && newX < mazeLayout[0].length && newY >= 0 && newY < mazeLayout.length && mazeLayout[newY][newX] != 1;
    }

    public void resetPosition(int startX, int startY) {
        x = startX;
        y = startY;
        direction = Direction.RIGHT;
    }

    public void animateMouth() {
        mouthOpen = !mouthOpen;
    }

    public void draw(Graphics g, int cellSize) {
        g.setColor(Color.YELLOW);
        int arcStartAngle = getArcStartAngle();
        int arcAngle = mouthOpen ? 300 : 360;
        g.fillArc(x * cellSize, y * cellSize, cellSize, cellSize, arcStartAngle, arcAngle);
    }

    private int getArcStartAngle() {
        switch (direction) {
            case UP:
                return 120; // Start angle for up direction
            case DOWN:
                return 300; // Start angle for down direction
            case LEFT:
                return 210; // Start angle for left direction
            case RIGHT:
            default:
                return 30; // Start angle for right direction
        }
    }

    public void updatePosition(List<Pellet> pellets, int[][] mazeLayout, int mazeWidth, int mazeHeight) {
        behavior.move(this, pellets, mazeWidth, mazeHeight, mazeLayout);
    }
}
