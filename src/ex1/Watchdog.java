package ex1;

import java.util.Map;

public class Watchdog extends Thread {
    private final Map<Integer, Status> statusMap;

    public Watchdog(Map<Integer, Status> statusMap) {
        this.statusMap = statusMap;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(500);
                System.out.println("\nSTATUS:\n");
                synchronized (statusMap) {
                    statusMap.forEach((id, status) ->
                            System.out.printf("Task %d %s%n", id, status));
                }
            }
            catch (InterruptedException e) {
                break;
            }
        }
    }
}
