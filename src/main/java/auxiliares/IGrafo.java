package auxiliares;

import clases.Vertice;
import java.util.List;

/**
 * Contrato que establece el comportamiento de un grafo
 * 
 * @param <T> implementa genéricos
 * 
 * @author Andre
 */
public interface IGrafo<T> {
    
    /**
     * Obtiene los vértices del grafo
     * 
     * @return los vértices
     */
    List<Vertice<T>> obtenerVertices();
    
    /**
     * Inserta un vértice dentro del grafo
     * 
     * @param vertice a insertar
     */
    void insertarVertice(Vertice<T> vertice);
    
    /**
     * Elimina un vértice dentro del grafo
     * 
     * @param vertice a eliminar
     */
    void eliminarVertice(Vertice<T> vertice);
    
    /**
     * Inserta una arista no ponderada entre dos vértices
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     */
    void insertarArista(Vertice<T> origen, Vertice<T> destino);
    
    /**
     * Inserta una arista ponderada entre dos vértices
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     * @param peso valor o ponderación de la arista
     */
    void insertarArista(Vertice<T> origen, Vertice<T> destino, int peso);
    
    /**
     * Obtiene la pondración de una arista
     * 
     * @param origen
     * @param destino
     * 
     * @return la ponderación
     */
    int obtenerPonderacion(Vertice<T> origen, Vertice<T> destino);
    
    /**
     * Elimina una arista entre dos vértices
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     */
    void eliminarArista(Vertice<T> origen, Vertice<T> destino);
    
    /**
     * Establece o actualiza la ponderación de una arista existente
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     * @param nuevaPonderacion de la arista
     */
    void establecerPonderacionArista(Vertice<T> origen, Vertice<T> destino, int nuevaPonderacion);
    
    /**
     * Obtiene e imprime el grado de un vértice (entrada y salida si el grafo es dirigido)
     * 
     * @param vertice a consultar
     */
    void imprimirGradosVertice(Vertice<T> vertice);
    
    /**
     * Obtiene e imprime el conjunto de vértices adyacentes a un vértice
     * 
     * @param vertice a consultar
     */
    void imprimirAdyacentes(Vertice<T> vertice);
    
    /**
     * Obtiene la lista de vértices adyacentes a un vértice
     * 
     * @param vertice a consultar
     * 
     * @return lista de nodos adyacentes
     */
    List<Vertice<T>> obtenerAdyacentes(Vertice<T> vertice);
    
    
    /**
     * Obtiene el grado de entrada de un vértice en concreto
     * 
     * @param vertice a analizar
     * 
     * @return el grado de entrada del vértice
     */
    int gradoEntrada(Vertice<T> vertice);
    
     /**
     * Obtiene el grado de salida de un vértice en concreto
     * 
     * @param vertice a analizar
     * 
     * @return el grado de salida del vértice
     */
    int gradoSalida(Vertice<T> vertice);
    
    /**
     * Imprime la matriz de adyacencia del grafo
     */
    void imprimirMatrizAdyacencia();
    
    /**
     * Obtiene la cantidad total de vértices
     * 
     * @return número de vértices
     */
    int obtenerNumeroVertices();
    
    /**
     * Obtiene la cantidad total de aristas
     * 
     * @return número de aristas
     */
    int obtenerNumeroAristas();
    
    /**
     * Verifica si el grafo es ponderado
     * 
     * @return true si es ponderado, false en caso contrario
     */
    boolean esPonderado();
    
    /**
     * Verifica si el grafo es dirigido
     * 
     * @return true si es dirigido, false en caso contrario
     */
    public boolean esDirigido();
}