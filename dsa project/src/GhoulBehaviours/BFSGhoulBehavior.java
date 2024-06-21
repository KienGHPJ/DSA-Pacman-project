package GhoulBehaviours;
import java.util.*;

import Model.Direction;
import Model.Ghoul;

public class BFSGhoulBehavior implements GhoulBehavior {

    private static class Node {
        int x, y;
        Node parent;

        Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }
    }

    @Override
    public void move(Ghoul ghoul, int pacManX, int pacManY, int mazeWidth, int mazeHeight, int[][] mazeLayout) {
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Node startNode = new Node(ghoul.getX(), ghoul.getY(), null);
        queue.add(startNode);
        visited.add(ghoul.getX() + "," + ghoul.getY());

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.x == pacManX && current.y == pacManY) {
                while (current.parent != null && current.parent.parent != null) {
                    current = current.parent;
                }
                ghoul.move(current.x - ghoul.getX(), current.y - ghoul.getY());
                return;
            }
            for (Direction direction : Direction.values()) {
                int nextX = current.x + direction.getDx();
                int nextY = current.y + direction.getDy();
                if (isValidMove(nextX, nextY, mazeWidth, mazeHeight, mazeLayout) && !visited.contains(nextX + "," + nextY)) {
                    Node neighbor = new Node(nextX, nextY, current);
                    queue.add(neighbor);
                    visited.add(nextX + "," + nextY);
                }
            }
        }
    }

    private boolean isValidMove(int x, int y, int mazeWidth, int mazeHeight, int[][] mazeLayout) {
        return x >= 0 && x < mazeWidth && y >= 0 && y < mazeHeight && mazeLayout[y][x] != 1;
    }
}
