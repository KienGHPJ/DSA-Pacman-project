	package pacmanbehaviors;
	
	import Model.Direction;
	import Model.PacMan;
	import Model.Pellet;
	
	import java.util.*;
	
	public class BFSPacManBehavior implements PacManBehavior {
	
	    @Override
	    public void move(PacMan pacMan, List<Pellet> pellets, int mazeWidth, int mazeHeight, int[][] mazeLayout) {
	        Pellet targetPellet = findNearestPellet(pacMan, pellets);
	        if (targetPellet == null) return;
	
	        boolean[][] visited = new boolean[mazeHeight][mazeWidth];
	        Queue<Node> queue = new LinkedList<>();
	        queue.add(new Node(pacMan.getX(), pacMan.getY(), null, null));
	
	        while (!queue.isEmpty()) {
	            Node current = queue.poll();
	            if (current.x == targetPellet.getX() && current.y == targetPellet.getY()) {
	                while (current.parent != null && current.parent.parent != null) {
	                    current = current.parent;
	                }
	                pacMan.move(current.x - pacMan.getX(), current.y - pacMan.getY(), mazeLayout);
	                return;
	            }
	
	            for (Direction direction : Direction.values()) {
	                int nextX = current.x + direction.getDx();
	                int nextY = current.y + direction.getDy();
	                if (isValidMove(nextX, nextY, mazeWidth, mazeHeight, mazeLayout) && !visited[nextY][nextX]) {
	                    visited[nextY][nextX] = true;
	                    queue.add(new Node(nextX, nextY, current, direction));
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
	
	    private static class Node {
	        int x, y;
	        Node parent;
	        Direction direction;
	
	        Node(int x, int y, Node parent, Direction direction) {
	            this.x = x;
	            this.y = y;
	            this.parent = parent;
	            this.direction = direction;
	        }
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
