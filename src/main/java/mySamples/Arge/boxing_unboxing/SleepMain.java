package mySamples.Arge.boxing_unboxing;

public class SleepMain {
    public static void main(String... args) {
        Thread t = new MyThread();
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
            try {
                t.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted in main");
            }
        }
    }

    static class MyThread extends Thread {
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.print(i + " ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted in myThread");
                }
            }
        }
    }
}
