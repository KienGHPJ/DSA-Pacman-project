package GhoulBehaviours;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import Model.Direction;
import Model.Ghoul;

public class DijkstraGhoulBehavior implements GhoulBehavior {

    private static class Node {
        int x, y, cost;
        Node parent;

        Node(int x, int y, int cost, Node parent) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.parent = parent;
        }

        int getScore() {
            return cost;
        }
    }

    @Override
    public void move(Ghoul ghoul, int pacManX, int pacManY, int mazeWidth, int mazeHeight, int[][] mazeLayout) {
        Node startNode = new Node(ghoul.getX(), ghoul.getY(), 0, null);
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getScore));
        Set<Node> closedSet = new HashSet<>();
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.x == pacManX && current.y == pacManY) {
                while (current.parent != null && current.parent.parent != null) {
                    current = current.parent;
                }
                ghoul.move(current.x - ghoul.getX(), current.y - ghoul.getY());
                return;
            }
            closedSet.add(current);
            for (Direction direction : Direction.values()) {
                int nextX = current.x + direction.getDx();
                int nextY = current.y + direction.getDy();
                if (isValidMove(nextX, nextY, mazeWidth, mazeHeight, mazeLayout) && !isInSet(closedSet, nextX, nextY)) {
                    Node neighbor = new Node(nextX, nextY, current.cost + 1, current);
                    openSet.add(neighbor);
                }
            }
        }
    }

    private boolean isValidMove(int x, int y, int mazeWidth, int mazeHeight, int[][] mazeLayout) {
        return x >= 0 && x < mazeWidth && y >= 0 && y < mazeHeight && mazeLayout[y][x] != 1;
    }

    private boolean isInSet(Set<Node> set, int x, int y) {
        return set.stream().anyMatch(node -> node.x == x && node.y == y);
    }
}
