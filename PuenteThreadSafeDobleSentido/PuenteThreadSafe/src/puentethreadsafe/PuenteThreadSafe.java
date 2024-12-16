package puentethreadsafe;

import java.util.Random;

/*
    Sistema que controla el paso de personas por un puente:
    Las personas pueden pasar en los dos sentidos.
    No puede haber más de cuatro personas a la vez.
    No puede haber más de tres personas en cada sentido.
    No puede haber más de 300 kg de peso en ningún momento.
    El tiempo entre la llegada de dos personas es aleatorio entre 1 y 30 segundos.
    El tiempo en atravesar el puente es aleatorio, entre 10 y 50 segundos.
    Las personas tienen un peso aleatorio entre 40 y 120 kg.
*/

public class PuenteThreadSafe {

    public static void main(String[] args) {
        final int TIEMPO_MINIMO_LLEGADA = 1;
        final int TIEMPO_MAXIMO_LLEGADA = 30;
        final int TIEMPO_MINIMO_CRUCE = 10;
        final int TIEMPO_MAXIMO_CRUCE = 50;
        final int PESO_MINIMO_PERSONA = 40;
        final int PESO_MAXIMO_PERSONA = 120;
        
        // Instancia del puente
        final Puente puente = new Puente();
        int contadorPersonas = 0;
        
        // Bucle infinito creando personas para cruzar el puente
        while (true) {
            // Crear una persona
            contadorPersonas++;
            String identificadorPersona = "Persona " + contadorPersonas;
            int tiempoEsperaLlegada = numeroAleatorio(TIEMPO_MINIMO_LLEGADA, TIEMPO_MAXIMO_LLEGADA);
            int tiempoCruce = numeroAleatorio(TIEMPO_MINIMO_CRUCE, TIEMPO_MAXIMO_CRUCE);
            int pesoPersona = numeroAleatorio(PESO_MINIMO_PERSONA, PESO_MAXIMO_PERSONA);
            String direccionPersona = numeroAleatorio(0, 1) == 0 ? "NORTE" : "SUR";
            System.out.printf("La %s llegará en %d segundos, en dirección %s, pesa %d kilos y tardará %d segundos en cruzar. \n",
                        identificadorPersona, tiempoEsperaLlegada, direccionPersona, pesoPersona, tiempoCruce);
            
            // Crear el hilo de la persona
            Thread hiloPersona = new Thread(new Persona(identificadorPersona, tiempoCruce, pesoPersona, direccionPersona, puente));
            
            // Esperar antes de que llegue la siguiente persona
            try {
                Thread.sleep(tiempoEsperaLlegada * 100); // Espera el tiempo entre llegadas
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Iniciar el hilo de la persona
            hiloPersona.start();
        }
    }

    public static int numeroAleatorio(int minimo, int maximo) {
        Random generadorAleatorio = new Random();
        return generadorAleatorio.nextInt(maximo - minimo + 1) + minimo;
    }
}
