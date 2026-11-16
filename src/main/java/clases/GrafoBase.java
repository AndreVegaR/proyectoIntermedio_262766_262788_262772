package clases;

import auxiliares.IGrafo;
import auxiliares.GrafoException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Clase base abstracta que centraliza la estructura y atributos
 * comunes para cualquier tipo de grafo basado en matriz de adyacencia
 * 
 * @param <T> implementa genéricos
 * 
 * @author Andre
 */
public abstract class GrafoBase<T> implements IGrafo<T> {

    // Mensajes de excepciones
    protected static final String NO_PONDERADO = "El grafo no es ponderado";
    protected static final String PESO_CERO = "El peso de una arista no puede ser cero";

    // Mensajes de error mostrados en consola
    protected static final String VERTICE_INEXISTENTE = "Error: El vértice no existe en el grafo.";
    protected static final String VERTICES_INEXISTENTES = "Error: Uno o ambos vértices no existen en el grafo.";
    protected static final String ARISTA_INEXISTENTE = "Error: La arista no existe en el grafo.";

    // Bandera que determina si el grafo debe ser ponderado o no
    protected boolean esPonderado;

    // Lista de vértices (nodos)
    protected List<Vertice<T>> vertices;

    // Matriz de adyacencia que almacena las conexiones entre nodos
    protected int[][] matrizAdyacencia;

    // Cantidad de aristas (arcos)
    protected int numAristas;

    /**
     * Constructor
     * 
     * @param esPonderado indica si las aristas tendrán pesos personalizados
     */
    public GrafoBase(boolean esPonderado) {
        this.esPonderado = esPonderado;
        this.vertices = new ArrayList<>();
        this.matrizAdyacencia = new int[0][0];
        this.numAristas = 0;
    }

    /**
     * Redimensiona la matriz de adyacencia incrementando sus 
     * dimensiones en una unidad para dar espacio a un nuevo
     * vértice, conservando las conexiones preexistentes e inicializando 
     * las nuevas posiciones (fila y columna) en cero
     */
    protected void expandirMatrizAdyacencia() {
        int nuevoTamanio = this.vertices.size();
        int[][] nuevaMatriz = new int[nuevoTamanio][nuevoTamanio];

        // Recorre la nueva matriz para copiar los datos de la anterior
        for (int indiceFila = 0; indiceFila < nuevoTamanio; indiceFila++) {
            for (int indiceColumna = 0; indiceColumna < nuevoTamanio; indiceColumna++) {

                // Si están dentro del rango de ltamaño, copia el elemento
                if (indiceFila<nuevoTamanio-1 && indiceColumna<nuevoTamanio-1) {
                    nuevaMatriz[indiceFila][indiceColumna] = this.matrizAdyacencia[indiceFila][indiceColumna];
                }
            }
        }

        // Reasigna la referencia interna a la nueva matriz redimensionada
        this.matrizAdyacencia = nuevaMatriz;
    }



    /**
     * Contrae la matriz de adyacencia, copiando todos los elementos
     * a otra estructura a excepción de los que se van a omitir
     * 
     * @param indiceEliminado de la fila y columna que se deben omitir
     */
    protected void contraerMatrizAdyacencia(int indiceEliminado) {
        int nuevoTamanio = this.vertices.size();
        int[][] nuevaMatriz = new int[nuevoTamanio][nuevoTamanio];
        int filaNueva = 0;
        
        // Itera
        for (int indiceFila = 0; indiceFila < this.matrizAdyacencia.length; indiceFila++) {
            
            // Omite la fila del vértice eliminado
            if (indiceFila == indiceEliminado) {
                continue;
            }

            // Columna auxiliar para no desfasar la matriz
            int columnaNueva = 0;
            for (int indiceColumna = 0; indiceColumna < this.matrizAdyacencia.length; indiceColumna++) {
                
                // Omite la columna del vértice eliminado
                if (indiceColumna == indiceEliminado) {
                    continue;
                }
                
                // Copia sin problemas
                nuevaMatriz[filaNueva][columnaNueva] = this.matrizAdyacencia[indiceFila][indiceColumna];
                columnaNueva++;
            }
            filaNueva++;
        }

        // Reasigna la referencia interna a la nueva matriz redimensionada
        this.matrizAdyacencia = nuevaMatriz;
    }
    
    
    
