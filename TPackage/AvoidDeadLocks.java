package TPackage;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AvoidDeadLocks {

    public BankAccount ba1 = new BankAccount();
    public BankAccount ba2 = new BankAccount();
    private Lock lock1 =new ReentrantLock();
    private Lock lock2 =new ReentrantLock();

    //here after running both threads the total balance in both accounts should be 20k because transfer
    //from one account to another so the sum should be same. But because of thread interleaving issues
    //as seen earlier the sum does not match. This problem can be solved by nested synchronised blocks
    //where we acquire lock on one account and then on another.
    //Lets say lock1  and lock2 associate with account 1 and 2 resp. We can use one lock but as number accounts
    //grows one lock will cause concurrency issues with all transfers or transaction on all accounts waiting
    // on one lock. Deadlocks can also occur in synchronised blocks.

    public void acquireLock(Lock l1, Lock l2) throws InterruptedException{
        //while is to make sure that both locks are indeed acquired. It is kind of a double check
        while(true){
            //try acquiring both locks
            boolean gotL2 = false;
            boolean gotL1 = false;

            try{
                //there is also an option of tryLock with timeout which can be used in real situations
                gotL1 = l1.tryLock();
                gotL2 = l2.tryLock();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                // if got both locks then continue doing what we wanted to do with acquired locks
                //if got only one lock then release that lock so that other thread can acquire it
                //these condition only happens if both locks are not acquired.
                if(gotL1 && gotL2){
                    return;
                }else if(gotL1){
                    l1.unlock();
                }else if(gotL2){
                    l2.unlock();
                }
            }
            //Locks not acquired then go to sleep and wait for other threads to free up that lock
            Thread.sleep(1);
        }
    }

    public void firstThread() throws InterruptedException {
        Random r1 = new Random();

        //Here deadlocks can happen as when we firstThread could acquire lock1 and second could acquire lock
        //2 and none of them will be able to proceed without the other locks resp.
        //The order in code below will work as both threads try to get same lock simultaneously, but if this
        //order is reversed in second thread then deadlock can occur.
        //To solve this problem tryLock method can be used. In a big system it will be difficult to track locks
        //associated with accounts.
        acquireLock(lock1, lock2);
        try{
            for(int i=0;i<10000;i++){
                BankAccount.transfer(ba2, ba1, r1.nextInt(100));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            lock1.unlock();
            lock2.unlock();
        }
    }

    public void secondThread() throws  InterruptedException {
        Random r2 = new Random();

        acquireLock(lock2, lock1);

        try{
            for(int i=0;i<10000;i++){
                BankAccount.transfer(ba1, ba2, r2.nextInt(100));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            lock1.unlock();
            lock2.unlock();
        }

    }

    public void finished(){
        System.out.println("Account 1 balance " + ba1.getBalance());
        System.out.println("Account 2 balance " + ba2.getBalance());
        System.out.println("Total balance " + (ba2.getBalance() + ba1.getBalance()));
    }

    public void useAvoidDeadLocks()throws InterruptedException{
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
