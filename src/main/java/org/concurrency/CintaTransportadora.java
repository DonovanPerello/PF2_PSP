package org.concurrency;
import org.resources.Bateria;
import org.view.ConsoleView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CintaTransportadora {
    private final int aforoBaterias;
    private final Lock candado = new ReentrantLock();
    private final Condition cintaNoPlena = candado.newCondition();
    private final Condition cintaNoBuida = candado.newCondition();
    private final List<Bateria> baterias = new ArrayList<>();


    public CintaTransportadora(int aforoBaterias) {
        this.aforoBaterias = aforoBaterias;

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
