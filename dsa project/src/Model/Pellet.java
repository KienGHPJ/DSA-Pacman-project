package Model;

import java.awt.Color;
import java.awt.Graphics;

public class Pellet {
    private int x, y;
    private boolean isPowerPellet;

    public Pellet(int x, int y, boolean isPowerPellet) {
        this.x = x;
        this.y = y;
        this.isPowerPellet = isPowerPellet;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPowerPellet() {
        return isPowerPellet;
    }

    public void draw(Graphics g, int cellSize) {
        if (isPowerPellet) {
            g.setColor(Color.YELLOW); // Power pellet color
            int size = cellSize / 2;
            g.fillOval(x * cellSize + size / 2, y * cellSize + size / 2, size, size);
        } else {
            g.setColor(Color.WHITE); // Regular pellet color
            int size = cellSize / 4;
            g.fillOval(x * cellSize + size, y * cellSize + size, size, size);
        }
    }
}
