package ex3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class SimpleConnectionPool {
    private final List<Connection> availableConnections = new ArrayList<>();
    private final String url, user, password;

    public SimpleConnectionPool(int M, String url, String user, String password) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;

        for (int i = 0; i < M; i++) {
            availableConnections.add(createConnection());
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public synchronized Connection getConnection() throws InterruptedException {
        while (availableConnections.isEmpty())
            wait();

        return availableConnections.removeFirst();
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            availableConnections.add(connection);
            notifyAll();
        }
    }

    public synchronized void closeAll() {
        for (Connection conn : availableConnections) {
            try {
                conn.close();
            }
            catch (SQLException e) {
                System.err.println("Eroare la oprirea conexiunii: " + e.getMessage());
            }
        }
    }
}
