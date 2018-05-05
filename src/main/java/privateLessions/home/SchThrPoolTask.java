package privateLessions.home;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchThrPoolTask {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("start: ");
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
        CountDownLatch cdl1 = new CountDownLatch(8);


        Thread t1 = new CDLThread(cdl1);

        ses.scheduleWithFixedDelay(t1, 1, 1, TimeUnit.SECONDS);

        ses.awaitTermination(5, TimeUnit.SECONDS);

        ses.shutdown();
        System.out.println("End!");

    }
}
