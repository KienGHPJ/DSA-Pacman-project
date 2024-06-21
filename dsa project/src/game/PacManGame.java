package game;

import Model.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

public class PacManGame extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final int CELL_SIZE = 20;
    private final int TIMER_DELAY = 150;
    private GameLogic gameLogic;
    private Timer timer;

    public PacManGame() {
        setLayout(new BorderLayout());

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                gameLogic.draw(g, CELL_SIZE);
                Toolkit.getDefaultToolkit().sync();
            }
        };
        gamePanel.setPreferredSize(new Dimension(500, 500)); // Set an initial size
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new TAdapter());

        add(gamePanel, BorderLayout.CENTER);

        String[] options = {"Small Maze", "Medium Maze", "Big Maze", "How to Play"};
        int choice = JOptionPane.showOptionDialog(null, "Select a Maze", "Maze Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 3) { // "How to Play" option
            showHowToPlay();
            // Show the maze selection dialog again after displaying the instructions
            choice = JOptionPane.showOptionDialog(null, "Select a Maze", "Maze Selection",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        }

        String selectedMazePath = "/resources/smallMaze.lay";  // Default to small maze

        switch (choice) {
            case 1:
                selectedMazePath = "/resources/mediumMaze.lay";
                break;
            case 2:
                selectedMazePath = "/resources/bigMaze.lay";
                break;
        }

        MazeLoader.MazeInfo mazeInfo = null;
        try {
            mazeInfo = MazeLoader.loadMaze(selectedMazePath);  // Load maze and get PacMan's start position
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        int[][] mazeLayout = mazeInfo.maze;
        int pacManStartX = mazeInfo.pacManStartX;
        int pacManStartY = mazeInfo.pacManStartY;
        List<int[]> ghoulPositions = mazeInfo.ghoulPositions;

        // Pass PacMan's and Ghouls' start positions to GameLogic
        gameLogic = new GameLogic(mazeLayout[0].length, mazeLayout.length, mazeLayout, pacManStartX, pacManStartY, ghoulPositions);
        gamePanel.setPreferredSize(new Dimension(mazeLayout[0].length * CELL_SIZE, (mazeLayout.length * CELL_SIZE) + 50));

        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameLogic.isGameOver()) {
            timer.stop();
            System.out.println("Game Over!");
            JOptionPane.showMessageDialog(this, "Game Over! ");
        } else {
            gameLogic.updateGame();
            gameLogic.animatePacManMouth();
            repaint();
        }
    }

    private void showHowToPlay() {
        String instructions = "How to Play PacMan:\n\n" +
                "- Use the arrow keys to move PacMan.\n" +
                "- Eat all the pellets to win.\n" +
                "- Avoid the ghouls or you'll lose a life.\n" +
                "- Collect power pellets to temporarily eat the ghouls.\n" +
                "- Press 'T' to toggle control mode.";

        JOptionPane.showMessageDialog(this, instructions, "How to Play", JOptionPane.INFORMATION_MESSAGE);
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            Direction direction = null;

            switch (key) {
                case KeyEvent.VK_UP -> direction = Direction.UP;
                case KeyEvent.VK_DOWN -> direction = Direction.DOWN;
                case KeyEvent.VK_LEFT -> direction = Direction.LEFT;
                case KeyEvent.VK_RIGHT -> direction = Direction.RIGHT;
                case KeyEvent.VK_T -> gameLogic.toggleControlMode(); // T to toggle control mode
            }

            if (direction != null) {
                gameLogic.changePacManDirection(direction);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("PacMan");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PacManGame());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
