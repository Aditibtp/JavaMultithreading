package TPackage;

import java.util.Random;
import java.util.concurrent.*;

public class HowToInterruptThreads {
    public void useInterrupt()throws InterruptedException{

        System.out.println("Starting now..");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random ran = new Random(1000);

                for(int i=0;i<1E6; i++){
                    try{
                        //we need something that throws Interrupted Exception
                        //how does thread.sleep know it got interrupted?
                        //interrupt() sets a flag in the thread to true currentThread.interrupt
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                        //System.out.println("We got interrupted so lets break up");
                        //break;
                       // e.printStackTrace();
                    }

                    //checking interrupt called on current thread
                    if(Thread.currentThread().isInterrupted()){
                        System.out.println("We got interrupted so lets break up");
                        break;
                    }

                    Math.sin(ran.nextDouble());
                }

            }
        });
        thread.start();

        Thread.sleep(500);

        //attempts to interrupt the thread. Just by calling this not much difference will happen
        //this method does not kill the thread. Killing the thread can create problems with state applications
        //to kill a thread a volatile boolean flag can be used
        thread.interrupt();

        thread.join();

    }

    public void useInterruptWithThreadPool() throws InterruptedException {

        System.out.println("Starting now..");

        ExecutorService exec = Executors.newCachedThreadPool();

        Future<?> fu = exec.submit(new Callable<Void>() {
            @Override
            public Void call() {

                Random ran = new Random(1000);

                for (int i = 0; i < 1E6; i++) {
                    try {
                        //we need something that throws Interrupted Exception
                        //how does thread.sleep know it got interrupted?
                        //interrupt() sets a flag in the thread to true currentThread.interrupt
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        //System.out.println("We got interrupted so lets break up");
                        //break;
                        // e.printStackTrace();
                    }

                    //checking interrupt called on current thread
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("We got interrupted so lets break up");
                        break;
                    }

                    Math.sin(ran.nextDouble());

                }
                return null;
            }
        });

        //it shuts down the managerial thread which runs all exec threads
        exec.shutdown();

        Thread.sleep(500);
        //false cancels the thread if it has not already run
        // if true then interrupted thread is set after thread is run
        fu.cancel(true);

        //another way is. it tries to kill all running threads.. basically sets interrupt flag
       // exec.shutdownNow();

        exec.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("Finished here..");
    }
}
