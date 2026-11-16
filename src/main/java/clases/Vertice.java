package clases;

/**
 * Clase que representa un nodo dentro de un grafo
 * 
 * @param <T> implementa genéricos
 * 
 * @author Andre
 */
public class Vertice<T> {
    
    /** Enumerador según su estado/color */
    public enum Color {
        BLANCO,
        GRIS,
        NEGRO
    }
    
    private int indice;
    private T dato;
    
    private Color color;
    private int distancia;
    private Vertice<T> padre;
    
    private int descubrimiento;
    private int finalizacion;
    
    public static final int INFINITO = 1000000;

    /**
     * Constructor que inicializa los valores del vértice
     * 
     * @param dato que almacenará el nodo
     */
    public Vertice(T dato) {
        this.dato = dato;
        this.color = Color.BLANCO;
        this.padre = null;
        this.distancia = INFINITO;
        this.descubrimiento = -1;
        this.finalizacion = -1;
    }
    
    /** Restablece los valores del vértice (excepto el dato) */
    public void restablecer() {
        this.color = Color.BLANCO;
        this.padre = null;
        this.distancia = INFINITO;
        this.descubrimiento = -1;
        this.finalizacion = -1;
    }
    
    /**
     * Representa el vértice como texto usando su dato
     *
     * @return dato del vértice convertido a texto
     */
    @Override
    public String toString() {
        return String.valueOf(dato);
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public Vertice<T> getPadre() {
        return padre;
    }

    public void setPadre(Vertice<T> padre) {
        this.padre = padre;
    }

    public int getDescubrimiento() {
        return descubrimiento;
    }

    public void setDescubrimiento(int descubrimiento) {
        this.descubrimiento = descubrimiento;
    }

    public int getFinalizacion() {
        return finalizacion;
    }

    public void setFinalizacion(int finalizacion) {
        this.finalizacion = finalizacion;
    }
    
    public boolean esBlanco() {
        return this.color.equals(Color.BLANCO);
    }
    
    public boolean esGris() {
        return this.color.equals(Color.GRIS);
    }
    
    public boolean esNegro() {
        return this.color.equals(Color.NEGRO);
    }
    
    public void colorearBlanco() {
        this.color = Color.BLANCO;
    }
    
    public void colorearGris() {
        this.color = Color.GRIS;
    }
    
    public void colorearNegro() {
        this.color = Color.NEGRO;
    }
}