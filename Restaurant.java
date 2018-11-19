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

        /*try{
            wn.useWaitAndNotify();
        }catch(InterruptedException e){
            e.printStackTrace();
        }*/

        SymphonySynchro ss  = new SymphonySynchro();
        /*try{
            ss.useSynchro();
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/
        FancyLocks fl = new FancyLocks();
        /*try{
            fl.useFancyLocks();
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/

        AvoidDeadLocks ad = new AvoidDeadLocks();
        /*try{
           ad.useAvoidDeadLocks();
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/

        TheSemaphores ts = new TheSemaphores();
        try{
            ts.useConnections();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}