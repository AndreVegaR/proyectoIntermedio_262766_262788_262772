package algoritmos;

import clases.Vertice;
import auxiliares.IGrafo;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que tiene la lógica de ordenar de diferentes maneras
 * 
 * @param <T> implementa genéricos
 * 
 * @author Andre
 * 
 */
public class Ordenamiento<T> {
 
    /**
     * Aplica el algoritmo de ordenamiento topológico, ordenando todo
     * el grafo en una secuencia lineal de posibles rutas para recorrer
     * los vértices
     * 
     * @param grafo cuyos vértices serán ordenados
     * 
     * @return los vértices ordenados
     */
    public List<Vertice<T>> ordenamientoTopologico(IGrafo<T> grafo) {
        int tamanio = grafo.obtenerNumeroVertices();
        
        // Almacena el grado de entrada de cada vértice en un arreglo
        int gradosEntrada[] = new int[tamanio];
        for (Vertice<T> u: grafo.obtenerVertices()) {
            gradosEntrada[u.getIndice()] = grafo.gradoEntrada(u);
        }

        // Guarda en una lista de candidatos los vértices cuyo grado de entrada sea 0
        List<Vertice<T>> candidatos = new ArrayList<>();
        for (Vertice<T> u: grafo.obtenerVertices()) {
            if (gradosEntrada[u.getIndice()] == 0) {
                candidatos.add(u);
            }
        }
         
        // Lista para guardar todos los vértices ordenados
        List<Vertice<T>> resultado = new ArrayList<>();
        
        // Mientras existan candidatos
        while (!candidatos.isEmpty()) {
            
            // Remueve el vértice de candidatos y lo agrega a lineal
            Vertice<T> u = candidatos.get(0);
            resultado.add(u);
            candidatos.remove(0);
            
            // Agrega como candidatos
            for (Vertice<T> v: grafo.obtenerAdyacentes(u)) {
                int i = v.getIndice();
                
                // Reduce uno su grado de entrada en el arreglo
                gradosEntrada[i]--;
                
                // Si al reducirlo llega a 0, se agrega a los candidatos
                if (gradosEntrada[i] == 0) {
                    candidatos.add(v);
                }
            }
        }
        return resultado;
    }  
}