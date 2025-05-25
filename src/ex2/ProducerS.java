package ex2;

import java.util.List;

public class ProducerS implements Runnable {
    private final List<Integer> bufferA, bufferB;

    public ProducerS(List<Integer> buf1, List<Integer> buf2) {
        this.bufferA = buf1;
        this.bufferB = buf2;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " vrea sa produca bufferA");
        synchronized (bufferA) {
            System.out.println(Thread.currentThread().getName() + " produce bufferA");
            sleep(100);
            System.out.println(Thread.currentThread().getName() + " vrea sa produca bufferB");
            synchronized (bufferB) {
                System.out.println(Thread.currentThread().getName() + " produce bufferB");
                bufferA.add(1);
                bufferB.add(2);
            }
        }
    }


    private void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {}
    }
}
