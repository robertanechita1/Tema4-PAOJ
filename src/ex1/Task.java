package ex1;

import java.util.Map;

public class Task implements Runnable {
    private final int id;
    private final int duration;
    private final Object monitor;
    private final Map<Integer, Status> statusMap;

    public Task(int id, int duration, Object monitor, Map<Integer, Status> statusMap) {
        this.id = id;
        this.duration = duration;
        this.monitor = monitor;
        this.statusMap = statusMap;
    }

    @Override
    public void run() {
        synchronized (monitor) {
            System.out.println("Task " + id + " started.");
        }

        try {
            Thread.sleep(duration);
            synchronized (statusMap) {
                statusMap.put(id, Status.COMPLETED);
            }
            synchronized (monitor) {
                System.out.println("Task " + id + " completed."); // AICI NU STIU CUM AR TREB DE FAPT SA FAC??
            }
        }
        catch (InterruptedException e) {
            synchronized (statusMap) {
                statusMap.put(id, Status.TIMED_OUT);
            }
            synchronized (monitor) {
                System.out.println("Task " + id + " timed out.");
            }
        }
    }
}
