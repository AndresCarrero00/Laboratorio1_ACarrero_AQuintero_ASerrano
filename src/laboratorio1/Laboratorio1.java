/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package laboratorio1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Laboratorio1 {
    // Constante para la ruta base del directorio

    public static final String RUTA_BASE = "C:\\Users\\carre\\OneDrive\\Escritorio\\laboratorio1_2\\data";

    public static class NodoAVL {

        public Imagen imagen;
        public NodoAVL hijoIzquierdo;
        public NodoAVL hijoDerecho;
        public int altura;

        public NodoAVL(Imagen imagen) {
            this.imagen = imagen;
            this.hijoIzquierdo = null;
            this.hijoDerecho = null;
            this.altura = 1;
        }

        public Imagen getImagen() {
            return imagen;
        }

        public void setImagen(Imagen imagen) {
            this.imagen = imagen;
        }

        public NodoAVL getHijoIzquierdo() {
            return hijoIzquierdo;
        }

        public void setHijoIzquierdo(NodoAVL hijoIzquierdo) {
            this.hijoIzquierdo = hijoIzquierdo;
        }

        public NodoAVL getHijoDerecho() {
            return hijoDerecho;
        }

        public void setHijoDerecho(NodoAVL hijoDerecho) {
            this.hijoDerecho = hijoDerecho;
        }

        public int getAltura() {
            return altura;
        }

        public void setAltura(int altura) {
            this.altura = altura;
        }
    }

    public static class Imagen {

        private String nombre;
        private long peso;
        private String tipo;
        private String subcarpeta;

        public Imagen(String nombre, String subcarpeta) {
            this.nombre = nombre;
            this.subcarpeta = subcarpeta;
            this.tipo = obtenerTipoArchivo(nombre);
            this.peso = calcularPeso(nombre, subcarpeta);
        }

        public String getNombre() {
            return nombre;
        }

        public long getPeso() {
            return peso;
        }

        public String getTipo() {
            return tipo;
        }

        public String getSubcarpeta() {
            return subcarpeta;
        }

        private static String obtenerTipoArchivo(String nombre) {
            int indicePunto = nombre.lastIndexOf('.');
            return (indicePunto != -1 && indicePunto < nombre.length() - 1) ? nombre.substring(indicePunto + 1) : "TipoDesconocido";
        }

        public static long calcularPeso(String nombreImagen, String subcarpeta) {
            String rutaImagen = Laboratorio1.RUTA_BASE + File.separator + subcarpeta + File.separator + nombreImagen;
            File imagen = new File(rutaImagen);
            try {
                Scanner scanner = new Scanner(imagen);
                long peso = imagen.length(); // Devuelve el tamaño en bytes del archivo
                scanner.close(); // Cerrar el recurso del Scanner
                return peso;
            } catch (FileNotFoundException e) {
                System.out.println("La imagen '" + nombreImagen + "' no existe en la carpeta especificada.");
                return 0; // Retornar 0 si el archivo no existe
            }
        }
    }

    public static class ArbolAVL {

        public NodoAVL raiz;

        public ArbolAVL() {
            this.raiz = null;
        }

        public NodoAVL getRaiz() {
            return raiz;
        }

        public void mostrarFactorEquilibrio(NodoAVL nodo, String prefijo, boolean esIzquierdo) {
            if (nodo != null) {
                System.out.print(prefijo);
                System.out.print(esIzquierdo ? "|-- " : "\\-- ");
                System.out.println("Nombre: " + nodo.imagen.getNombre() + ", Factor de Equilibrio: " + obtenerFactorEquilibrio(nodo));

                // Preparar el prefijo para los hijos
                String nuevoPrefijo = prefijo + (esIzquierdo ? "|   " : "    ");

                // Si hay un hijo izquierdo, mostrar el subárbol izquierdo
                if (nodo.hijoIzquierdo != null) {
                    mostrarFactorEquilibrio(nodo.hijoIzquierdo, nuevoPrefijo, true);
                }

                // Si hay un hijo derecho, mostrar el subárbol derecho
                if (nodo.hijoDerecho != null) {
                    mostrarFactorEquilibrio(nodo.hijoDerecho, nuevoPrefijo, false);
                }
            }
        }

        public static Imagen seleccionarImagen(String nombreCarpeta) {
            Scanner scanner = new Scanner(System.in);
            String rutaDirectorio = Laboratorio1.RUTA_BASE + "\\"+nombreCarpeta;
            File directorio = new File(rutaDirectorio);

            if (directorio.isDirectory()) {
                File[] archivos = directorio.listFiles();
                System.out.println("Archivos en la carpeta '" + nombreCarpeta + "':");
                for (File archivo : archivos) {
                    System.out.println("- " + archivo.getName());
                }

                System.out.print("Ingrese el nombre de la imagen que desea seleccionar: ");
                String nombreImagenSeleccionada = scanner.nextLine();

                boolean imagenEncontrada = false;
                for (File archivo : archivos) {
                    if (archivo.getName().equalsIgnoreCase(nombreImagenSeleccionada)) {
                        Imagen imagen = new Imagen(archivo.getName(), nombreCarpeta);
                        imagenEncontrada = true;
                        return imagen;
                    }
                }

                if (!imagenEncontrada) {
                    System.out.println("La imagen '" + nombreImagenSeleccionada + "' no existe en la carpeta especificada.");
                }
                return null;
            } else {
                System.out.println("La carpeta especificada no existe.");
                return null;
            }
        }

        public NodoAVL insertar(NodoAVL nodo, Imagen imagen) {
            if (nodo == null) {
                return new NodoAVL(imagen);
            }

            int comparacion = imagen.getNombre().compareToIgnoreCase(nodo.imagen.getNombre());

            if (comparacion < 0) {
                nodo.hijoIzquierdo = insertar(nodo.hijoIzquierdo, imagen);
            } else if (comparacion > 0) {
                nodo.hijoDerecho = insertar(nodo.hijoDerecho, imagen);
            } else {
                // Si la imagen ya existe en el árbol, podrías manejarlo de alguna manera,
                // aquí simplemente imprimo un mensaje y retorno el nodo actual sin hacer nada.
                System.out.println("La imagen '" + imagen.getNombre() + "' ya existe en el árbol AVL.");
                return nodo;
            }

            // Validar si la imagen existe antes de agregarla al árbol
            if (imagen.getPeso() == 0) {
                // No se agrega la imagen al árbol si no existe
                return nodo;
            }

            nodo.altura = 1 + Math.max(altura(nodo.hijoIzquierdo), altura(nodo.hijoDerecho));

            int factorEquilibrio = obtenerFactorEquilibrio(nodo);

            if (factorEquilibrio > 1 && imagen.getNombre().compareToIgnoreCase(nodo.hijoIzquierdo.imagen.getNombre()) < 0) {
                System.out.println("Desequilibrio detectado en la imagen: " + imagen.getNombre() + ". Se realiza rotación simple a la derecha.");
                return rotacionDerecha(nodo, imagen);
            }

            if (factorEquilibrio < -1 && imagen.getNombre().compareToIgnoreCase(nodo.hijoDerecho.imagen.getNombre()) > 0) {
                System.out.println("Desequilibrio detectado en la imagen: " + imagen.getNombre() + ". Se realiza rotación simple a la izquierda.");
                return rotacionIzquierda(nodo, imagen);
            }

            if (factorEquilibrio > 1 && imagen.getNombre().compareToIgnoreCase(nodo.hijoIzquierdo.imagen.getNombre()) > 0) {
                System.out.println("Desequilibrio detectado en la imagen: " + imagen.getNombre() + ". Se realiza rotación doble: izquierda-derecha.");
                nodo.hijoIzquierdo = rotacionIzquierda(nodo.hijoIzquierdo, imagen);
                return rotacionDerecha(nodo, imagen);
            }

            if (factorEquilibrio < -1 && imagen.getNombre().compareToIgnoreCase(nodo.hijoDerecho.imagen.getNombre()) < 0) {
                System.out.println("Desequilibrio detectado en la imagen: " + imagen.getNombre() + ". Se realiza rotación doble: derecha-izquierda.");
                nodo.hijoDerecho = rotacionDerecha(nodo.hijoDerecho, imagen);
                return rotacionIzquierda(nodo, imagen);
            }

            return nodo;
        }

        public NodoAVL eliminar(NodoAVL nodo, String nombre) {
            if (nodo == null) {
                return null;
            }

            // Buscar el nodo a eliminar
            if (nombre.compareToIgnoreCase(nodo.imagen.getNombre()) < 0) {
                nodo.hijoIzquierdo = eliminar(nodo.hijoIzquierdo, nombre);
            } else if (nombre.compareToIgnoreCase(nodo.imagen.getNombre()) > 0) {
                nodo.hijoDerecho = eliminar(nodo.hijoDerecho, nombre);
            } else {
                // Nodo encontrado, verificar los casos de eliminación
                if (nodo.hijoIzquierdo == null || nodo.hijoDerecho == null) {
                    // Caso 1: Nodo rama o nodo padre con un hijo
                    NodoAVL temp = null;
                    if (nodo.hijoIzquierdo == null) {
                        temp = nodo.hijoDerecho;
                    } else {
                        temp = nodo.hijoIzquierdo;
                    }

                    // Si el nodo es una hoja
                    if (temp == null) {
                        temp = nodo;
                        nodo = null;
                    } else {
                        // Si el nodo tiene un hijo
                        nodo = temp;
                    }
                } else {
                    // Caso 2: Nodo padre con dos hijos
                    // Encontrar el nodo sucesor en el subárbol derecho
                    NodoAVL temp = encontrarNodoSucesor(nodo.hijoDerecho);

                    // Copiar los datos del nodo sucesor al nodo actual
                    nodo.imagen = temp.imagen;

                    // Eliminar el nodo sucesor
                    nodo.hijoDerecho = eliminar(nodo.hijoDerecho, temp.imagen.getNombre());
                }
            }

            // Si el árbol tenía solo un nodo
            if (nodo == null) {
                return nodo;
            }

            // Actualizar la altura del nodo actual
            nodo.altura = 1 + Math.max(altura(nodo.hijoIzquierdo), altura(nodo.hijoDerecho));

            // Obtener el factor de equilibrio del nodo actual
            int factorEquilibrio = obtenerFactorEquilibrio(nodo);

            // Rebalancear el árbol si es necesario
            // Caso 1: Rotación a la derecha
            if (factorEquilibrio > 1 && obtenerFactorEquilibrio(nodo.hijoIzquierdo) >= 0) {
                System.out.println("Desequilibrio detectado en la imagen: " + nodo.imagen.getNombre() + ". Se realiza rotación simple a la derecha.");
                return rotacionDerecha(nodo, nodo.imagen);
            }

// Caso 2: Rotación a la izquierda
            if (factorEquilibrio < -1 && obtenerFactorEquilibrio(nodo.hijoDerecho) <= 0) {
                System.out.println("Desequilibrio detectado en la imagen: " + nodo.imagen.getNombre() + ". Se realiza rotación simple a la izquierda.");
                return rotacionIzquierda(nodo, nodo.imagen);
            }

            // Caso 3: Rotación doble izquierda-derecha
            if (factorEquilibrio > 1 && obtenerFactorEquilibrio(nodo.hijoIzquierdo) < 0) {
                nodo.hijoIzquierdo = rotacionIzquierda(nodo.hijoIzquierdo, nodo.imagen);
                return rotacionDerecha(nodo, nodo.imagen);
            }

            // Caso 4: Rotación doble derecha-izquierda
            if (factorEquilibrio < -1 && obtenerFactorEquilibrio(nodo.hijoDerecho) > 0) {
                nodo.hijoDerecho = rotacionDerecha(nodo.hijoDerecho, nodo.imagen);
                return rotacionIzquierda(nodo, nodo.imagen);
            }

            return nodo;
        }

        public NodoAVL encontrarNodoSucesor(NodoAVL nodo) {
            NodoAVL actual = nodo;
            while (actual.hijoIzquierdo != null) {
                actual = actual.hijoIzquierdo;
            }
            return actual;
        }

        public NodoAVL buscar(NodoAVL nodo, String nombre) {
            if (nodo == null) {
                return null;
            }

            if (nombre.compareToIgnoreCase(nodo.imagen.getNombre()) == 0) {
                return nodo;
            } else if (nombre.compareToIgnoreCase(nodo.imagen.getNombre()) < 0) {
                return buscar(nodo.hijoIzquierdo, nombre);
            } else {
                return buscar(nodo.hijoDerecho, nombre);
            }
        }

        public List<Imagen> buscarPorCriterios(NodoAVL nodo, String tipo, long pesoMin, long pesoMax) {
            List<Imagen> resultado = new ArrayList<>();
            buscarPorCriteriosRecursivo(nodo, tipo, pesoMin, pesoMax, resultado);
            return resultado;
        }

        private void buscarPorCriteriosRecursivo(NodoAVL nodo, String tipo, long pesoMin, long pesoMax, List<Imagen> resultado) {
            if (nodo == null) {
                return;
            }

            // Verificar si la imagen cumple con los criterios de búsqueda
            if (nodo.imagen.getSubcarpeta().equalsIgnoreCase(tipo) && nodo.imagen.getPeso() >= pesoMin && nodo.imagen.getPeso() <= pesoMax) {
                resultado.add(nodo.imagen);
            }

            // Recursivamente buscar en los subárboles izquierdo y derecho
            buscarPorCriteriosRecursivo(nodo.hijoIzquierdo, tipo, pesoMin, pesoMax, resultado);
            buscarPorCriteriosRecursivo(nodo.hijoDerecho, tipo, pesoMin, pesoMax, resultado);
        }

        public void inorden(NodoAVL nodo) {
            if (nodo != null) {
                inorden(nodo.hijoIzquierdo);
                System.out.println("Nombre: " + nodo.imagen.getNombre() + ", Peso: " + nodo.imagen.getPeso() + " bytes, Tipo: " + nodo.imagen.getTipo());
                inorden(nodo.hijoDerecho);
            }
        }

        private NodoAVL rotacionDerecha(NodoAVL y, Imagen imagen) {
            NodoAVL x = y.hijoIzquierdo;
            NodoAVL T2 = x.hijoDerecho;

            x.hijoDerecho = y;
            y.hijoIzquierdo = T2;

            y.altura = Math.max(altura(y.hijoIzquierdo), altura(y.hijoDerecho)) + 1;
            x.altura = Math.max(altura(x.hijoIzquierdo), altura(x.hijoDerecho)) + 1;

            System.out.println("Rotación a la derecha realizada en la imagen: " + imagen.getNombre());
            return x;
        }

        private NodoAVL rotacionIzquierda(NodoAVL x, Imagen imagen) {
            NodoAVL y = x.hijoDerecho;
            NodoAVL T2 = y.hijoIzquierdo;

            y.hijoIzquierdo = x;
            x.hijoDerecho = T2;

            x.altura = Math.max(altura(x.hijoIzquierdo), altura(x.hijoDerecho)) + 1;
            y.altura = Math.max(altura(y.hijoIzquierdo), altura(y.hijoDerecho)) + 1;

            System.out.println("Rotación a la izquierda realizada en la imagen: " + imagen.getNombre());
            return y;
        }

        public static int obtenerFactorEquilibrio(NodoAVL nodo) {
            if (nodo == null) {
                return 0;
            }
            return altura(nodo.hijoIzquierdo) - altura(nodo.hijoDerecho);
        }

        public void mostrarArbolAVL(NodoAVL nodo, String prefijo, boolean esIzquierdo) {
            if (nodo != null) {
                System.out.print(prefijo);
                System.out.print(esIzquierdo ? "|-- " : "\\-- ");
                System.out.println("Nombre: " + nodo.imagen.getNombre());

                // Preparar el prefijo para los hijos
                String nuevoPrefijo = prefijo + (esIzquierdo ? "|   " : "    ");

                // Si hay un hijo izquierdo, mostrar el subárbol izquierdo
                if (nodo.hijoIzquierdo != null) {
                    mostrarArbolAVL(nodo.hijoIzquierdo, nuevoPrefijo, true);
                }

                // Si hay un hijo derecho, mostrar el subárbol derecho
                if (nodo.hijoDerecho != null) {
                    mostrarArbolAVL(nodo.hijoDerecho, nuevoPrefijo, false);
                }
            }
        }

        public void recorridoPorNiveles(NodoAVL nodo) {
            int altura = altura(nodo);
            for (int i = 1; i <= altura; i++) {
                mostrarNodosNivel(nodo, i);
            }
        }

        // Método auxiliar para mostrar los nodos en un nivel específico
        private void mostrarNodosNivel(NodoAVL nodo, int nivel) {
            if (nodo == null) {
                return;
            }
            if (nivel == 1) {
                System.out.println(nodo.imagen.getNombre());
            } else if (nivel > 1) {
                mostrarNodosNivel(nodo.hijoIzquierdo, nivel - 1);
                mostrarNodosNivel(nodo.hijoDerecho, nivel - 1);
            }
        }

        // Método para obtener el nivel de un nodo
        public int obtenerNivel(NodoAVL nodo, NodoAVL buscado, int nivel) {
            if (nodo == null) {
                return 0;
            }
            if (nodo == buscado) {
                return nivel;
            }
            int nivelIzquierdo = obtenerNivel(nodo.hijoIzquierdo, buscado, nivel + 1);
            if (nivelIzquierdo != 0) {
                return nivelIzquierdo;
            }
            return obtenerNivel(nodo.hijoDerecho, buscado, nivel + 1);
        }

        // Método para obtener el factor de balanceo (equilibrio) de un nodo
        // Método para encontrar el padre de un nodo
        public NodoAVL encontrarPadre(NodoAVL nodo, NodoAVL buscado) {
            if (nodo == null || nodo == buscado) {
                return null;
            }
            if (nodo.hijoIzquierdo == buscado || nodo.hijoDerecho == buscado) {
                return nodo;
            }
            NodoAVL izquierda = encontrarPadre(nodo.hijoIzquierdo, buscado);
            if (izquierda != null) {
                return izquierda;
            }
            return encontrarPadre(nodo.hijoDerecho, buscado);
        }

        // Método para encontrar el abuelo de un nodo
        public NodoAVL encontrarAbuelo(NodoAVL nodo, NodoAVL buscado) {
            if (nodo == null || nodo == buscado) {
                return null;
            }
            NodoAVL padre = encontrarPadre(raiz, buscado);
            if (padre != null) {
                return encontrarPadre(raiz, padre);
            }
            return null;
        }

        // Método para encontrar el tío de un nodo
        public NodoAVL encontrarTio(NodoAVL nodo) {
            NodoAVL padre = encontrarPadre(raiz, nodo);
            NodoAVL abuelo = encontrarAbuelo(raiz, nodo);

            if (padre != null && abuelo != null) {
                if (abuelo.hijoIzquierdo == padre) {
                    return abuelo.hijoDerecho;
                } else {
                    return abuelo.hijoIzquierdo;
                }
            }

            return null;
        }
    }

    public static void main(String[] args) {
        ArbolAVL arbol = new ArbolAVL();
        Scanner scanner = new Scanner(System.in);

        boolean continuar = true;
        do {
            System.out.print("Ingresa el nombre de la carpeta donde se encuentra la imagen: ");
            String nombreCarpeta = scanner.nextLine();
            Imagen imagenSeleccionada = ArbolAVL.seleccionarImagen(nombreCarpeta);
            if (imagenSeleccionada != null) {
                arbol.raiz = arbol.insertar(arbol.raiz, imagenSeleccionada);
                mostrarEstadoArbol(arbol, "Árbol AVL después de insertar la imagen '" + imagenSeleccionada.getNombre() + "':");

                System.out.print("¿Deseas insertar otra imagen? (s/n): ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s")) {
                    continuar = false;
                }
            } else {
                System.out.println("No se pudo seleccionar ninguna imagen de la carpeta especificada.");
                System.out.print("¿Deseas intentarlo nuevamente? (s/n): ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s")) {
                    continuar = false;
                }
            }
        } while (continuar);

        boolean salir = false;
        while (!salir) {
            System.out.println("\n¿Qué operación deseas realizar?");
            System.out.println("1. Eliminar un nodo");
            System.out.println("2. Buscar un nodo por nombre");
            System.out.println("3. Buscar nodos por criterios (tipo y peso)");
            System.out.println("4. Mostrar recorrido por niveles del árbol");
            System.out.println("5. Obtener información de un nodo");
            System.out.println("6. Salir");
            System.out.print("Ingresa el número de la opción deseada: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    System.out.print("Ingresa el nombre de la imagen a eliminar: ");
                    String nombreEliminar = scanner.nextLine();
                    arbol.raiz = arbol.eliminar(arbol.getRaiz(), nombreEliminar);
                    System.out.println("Nodo eliminado, si existía.");
                    mostrarEstadoArbol(arbol, "Árbol AVL después de la operación:");
                    break;
                case 2:
                    System.out.print("Ingresa el nombre de la imagen a buscar: ");
                    String nombreBuscar = scanner.nextLine();
                    NodoAVL nodoEncontrado = arbol.buscar(arbol.getRaiz(), nombreBuscar);
                    if (nodoEncontrado != null) {
                        System.out.println("Imagen encontrada: " + nodoEncontrado.imagen.getNombre());
                    } else {
                        System.out.println("La imagen no se encontró en el árbol.");
                    }
                    break;
                case 3:
                    System.out.print("Ingresa el tipo de imagen (tipo): ");
                    String tipo = scanner.nextLine();
                    System.out.print("Ingresa el peso mínimo en bytes: ");
                    long pesoMin = scanner.nextLong();
                    System.out.print("Ingresa el peso máximo en bytes: ");
                    long pesoMax = scanner.nextLong();
                    scanner.nextLine(); // Consumir el salto de línea
                    List<Imagen> imagenesEncontradas = arbol.buscarPorCriterios(arbol.getRaiz(), tipo, pesoMin, pesoMax);
                    if (!imagenesEncontradas.isEmpty()) {
                        System.out.println("Imágenes encontradas:");
                        for (Imagen imagen : imagenesEncontradas) {
                            System.out.println("Nombre: " + imagen.getNombre() + ", Tipo: " + imagen.getTipo() + ", Peso: " + imagen.getPeso());
                        }
                    } else {
                        System.out.println("No se encontraron imágenes que cumplan con los criterios especificados.");
                    }
                    break;
                case 4:
                    System.out.println("Recorrido por niveles del árbol:");
                    arbol.recorridoPorNiveles(arbol.getRaiz());
                    break;
                case 5:
                    System.out.print("Ingresa el nombre de la imagen para obtener información: ");
                    String nombreNodo = scanner.nextLine();
                    NodoAVL nodoBuscado = arbol.buscar(arbol.getRaiz(), nombreNodo);
                    if (nodoBuscado != null) {
                        int nivel = arbol.obtenerNivel(arbol.getRaiz(), nodoBuscado, 1);
                        int factorEquilibrio = arbol.obtenerFactorEquilibrio(nodoBuscado);
                        NodoAVL padre = arbol.encontrarPadre(arbol.getRaiz(), nodoBuscado);
                        NodoAVL abuelo = arbol.encontrarAbuelo(arbol.getRaiz(), nodoBuscado);
                        NodoAVL tio = arbol.encontrarTio(nodoBuscado);
                        Imagen imagen = nodoBuscado.imagen;
                        System.out.println("Información del nodo:");
                        System.out.println("Nombre: " + imagen.getNombre());
                        System.out.println("Tipo: " + imagen.getTipo());
                        System.out.println("Peso: " + imagen.getPeso() + " bytes");
                        System.out.println("Nivel: " + nivel);
                        System.out.println("Factor de equilibrio: " + factorEquilibrio);
                        System.out.println("Padre: " + (padre != null ? padre.imagen.getNombre() : "No tiene padre"));
                        System.out.println("Abuelo: " + (abuelo != null ? abuelo.imagen.getNombre() : "No tiene abuelo"));
                        System.out.println("Tío: " + (tio != null ? tio.imagen.getNombre() : "No tiene tío"));
                    } else {
                        System.out.println("La imagen no se encontró en el árbol.");
                    }
                    break;
                case 6:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, ingresa un número válido.");
            }
        }

        scanner.close();
    }

    public static int altura(NodoAVL nodo) {
        if (nodo == null) {
            return 0;
        }
        return nodo.altura;
    }

    public static int obtenerFactorEquilibrio(NodoAVL nodo) {
        if (nodo == null) {
            return 0;
        }
        return altura(nodo.hijoIzquierdo) - altura(nodo.hijoDerecho);
    }

    public static NodoAVL rotacionDerecha(NodoAVL y, Imagen imagen) {
        NodoAVL x = y.hijoIzquierdo;
        NodoAVL T2 = x.hijoDerecho;

        x.hijoDerecho = y;
        y.hijoIzquierdo = T2;

        y.altura = Math.max(altura(y.hijoIzquierdo), altura(y.hijoDerecho)) + 1;
        x.altura = Math.max(altura(x.hijoIzquierdo), altura(x.hijoDerecho)) + 1;

        System.out.println("Rotación a la derecha realizada en la imagen: " + imagen.getNombre());
        return x;
    }

    public static NodoAVL rotacionIzquierda(NodoAVL x, Imagen imagen) {
        NodoAVL y = x.hijoDerecho;
        NodoAVL T2 = y.hijoIzquierdo;

        y.hijoIzquierdo = x;
        x.hijoDerecho = T2;

        x.altura = Math.max(altura(x.hijoIzquierdo), altura(x.hijoDerecho)) + 1;
        y.altura = Math.max(altura(y.hijoIzquierdo), altura(y.hijoDerecho)) + 1;

        System.out.println("Rotación a la izquierda realizada en la imagen: " + imagen.getNombre());
        return y;
    }

    public static void mostrarEstadoArbol(ArbolAVL arbol, String mensaje) {
        if (arbol.getRaiz() != null) {
            System.out.println(mensaje);
            arbol.mostrarArbolAVL(arbol.getRaiz(), "", false);
            System.out.println("Factor de Equilibrio después de la operación:");
            arbol.mostrarFactorEquilibrio(arbol.getRaiz(), "", false);
            System.out.println("-----------------------------------------------");
        } else {
            System.out.println("El árbol está vacío.");
        }
    }
}
