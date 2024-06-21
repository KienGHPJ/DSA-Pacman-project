package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Model.Direction;

public class PacManKeyListener extends KeyAdapter {
    private GameLogic gameLogic;

    public PacManKeyListener(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                gameLogic.changePacManDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                gameLogic.changePacManDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                gameLogic.changePacManDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                gameLogic.changePacManDirection(Direction.RIGHT);
                break;
        }
    }
}
