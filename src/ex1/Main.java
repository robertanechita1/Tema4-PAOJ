package ex1;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        final int nr_tasks = Integer.parseInt(args[0]);
        final int N = Integer.parseInt(args[1]);
        final int Tmax = Integer.parseInt(args[2]);

        Map<Integer, Status> statusMap = new ConcurrentHashMap<>();
        Object monitor = new Object();
        Queue<Thread> threadPool = new ArrayDeque<>();

        Watchdog watchdog = new Watchdog(statusMap);
        watchdog.start();

        for (int i = 1; i <= nr_tasks; i++) {
            int duration = new Random().nextInt(1000, 3000);
            statusMap.put(i, Status.RUNNING);
            Task task = new Task(i, duration, monitor, statusMap);
            Thread t = new Thread(task); // task ul ruleaza in t
            threadPool.add(t);
        }

        while (!threadPool.isEmpty()) {
            List<Thread> running = new ArrayList<>();

            for (int i = 0; i < N && !threadPool.isEmpty(); i++) {
                Thread t = threadPool.poll();
                if (t != null) {
                    t.start();
                    running.add(t);
                }
            }

            for (Thread t : running) {
                try {
                    t.join(Tmax);
                    if (t.isAlive()) {
                        t.interrupt(); // task ul va prinde InterruptedException
                        t.join();
                    }
                }
                catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for task.");
                }
            }
        }

        watchdog.interrupt();
        try {
            watchdog.join();
        }
        catch (InterruptedException e) {
            System.err.println("Watchdog interrupted while joining.");
        }

        System.out.println("\nToate task-urile au fost monitorizate.");
    }
}