    /**
     * Cuenta las celdas distintas de cero de la fila de un vértice
     * 
     * @param indice del vértice a consultar
     * 
     * @return cantidad de aristas que salen del vértice
     */
    protected int contarAristasSalientes(int indice) {
        // Inicia el contador de aristas
        int cantidad = 0;

        // Recorre la fila del vértice sumando las conexiones existentes
        for (int j = 0; j < this.matrizAdyacencia.length; j++) {
            if (this.matrizAdyacencia[indice][j] != 0.0) {
                cantidad++;
            }
        }

        // Regresa el total encontrado
        return cantidad;
    }
    
    
    
    /**
     * Cuenta las celdas distintas de cero de la columna de un vértice
     * 
     * @param indice del vértice a consultar
     * 
     * @return cantidad de aristas que llegan al vértice
     */
    protected int contarAristasEntrantes(int indice) {
        // Recorre la columna del vértice sumando las conexiones existentes
        int cantidad = 0;
        for (int i = 0; i < this.matrizAdyacencia.length; i++) {
            if (this.matrizAdyacencia[i][indice] != 0.0) {
                cantidad++;
            }
        }
        return cantidad;
    }
    
    
    
    /**
     * Verifica que un vértice exista en el grafo, mostrando un
     * mensaje de error en consola en caso contrario
     * 
     * @param vertice a validar
     * 
     * @return true si el vértice existe en el grafo, false en caso contrario
     */
    protected boolean verticeValido(Vertice<T> vertice) {
        return existeVertice(vertice);
    }

    
    
    /**
     * Verifica si un vértice tiene una arista hacia sí mismo
     * 
     * @param indice del vértice a consultar
     * 
     * @return true si existe un lazo, false en caso contrario
     */
    protected boolean tieneLazo(int indice) {
        return this.matrizAdyacencia[indice][indice] != 0.0;
    }
    
    
    
    /** Lanza una excepción si el grafo no es ponderado */
    protected void excepcionNoPonderado() {
        if (!esPonderado()) {
            throw new GrafoException(NO_PONDERADO);
        }
    }


    
    // MÉTODOS ABSTRACTOS
    /**
     * Escribe el peso de una arista en las celdas de la matriz que le
     * corresponden según el tipo de grafo, usando 0.0 para borrarla
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     * @param peso a escribir en la matriz
     */
    protected abstract void escribirPesoArista(Vertice<T> origen, Vertice<T> destino, int peso);

    
    
    /**
     * Cuenta las aristas que se pierden al eliminar un vértice
     * 
     * @param indice del vértice a eliminar
     * 
     * @return cantidad de aristas incidentes al vértice
     */
    protected abstract int contarAristasIncidentes(int indice);
    
    
    
