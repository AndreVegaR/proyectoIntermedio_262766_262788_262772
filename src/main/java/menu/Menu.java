package menu;

import algoritmos.Algoritmos;
import clases.GrafoDirigido;
import clases.GrafoNoDirigido;
import clases.Vertice;
import auxiliares.GrafoException;
import auxiliares.IGrafo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Vista y controlador del sistema que maneja el bucle principal y la
 * navegación entre los menús, administra el único grafo que puede existir a
 * la vez e imprime en la terminal todos los menús, solicitudes de datos y
 * mensajes
 * 
 * @author Andre
 */
public class Menu {

    // Bandera que maneja el bucle
    private boolean ejecutando;
    private final Scanner scanner;

    // Único grafo del sistema, nulo mientras no se cree uno
    private IGrafo<String> grafo = null;
    
    // Clase que implementa diferente algoritmos en el grafo
    private Algoritmos algoritmos = new Algoritmos();

    // Textos de las opciones del menú principal, en el mismo orden en que se numeran
    private final String[] opciones = {
        "Crear grafo",
        "Insertar vértice",
        "Eliminar vértice",
        "Insertar arista",
        "Eliminar arista",
        "Imprimir conjunto de vértices adyacentes de un vértice",
        "Imprimir matriz de adyacencia del grafo",
        "Imprimir número de vértices",
        "Imprimir número de aristas",
        "[GRAFO PONDERADO] Establecer y actualizar ponderación de arista",
        "[GRAFO DIRIGIDO] Imprimir grado de entrada y salida de un vértice",
        "Aplicar algoritmo Búsqueda por anchura (BFS)",
        "Aplicar algoritmo Búsqueda por profundidad (DFS)",
        "[DAG] Aplicar algoritmo Orden topológico",
        "[DAG] Aplicar algoritmo Caminos más cortos",
        "[DAG] Aplicar algoritmo Caminos más largos",
        "Aplicar algoritmo Dijkstra",
        "Salir del sistema"
    };
    
    // Mapa que relaciona el número de cada opción con el método que la ejecuta
    private final Map<String, Runnable> metodos = new HashMap<>();

    /** Inicializa la bandera del bucle, el scanner y el mapa de opciones */
    public Menu() {
        ejecutando = true;
        scanner = new Scanner(System.in);
        inicializarMetodos();
    }
    
    /** Asocia el número de cada opción del menú principal con el método que la ejecuta */
    private void inicializarMetodos() {
        // Métodos en el mismo orden que el arreglo de opciones
        Runnable[] funciones = {
            () -> gestionarCrearGrafo(),
            () -> gestionarInsertarVertice(),
            () -> gestionarEliminarVertice(),
            () -> gestionarInsertarArista(),
            () -> gestionarEliminarArista(),
            () -> gestionarAdyacentes(),
            () -> gestionarMatriz(),
            () -> gestionarNumeroVertices(),
            () -> gestionarNumeroAristas(),
            () -> gestionarPonderacion(),
            () -> gestionarGrados(),
            () -> gestionarAlgoritmoBFS(),
            () -> gestionarAlgoritmoDFS(),
            () -> gestionarBusquedaTopologica(),
            () -> gestionarCaminosCortos(),
            () -> gestionarCaminosLargos(),
            () -> gestionarDijkstra(),
            () -> gestionarSalir()
        };

        // Registra cada método con su número de opción, donde 1 es el primero
        for (int i = 0; i < funciones.length; i++) {
            metodos.put(String.valueOf(i + 1), funciones[i]);
        }
    }
    
    
    
