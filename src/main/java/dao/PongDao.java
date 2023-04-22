package dao;

import application.Ball;
import application.Paddle;

public interface PongDao {

    int createPaddle(int x, int y);

    void movePaddle(int paddleId, int yVelocity);

    void setPaddle(Paddle paddle);

    void setPaddleY(int paddleId, int newY);

    int createBall(int x, int y);

    void moveBall(int ballId, int xVelocity, int yVelocity);

    void setBall(Ball ball);

    int[] getScores(int gameId);

    void incrementPlayerScore(int gameId, int playerNumber);

    void resetScores(int gameId);

    int startNewGame();

    Boolean isGameStarted(int gameId);
}
