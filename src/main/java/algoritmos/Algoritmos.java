package algoritmos;

import clases.Vertice;
import auxiliares.IGrafo;
import java.util.List;

/**
 * Fachada de todos los algoritmos del sistema
 * 
 * @param <T>
 * 
 * @author Andre
 */
public class Algoritmos<T> {
    
    // Instancias de clases dedicadas a un tipo de algoritmo en específico
    private final Distancia distancia = new Distancia();
    private final Ordenamiento orden = new Ordenamiento();
    private final Visita visita = new Visita();
    private final ArbolExpansionMinima<T> arbol = new ArbolExpansionMinima();
    
     /**
     * Visita y colorea los vértices por capas, iniciando por un nodo s
     * 
     * @param grafo Estructura sobre donde se trabaja
     * @param s Vértice raíz
     */
    public void busquedaAnchura(IGrafo<T> grafo, Vertice<T> s) {
        restablecerGrafo(grafo);
        visita.busquedaAnchura(grafo, s);
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
        restablecerGrafo(grafo);
        visita.busquedaProfundidad(grafo);
    }
    
    /**
     * Aplica el algoritmo de ordenamiento topológico, ordenando todo
     * el grafo en una secuencia lineal de posibles rutas para recorrer
     * los vértices
     * 
     * @param grafo cuyos vértices serán ordenados
     * 
     * @return los vértices ordenados
     */
    public List<Vertice<T>> ordenamientoTopologico(IGrafo grafo) {
        restablecerGrafo(grafo);
        return orden.ordenamientoTopologico(grafo);
    }
    
    /**
     * Calcula los caminos más cortos desde un vértice origen recorriendo los
     * vértices en orden topológico, por lo que solo funciona en grafos
     * dirigidos acíclicos
     *
     * @param grafo sobre el que se calculan los caminos
     * @param s vértice origen desde el que se miden las distancias
     */
    public void caminosCortos(IGrafo<T> grafo, Vertice<T> s) {
        List<Vertice<T>> lista = ordenamientoTopologico(grafo);
        restablecerGrafo(grafo);
        distancia.caminosCortos(grafo, lista, s);
    }
    
    /**
     * Calcula los caminos más largos desde un vértice origen recorriendo los
     * vértices en orden topológico, por lo que solo funciona en grafos
     * dirigidos acíclicos
     *
     * @param grafo sobre el que se calculan los caminos
     * @param s vértice origen desde el que se miden las distancias
     */
    public void caminosLargos(IGrafo<T> grafo, Vertice<T> s) {
        List<Vertice<T>> lista = ordenamientoTopologico(grafo);
        restablecerGrafo(grafo);
        distancia.caminosLargos(grafo, lista, s);
    }
    
    /**
     * Calcula los caminos más cortos desde un vértice origen con el algoritmo
     * de Dijkstra, por lo que no admite ponderaciones negativas
     *
     * @param grafo sobre el que se calculan los caminos
     * @param s vértice origen desde el que se miden las distancias
     */
    public void dijkstra(IGrafo<T> grafo, Vertice<T> s) {
        restablecerGrafo(grafo);
        distancia.dijkstra(grafo, s);
    }
    
    public IGrafo<T> kruskal(IGrafo<T> grafo) {
        restablecerGrafo(grafo);
        return arbol.kruskal(grafo);
    }
    
    public void prim(IGrafo<T> grafo, Vertice<T> s) {
        restablecerGrafo(grafo);
        arbol.prim(grafo, s);
    }
    
    /**
     * Restablece todos los vértices del grafo a sus valores
     * predeterminados al inicio de un algoritmo para tener
     * la certeza de no arrastrar datos previos
     * 
     * @param grafo a restablecer
     */
    private void restablecerGrafo(IGrafo<T> grafo) {
        for (Vertice<T> vertice: grafo.obtenerVertices()) {
            vertice.restablecer();
        }
    }
}