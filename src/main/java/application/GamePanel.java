package application;

import dao.PongDao;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import static java.lang.System.nanoTime;

public class GamePanel extends JPanel implements Runnable {

    public static final int GAME_WIDTH = 1000;
    public static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    public static final int BALL_DIAMETER = 20;
    public static final int PADDLE_WIDTH = 25;
    public static final int PADDLE_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle;
    Ball ball;
    Score score;

    int gameId;
    boolean isRightPaddle;
    PongDao pongDao;


    GamePanel(PongDao pongDao, int gameId, boolean isRightPaddle) {
        this.pongDao = pongDao;
        this.gameId = gameId;
        this.isRightPaddle = isRightPaddle;
        newPaddle(isRightPaddle);
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall(){
        if(ball == null) {
            random = new Random();
            ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER, pongDao);
        }
    }
    public void newPaddle(boolean isRightPaddle){
        int x = 0;

        if(isRightPaddle) {
            x = GAME_WIDTH - PADDLE_WIDTH;
        }
        paddle = new Paddle(x, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2, pongDao);
    }
    public void paint(Graphics g){
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);

    }
    public void draw(Graphics g){
        paddle.draw(g);
        score.draw(g);

        if(ball != null) {
            ball.draw(g);
        }
    }
    public void move(){
        paddle.move();

        if(ball != null) {
            ball.move();
        }
    }
    public void checkCollision() {
        if(ball == null){
            return;
        }

        //bounce ball or top and bottom edges of window
        if (ball.y <= 0) {
            if(ball.yVelocity < 0) {
                ball.setYDirection(-ball.yVelocity);
            }
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            if(ball.yVelocity > 0) {
                ball.setYDirection(-ball.yVelocity);
            }
        }
        //bounces ball off paddles
        if (ball.intersects(paddle)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // optional for more difficulty
            if (ball.yVelocity > 0)
                ball.yVelocity++; // optional for more difficulty
            else
                ball.yVelocity--;
            if(isRightPaddle) {
                ball.setXDirection(-ball.xVelocity);
            } else {
                ball.setXDirection(ball.xVelocity);
            }
            ball.setYDirection(ball.yVelocity);
        }

        if (ball.x <= 0) {
            pongDao.incrementPlayerScore(this.gameId, 2);
            ball = null;
        }
        else if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            pongDao.incrementPlayerScore(this.gameId, 1);
            ball = null;
        }

        int[] scores = pongDao.getScores(this.gameId);
        score.player1 = scores[0];
        score.player2 = scores[1];
    }
    public void run(){
        //game loop
        long lastTime = nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true) {
            long now = System.nanoTime();
            delta += (now - lastTime)/ ns;
            lastTime = now;
            if(delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }

    } //AL is short for Action Listener
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            paddle.keyPressed(e);
        }
        public void keyReleased(KeyEvent e) {
            paddle.keyReleased(e);

            if(e.getKeyCode() == KeyEvent.VK_S){
                newBall();
            }
            else if(e.getKeyCode() == KeyEvent.VK_R){
                pongDao.resetScores(gameId);
            }
        }
    }
}
