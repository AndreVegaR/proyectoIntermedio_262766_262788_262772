package algoritmos;

import clases.Vertice;
import auxiliares.IGrafo;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Algoritmos para medir distancia de grafos
 * 
 * @author Andre
 * @param <T>
 */
public class Distancia<T> {
    
    /**
     * 
     * 
     * @param u Vértice 1
     * @param v Vértice 2
     * @param peso de la arista
     */
    private void relajar(Vertice<T> u, Vertice<T> v, int peso) {
        if (u.getDistancia() < 1000000) {
            int suma = u.getDistancia() + peso;
            if (suma < v.getDistancia()) {
                v.setDistancia(suma);
                v.setPadre(u);
            }
        }
    }
    
    /**
     * 
     * 
     * @param grafo
     * @param vertices
     * @param s 
     */
    public void caminosCortos(IGrafo<T> grafo, List<Vertice<T>> vertices, Vertice<T> s) {
        s.setDistancia(0);
        for (Vertice<T> u: vertices) {
            for (Vertice<T> v: grafo.obtenerAdyacentes(u)) {
                relajar(u, v, grafo.obtenerPonderacion(u, v));
            }
        }
    }
    
    
    
    /**
     * Calcula los caminos más largos desde un vértice origen relajando las
     * aristas en orden topológico, por lo que solo funciona en grafos
     * dirigidos acíclicos
     *
     * @param grafo sobre el que se calculan los caminos
     * @param vertices del grafo en orden topológico
     * @param s vértice origen desde el que se miden las distancias
     */
    public void caminosLargos(IGrafo<T> grafo, List<Vertice<T>> vertices, Vertice<T> s) {
        // Marca todos los vértices como inalcanzables y deja el origen en cero
        int INFINITO = -Vertice.INFINITO;
        for (Vertice<T> v : grafo.obtenerVertices()) {
            v.setDistancia(INFINITO);
        }
        s.setDistancia(0);

        // Relaja las aristas en orden topológico, saltando los vértices que no se han alcanzado
        for (Vertice<T> u : vertices) {
            if (u.getDistancia() == INFINITO) {
                continue;
            }
            for (Vertice<T> v : grafo.obtenerAdyacentes(u)) {
                int suma = u.getDistancia() + grafo.obtenerPonderacion(u, v);
                if (suma > v.getDistancia()) {
                    v.setDistancia(suma);
                    v.setPadre(u);
                }
            }
        }
    }
    
    
    
    /**
     * Calcula la distancia mínima desde un vértice origen hacia todos 
     * los demás vértices del grafo usando el algoritmo de Dijkstra, por 
     * lo que las ponderaciones no deben ser negativas
     *
     * @param grafo en el que se calcularán los caminos más cortos
     * @param s vértice origen desde el cual se miden las distancias
     */
    public void dijkstra(IGrafo<T> grafo, Vertice<T> s) {
        s.setDistancia(0);

        // Crea la cola de prioridad
        PriorityQueue<Vertice<T>> cola = new PriorityQueue<>(
            Comparator.comparingInt(Vertice::getDistancia)
        );

        // Mete todos los vértices a la cola
        for (Vertice<T> v : grafo.obtenerVertices()) {
            cola.add(v);
        }

        // Por cada vértice:
        while (!cola.isEmpty()) {
            Vertice<T> u = cola.poll();

            // Por cada vértice adyacente del recién extraído
            for (Vertice<T> v: grafo.obtenerAdyacentes(u)) {

                // Lo saca de la cola antes de cambiar su distancia
                boolean estabaEnCola = cola.remove(v);

                // Relaja
                relajar(u, v, grafo.obtenerPonderacion(u, v));

                // Lo regresa a la cola con su distancia actualizada
                if (estabaEnCola) {
                    cola.add(v);
                }
            }
        }
    }
}