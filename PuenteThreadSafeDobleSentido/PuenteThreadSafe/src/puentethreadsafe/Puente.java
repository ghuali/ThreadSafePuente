package puentethreadsafe;


public class Puente {
    // Constantes
    private static final int LIMITE_PERSONAS = 4; // Número máximo de personas en el puente
    private static final int LIMITE_PESO = 300; // Peso máximo en el puente
    private static final int LIMITE_PERSONAS_DIRECCION = 3; // Máximo de personas por dirección (NORTE o SUR)
    
    // Variables
    private int totalPersonas = 0; // Total de personas en el puente
    private int pesoTotal = 0; // Peso total de las personas en el puente
    private int personasNorte = 0; // Personas cruzando hacia el norte
    private int personasSur = 0; // Personas cruzando hacia el sur
    private String direccionActual;
    
    // Constructor
    public Puente() {}
    
    // Métodos de acceso
    public int getTotalPersonas() {
        return totalPersonas;
    }
    
    public int getPesoTotal() {
        return pesoTotal;
    }
    
    // Método para permitir que una persona entre al puente
    public void entrar(Persona persona) throws InterruptedException {
        synchronized (this) {
            String direccionPersona = persona.getDireccion();
            
            // Si no hay personas cruzando, cualquiera puede pasar
            while ((totalPersonas + 1 > LIMITE_PERSONAS) ||
                    (pesoTotal + persona.getPeso() > LIMITE_PESO) ||
                    // Si ya hay personas cruzando en el mismo sentido, bloqueamos
                    (direccionPersona.equals("NORTE") && personasNorte >= LIMITE_PERSONAS_DIRECCION) ||
                    (direccionPersona.equals("SUR") && personasSur >= LIMITE_PERSONAS_DIRECCION)) {
                System.out.printf("*** La %s debe esperar.\n", persona.getIdentificadorPersona());
                this.wait(); // El hilo espera
            }
    
            // Si el puente está vacío, puede entrar la primera persona y marcar la dirección
            if (totalPersonas == 0) {
                // Establecer la dirección solo cuando el puente está vacío
                this.direccionActual = direccionPersona;
            }
    
            // Si el puente no está lleno y el peso no excede, permitimos que entre la persona
            totalPersonas++;
            pesoTotal += persona.getPeso();
    
            // Si entra hacia el norte o sur, incrementamos el contador de esa dirección
            if (direccionPersona.equals("NORTE")) {
                personasNorte++;
            } else {
                personasSur++;
            }
    
            System.out.printf(">>> La %s entra. Estado del puente: %d personas, %d kilos.\n",
                    persona.getIdentificadorPersona(), totalPersonas, pesoTotal);
        }
    }
    
    // Método para permitir que una persona salga del puente
    public void salir(Persona persona) {
        synchronized (this) {
            String direccionPersona = persona.getDireccion();
            
            // Actualizamos el número de personas y el peso
            totalPersonas--;
            pesoTotal -= persona.getPeso();
    
            if (direccionPersona.equals("NORTE")) {
                personasNorte--;
            } else {
                personasSur--;
            }
    
            // Si ya no hay personas en el puente, se borra la dirección
            if (totalPersonas == 0) {
                direccionActual = null;
            }
    
            // Notificamos a todos los hilos esperando para que puedan intentar entrar
            this.notifyAll();
    
            System.out.printf(">>> La %s sale. Estado del puente: %d personas, %d kilos.\n",
                    persona.getIdentificadorPersona(), totalPersonas, pesoTotal);
        }
    }
}

