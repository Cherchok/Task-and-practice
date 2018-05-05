package privateLessions.home;

import java.util.List;
import java.util.concurrent.*;

public class SchThrPoolTask {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("start: ");
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
        CountDownLatch cdl1 = new CountDownLatch(8);


        ses.scheduleWithFixedDelay(new CDLThread(cdl1), 1, 1, TimeUnit.SECONDS);


        ses.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + "  secondary task " + cdl1.getCount());
            cdl1.countDown();
        }, 2000, 1000, TimeUnit.MILLISECONDS);

        cdl1.await();

        List<Runnable> runnables = ses.shutdownNow();
        ses.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("End!");

    }
}
