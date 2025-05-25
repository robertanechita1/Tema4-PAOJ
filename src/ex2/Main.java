package ex2;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        final List<Integer> BufferA = Collections.synchronizedList(new ArrayList<>());
        final List<Integer> BufferB = Collections.synchronizedList(new ArrayList<>());

        Scanner scanner = new Scanner(System.in);

        System.out.println("Alege 1 sau 2:");
        System.out.println("1. Cu deadlock\n2. Fara deadlock\n");

        int opt = scanner.nextInt();

        if(opt == 1) {
            Thread p1 = new Thread(new Producer(BufferA, BufferB), "P1");
            Thread p2 = new Thread(new Producer(BufferB, BufferA), "P2"); // invers pt deadlock

            Thread c1 = new Thread(new Consumer(BufferA, BufferB), "C1");
            Thread c2 = new Thread(new Consumer(BufferB, BufferA), "C2"); // invers pt deadlock

            p1.start();
            p2.start();
            c1.start();
            c2.start();
        }
        else{
            Thread p1 = new Thread(new ProducerS(BufferA, BufferB), "P1");
            Thread p2 = new Thread(new ProducerS(BufferA, BufferB), "P2");

            Thread c1 = new Thread(new ConsumerS(BufferA, BufferB), "C1");
            Thread c2 = new Thread(new ConsumerS(BufferA, BufferB), "C2");

            p1.start();
            p2.start();
            c1.start();
            c2.start();
        }
    }
}
