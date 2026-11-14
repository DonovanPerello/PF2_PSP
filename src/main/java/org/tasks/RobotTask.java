package org.tasks;

import org.bateries.Bateria;
import org.concurrency.CentralSoldadura;
import org.concurrency.CintaTransportadora;
import org.concurrency.SupervisorCentral;
import org.view.ConsoleView;


import java.io.IOException;
import java.util.Random;

public class RobotTask implements Runnable {

    private int id;
    private int nivelEnergia;
    private final CentralSoldadura centralSoldadura;
    private final CintaTransportadora cintaTransportadora;
    private final SupervisorCentral supervisorCentral;
    private  final ConsoleView consoleView;

    public RobotTask(int id, CentralSoldadura centralSoldadura, CintaTransportadora cintaTransportadora, SupervisorCentral supervisorCentral, ConsoleView consoleView) {
        this.id = id;
        this.centralSoldadura = centralSoldadura;
        this.cintaTransportadora = cintaTransportadora;
        this.supervisorCentral = supervisorCentral;
        this.consoleView = consoleView;
        nivelEnergia = 50;
    }

    public int getNivelEnergia() {
        return nivelEnergia;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while(nivelEnergia > 0) {
            try {
                supervisorCentral.comprobarAlerta();

               Bateria bateria = cintaTransportadora.sacarBateriaEnCinta();
                nivelEnergia -= rand.nextInt(5) + 1;
               consoleView.logEventos("Robot num: " + this.id + " y nivel de energia al "+this.nivelEnergia +"% ha extraido la bateria num: " + bateria.id() + " de la cintaTransportadora");

                centralSoldadura.EstacionSoldadura(this.id, this.nivelEnergia);
                nivelEnergia -= rand.nextInt(5) + 1;
                consoleView.logEventos("Robot num: " + this.id + " y nivel de energia al "+this.nivelEnergia +"% ha soldado la bateria num: " + bateria.id() + " en la estacionSoldadura");

            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

        }
        consoleView.logEventos("Robot num: " + this.id + " se ha quedado sin energia: "+this.nivelEnergia +"%");
        return;

    }
}
