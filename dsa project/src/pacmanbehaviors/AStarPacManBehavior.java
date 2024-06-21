package pacmanbehaviors;

import Model.Direction;
import Model.PacMan;
import Model.Pellet;

import java.util.*;

public class AStarPacManBehavior implements PacManBehavior {

    private static class Node {
        int x, y, cost, heuristic;
        Node parent;

        Node(int x, int y, int cost, int heuristic, Node parent) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.heuristic = heuristic;
            this.parent = parent;
        }

        int getScore() {
            return cost + heuristic;
        }
    }

    @Override
    public void move(PacMan pacMan, List<Pellet> pellets, int mazeWidth, int mazeHeight, int[][] mazeLayout) {
        Pellet targetPellet = findNearestPellet(pacMan, pellets);
        if (targetPellet == null) return;

        Node startNode = new Node(pacMan.getX(), pacMan.getY(), 0, heuristic(pacMan.getX(), pacMan.getY(), targetPellet.getX(), targetPellet.getY()), null);
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getScore));
        Map<String, Node> allNodes = new HashMap<>();
        openSet.add(startNode);
        allNodes.put(nodeKey(startNode.x, startNode.y), startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.x == targetPellet.getX() && current.y == targetPellet.getY()) {
                while (current.parent != null && current.parent.parent != null) {
                    current = current.parent;
                }
                int dx = current.x - pacMan.getX();
                int dy = current.y - pacMan.getY();
                pacMan.move(dx, dy, mazeLayout);
                updatePacManDirection(pacMan, dx, dy);
                return;
            }

            for (Direction direction : Direction.values()) {
                int nextX = current.x + direction.getDx();
                int nextY = current.y + direction.getDy();
                if (isValidMove(nextX, nextY, mazeWidth, mazeHeight, mazeLayout)) {
                    String key = nodeKey(nextX, nextY);
                    int newCost = current.cost + 1;
                    Node neighbor = allNodes.get(key);
                    if (neighbor == null) {
                        neighbor = new Node(nextX, nextY, newCost, heuristic(nextX, nextY, targetPellet.getX(), targetPellet.getY()), current);
                        allNodes.put(key, neighbor);
                        openSet.add(neighbor);
                    } else if (newCost < neighbor.cost) {
                        neighbor.cost = newCost;
                        neighbor.parent = current;
                        openSet.remove(neighbor);
                        openSet.add(neighbor);
                    }
                }
            }
        }
    }

    private Pellet findNearestPellet(PacMan pacMan, List<Pellet> pellets) {
        if (pellets.isEmpty()) return null;

        Pellet nearestPellet = null;
        int minDistance = Integer.MAX_VALUE;
        for (Pellet pellet : pellets) {
            int distance = Math.abs(pacMan.getX() - pellet.getX()) + Math.abs(pacMan.getY() - pellet.getY());
            if (distance < minDistance) {
                minDistance = distance;
                nearestPellet = pellet;
            }
        }
        return nearestPellet;
    }

    private boolean isValidMove(int x, int y, int mazeWidth, int mazeHeight, int[][] mazeLayout) {
        return x >= 0 && x < mazeWidth && y >= 0 && y < mazeHeight && mazeLayout[y][x] != 1;
    }

    private int heuristic(int x, int y, int targetX, int targetY) {
        return Math.abs(x - targetX) + Math.abs(y - targetY);
    }

    private String nodeKey(int x, int y) {
        return x + "," + y;
    }

    private void updatePacManDirection(PacMan pacMan, int dx, int dy) {
        if (dx > 0) {
            pacMan.setDirection(Direction.RIGHT);
        } else if (dx < 0) {
            pacMan.setDirection(Direction.LEFT);
        } else if (dy > 0) {
            pacMan.setDirection(Direction.DOWN);
        } else if (dy < 0) {
            pacMan.setDirection(Direction.UP);
        }
    }
}
