package pacmanbehaviors;

import Model.PacMan;
import Model.Pellet;

import java.util.List;

public interface PacManBehavior {
void move(PacMan pacMan, List<Pellet> pellets, int mazeWidth, int mazeHeight, int[][] mazeLayout);
}