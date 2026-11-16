package clases;

/**
 * Implementación de un grafo no dirigido, donde cada arista conecta
 * a dos vértices en ambos sentidos y la matriz de adyacencia es simétrica
 * 
 * @param <T> implementa genéricos
 * 
 * @author Andre
 */
public class GrafoNoDirigido<T> extends GrafoBase<T> {

    /**
     * Constructor
     * 
     * @param esPonderado indica si las aristas tendrán pesos personalizados
     */
    public GrafoNoDirigido(boolean esPonderado) {
        super(esPonderado);
    }
    
    @Override
    public void imprimirGradosVertice(Vertice<T> vertice) {
        if (!verticeValido(vertice)) {
            return;
        }
        int grado = obtenerGrado(vertice.getIndice());
        System.out.println("Vértice [" + vertice.getDato() + "] -> Grado: " + grado);
    }
    
    /**
     * Escribe el peso de la arista en las celdas [origen][destino] y
     * [destino][origen] para mantener la matriz simétrica
     * 
     * @param origen vértice inicial
     * @param destino vértice final
     * @param peso a escribir en la matriz
     */
    @Override
    protected void escribirPesoArista(Vertice<T> origen, Vertice<T> destino, int peso) {
        int fila = origen.getIndice();
        int columna = destino.getIndice();
        this.matrizAdyacencia[fila][columna] = peso;
        this.matrizAdyacencia[columna][fila] = peso;
    }

    /**
     * Cuenta las aristas de un vértice, que equivalen a las celdas distintas
     * de cero de su fila porque la matriz es simétrica
     * 
     * @param indice del vértice a eliminar
     * 
     * @return cantidad de aristas incidentes al vértice
     */
    @Override
    protected int contarAristasIncidentes(int indice) {
        return contarAristasSalientes(indice);
    }

    /**
     * Calcula el grado de un vértice, contando el lazo dos veces
     * 
     * @param indice del vértice a consultar
     * 
     * @return grado del vértice
     */
    private int obtenerGrado(int indice) {
        int grado = contarAristasSalientes(indice);
        if (tieneLazo(indice)) {
            grado++;
        }
        return grado;
    }
}
