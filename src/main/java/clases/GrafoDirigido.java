package clases;

/**
 * Implementación de un grafo dirigido, donde cada arista tiene un
 * sentido que va desde un vértice de origen hacia uno de destino
 * 
 * @param <T> implementa genéricos
 * 
 * @author Andre
 */
public class GrafoDirigido<T> extends GrafoBase<T> {

    /**
     * Constructor
     * 
     * @param esPonderado indica si estarán ponderados los nodos
     */
    public GrafoDirigido(boolean esPonderado) {
        super(esPonderado);
    }
    
    
    @Override
    public void imprimirGradosVertice(Vertice<T> vertice) {
        // Verifica que el vértice pertenezca al grafo
        if (!verticeValido(vertice)) {
            return;
        }

        // Obtiene los grados de entrada y salida del vértice
        int indice = vertice.getIndice();
        int gradoEntrada = contarAristasEntrantes(indice);
        int gradoSalida = contarAristasSalientes(indice);

        // Imprime los grados obtenidos
        System.out.println("Vértice [" + vertice.getDato() + "] -> Grado de entrada: " + gradoEntrada + ", Grado de salida: " + gradoSalida);
    }
    
    
    
    /**
     * Escribe el peso de la arista únicamente en la celda [origen][destino],
     * ya que la conexión solo existe en un sentido
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     * @param peso a escribir en la matriz
     */
    @Override
    protected void escribirPesoArista(Vertice<T> origen, Vertice<T> destino, int peso) {
        int fila = origen.getIndice();
        int columna = destino.getIndice();
        this.matrizAdyacencia[fila][columna] = peso;
    }



    /**
     * Cuenta las aristas salientes y entrantes de un vértice, restando
     * el lazo una vez porque aparece en la fila y en la columna
     * 
     * @param indice del vértice a eliminar
     * 
     * @return cantidad de aristas incidentes al vértice
     */
    @Override
    protected int contarAristasIncidentes(int indice) {
        // Obtiene las aristas que salen y las que llegan al vértice
        int salientes = contarAristasSalientes(indice);
        int entrantes = contarAristasEntrantes(indice);

        // Evita contar dos veces el lazo, que está en ambos conteos
        int lazo = tieneLazo(indice) ? 1 : 0;

        return salientes + entrantes - lazo;
    }
    
    
    /* Sobreeescribe para que regrese verdadero */
    @Override
    public boolean esDirigido() {
        return true;
    }
}
