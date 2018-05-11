package privateLessions.home.countDownLatch.examples;

import java.util.concurrent.CountDownLatch;


public class CountDownLatchEx extends Thread {
    private CountDownLatch count;

    public CountDownLatchEx(CountDownLatch count) {
        this.count = count;
        start();

    }

    @Override
    public void run() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("{%s} waiting\n", Thread.currentThread().getName());
        count.countDown();

    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        new CountDownLatchEx(latch);
        new CountDownLatchEx(latch);
        new CountDownLatchEx(latch);
        new CountDownLatchEx(latch);
        new CountDownLatchEx(latch);
        latch.await();
        System.out.println("End");


    }
}
