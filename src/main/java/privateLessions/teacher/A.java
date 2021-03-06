package privateLessions.teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class A implements Runnable {

    // создать несколоько потоков schedule thread pool await on condition countdown latch

    private static volatile boolean goToSleep;

    @Override
    public void run() {
        try {
            int idx = 0;
            while (!Thread.currentThread().isInterrupted()) {
                if (goToSleep) {
                    TimeUnit.SECONDS.sleep(5);
                    goToSleep = false;
                }
                int fib = fib(idx);
                System.err.printf("hello {%s} fib[%d] = %d\n", Thread.currentThread().getName(), idx++, fib);
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            System.err.printf("Thread %s was interrupted",  Thread.currentThread().getName());
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
                        goToSleep = true;
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
