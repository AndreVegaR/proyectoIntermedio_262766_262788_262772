package algoritmos;

import auxiliares.GrafoException;
import auxiliares.IGrafo;
import clases.GrafoNoDirigido;
import clases.Vertice;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
     * Este algoritmo se aplica en grafos no dirigidos y ponderados
     * que este toma todas la aristas las ordena de menor a mayor y separa
     * a cada vertice en un componente independendiente , despues 
     * en base a las aristas va uniendo esos componentes si se cumple la
     * condición de que no estaban unidos.
     * @param grafo grafo Grafo no dirigido y ponderado
     * @return Nuevo grafo no dirigido con las aristas del árbol de expansión mínima
     */
    public IGrafo<T> kruskal(IGrafo<T> grafo) {
        if (grafo.esDirigido()) {
            throw new GrafoException("Error: Kruskal solo se puede aplicar en grafos no dirigidos");
        }
        if (!grafo.esPonderado()) {
            throw new GrafoException("Error: Kruskal solo se puede aplicar en grafos ponderados");
        }

        // Aqui se divide cada vértice para que sea como su propio grupo
        int numVertices = grafo.obtenerNumeroVertices();
        int[] padre = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            padre[i] = i;
        }
        
        // Aqui tomamos todas las aristas de los vertices y las ordenamos de menor a mayor
        List<Arista<T>> aristas = new ArrayList<>();
        for (Vertice<T> u : grafo.obtenerVertices()) {
            for (Vertice<T> v : grafo.obtenerAdyacentes(u)) {
                if (u.getIndice() < v.getIndice()) {
                    int peso = grafo.obtenerPonderacion(u, v);
                    aristas.add(new Arista<>(u, v, peso));
                }
            }
        }
        Collections.sort(aristas);
        
        // Se crea como un sub grafo en el cual copiamos los vertices.
        IGrafo<T> arbolMinimo = new GrafoNoDirigido<>(true);
        for (Vertice<T> v : grafo.obtenerVertices()) {
            arbolMinimo.insertarVertice(new Vertice<>(v.getDato()));
        }

        int aristasSeleccionadas = 0;

        //Aqui se evalúa cada arista y se agrega si los dos componentes de cada vértice no estan conectados
        for (Arista<T> arista : aristas) {
            int indiceU = arista.origen.getIndice();
            int indiceV = arista.destino.getIndice();

            int raizU = encontrarRaiz(padre, indiceU);
            int raizV = encontrarRaiz(padre, indiceV);

            if (raizU != raizV) {
                unirConjuntos(padre, raizU, raizV);

                Vertice<T> uNuevo = arbolMinimo.obtenerVertices().get(indiceU);
                Vertice<T> vNuevo = arbolMinimo.obtenerVertices().get(indiceV);
                arbolMinimo.insertarArista(uNuevo, vNuevo, arista.peso);

                aristasSeleccionadas++;
                if (aristasSeleccionadas == numVertices - 1) {
                    break;
                }
            }
        }

        //Se verifica si todos quedaon conectados en un arbol
        if (numVertices > 0 && aristasSeleccionadas < numVertices - 1) {
            throw new GrafoException("El grafo no es conexo; no se puede formar un árbol de expansión mínima completo.");
        }

        return arbolMinimo;
    }
    /**
     * Ayuda a encontrar el reprenstante o la raiz de cada conjunto
     * en el cual estan los vértices
     * @param padre Arreglo donde cada posición almacena el índice del padre o representante
     * @param indice Índice del vértice del cual se busca su raíz
     * @return Índice del vértice raíz que representa al conjunto
     */
    private int encontrarRaiz(int[] padre, int indice) {
        while (padre[indice] != indice) {
            indice = padre[indice];
        }
            return indice;
        }

    /**
     * Este metodo une los conjuntos que no estan unidos mediante la raiz
     * a la raiz del otro y se marque que son del mismo componente
     * @param padre Arreglo que almacena las relaciones de pertenencia de los vértices
     * @param raizU Índice de la raíz del primer conjunto
     * @param raizV Índice de la raíz del segundo conjunto
     */
    private void unirConjuntos(int[] padre, int raizU, int raizV) {
        padre[raizU] = raizV;
    }
    
    /**
     * Se crea la clase Arista la cual usaremos en el kruskal
     * @param <T> 
     */
    private static class Arista<T> implements Comparable<Arista<T>>{
        private Vertice<T> origen;
        private Vertice<T> destino;
        private int peso;

        public Arista(Vertice<T> origen, Vertice<T> destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
        
        @Override
        public int compareTo(Arista<T> otra) {
            return Integer.compare(this.peso, otra.peso);
        }
    }
}