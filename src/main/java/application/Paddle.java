package application;

import dao.PongDao;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class Paddle extends Rectangle {

    int id;
    int yVelocity;
    int speed = 10;
    int paddleId;
    PongDao pongDao;

    Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id, PongDao pongDao) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
        this.id = id;
        this.pongDao = pongDao;
        this.paddleId = pongDao.createPaddle(x, y);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            setYDirection(-speed);
            move();
        }
        else if (key == KeyEvent.VK_DOWN) {
            setYDirection(speed);
            move();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(0);
            move();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(0);
            move();
        }
    }

    public void setYDirection(int yDirection) {
        yVelocity = yDirection;

    }

    public void move() {

        pongDao.movePaddle(this.paddleId, yVelocity);
        this.pongDao.setPaddle(this);

        if (this.y <= 0) {
            pongDao.setPaddleY(this.paddleId, 0);
        } else if (this.y >= (GamePanel.GAME_HEIGHT - GamePanel.PADDLE_HEIGHT)) {
            pongDao.setPaddleY(this.paddleId, GamePanel.GAME_HEIGHT - GamePanel.PADDLE_HEIGHT);
        }
        this.pongDao.setPaddle(this);

//        y = y + yVelocity;
    }

    public void draw(Graphics g) {

        this.pongDao.setPaddle(this);

        if (id == 1)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public void setY(int y) {
        super.y = y;
    }

    public int getPaddleId() {
        return this.paddleId;
    }
}
