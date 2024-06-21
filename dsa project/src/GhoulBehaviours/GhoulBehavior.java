package GhoulBehaviours;
import Model.Ghoul;

public interface GhoulBehavior {
    void move(Ghoul ghoul, int pacManX, int pacManY, int mazeWidth, int mazeHeight, int[][] mazeLayout);
}