    /**
     * Arranca el ciclo principal del menú y lo mantiene mientras ejecutando sea
     * verdadero y el usuario no haya salido del sistema
     */
    public void iniciar() {
        // Da la bienvenida al usuario
        System.out.println("Bienvenido al Sistema de Grafos");

        // Mantiene el menú principal hasta que se detenga la ejecución
        while (ejecutando) {
            imprimirMenuPrincipal();
            System.out.print("Seleccione una opción: ");
            String opcion = leerLinea();

            // Mientras siga ejecutando el programa
            if (!ejecutando) {
                break;
            }

            // Busca el método según el número ingresado
            try {
                Runnable accion = metodos.get(opcion);
                if (accion == null) {
                    System.out.println("Opción inválida");
                } else {
                    accion.run();
                }
            } catch (GrafoException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    
    
    /**
     * Gestiona la creación de un grafo, pidiendo confirmación si ya existe
     * uno porque solo puede haber un grafo a la vez
     */
    private void gestionarCrearGrafo() {
        // Si ya existe un grafo, exige confirmar su eliminación y sustitución
        if (existeGrafo()) {
            System.out.println("Ya existe un grafo en el sistema");
            System.out.println("Si crea uno nuevo, el grafo actual y todos sus datos serán eliminados");
            if (!confirmar()) {
                System.out.println("Operación cancelada");
                return;
            }
        }

        // Mantiene el submenú hasta elegir un tipo de grafo o regresar
        IGrafo<String> nuevoGrafo = null;
        boolean regresar = false;
        while (nuevoGrafo == null && !regresar && ejecutando) {
            imprimirMenuGrafo();
            System.out.print("Seleccione una opción: ");

            switch (leerLinea()) {
                case "0" -> regresar = true;
                case "1" -> nuevoGrafo = new GrafoNoDirigido<>(false);
                case "2" -> nuevoGrafo = new GrafoNoDirigido<>(true);
                case "3" -> nuevoGrafo = new GrafoDirigido<>(false);
                case "4" -> nuevoGrafo = new GrafoDirigido<>(true);
                default -> System.out.println("Opción inválida");
            }
        }

        // Sustituye el grafo anterior solo si se eligió un tipo, y regresa al menú principal para no saltarse la confirmación
        if (nuevoGrafo != null) {
            this.grafo = nuevoGrafo;
            System.out.println("Grafo creado exitosamente");
        }
    }



    /** Gestiona la inserción de un vértice */
    private void gestionarInsertarVertice() throws GrafoException {
        // Verifica que exista un grafo
        if (!existeGrafo()) {
            throw new GrafoException("Primero debe crear un grafo (opción 1)");
        }

        // Pide el nombre del nuevo vértice
        System.out.print("Ingrese el nombre del nuevo vértice: ");
        String nombre = leerLinea();

        // Valida que el nombre no esté vacío ni repetido
        if (nombre.isEmpty()) {
            throw new GrafoException("Error: El nombre del vértice no puede estar vacío");
        }
        for (Vertice<String> vertice : this.grafo.obtenerVertices()) {
            if (vertice.getDato().equals(nombre)) {
                throw new GrafoException("Error: Ya existe un vértice con ese nombre");
            }
        }

        // Inserta el vértice
        this.grafo.insertarVertice(new Vertice<>(nombre));
        System.out.println("Vértice insertado exitosamente");
    }



    /** Gestiona la eliminación de un vértice */
    private void gestionarEliminarVertice() throws GrafoException {
        // Verifica que exista un grafo con vértices
        validarVertices();

        // Muestra los vértices y pide el número del que se eliminará
        imprimirVertices();
        System.out.print("Ingrese el número del vértice: ");

        // Elimina el vértice
        this.grafo.eliminarVertice(buscarVertice(leerLinea()));
        System.out.println("Vértice eliminado exitosamente");
    }
    
    
   
    
    



    /** Gestiona la inserción de una arista, pidiendo la ponderación solo si el grafo es ponderado */
    private void gestionarInsertarArista() throws GrafoException {
        // Verifica que exista un grafo con vértices
        validarVertices();

        // Muestra los vértices y pide los números de los que conecta la arista
        imprimirVertices();
        System.out.print("Ingrese el número del vértice de origen: ");
        String origen = leerLinea();
        System.out.print("Ingrese el número del vértice de destino: ");
        String destino = leerLinea();

        // Busca ambos vértices y evita sobrescribir una arista que ya existe
        Vertice<String> nodoOrigen = buscarVertice(origen);
        Vertice<String> nodoDestino = buscarVertice(destino);
        if (this.grafo.obtenerAdyacentes(nodoOrigen).contains(nodoDestino)) {
            throw new GrafoException("Error: La arista ya existe en el grafo");
        }

        // Inserta la arista, pidiendo la ponderación únicamente si el grafo es ponderado
        if (this.grafo.esPonderado()) {
            System.out.print("Ingrese la ponderación de la arista (distinta de cero): ");
            this.grafo.insertarArista(nodoOrigen, nodoDestino, parsearPeso(leerLinea()));
        } else {
            this.grafo.insertarArista(nodoOrigen, nodoDestino);
        }
        System.out.println("Arista insertada exitosamente");
    }
    
    

    /** Gestiona la eliminación de una arista */
    private void gestionarEliminarArista() throws GrafoException {
        // Verifica que exista un grafo con vértices
        validarVertices();

        // Muestra los vértices y pide los números de los que conecta la arista
        imprimirVertices();
        System.out.print("Ingrese el número del vértice de origen: ");
        String origen = leerLinea();
        System.out.print("Ingrese el número del vértice de destino: ");
        String destino = leerLinea();

        // Busca ambos vértices y verifica que la arista exista antes de eliminarla
        Vertice<String> nodoOrigen = buscarVertice(origen);
        Vertice<String> nodoDestino = buscarVertice(destino);
        if (!this.grafo.obtenerAdyacentes(nodoOrigen).contains(nodoDestino)) {
            throw new GrafoException("Error: La arista no existe en el grafo");
        }

        // Elimina la arista
        this.grafo.eliminarArista(nodoOrigen, nodoDestino);
        System.out.println("Arista eliminada exitosamente");
    }



    /** Gestiona la impresión de los vértices adyacentes a un vértice */
    private void gestionarAdyacentes() throws GrafoException {
        // Verifica que exista un grafo con vértices
        validarVertices();

        // Muestra los vértices y pide el número del que se consultará
        imprimirVertices();
        System.out.print("Ingrese el número del vértice: ");

        // Imprime sus adyacentes
        this.grafo.imprimirAdyacentes(buscarVertice(leerLinea()));
    }



    /** Gestiona la impresión de la matriz de adyacencia */
    private void gestionarMatriz() throws GrafoException {
        if (!existeGrafo()) {
            throw new GrafoException("Primero debe crear un grafo (opción 1)");
        }
        this.grafo.imprimirMatrizAdyacencia();
    }



    /** Gestiona la impresión del número de vértices */
    private void gestionarNumeroVertices() throws GrafoException {
        if (!existeGrafo()) {
            throw new GrafoException("Primero debe crear un grafo (opción 1)");
        }
        System.out.println("Número de vértices: " + this.grafo.obtenerNumeroVertices());
    }



    /** Gestiona la impresión del número de aristas */
    private void gestionarNumeroAristas() throws GrafoException {
        if (!existeGrafo()) {
            throw new GrafoException("Primero debe crear un grafo (opción 1)");
        }
        System.out.println("Número de aristas: " + this.grafo.obtenerNumeroAristas());
    }



    /** Gestiona el establecimiento o actualización de la ponderación de una arista */
    private void gestionarPonderacion() throws GrafoException {
        // Verifica que el grafo sea ponderado, y si no existe grafo lo avisa validarVertices
        if (existeGrafo() && !this.grafo.esPonderado()) {
            throw new GrafoException("Error: Esta opción solo está disponible en grafos ponderados");
        }
        validarVertices();

        // Muestra los vértices y pide los números de los que conecta la arista
        imprimirVertices();
        System.out.print("Ingrese el número del vértice de origen: ");
        String origen = leerLinea();
        System.out.print("Ingrese el número del vértice de destino: ");
        String destino = leerLinea();

        // Busca ambos vértices y verifica que la arista exista
        Vertice<String> nodoOrigen = buscarVertice(origen);
        Vertice<String> nodoDestino = buscarVertice(destino);
        if (!this.grafo.obtenerAdyacentes(nodoOrigen).contains(nodoDestino)) {
            throw new GrafoException("Error: La arista no existe en el grafo");
        }

        // Pide la nueva ponderación y la aplica
        System.out.print("Ingrese la ponderación de la arista (distinta de cero): ");
        this.grafo.establecerPonderacionArista(nodoOrigen, nodoDestino, parsearPeso(leerLinea()));
        System.out.println("Ponderación actualizada exitosamente");
    }



    /** Gestiona la impresión de los grados de entrada y salida de un vértice */
    private void gestionarGrados() throws GrafoException {
        // Verifica que el grafo sea dirigido, y si no existe grafo lo avisa validarVertices
        if (existeGrafo() && !this.grafo.esDirigido()) {
            throw new GrafoException("Error: Esta opción solo está disponible en grafos dirigidos");
        }
        validarVertices();

        // Muestra los vértices y pide el número del que se consultará
        imprimirVertices();
        System.out.print("Ingrese el número del vértice: ");

        // Imprime sus grados
        this.grafo.imprimirGradosVertice(buscarVertice(leerLinea()));
    }
    
    
    
    /** Gestiona la ejecución del algoritmo Búsqueda por anchura */
    private void gestionarAlgoritmoBFS() {
        if(!existeGrafo() || grafo.obtenerVertices().isEmpty()) {
            throw new GrafoException("El grafo está vacío");
        }

        // Pide el número del vértice raíz para el algoritmo
        imprimirVertices();
        System.out.println("Ingrese el número del vértice raíz");
        String vertice = leerLinea();
        
        // Ejecuta el algoritmo
        Vertice<String> raiz = buscarVertice(vertice);
        algoritmos.busquedaAnchura(grafo, raiz);
        
        // Obtiene la lista de vértices y la ordena por distancia (de menor a mayor)
        List<Vertice<String>> verticesOrdenados = new ArrayList<>(grafo.obtenerVertices());
        verticesOrdenados.sort(Comparator.comparingInt(Vertice::getDistancia));
        
        // Imprime los resultados del algoritmo en orden
        System.out.println("Vértices por capas (-1 significa no explorado):");
        for (Vertice<String> v: verticesOrdenados) {
            System.out.println(v.getDato() + ": distancia de " + v.getDistancia());
        }
    }
    
    
    
    /** Gestiona la ejecución del algoritmo Búsqueda por profundidad */
    private void gestionarAlgoritmoDFS() {
        if(!existeGrafo() || grafo.obtenerVertices().isEmpty()) {
            throw new GrafoException("El grafo está vacío");
        }

        // Ejecuta el algoritmo
        algoritmos.busquedaProfundidad(grafo);
        
        // Obtiene la lista de vértices y la ordena por tiempo de descubrimiento (de menor a mayor)
        List<Vertice<String>> verticesOrdenados = new ArrayList<>(grafo.obtenerVertices());
        verticesOrdenados.sort(Comparator.comparingInt(Vertice::getDescubrimiento));
        
        // Imprime el descubrimiento y finalización de cada vértice (n / m) en orden
        System.out.println("Vértices por orden de descubrimiento:");
        for (Vertice<String> v: verticesOrdenados) {
            String tiempoVertice = v.getDescubrimiento() + " / " + v.getFinalizacion();
            System.out.println(v.getDato() + ": " + tiempoVertice);
        }
    }

    
    
    /** Gestiona la implementación de la búsqueda topológica en el grafo*/
    private void gestionarBusquedaTopologica() {
        if(!existeGrafo() || grafo.obtenerVertices().isEmpty()) {
            throw new GrafoException("El grafo está vacío");
        }
        imprimirListaVertices(algoritmos.ordenamientoTopologico(grafo));
    }
    
    
    
    /** Gestiona la ejecución de caminos más cortos */
    public void gestionarCaminosCortos() {
        if(!existeGrafo() || grafo.obtenerVertices().isEmpty()) {
            throw new GrafoException("El grafo está vacío");
        }
        
        // Muestra los vértices y pide el vértice raíz
        imprimirVertices();
        System.out.print("Ingrese el número del vértice raíz: ");
        Vertice<String> raiz = buscarVertice(leerLinea());

        // Ejecuta el algoritmo
        algoritmos.caminosCortos(grafo, raiz);
        mostrarResultadosDistancia(grafo, raiz, false);
    }
    
    
    
    /** Gestiona la ejecución de caminos más largos */
    public void gestionarCaminosLargos() {
        if(!existeGrafo() || grafo.obtenerVertices().isEmpty()) {
            throw new GrafoException("El grafo está vacío");
        }
        
        // Muestra los vértices y pide el vértice raíz
        imprimirVertices();
        System.out.print("Ingrese el número del vértice raíz: ");
        Vertice<String> raiz = buscarVertice(leerLinea());

        // Ejecuta el algoritmo
        algoritmos.caminosLargos(grafo, raiz);
        mostrarResultadosDistancia(grafo, raiz, true);
    }
    
    
    
    /** Gestiona la ejecución de Dijkstra */
    public void gestionarDijkstra() {
        if(!existeGrafo() || grafo.obtenerVertices().isEmpty()) {
            throw new GrafoException("El grafo está vacío");
        }
        
        // Muestra los vértices y pide el vértice raíz
        imprimirVertices();
        System.out.print("Ingrese el número del vértice raíz: ");
        Vertice<String> raiz = buscarVertice(leerLinea());

        // Ejecuta el algoritmo
        algoritmos.dijkstra(grafo, raiz);
        mostrarResultadosDistancia(grafo, raiz, false);
    }
    
    
    
    /** Gestiona la salida del programa */
    private void gestionarSalir() {
        if (confirmar()) {
            System.out.println("Saliendo del sistema...");
            ejecutando = false;
        }
    }
    
    
    
    // ==== MÉTODOS AUXILIARES ===
    /**
     * Verifica si ya se creó un grafo
     * 
     * @return true si existe un grafo, false de lo contrario
     */
    private boolean existeGrafo() {
        return this.grafo != null;
    }



    /**
     * Verifica que exista un grafo con al menos un vértice, lanzando una
     * excepción que avisa al usuario en caso contrario
     */
    private void validarVertices() throws GrafoException {
        if (!existeGrafo()) {
            throw new GrafoException("Primero debe crear un grafo (opción 1)");
        }
        if (this.grafo.obtenerNumeroVertices() == 0) {
            throw new GrafoException("El grafo no tiene vértices, inserte alguno primero (opción 2)");
        }
    }



    /**
     * Busca un vértice a partir del número que el usuario eligió de la lista,
     * lanzando una excepción si el número no corresponde a ningún vértice
     * 
     * @param numero escrito por el usuario, donde 1 es el primer vértice
     * 
     * @return vértice que corresponde al número
     */
    private Vertice<String> buscarVertice(String numero) throws GrafoException {
        try {
            return this.grafo.obtenerVertices().get(Integer.parseInt(numero) - 1);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new GrafoException("Error: Debe ingresar el número de uno de los vértices de la lista");
        }
    }


    
    // ==== MÉTODOS DE IMPRESIÓN ===
    /**
     * Convierte el texto escrito por el usuario en una ponderación válida,
     * lanzando una excepción si no es un número finito distinto de cero
     * 
     * @param texto a convertir
     * 
     * @return ponderación obtenida
     */
    private int parsearPeso(String texto) throws GrafoException {
        try {
            // Una celda en cero significa ausencia de arista, y no se aceptan valores no finitos
            int peso = Integer.parseInt(texto);
            if (peso != 0.0 && Double.isFinite(peso)) {
                return peso;
            }
        } catch (NumberFormatException e) {

        }
        throw new GrafoException("Error: La ponderación debe ser un número distinto de cero");
    }
    
    
    
    /**
     * Pide al usuario una confirmación de sí o no, repitiendo la pregunta
     * hasta recibir una respuesta válida
     * 
     * @return true si el usuario confirmó, false si lo rechazó o ya no hay entrada
     */
    private boolean confirmar() {
        while (ejecutando) {
            System.out.print("¿Desea continuar? (s/n): ");
            String respuesta = leerLinea().toLowerCase();

            // Si ya no hay entrada, se toma como un rechazo
            if (!ejecutando) {
                return false;
            }

            // Interpreta la respuesta
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Opción inválida");
        }
        return false;
    }



    /**
     * Lee una línea escrita por el usuario, y si ya no hay entrada detiene el
     * ciclo del menú
     *
     * @return texto leído sin espacios en los extremos, o 0 si ya no hay
     * entrada
     */
    private String leerLinea() {
        if (!scanner.hasNextLine()) {
            ejecutando = false;
            return "0";
        }
        return scanner.nextLine().trim();
    }
    
    
    
    /** Imprime las opciones del menú principal */
    private void imprimirMenuPrincipal() {
        System.out.println();
        
        // Imprime las secciones según la posición de la opción
        int longitud = opciones.length-1;
        for (int i = 0; i <= longitud; i++) {
            switch (i) {
                case 0  -> System.out.println("====GRAFO====");
                case 1  -> System.out.println("====VÉRTICES====");
                case 3  -> System.out.println("====ARISTAS====");
                case 5  -> System.out.println("====IMPRESIONES====");
                case 9  -> System.out.println("====ESPECIALES====");
                case 11 -> System.out.println("====ALGORITMOS DE VISITA====");
                case 13 -> System.out.println("====ALGORITMOS DE ORDENAMIENTO====");
                case 14 -> System.out.println("====ALGORITMOS DE DISTANCIA====");
                default -> {
                    if (i == longitud) {
                        System.out.println("====SISTEMA====");
                    }
                }
            }
            System.out.println((i + 1) + ". " + opciones[i]);
        }
    }

    

    /** Imprime las opciones del submenú para elegir el tipo de grafo */
    private void imprimirMenuGrafo() {
        System.out.println();
        System.out.println("1. Grafo no dirigido no ponderado");
        System.out.println("2. Grafo no dirigido ponderado");
        System.out.println("3. Grafo dirigido no ponderado");
        System.out.println("4. Grafo dirigido ponderado");
        System.out.println("0. Regresar");
    }



    /**
     * Imprime los vértices del grafo numerados desde 1, que es el número
     * que el usuario debe ingresar para elegirlos
     */
    private void imprimirVertices() {
        // Imprime el encabezado de la lista
        System.out.println();
        System.out.println("Vértices del grafo:");
        imprimirListaVertices(grafo.obtenerVertices());
        
    }
    
    
    
    /**
     * Imprime una lista de vértices numerados desde 1
     * 
     * @param vertices a imprimir
     */
    private void imprimirListaVertices(List<Vertice<String>> vertices) {
        // Numera e imprime cada vértice en el orden de la lista
        int numero = 1;
        for (Vertice<String> vertice: vertices) {
            System.out.println(numero + ". " + vertice.getDato());
            numero++;
        }
    }
    
    
    private void mostrarResultadosDistancia(IGrafo<String> grafo, Vertice<String> origen, boolean esCaminoLargo) {
        int infinito = esCaminoLargo ? -1000000 : 1000000;

        System.out.println("\n=== RESULTADOS DESDE EL ORIGEN: " + origen + " ===");
        System.out.printf("%-12s | %-12s | %s%n", "Vértice", "Distancia", "Camino");
        System.out.println("--------------------------------------------------");

        for (Vertice<String> v: grafo.obtenerVertices()) {
            if (v.getDistancia() == infinito) {
                System.out.printf("%-12s | %-12s | %s%n", v, "X", "Inalcanzable");
            } else {
                String ruta = reconstruirCamino(v);
                System.out.printf("%-12s | %-12d | %s%n", v, v.getDistancia(), ruta);
            }
        }
        System.out.println();
    }

    private String reconstruirCamino(Vertice<String> destino) {
        List<String> pasos = new ArrayList<>();
        Vertice<String> actual = destino;

        while (actual != null) {
            pasos.add(actual.toString());
            actual = actual.getPadre();
        }

        Collections.reverse(pasos);
        return String.join(" -> ", pasos);
    }
}