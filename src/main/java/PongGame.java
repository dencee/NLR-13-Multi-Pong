import application.GameFrame;
import dao.JdbcPongDao;
import dao.PongDao;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.Scanner;

public class PongGame {
    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/PongGame";
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        PongDao gameDb = getGameDb();
        int gameId = askCreateGame(gameDb);
        boolean isRightPaddle = askPaddle();

        new GameFrame(gameDb, gameId, isRightPaddle);
    }

    public static PongDao getGameDb(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(DATABASE_URL);
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        return new JdbcPongDao(dataSource);
    }

    public static int askCreateGame(PongDao gameDb){
        int gameId = -1;
        System.out.println("Do you want to create a game (Y/N)?");

        if( scan.nextLine().equalsIgnoreCase("y") ){
            gameId = gameDb.startNewGame();
            System.out.println("Starting game id: " + gameId);
        } else {
            System.out.println("Enter the game id you want to join: ");
            gameId = Integer.parseInt(scan.nextLine());
        }

        return gameId;
    }

    public static boolean askPaddle(){
        System.out.println("Do you want the paddle on the right side (Y/N)?");
        return scan.nextLine().equalsIgnoreCase("y");
    }
}
