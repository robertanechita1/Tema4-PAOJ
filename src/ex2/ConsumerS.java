package ex2;

import java.util.List;

public class ConsumerS implements Runnable {
    private final List<Integer> bufferA, bufferB;

    public ConsumerS(List<Integer> buf1, List<Integer> buf2) {
        this.bufferA = buf1;
        this.bufferB = buf2;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " vrea sa consume bufferA");
        synchronized (bufferA) {
            System.out.println(Thread.currentThread().getName() + " consuma bufferA");
            sleep(100);
            System.out.println(Thread.currentThread().getName() + " vrea sa consume bufferB");
            synchronized (bufferB) {
                System.out.println(Thread.currentThread().getName() + " consuma bufferB");
                if (!bufferA.isEmpty())
                    bufferA.removeFirst();
                if (!bufferB.isEmpty())
                    bufferB.removeFirst();
            }
        }
    }


    private void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {}
    }
}

