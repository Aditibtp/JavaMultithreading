package TPackage;

// thread pools - way of managing lot of threads at the same time

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Manager implements Runnable{

    private int id;

    public Manager(int id){
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Starting id "+id);

        try{
            Thread.sleep(200);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Completed id "+id);
    }
}

public class SwimmingPools {


    // thread pool is like number of workers in a factory
    // so each of these worker threads will work on each task one at a time
    // once one thread finishes one task -- it starts another task // in this way we are recycling threads
    // so to allot tasks we submit tasks to excecutor
    public void createExecutor(){
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for(int i=0; i<10; i++) {
            executor.submit(new Manager(i));
        }
        //waits for all threads to complete doing what they are doing
        executor.shutdown();

        System.out.println("All tasks submitted");

        //waits for 2 hours it waits for 2 hours and returns and after that more stuff can execute
        try{
            executor.awaitTermination(2, TimeUnit.HOURS);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("All tasks completed");

    }
}
