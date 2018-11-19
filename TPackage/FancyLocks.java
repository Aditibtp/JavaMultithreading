package TPackage;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FancyLocks {

    //Reentrant Locks is fancy name for normal locks. When using these we dont have to use synchronised keyword
    //they are kind of like the locks used in class
    private int count = 0;
    private Lock lock = new ReentrantLock();
    private Condition con = lock.newCondition();

    //lock.await = wait
    //lock.signal/signalAll = notify/notifyAll
    //again they can only be called after getting the lock

    private void increment(){
        for(int i=0;i<10000;i++){
            count++;
        }
    }

    public void firstThread() throws InterruptedException {
        System.out.println("got signal !!!!");
        lock.lock();
        //try around increment takes care of handling exceptions in increment so if something goes wrong with
        //increment finally will always take care of releasing the lock so that other thread can proceed
        //Execution sequence
        //Because of thread.sleep in second thread this thread starts first--it gets the lock then await is
        // called. await gives the control to second thread. Second thread then calls signal which wakes up the
        //first thread but does not yet give up the lock. Second thread first increments and then calls unlock
        //after which first thread can acquire the lock

        System.out.println("Waiting !!!!");
        con.await();
        System.out.println("Woken up i am dancing now");

        try{
            increment();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("And me the first is done now :)");
            lock.unlock();
        }
    }

    public void secondThread() throws  InterruptedException {
        Thread.sleep(1000);
        lock.lock();
        System.out.println("Press the return key");
        new Scanner(System.in).nextLine();
        System.out.println("Got the return key");

        con.signal();
        try{
            increment();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("And me the second is done now :)");
            lock.unlock();
        }

    }

    public void finished(){
        System.out.println("Count is: " + count);
    }

    public void useFancyLocks() throws InterruptedException{
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    firstThread();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    secondThread();
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
        finished();
    }
}
