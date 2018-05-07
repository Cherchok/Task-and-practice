package privateLessions.teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class A implements Runnable {

    // hw1: создать несколоько потоков schedule thread pool await on condition countdown latch
    // hw2: cyclicBarrier condition Delayed Queue та же задача на этих классах.

    private static volatile boolean goToSleep;
    private static volatile CountDownLatch latch;

    @Override
    public void run() {
        try {
            int idx = 0;
            while (!Thread.currentThread().isInterrupted()) {
                if (latch != null) {
                    latch.await();
//                    TimeUnit.SECONDS.sleep(5);
                    goToSleep = false;
                }
                int fib = fib(idx);
                System.err.printf("hello {%s} fib[%d] = %d\n", Thread.currentThread().getName(), idx++, fib);
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            System.err.printf("Thread %s was interrupted", Thread.currentThread().getName());
        }
    }

    public int fib(int i) {
        if (i < 2) {
            return 1;
        }
        return fib(i - 1) + fib(i - 2);
    }

    public static void main(String[] args) throws IOException {
        Thread t1 = new Thread(new A());
        t1.start();

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);


        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) continue;
                switch (line) {
                    case "q":
                        System.err.printf("interrupting %s", t1.getName());
                        t1.interrupt();
                        break;
                    case "s":
                        System.err.println("Go to sleep!");
//                        goToSleep = true;
                        latch = new CountDownLatch(1);
                        ses.schedule(() -> latch.countDown(), 5, TimeUnit.SECONDS);
                        break;
                    case "e": {
                        System.err.printf("Exiting thread %s", t1.getName());
                        return;
                    }
                }
            }
        }
    }
}
