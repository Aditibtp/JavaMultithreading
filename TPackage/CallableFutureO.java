package TPackage;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

public class CallableFutureO {
    public void tryingSomething() throws InterruptedException{
        ExecutorService executor = Executors.newCachedThreadPool();

        //here what if we wanted this code to return something or some value after thread execution
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                int duration = r.nextInt(4000);
                System.out.println("Thread is starting to run..");

                try{
                    Thread.sleep(duration);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("Thread is finished to run..");
            }
        });



        ExecutorService executor2 = Executors.newCachedThreadPool();

        //callable returns a value of time specified in <> brackets
        //submit return an object of type future
        // if there are number of threads then an arraylist of futures can be created
        // in case we dont want to return anything we can pass void in call template and ?(wildcard) in future template
        Future<Integer> f = executor2.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                Random r = new Random();
                int duration = r.nextInt(4000);

                // this exception from call gets thrown by get.
                //get throws Execution exception
                if(duration > 2000){
                    throw new IOException("Sleeping for too long");
                }

                System.out.println("Thread is starting to run..");

                try{
                    Thread.sleep(duration);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("Thread is finished to run..");
                return duration;
                //if not return type // return null;
            }
        });

        executor.shutdown();
        executor2.shutdown();

        //f.cancel(true);
        //useful to check if a thread is done
        //f.isDone()

        // we could have used awaitTermination to wait for the thread to finish
        //f.get waits/blocks until the thread associated with future terminates
        //after the thread terminated get stops blocking
        try{
            System.out.println("Result is " + f.get());
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(ExecutionException e){
            //System.out.println(e.getMessage());
            IOException x = (IOException) e.getCause();
            System.out.println(x.getMessage());
        }

    }
}
