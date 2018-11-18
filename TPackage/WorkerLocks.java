package TPackage;

import java.util.*;

public class WorkerLocks {
    private Random r = new Random();

    // to soleve the problem caused by synchronised functions we create lock objects
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public List<Integer> l1 = new ArrayList<Integer>();
    public List<Integer> l2 = new ArrayList<Integer>();

    public void task1(){
        try{
            //assuming we are making some kind of call or pinging a machine so thread.sleep.
            Thread.sleep(1);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        l1.add(r.nextInt(100));
    }

    public void task2(){
        try{
            Thread.sleep(1);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        l2.add(r.nextInt(200));
    }

    // the problem is similar to accessing the same variables from two threads so we do not end up
    // with 2000 numbers in each list(threads interleaving). And time taken is also more than 2 seconds
    //In theory the threads should take around 2 sec as they run simultaneously
    // so to solve this we can synchronised with task1 and task2
    // doing that populates the lists perfectly but takes 4 seconds which makes using threads pointless
    // reason is synchronised methods acquires lock on workerlock object.
    // so the two threads cannot access the two methods at the same time even though the two
    // functions change different objects. So if t1 acquires the lock t2 cannot access the w1 object
    //instance before t1 is done

    public synchronized void taskv1(){
        try{
            //assuming we are making some kind of call or pinging a machine so thread.sleep.
            Thread.sleep(1);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        l1.add(r.nextInt(100));
    }

    public synchronized void taskv2(){
        try{
            Thread.sleep(1);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        l2.add(r.nextInt(200));
    }

    // so now synchronised code blocks work the same way as sync methods but now two threads can run
    // the methods at same time. but if one thread has entered sync code block another thread will have to wait
    // until first is done
    //but we are waiting on different locks so two threads can access the sync code blocks simultaneously
    // we could also lock on lists but we might want to reassign the lists or variables to other values
    // and make things complicated.

    public void taskv3_1(){
        synchronized (lock1){
            try{
                //assuming we are making some kind of call or pinging a machine so thread.sleep.
                Thread.sleep(1);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            l1.add(r.nextInt(100));
        }
    }

    public void taskv3_2(){
        synchronized (lock2){
            try{
                Thread.sleep(1);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            l2.add(r.nextInt(100));
        }
    }

    //so we might assume that by creating multiple threads we can speed this up
    public void process() {
        for(int i=0;i<1000;i++){
            taskv1();
            taskv2();
        }
    }

    public void startTasks(){
        long start = System.currentTimeMillis();
        System.out.println("Execution starts at  " + start);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                process();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                process();
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

        long end = System.currentTimeMillis();

        System.out.println("Total time taken in milliseconds  " + (end - start));
        System.out.println("List 1 size " + l1.size());
        System.out.println("List 2 size " + l2.size());
    }
}
