package privateLessions.home.delayQueue;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class DQTest {
    static class DelayedTask implements Delayed, Runnable {
        private long expireTime;
        private int idxFib;

        public DelayedTask(long delayTime, int idxFib) {
            this.expireTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(delayTime);
            this.idxFib = idxFib;
        }

        @Override
        public long getDelay(@NotNull TimeUnit unit) {
            return unit.convert(expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(@NotNull Delayed o) {
            return (int) (expireTime - ((DelayedTask) o).expireTime);
        }

        @Override
        public String toString() {
            return "DelayedTask{" +
                    "expireTime=" + expireTime +
                    '}';
        }

        @Override
        public void run() {
            System.out.printf("[%s] fib(%d) = %d\n", Thread.currentThread().getName(), idxFib, fib(idxFib));
        }

        public int fib(int i) {
            if (i < 2) {
                return 1;
            }
            return fib(i - 1) + fib(i - 2);
        }
    }

    static class TaskConsumer implements Runnable {
        private BlockingQueue<DelayedTask> queue;
        private ExecutorService es;

        public TaskConsumer(BlockingQueue<DelayedTask> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            es = Executors.newFixedThreadPool(2);
            try {
                while (true) {

                    DelayedTask task = queue.take();
                    System.out.printf("{%s} consumed: %d\n", Thread.currentThread().getName(), task.idxFib);
                    es.submit(task);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                es.shutdownNow();
            }
        }
    }

    static class TaskSupplier implements Runnable {
        private BlockingQueue<DelayedTask> queue;
        private volatile long delayForTaskOld = 1;
        private volatile long delayForTask = 1;
        private int count;

        public void setDelayForTask(long delayForTask, int count) {
            this.delayForTaskOld = this.delayForTask;
            this.delayForTask = delayForTask;
            this.count = count;
        }

        public TaskSupplier(BlockingQueue<DelayedTask> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            int idx = 0;
            try {
                while (true) {
                    System.out.printf("{%s} supplied: %d\n", Thread.currentThread().getName(), idx);
                    long delayForTask = this.delayForTask;
                    queue.offer(new DelayedTask(delayForTask, idx++));
                    TimeUnit.SECONDS.sleep(delayForTask);
                    if (delayForTask == this.delayForTask && --count == 0) {
                        this.delayForTask = delayForTaskOld;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) throws IOException {

        BlockingQueue<DelayedTask> queue = new DelayQueue<>();
        TaskConsumer taskConsumer = new TaskConsumer(queue);
        TaskSupplier taskSupplier = new TaskSupplier(queue);

        Thread supplier = new Thread(taskSupplier);
        supplier.start();
        Thread consumer = new Thread(taskConsumer);
        consumer.start();


//        DelayedTask dqTest0 = new DelayedTask(1, 3);
//        DelayedTask dqTest1 = new DelayedTask(4, 4);
//        DelayedTask dqTest2 = new DelayedTask(2, 6);
//        DelayedTask dqTest3 = new DelayedTask(1, 1);
//
//        queue.offer(dqTest0);
//        queue.offer(dqTest1);
//        queue.offer(dqTest2);
//        queue.offer(dqTest3);
//
//
//            for (Object dqt : queue) {
//                System.out.println(((DelayedTask) dqt).delayTime);
//            }

//        while (!queue.isEmpty()) {
//            DelayedTask delayedTask = queue.take();
//            System.out.println("delayedTask = " + delayedTask);
//        }


        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) continue;
                switch (line) {
                    case "q":
                        consumer.interrupt();
                        supplier.interrupt();
                        break;
                    case "s":
                        taskSupplier.setDelayForTask(5, 2);
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

