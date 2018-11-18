import java.util.Scanner;

import TPackage.*;

public class Restaurant {

    public static void main(String args[]){

        WorkerLocks w1 = new WorkerLocks();
        //w1.startTasks();

        SwimmingPools poo = new SwimmingPools();
        //poo.createExecutor();

        DoorLatch dl = new DoorLatch();
        //dl.useLatch();

        ProducerConsumer pc = new ProducerConsumer();
        //pc.usePC();

        WaitAndNotify wn = new WaitAndNotify();

        try{
            wn.useWaitAndNotify();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}