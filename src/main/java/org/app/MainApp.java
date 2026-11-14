package org.app;

import org.concurrency.CentralSoldadura;
import org.concurrency.CintaTransportadora;
import org.concurrency.SupervisorCentral;
import org.tasks.AGVTask;
import org.tasks.RobotTask;
import org.view.ConsoleView;

import java.util.Random;

public class MainApp {

    static void main(String[] args) {
        //VIEW--------------------------------------------------------------------
        ConsoleView consoleView = new ConsoleView();
        //VIEW--------------------------------------------------------------------

        //CONCURRENCY-------------------------------------------------------------
        CentralSoldadura centralSoldadura = new CentralSoldadura(3);
        CintaTransportadora cintaTransportadora = new CintaTransportadora(3,consoleView);
        SupervisorCentral supervisorCentral = new SupervisorCentral(consoleView);
        //CONCURRENCY-------------------------------------------------------------

        //TASKS-------------------------------------------------------------------
        AGVTask agv1 = new AGVTask(0,cintaTransportadora,supervisorCentral,consoleView);
        AGVTask agv2 = new AGVTask(1,cintaTransportadora,supervisorCentral,consoleView);
        AGVTask agv3 = new AGVTask(2,cintaTransportadora,supervisorCentral,consoleView);
        RobotTask robot1 = new RobotTask(0,centralSoldadura,cintaTransportadora,supervisorCentral,consoleView);
        RobotTask robot2 = new RobotTask(1,centralSoldadura,cintaTransportadora,supervisorCentral,consoleView);
        RobotTask robot3 = new RobotTask(2,centralSoldadura,cintaTransportadora,supervisorCentral,consoleView);
        //TASKS-------------------------------------------------------------------

        //THREADS-----------------------------------------------------------------
        Thread t1 = new Thread(agv1);
        Thread t2 = new Thread(agv2);
        Thread t6 = new Thread(agv3);
        Thread t3 = new Thread(robot1);
        Thread t4 = new Thread(robot2);
        Thread t5 = new Thread(robot3);
        //THREADS-----------------------------------------------------------------

        t1.start();
        t2.start();
        t6.start();
        t3.start();
        t4.start();
        t5.start();



        try {
            Thread.sleep(6000);
            supervisorCentral.activarAlerta();
            Thread.sleep(3500);
            supervisorCentral.desactivarAlerta();
            t3.join();
            t4.join();
            t5.join();

            t1.interrupt();
            t2.interrupt();
            t6.interrupt();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
