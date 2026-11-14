package org.tasks;

import org.resources.Bateria;
import org.concurrency.CintaTransportadora;
import org.concurrency.SupervisorCentral;
import org.view.ConsoleView;

import java.util.Random;

public class AGVTask implements Runnable {
    private int id;
    private static int asignarIdBaterias = 0;
    private final CintaTransportadora transportadora;
    private final SupervisorCentral supervisorCentral;
    private final ConsoleView consoleView;

    public AGVTask(int id, CintaTransportadora transportadora, SupervisorCentral supervisorCentral, ConsoleView consoleView) {
        this.transportadora = transportadora;
        this.supervisorCentral = supervisorCentral;
        this.consoleView = consoleView;
        this.id = id;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            asignarIdBaterias++;
            Random rand = new Random();
            int numRandom = rand.nextInt((1500 - 500) + 1) + 500;
            try {
                Bateria bateria = new Bateria(asignarIdBaterias);
                supervisorCentral.comprobarAlerta();

                transportadora.meterBateriaEnCinta(bateria);
                consoleView.logEventos("AGV num: " + this.id + " ha metido la bateria num: " + bateria.id() + " a la estacionSoldadura");

            } catch (InterruptedException e) {
                consoleView.logEventos("AGV num: " + this.id + " apagandose......" );
                return;
            }


        }return;
    }
}
