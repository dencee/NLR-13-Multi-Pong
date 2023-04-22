package application;

import dao.PongDao;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame{

    GamePanel panel;

    public GameFrame(PongDao pongDao, int gameId, boolean isRightPaddle){
        panel = new GamePanel(pongDao, gameId, isRightPaddle);
        this.add(panel);
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
