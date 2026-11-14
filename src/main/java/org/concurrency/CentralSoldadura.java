package org.concurrency;

import org.bateries.Bateria;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

public class CentralSoldadura {
    private int numPlazas;
    private Semaphore aforo;
    private boolean[] mesaSoldadura;
    private PriorityBlockingQueue<RobotRef> prioridadEnergia;
    public record RobotRef(int id, int energia) {}
    private Semaphore aforoFirma =  new Semaphore(1);

    public CentralSoldadura(int numPlazas) {
        this.numPlazas = numPlazas;
        this.aforo = new Semaphore(numPlazas);
        this.mesaSoldadura = new boolean[numPlazas];
        this.prioridadEnergia = new PriorityBlockingQueue<>(5, comparadorEnergia);

    }

    Comparator<RobotRef> comparadorEnergia = (p1, p2) -> {
        if ( p1.energia< p2.energia) {
            return -1;
        } else if (p1.energia>p2.energia)  {
            return 1;
        }
        return 0;
    };


    public void EstacionSoldadura(int id, int energiaRobot) {
        int mesaUsandose = numPlazas + 1;
        RobotRef robot = new RobotRef(id, energiaRobot);

        prioridadEnergia.put(robot);
        synchronized (prioridadEnergia) {
            prioridadEnergia.notifyAll();

            try {
                while (prioridadEnergia.peek().id() != id){
                    prioridadEnergia.wait();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        try {
            aforo.acquire();
            prioridadEnergia.remove(robot);

            synchronized (mesaSoldadura) {
                for (int i = 0; i < mesaSoldadura.length; i++) {
                    if (mesaSoldadura[i] == false) {
                        mesaSoldadura[i] = true;
                        mesaUsandose = i;
                        break;
                    }
                }
            }
            Thread.sleep(1000);

            synchronized (mesaSoldadura) {
                if (mesaUsandose == numPlazas + 1) {
                    System.out.println("Fallo a la hora de encontrar mesa y cerrarla.....");
                    return;
                }
                mesaSoldadura[mesaUsandose] = false;
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        } finally{

            aforo.release();
            synchronized (prioridadEnergia) {
                prioridadEnergia.notifyAll();
            }

        }
        firmarRegistro();

    }

    private void firmarRegistro(){

        try {
            aforoFirma.acquire();
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            aforoFirma.release();
        }

    }

}
