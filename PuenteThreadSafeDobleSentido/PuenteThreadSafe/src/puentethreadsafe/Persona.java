package puentethreadsafe;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Persona implements Runnable {
    private final String identificadorPersona;
    private final int tiempoCruce;
    private final int peso;
    private final String direccion;
    private final Puente puente;

    public Persona(String identificadorPersona, int tiempoCruce, int peso, String direccion, Puente puente) {
        this.identificadorPersona = identificadorPersona;
        this.tiempoCruce = tiempoCruce;
        this.peso = peso;
        this.direccion = direccion;
        this.puente = puente;
    }

    public String getIdentificadorPersona() {
        return identificadorPersona;
    }

    public int getTiempoCruce() {
        return tiempoCruce;
    }

    public int getPeso() {
        return peso;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public void run() {
        System.out.printf(">>> La %s con %d kilos quiere cruzar en %d segundos y en dirección %s.\n" +
                          "    Estado del Puente: %d personas, %d kilos.\n",
                          identificadorPersona, peso, tiempoCruce, direccion, puente.getTotalPersonas(), puente.getPesoTotal());
        
        // Intentar entrar
        try {
            puente.entrar(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Cruza el puente
        try {
            Thread.sleep(tiempoCruce * 100); // Simula el tiempo de cruce
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Salir
        puente.salir(this);
    }
}


