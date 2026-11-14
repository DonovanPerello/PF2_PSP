package org.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleView {
private LocalDateTime fecha =  LocalDateTime.now();
private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

public ConsoleView(){

}

    public synchronized void logEventos(String mensaje){
       String logTime =  fecha.now().format(formato);

        System.out.println(logTime + ": " + mensaje);
    }
}
