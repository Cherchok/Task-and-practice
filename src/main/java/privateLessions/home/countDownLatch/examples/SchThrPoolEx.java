package privateLessions.home.countDownLatch.examples;

import java.util.concurrent.*;

public class SchThrPoolEx {

    public static void main(String[] args) throws ExecutionException, InterruptedException{
        Runnable taskRun = () -> System.out.printf("{%s} - is running\n", Thread.currentThread().getName());

        Callable taskCall = () -> "Goodbye";

        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(2);
        threadPool.scheduleWithFixedDelay(taskRun, 1, 1, TimeUnit.SECONDS);
        ScheduledFuture sf = threadPool.schedule(taskCall, 7, TimeUnit.SECONDS);
        String value = String.valueOf(sf.get());
        System.out.printf("{%s}\n", value);
        threadPool.shutdown();
    }
}
