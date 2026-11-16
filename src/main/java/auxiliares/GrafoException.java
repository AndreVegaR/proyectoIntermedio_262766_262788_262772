package auxiliares;

/**
 * Excepción que se lanza cuando se realiza una operación inválida sobre un grafo
 * 
 * @author Andre
 */
public class GrafoException extends RuntimeException {
    
    /** Constructor vacío */
    public GrafoException() {
        super();
    }

    /**
     * Constructor con mensaje
     * 
     * @param mensaje de la excepción
     */
    public GrafoException(String mensaje) {
        super(mensaje);
    }
}