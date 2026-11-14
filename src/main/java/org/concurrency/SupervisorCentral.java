package org.concurrency;

import org.view.ConsoleView;

public class SupervisorCentral {
    private boolean alertaActivada = false;
    private ConsoleView consoleView;

    public SupervisorCentral(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    public synchronized void comprobarAlerta() throws InterruptedException {
        while (alertaActivada) {
            wait();

        }
    }

    public synchronized void activarAlerta() {
    alertaActivada = true;

    consoleView.logEventos("\uD83D\uDEA8 ALERTA DE EMERGENCIA ACTIVADA: Todos los sistemas se detendrán en breve...");
        }

    public synchronized void desactivarAlerta() {

        alertaActivada = false;
        notifyAll();
        consoleView.logEventos("Todos los sistemas se han restablecido, se procede a continuar con las tareas");
    }

    public boolean isAlertaActivada() {
        return alertaActivada;
    }
}
