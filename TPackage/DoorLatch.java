package TPackage;


//count down latches lets countdown from a number specified it lets one or more threads wait until
// a latch reaches count of 0. When latch reaches 0 then the thread can continue
//  It is a thread safe class can be safely accssed from multiple threads without worrying about synchronization
// it can be used for inter-thread communication

//CountDownLatch is used to make sure that a task waits for other threads before it starts.
// To understand its application, let us consider a server where the main task can only start when
// all the required services have started.
//Working of CountDownLatch:
//When we create an object of CountDownLatch, we specify the number of threads it should wait for,
// all such thread are required to do count down by calling CountDownLatch.countDown() once they are
// completed or ready to the job. As soon as count reaches zero, the waiting task starts running.

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyLatch implements Runnable{
    private CountDownLatch l;

    public MyLatch(CountDownLatch l){
        this.l = l;
    }
    @Override
    public void run() {
        System.out.println("Started");
        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        l.countDown();
    }
}

public class DoorLatch {
    public void useLatch(){
        // here we intialise countdown latch with 4 to wait for 4 threads before main thread continues
        CountDownLatch l = new CountDownLatch(5);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for(int i=0;i<5;i++) {
            executor.submit(new MyLatch(l));
        }
        try{
            //await waits until countdown latch has counted down to 0
            l.await();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Completed");
    }
}
