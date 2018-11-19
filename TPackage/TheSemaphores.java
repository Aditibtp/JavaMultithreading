package TPackage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TheSemaphores {

    public void createSemaphore(){
        Semaphore sm = new Semaphore(1);
        //to see number of available permits on semaphore--> use availablePermits
        //Method to increment number of available permits
        sm.release();
        //method to decrement counts of available permits -- acquire
        //acquire will wait if there are no permits available
        //waits until a permit is released somewhere // so if we had intialised with zero permits
        //and called acquire--the program would wait indefinitely
        //used to control access to a resource
        System.out.println("Available permits:  " + sm.availablePermits());

    }

    public void useConnections() throws InterruptedException{
        //every time we call submit on executor it just creates a new thread
        //it tries to reuse threads.
        ExecutorService executor = Executors.newCachedThreadPool();

        //here this will run 200 threads and will immediately create 200 connection.
        //however let's say we only wanted to allow limited number of connections, lets say 20
        //then using a semaphore we can permit only 20 connections and using synchronise on connection number
        //release and acquire on semaphore --manage those connections
        //in connections class we manage the connections using semaphores
        for(int i=0;i<200;i++){
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    Connection.getInstance().connect();
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
