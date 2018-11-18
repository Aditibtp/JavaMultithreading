package TPackage;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer {

    // data structure to hold items FIFO
    // Thread safe
    private BlockingQueue<Integer> q = new ArrayBlockingQueue<Integer>(10);

    public void producer() throws  InterruptedException{
        Random r = new Random(100);
        while(true){
            q.put(r.nextInt(100));
        }
    }

    public void consumer() throws InterruptedException {
        Random r2 = new Random();
        while(true){
            Thread.sleep(100);
            if(r2.nextInt(10) == 0){
                Integer value = q.take();
                System.out.println("Taken value: "+ value + "; Queue size is: " + q.size());
            }
        }
    }

    public void usePC(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    producer();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    consumer();
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
