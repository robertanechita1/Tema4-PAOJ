package ex3;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        final int M = 5;
        final int K = 10;

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "robi19";

        try {
            Class.forName("org.postgresql.Driver"); // necesar doar uneori
            SimpleConnectionPool pool = new SimpleConnectionPool(M, url, user, password);

            Thread[] workers = new Thread[K];
            for (int i = 0; i < K; i++) {
                workers[i] = new WorkerThread(pool);
                workers[i].start();
            }

            for (Thread t : workers) {
                t.join();
            }

            // afisez nr de inregistrari in Log
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Log")) {

                if (rs.next()) {
                    System.out.println("Total inregistrari: " + rs.getInt(1));
                }
            }

            // apel procedura stocata stergere
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 CallableStatement cs = conn.prepareCall("call sterge_vechi()")) {
                 cs.execute();
                 System.out.println("Intrarile vechi au fost sterse.");
            }

            pool.closeAll();

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
