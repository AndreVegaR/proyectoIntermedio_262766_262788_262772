package algoritmos;

import clases.Vertice;
import java.util.Queue;
import auxiliares.IGrafo;
import java.util.ArrayDeque;

/**
 * Clase que tiene la lógica de diferentes algoritmos para
 * visitar vértices
 *  
 * @param <T> implementa genéricos
 * 
 * @author Andre 
 */
public class Visita<T> {
    
    /**
     * Visita y colorea los vértices por capas, iniciando por un nodo r
     * 
     * @param grafo Estructura sobre donde se trabaja
     * @param s Vértice raíz
     */
    public void busquedaAnchura(IGrafo<T> grafo, Vertice<T> s) {
        // Configura el vértice raíz visitado
        s.colorearGris();
        s.setDistancia(0);
        
        // Encola el vértice raíz
        Queue<Vertice<T>> cola = new ArrayDeque<>();
        cola.offer(s);

        // Itera en cada vértice de la cola
        while (!cola.isEmpty()) {
            Vertice<T> u = cola.poll();
            for (Vertice<T> v: grafo.obtenerAdyacentes(u)) {
                
                // Si el vértice no ha sido explorado, lo configura y agrega a la cola
                if (v.esBlanco()) {
                    v.colorearGris();
                    v.setDistancia(u.getDistancia() + 1);
                    v.setPadre(u);
                    
                    // Agrega el vértice a la cola
                    cola.offer(v);
                }
            }
            
            // Lo establece como negro al visitar todos sus vecinos
            u.colorearNegro();
        }
    }
    
    
    
    /**
     * Algoritmo encargado de visitar cada nodo por profunidad, o
     * sea, recorriendo el cámino máximo de cada vértice y repetir
     * el proceso con el resto de vértices disponibles. Marca el 
     * tiempo de los vértices según su visita y salida final
     * 
     * @param grafo donde se va a trabajar
     */
    public void busquedaProfundidad(IGrafo<T> grafo) {
        // Variable global donde guarda el tiempo donde se visita o colorea un nodo
        int[] tiempo = new int[]{0};
        
        // Visita cada vértice
        for (Vertice<T> u: grafo.obtenerVertices()) {
            if (u.esBlanco()) {
                visitaProfundidad(grafo, u, tiempo);
            }
        }
    }
    
    
    
    /**
     * Parte recursiva de la búsqueda por profunidad. Establece el tiempo,
     * colorea los vértices, itera en cada adyacencia del vértice en el 
     * parámetro, y si se  necesita hace una llamada recursiva para repetir 
     * el proceso
     * 
     * @param grafo
     * @param u
     * @param tiempo 
     */
    private void visitaProfundidad(IGrafo<T> grafo, Vertice<T> u, int[] tiempo) {
        // Configura inicialmente el vértice
        tiempo[0] = tiempo[0] + 1;
        u.setDescubrimiento(tiempo[0]);
        u.colorearGris();
        
        // Itera en cada adyacencia del vértice
        for (Vertice<T> v: grafo.obtenerAdyacentes(u)) {
            
            // Si no ha sido visitado, configura su padre y repite el procedimiento recursivamente
            if (v.esBlanco()) {
                v.setPadre(u);
                visitaProfundidad(grafo, v, tiempo);
            }  
        }
        
        // Configura el vértice al ya estar totalmente visitado
        u.colorearNegro();
        tiempo[0] = tiempo[0] + 1;
        u.setFinalizacion(tiempo[0]); 
    }
}