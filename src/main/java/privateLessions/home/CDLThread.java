package privateLessions.home;

import java.util.concurrent.CountDownLatch;

public class CDLThread implements Runnable {
    private CountDownLatch cdl;
    private String name;

    public CDLThread(CountDownLatch cdl, String name) {
        this.cdl = cdl;
        this.name = name;
    }

     public CDLThread(CountDownLatch cdl) {
        this.cdl = cdl;
        this.name = Thread.currentThread().getName();
    }

    @Override
    public void run() {
            System.out.printf("{%s} - %d - done! \n", Thread.currentThread().getName(), cdl.getCount());
            cdl.countDown();
    }
}