    // MÉTODOS AUXILIARES
    /** Actualiza el índice de cada vértice según su posición en la lista */
    private void reasignarIndices() {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).setIndice(i);
        }
    }

    
    
    /**
     * Verifica que un vértice no sea nulo y pertenezca al grafo
     * 
     * @param vertice a consultar
     * 
     * @return true si el vértice existe en el grafo, false en caso contrario
     */
    private boolean existeVertice(Vertice<T> vertice) {
        return vertice != null && this.vertices.contains(vertice);
    }
    
    
    
    /**
     * Verifica que dos vértices existan en el grafo, mostrando un
     * mensaje de error en consola en caso contrario
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     * 
     * @return true si ambos vértices existen en el grafo, false en caso contrario
     */
    private boolean verticesValidos(Vertice<T> origen, Vertice<T> destino) {
        return !(!existeVertice(origen) || !existeVertice(destino));
    }

    
    
    /**
     * Verifica si la matriz de adyacencia tiene una conexión de origen a destino, 
     * asumiendo que ambos vértices ya fueron validados
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     * 
     * @return true si la arista existe, false en caso contrario
     */
    private boolean existeArista(Vertice<T> origen, Vertice<T> destino) {
        int fila = origen.getIndice();
        int columna = destino.getIndice();
        return this.matrizAdyacencia[fila][columna] != 0.0;
    }



    /**
     * Verifica que ambos vértices existan en el grafo y que estén conectados 
     * por una arista, mostrando un mensaje de error en consola en caso contrario
     * 
     * @param origen vértice de origen
     * @param destino vértice de destino
     * 
     * @return true si la arista existe, false en caso contrario
     */
    private boolean aristaValida(Vertice<T> origen, Vertice<T> destino) {
        // Verifica que ambos vértices pertenezcan al grafo
        if (!verticesValidos(origen, destino)) {
            return false;
        }

        // Verifica que exista una conexión entre ambos
        if (!existeArista(origen, destino)) {
            System.out.println(ARISTA_INEXISTENTE);
            return false;
        }
        return true;
    }
    
    
    
    /**
     * Lanza una excepción si el peso es cero, ya que una celda
     * en cero representa la ausencia de arista
     * 
     * @param peso a validar
     */
    private void validarPeso(int peso) {
        if (peso == 0) {
            throw new GrafoException(PESO_CERO);
        }
    }

    
    
    /**
     * Determina el peso que se almacenará en la matriz, asignando 1.0 si
     * el grafo no es ponderado o validando el peso recibido en caso contrario
     * 
     * @param peso recibido para la arista
     * 
     * @return peso que será almacenado en la matriz
     */
    private int calcularPesoAplicado(int peso) {
        if (this.esPonderado) {
            validarPeso(peso);
            return peso;
        }
        return 1;
    }
    
    
    
    /**
     * Construye la representación de una lista de nodos como conjunto
     * 
     * @param nodos a representar
     * 
     * @return cadena con los datos de los nodos entre llaves
     */
    private String formatearNodos(List<Vertice<T>> nodos) {
        StringJoiner conjunto = new StringJoiner(", ", "{", "}");
        for (Vertice<T> nodo : nodos) {
            conjunto.add("[" + nodo.getDato() + "]");
        }
        return conjunto.toString();
    }

    
    
    /** Imprime el encabezado con las columnas de la matriz de adyacencia */
    private void imprimirEncabezadoMatriz() {
        System.out.print("\t");
        for (Vertice<T> v : this.vertices) {
            System.out.print("[" + v.getDato() + "]\t");
        }
        System.out.println();
    }

    
    
    /**
     * Imprime una fila de la matriz de adyacencia precedida
     * por el dato de su vértice
     * 
     * @param fila a imprimir
     */
    private void imprimirFilaMatriz(int fila) {
        System.out.print("[" + this.vertices.get(fila).getDato() + "]\t");
        for (int j = 0; j < this.matrizAdyacencia[fila].length; j++) {
            System.out.print(this.matrizAdyacencia[fila][j] + "\t");
        }
        System.out.println();
    }
    
    

    // IMPLEMENTACIÓN DE IGRAFO
    public List<Vertice<T>> obtenerVertices() {
        return this.vertices;
    }
    
    
    @Override
    public void insertarVertice(Vertice<T> vertice) {
        if (vertice == null || this.vertices.contains(vertice)) {
            return;
        }
        vertice.setIndice(this.vertices.size());
        this.vertices.add(vertice);
        expandirMatrizAdyacencia();
    }



    @Override
    public void eliminarVertice(Vertice<T> vertice) {
        // Ignora el vértice si no pertenece al grafo
        if (!existeVertice(vertice)) {
            return;
        }

        // Descuenta del contador las aristas que desaparecen junto con el vértice
        int indiceEliminar = vertice.getIndice();
        this.numAristas -= contarAristasIncidentes(indiceEliminar);

        // Remueve el vértice y actualiza los índices de los nodos restantes
        this.vertices.remove(vertice);
        reasignarIndices();

        // Remueve la fila y columna correspondientes de la matriz de adyacencia
        contraerMatrizAdyacencia(indiceEliminar);
    }



    @Override
    public void insertarArista(Vertice<T> origen, Vertice<T> destino) {
        insertarArista(origen, destino, 1);
    }


    
    @Override
    public void insertarArista(Vertice<T> origen, Vertice<T> destino, int peso) {
        // Se asegura de que ambos vértices existan en la lista del grafo
        if (!verticesValidos(origen, destino)) {
            return;
        }

        // Si el grafo no es ponderado, fuerza la arista a 1.0
        int pesoAplicado = calcularPesoAplicado(peso);

        // Si la celda estaba en 0.0, es una nueva arista; incrementa el contador
        if (!existeArista(origen, destino)) {
            this.numAristas++;
        }

        // Escribe la arista en la matriz según el tipo de grafo
        escribirPesoArista(origen, destino, pesoAplicado);
    }



    @Override
    public void eliminarArista(Vertice<T> origen, Vertice<T> destino) {
        if (aristaValida(origen, destino)) {
            escribirPesoArista(origen, destino, 0);
            this.numAristas--;
        }
    }
    
    
    @Override
    public int obtenerPonderacion(Vertice<T> origen, Vertice<T> destino) {
        int i = origen.getIndice();
        int j = destino.getIndice();
        return this.matrizAdyacencia[i][j];
    }

    
    
    @Override
    public void establecerPonderacionArista(Vertice<T> origen, Vertice<T> destino, int nuevaPonderacion) {
        excepcionNoPonderado();
        validarPeso(nuevaPonderacion);
        if (aristaValida(origen, destino)) {
            escribirPesoArista(origen, destino, nuevaPonderacion);
        }
    }

    
    
    @Override
    public void imprimirAdyacentes(Vertice<T> vertice) {
        if (verticeValido(vertice)) {
            List<Vertice<T>> adyacentes = obtenerAdyacentes(vertice);
            System.out.println("Adyacentes de [" + vertice.getDato() + "]: " + formatearNodos(adyacentes));
        }
    }



    @Override
    public List<Vertice<T>> obtenerAdyacentes(Vertice<T> vertice) {
        List<Vertice<T>> adyacentes = new ArrayList<>();

        // Recorre la fila del vértice en la matriz
        if (existeVertice(vertice)) {
            int fila = vertice.getIndice();
            for (int j = 0; j < this.matrizAdyacencia[fila].length; j++) {
                
                // Si no es 0, entonces sí hay un valor
                if (this.matrizAdyacencia[fila][j] != 0.0) {
                    adyacentes.add(this.vertices.get(j));
                }
            }
        }
        return adyacentes;
    }
    
    @Override
    public int gradoEntrada(Vertice<T> vertice) {
        if (!existeVertice(vertice)) return 0;
        
        // Contador
        int contadorEntrada = 0;
        
        // Columna donde trabajar
        int columna = vertice.getIndice();

        // Recorre la matriz, solo basándose en la columna del índice y revisando cualquier fila adyacente
        for (int i = 0; i < this.matrizAdyacencia.length; i++) {
            if (this.matrizAdyacencia[i][columna] != 0.0) {
                contadorEntrada++;
            } 
        }
        return contadorEntrada;
    }
    
    @Override
    public int gradoSalida(Vertice<T> vertice) {
        if (!existeVertice(vertice)) return 0;
        return obtenerAdyacentes(vertice).size();
    }
    
    @Override
    public void imprimirMatrizAdyacencia() {
        // Evita imprimir una matriz sin datos
        if (this.vertices.isEmpty()) {
            System.out.println("El grafo está vacío.");
            return;
        }

        // Imprime el encabezado con las columnas definidas
        imprimirEncabezadoMatriz();

        // Imprime las filas de la matriz
        for (int i = 0; i < this.matrizAdyacencia.length; i++) {
            imprimirFilaMatriz(i);
        }
    }
    
    @Override
    public int obtenerNumeroVertices() {
        return this.vertices.size();
    }

    @Override
    public int obtenerNumeroAristas() {
        return this.numAristas;
    }

    @Override
    public boolean esPonderado() {
        return this.esPonderado;
    }

    @Override
    public boolean esDirigido() {
        return false;
    }
}
