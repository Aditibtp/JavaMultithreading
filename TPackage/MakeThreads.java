package TPackage;

import java.util.Scanner;

class SampleRunnable implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getId() + " value " + i);
        }
        try {
            //pauses the program for specified number of milliseconds
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // super.run();
    }
}

class SampleThread extends Thread {
    @Override
    public void run() {
        for(int i=0;i<10;i++){
            System.out.println(Thread.currentThread().getId() + " value " + i);
        }
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        // super.run();
    }
}

class Processor extends Thread {

    private volatile boolean running  = true;

    public void run(){
        while(running){
            System.out.println("Hello there");

            try {
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void shutdown(){
        running = false;
    }
}

public class MakeThreads {

    private static int count = 0;

    //synchronised can be used here to maintain access of count by two threads t1 and t2
    // why it works is because --  every object in java has an intrinsic lock or monitor lock or mutex
    // to call a synchronised method of an object we have to acquire the intrinsic lock if the object
    // only one thread can acquire the intrinsic lock at a time
    // so if one thread acquires the  lock the second thread will have to wait until the current thread releases
    // the intrinsic lock.
    public static synchronized void incCount(){
        count++;
    }

    public void dispFunction(){
        // if run is called using s1 s2 objects no threads will be created.
        // start tells the thread class to go look for run method
        // It will run the code in main thread of the application
        //  SampleThread s1 = new SampleThread();
        //  s1.start();
        //  SampleThread s2 = new SampleThread();
        //  s2.start();

        //Thread t1 =  new Thread(new SampleRunnable());
        // Thread t2 = new Thread(new SampleRunnable());
        // t1.start();
        // t2.start();

        // Thread inside main class

        Thread t3 = new Thread(new Runnable(){
            @Override
            public void run() {
                for(int i=0;i<10000;i++){
                    //System.out.println(Thread.currentThread().getId() + " value " + i);
                    incCount();
                }
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread t4 = new Thread(new Runnable(){
            @Override
            public void run() {
                for(int i=0;i<10000;i++){
                    //System.out.println(Thread.currentThread().getId() + " value " + i);
                    incCount();
                }
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        /*t3.start();
       t4.start();
 // Join Key word
        try{
            t3.join();
            t4.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        //without join count prints 0 because right after start is called it returns after spawning and
        // starting the thread. then t2 start returns with its thread spawned. And hence we dont see
        // the change in value of count. so we need to wait for both threads to finish executing
        // join -- pauses execution(of main thread) until the thread it is called on is finished
        //an important point to note down, t1 and t2 themselves can run in parallel irrespective of
        // the join call sequence on t1 and t2. It is the main/daemon thread that has to wait.
        // so t1 and t2 are still running in parallel and hence count is being accessed by both threads
        The t2 thread is running in parallel and is not affected by t1 or the t1.join() call at all.

        //In terms of the try/catch, the join() throws InterruptedException meaning that the main
        //thread that is calling join() may itself be interrupted by another thread.

        System.out.println(count);*/

        // The thread that is running the processor code might decide to cache the value of running
        // so it will never see the changed value of it
        // The reason is we have two threads --> the main program and the thread which we spawn from it
        // So we have two threads and both are accesing the running variable
        // the spawned thread is reading running and main thread is writing to running
        //one thread generally does not expect other threads to modify its data(java optimization)
        // so volatile keyword helps because it helps in not caching variables when they are not changes
        // within that thread.
        // but the main problem is thread interleaving not caching.. so volatile as well will not work
        // every time.

        Processor p1 = new Processor();
        p1.start();
        System.out.println("Press enter to stop");
        Scanner scanner  = new Scanner(System.in);
        scanner.nextLine();

        p1.shutdown();
    }
}
