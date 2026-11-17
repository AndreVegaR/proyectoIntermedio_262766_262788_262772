package algoritmos;

import auxiliares.GrafoException;
import auxiliares.IGrafo;
import clases.GrafoNoDirigido;
import clases.Vertice;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 
 * 
 * @author Andre
 */
public class ArbolExpansionMinima<T> {
    
    /**
     * xd
     * 
     * @param grafo
     * @param inicio 
     */
    public void prim(IGrafo<T> grafo, Vertice<T> inicio) {

        if (grafo.esDirigido()) {
            throw new GrafoException("Error: Prim solo se puede aplicar en grafos no dirigidos");
        }

        for (Vertice<T> vertice : grafo.obtenerVertices()){
            vertice.restablecer();

        }

        // excluir el inicio
        inicio.setDistancia(0);

        // La prioridad la toma de la distancia, extrae la mas baja
        PriorityQueue<Vertice<T>> colaPrioridad = new PriorityQueue<>(
                Comparator.comparingInt(Vertice::getDistancia)
        );

        colaPrioridad.addAll(grafo.obtenerVertices());

        while(!colaPrioridad.isEmpty()){

            // se extrae el vertice con el menor valor
            Vertice<T> actual = colaPrioridad.poll();

            // necesario para no considerar islas
            if (actual.getDistancia() == Vertice.INFINITO) {
                break;
            }

            for (Vertice<T> vecino : grafo.obtenerAdyacentes(actual)){
                if(colaPrioridad.contains(vecino) && grafo.obtenerPonderacion(actual, vecino) < vecino.getDistancia()){

                    // sacar al vecino de la cola
                    colaPrioridad.remove(vecino);

                    vecino.setDistancia(grafo.obtenerPonderacion(actual, vecino));
                    vecino.setPadre(actual);

                    // volver a meter para reubicar su prioridad ya que cambió su peso
                    colaPrioridad.add(vecino);
                }
            }
        }
    }
    
    /**
     * xd
     * 
     * @param grafo 
     */
    public void kruskal(IGrafo<T> grafo) {
        System.out.println("xd");
    }
}