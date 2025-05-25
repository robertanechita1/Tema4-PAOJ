package ex3;

import java.sql.*;
import java.util.Random;

public class WorkerThread extends Thread {
    private final SimpleConnectionPool pool;
    private final Random rand = new Random();

    public WorkerThread(SimpleConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Log(mesaj, timestamp) VALUES (?, CURRENT_TIMESTAMP)");
            ps.setString(1, "Inregistrare in log pentru " + Thread.currentThread().getName());
            ps.executeUpdate();//executa log-ul in db
            ps.close();// elibereaza memoria de la statement

            Thread.sleep(rand.nextInt(400) + 100); // 100-500 ms
        }
        catch (Exception e) {
            System.err.println(Thread.currentThread().getName() + ": " + e.getMessage());
        }
        finally {
            pool.releaseConnection(conn); // elibereaza conexiunea inapoi in pool
        }
    }
}
