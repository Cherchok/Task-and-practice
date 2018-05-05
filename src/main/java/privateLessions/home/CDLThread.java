package privateLessions.home;

import java.util.concurrent.CountDownLatch;

public class CDLThread extends Thread {
    private CountDownLatch cdl;
    private String name;

    public CDLThread(CountDownLatch cdl, String name) {
        this.cdl = cdl;
        this.name = name;
        start();
    }

     CDLThread(CountDownLatch cdl) {
        this.cdl = cdl;
        this.name = Thread.currentThread().getName();
        start();
    }

    @Override
    public void run() {
            System.out.printf("{%s} - %d - done! \n", name, cdl.getCount());
            cdl.countDown();
    }
}
