package TPackage;

import java.util.LinkedList;
import java.util.Random;

public class SymphonySynchro {
    private LinkedList<Integer> list = new LinkedList<Integer>();

    private final int limit  = 10;
    private Object lock = new Object();

    public void produce() throws InterruptedException {
        int value = 0;

        //here we lock on object lock instead of intrinsic class object lock
        while(true){
            synchronized (lock){

                //here even if we get a signal from other thread to start we just recheck the condition by going
                // in a loop again
                // same applies to consumer method.
                while(list.size() == limit){
                    lock.wait();
                }

                list.add(value++);
                lock.notify();
            }
        }
    }

    public void consume() throws InterruptedException {

        while(true){

            synchronized (lock){

                while(list.size() == 0){
                    lock.wait();
                }
                System.out.println("List size is: " + list.size());
                int value = list.removeFirst();
                System.out.println("; value is " + value);
                lock.notify();
                //generates a number between 0 and 1000 so on average each thread takes 500ms to start
                //producer thread after notiy call
                //here consume takes more time so list is almost always full
                Thread.sleep(new Random().nextInt(1000));
            }
        }
    }

    public void useSynchro() throws InterruptedException{
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    produce();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    consume();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
