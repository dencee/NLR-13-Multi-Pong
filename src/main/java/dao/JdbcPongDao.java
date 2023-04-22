package dao;

import application.Ball;
import application.Paddle;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcPongDao implements PongDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcPongDao(BasicDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "DELETE FROM paddle;";
        jdbcTemplate.update(sql);
    }

    @Override
    public int createPaddle(int x, int y){
        String sql = "INSERT INTO paddle (x, y) VALUES (?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Integer.class, x, y);
    }

    @Override
    public void movePaddle(int paddleId, int yVelocity){
        String sql = "UPDATE paddle " +
                "SET " +
                "y = y + ? " +
                "WHERE id = ?;";

        jdbcTemplate.update(sql, yVelocity, paddleId);
    }

    @Override
    public void setPaddle(Paddle paddle){
        String sql = "SELECT y FROM paddle WHERE id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, paddle.getPaddleId());

        if(result.next()){
            paddle.setY(result.getInt("y"));
        }
    }

    public void setPaddleY(int paddleId, int newY){
        String sql = "UPDATE paddle SET y = ? WHERE id = ?;";
        jdbcTemplate.update(sql, newY, paddleId);
    }

    @Override
    public int createBall(int x, int y) {

        /*
         * TODO: Only 1 ball at a time
         *  Move to constructor if later there will be multiple
         *  balls at the same time
         */
        String sql = "DELETE FROM ball;";
        jdbcTemplate.update(sql);

        sql = "INSERT INTO ball (x, y) VALUES (?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Integer.class, x, y);
    }

    @Override
    public void moveBall(int ballId, int xVelocity, int yVelocity) {
        String sql = "UPDATE ball " +
                     "SET " +
                     "x = x + ?, " +
                     "y = y + ? " +
                     "WHERE id = ?;";

        jdbcTemplate.update(sql, xVelocity, yVelocity, ballId);
    }

    @Override
    public void setBall(Ball ball){
        String sql = "SELECT x, y FROM ball WHERE id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, ball.getBallId());

        if(result.next()){
            ball.setX(result.getInt("x"));
            ball.setY(result.getInt("y"));
        }
    }

    @Override
    public int[] getScores(int gameId) {
        int[] scores = new int[2];
        String sql = "SELECT player1_score, player2_score FROM pong WHERE id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, gameId);
        if(result.next()){
            scores[0] = result.getInt("player1_score");
            scores[1] = result.getInt("player2_score");
        }

        return scores;
    }

    @Override
    public void incrementPlayerScore(int gameId, int playerNumber) {
        String sql = "UPDATE pong SET player1_score = player1_score + 1 WHERE id = ?";

        if(playerNumber == 2) {
            sql = "UPDATE pong SET player2_score = player2_score + 1 WHERE id = ?";
        }

        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public void resetScores(int gameId) {
        String sql = "UPDATE pong SET player1_score = 0, player2_score = 0 WHERE id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public int startNewGame() {
        String sql = "INSERT INTO pong " +
                     "(is_started, player1_score, player2_score) " +
                     "VALUES (false, 0, 0) RETURNING id;";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Boolean isGameStarted(int gameId) {
        Boolean isStarted = null;
        String sql = "SELECT is_started FROM pong WHERE id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, gameId);
        if(result.next()){
            isStarted = result.getBoolean("is_started");
        }

        return isStarted;
    }
}
