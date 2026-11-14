package org.concurrency;
import org.bateries.Bateria;
import org.view.ConsoleView;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CintaTransportadora {
    private int aforoBaterias;
    private Lock candado = new ReentrantLock();
    private Condition cintaNoPlena = candado.newCondition();
    private Condition cintaNoBuida = candado.newCondition();
    private List<Bateria> baterias = new ArrayList<>();
    private ConsoleView consoleView;

    public CintaTransportadora(int aforoBaterias, ConsoleView consoleView) {
        this.aforoBaterias = aforoBaterias;
        this.consoleView = consoleView;
    }
    public void meterBateriaEnCinta(Bateria bateria)  throws InterruptedException{
        candado.lock();

        try {
            while(baterias.size() >=aforoBaterias) {
                cintaNoPlena.await();
                Thread.sleep(100);
            }
            baterias.add(bateria);
            cintaNoBuida.signal();
        } finally {
            candado.unlock();
        }

    }

    public Bateria sacarBateriaEnCinta() throws  InterruptedException {
        candado.lock();
        Bateria bateria = null;
        try {
            while(baterias.isEmpty()) {
                cintaNoBuida.await();
            }
          bateria = baterias.removeFirst();
            cintaNoPlena.signal();

        }finally {
            candado.unlock();
        }

        return  bateria;
    }
}
