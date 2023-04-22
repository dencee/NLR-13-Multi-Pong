package application;

import dao.PongDao;

import java.awt.*;
import java.util.Random;

public class Ball extends Rectangle {

    Random random;
    int xVelocity;
    int yVelocity;
    int initialSpeed = 3;
    int ballId;
    PongDao pongDao;

    Ball(int x, int y, int width, int height, PongDao pongDao) {
        super(x,y,width,height);
        random = new Random();
        int randomXDirection = random.nextInt(2);
        if(randomXDirection ==0)
            randomXDirection--;
        setXDirection(randomXDirection*initialSpeed);

        int randomYDirection = random.nextInt(2);
        if(randomYDirection ==0)
            randomYDirection--;
        setYDirection(randomYDirection*initialSpeed);

        this.pongDao = pongDao;
        this.ballId = pongDao.createBall(x, y);
    }
    public void setXDirection(int randomXDirection) {
        xVelocity = randomXDirection;

    }
    public void setYDirection(int randomYDirection) {
        yVelocity = randomYDirection;

    }
    public void move() {

        pongDao.moveBall(this.ballId, xVelocity, yVelocity);

//        x += xVelocity;
//        y += yVelocity;

    }
    public void draw(Graphics g) {

        this.pongDao.setBall(this);

        g.setColor(Color.white);
        g.fillOval(x,y,height,width);

    }

    public void setX(int x){
        super.x = x;
    }

    public void setY(int y){
        super.y = y;
    }

    public int getBallId(){
        return this.ballId;
    }
}
