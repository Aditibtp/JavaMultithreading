package TPackage;

import java.util.Scanner;

class WaitAndNotifyOnMe {
    public void produce() throws InterruptedException{
        // here we acquire the intrinsic lock of WaitAndNotifyMe
        synchronized (this){
            System.out.println("Producer thread running");
            //wait gives up the lock on "this" so the consumer can get it
            //Every object in java has wait method because it is a method of Object class which is Ancestor of all
            // objects. // there is also a wait with timeout. Normal wait can cause thread to wait indefinetly
            // wait does not consume lot of system resources unlike while loops which keeps checking a flag
            // hands over control of lock that synchronised block is locked on.
            // wait and notify can only be called from synchronised blocks.
            wait();
            //so now this thread will not run unttil it regains control of this lock
            // also we must run another thread that is locked on same object and call method notify on it
            System.out.println("Resumed...");
        }
    }

    public void consume() throws InterruptedException{
        Scanner sc = new Scanner(System.in);
        // to make sure producer starts first
        Thread.sleep(2000);

        synchronized (this){
            System.out.println("Waiting for enter key..");
            sc.nextLine();
            System.out.println("Enter key pressed!!");
            // notify only "notifies" the other first thread waiting on "this" lock
            // does not actually relinquish control like wait
            // only after the entire synchronised block has finished the control will go back to other waiting  thread
            notify();
            Thread.sleep(5000);
        }
    }
}

public class WaitAndNotify{

    public void useWaitAndNotify() throws InterruptedException{
        WaitAndNotifyOnMe wn = new WaitAndNotifyOnMe();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wn.produce();
                } catch (InterruptedException ignored) {}
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wn.consume();
                } catch (InterruptedException ignored) {}
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}


