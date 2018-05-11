package privateLessions.home.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CBThread implements Runnable {
    private CyclicBarrier cb;


    private CBThread(CyclicBarrier cb) {
        this.cb = cb;
    }


    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        try {
            cb.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(2, () -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println("hello");
        });


// почему не отрабатывает после достижения барьера заданного кол-ва потоков в случае отсутствия задержки???????????
        System.out.println("start:");
        for (int i = 0; i <8 ; i++) {
            new Thread(new CBThread(cb)).start();
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println("end!!!!!");
    }

}
