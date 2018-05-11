package privateLessions.home;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class CBandAQ extends Thread implements Delayed {
    private CyclicBarrier cb;
    private long delayTime;

    private CBandAQ(CyclicBarrier cb, long delayTime) {
        this.cb = cb;
        this.delayTime = delayTime;
    }


    @Override
    public void run() {
        int idx = 0;
        while (!Thread.currentThread().isInterrupted()) {
            if (cb != null) {
                try {
                    cb.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
            int fib = fib(idx);
            System.out.printf("hello {%s} fib[%d] = %d\n", Thread.currentThread().getName(), idx++, fib);
        }
    }

    @Override
    public long getDelay(@NotNull TimeUnit unit) {
       return unit.convert(delayTime, TimeUnit.SECONDS);
    }

    @Override
    public int compareTo(@NotNull Delayed o) {
        if (this.delayTime < ((CBandAQ) o).delayTime) {
            return -1;
        } else if (this.delayTime > ((CBandAQ) o).delayTime) {
            return 1;
        }
        return 0;
    }

    private int fib(int i) {
        if (i < 2) {
            return 1;
        }
        return fib(i - 1) + fib(i - 2);
    }

    public static void main(String[] args) throws IOException {
        DelayQueue<CBandAQ> delayQueue = new DelayQueue<>();
        CyclicBarrier cb = new CyclicBarrier(3, () -> {
            System.out.println("small pause!!!!");
            try {
                CBandAQ th = delayQueue.poll(2, TimeUnit.SECONDS);
                th.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CBandAQ t1 = new CBandAQ(cb, 1);
        CBandAQ t2 = new CBandAQ(cb, 5);
        CBandAQ t3 = new CBandAQ(cb, 3);
        CBandAQ t4 = new CBandAQ(cb, 2);

        delayQueue.offer(t1);
        delayQueue.offer(t2);
        delayQueue.offer(t3);
        delayQueue.offer(t4);

        for (Thread dqt : delayQueue) {
            dqt.start();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) continue;
                switch (line) {
                    case "q":
                        for (Thread dqt : delayQueue) {
                            dqt.interrupt();
                            System.out.printf("interrupting %s", Thread.currentThread().getName());
                        }
                        break;
                    case "s":
                        System.out.println("Go to sleep!");
                        delayQueue.poll();
                        break;
                    case "e": {
                        System.out.printf("Exiting thread %s", Thread.currentThread().getName());
                        return;
                    }
                }
            }
        }
    }
}
