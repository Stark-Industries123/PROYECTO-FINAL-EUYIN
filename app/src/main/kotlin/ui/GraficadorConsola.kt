package ui

import fisicas.motor.PuntoTrayectoria
import kotlin.math.roundToInt

/**
 * Componente de interfaz de usuario por consola encargado de transformar y renderizar
 * la trayectoria física continua de un proyectil en una representación bidimensional
 * matricial en formato ASCII.
 *
 * Principios de Diseño SOLID y POO:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es proyectar y dibujar
 *   la colección inmutable de puntos en una matriz de caracteres para la consola. No calcula
 *   física ni gestiona entradas de usuario.
 * - Open/Closed Principle (OCP) y Encapsulamiento: Las dimensiones y los caracteres de dibujo
 *   se encuentran encapsulados en el constructor con valores por defecto configurables. Esto permite
 *   cambiar la resolución visual o los símbolos sin modificar la lógica interna de la clase.
 * - Dependency Inversion Principle (DIP): Admite la inyección opcional de una función de escritura
 *   [escritorLinea], desacoplando la clase de la salida estándar [System.out] para maximizar la testabilidad.
 * - Inmutabilidad y Thread-Safety: La clase es completamente inmutable y no comparte estado mutable
 *   entre ejecuciones, garantizando seguridad en entornos concurrentes.
 *
 * @property filas Cantidad de líneas verticales de la matriz visual (altura de la pantalla en caracteres).
 * @property columnas Cantidad de columnas horizontales de la matriz visual (ancho de la pantalla en caracteres).
 * @property caracterProyectil Símbolo utilizado para representar los puntos ocupados por el proyectil.
 * @property caracterFondo Símbolo utilizado para representar el espacio vacío del lienzo.
 * @property escritorLinea Función receptora para la emisión de líneas de texto (por defecto [println]).
 */
class GraficadorConsola(
    private val filas: Int = DEFAULT_FILAS,
    private val columnas: Int = DEFAULT_COLUMNAS,
    private val caracterProyectil: Char = DEFAULT_CARACTER_PROYECTIL,
    private val caracterFondo: Char = DEFAULT_CARACTER_FONDO,
    private val escritorLinea: (String) -> Unit = ESCRITOR_LINEA_DEFECTO,
    private val caracterEjeOrigen: Char = DEFAULT_CARACTER_EJE_ORIGEN
) : Graficador {

    fun obtenerFilas(): Int = filas
    fun obtenerColumnas(): Int = columnas
    fun obtenerCaracterProyectil(): Char = caracterProyectil
    fun obtenerCaracterFondo(): Char = caracterFondo
    fun obtenerCaracterEjeOrigen(): Char = caracterEjeOrigen

    init {
        require(filas > 0) { "El número de filas debe ser estrictamente mayor a 0 (recibido: $filas)." }
        require(columnas > 0) { "El número de columnas debe ser estrictamente mayor a 0 (recibido: $columnas)." }
    }

    /**
     * Dibuja la trayectoria en la consola renderizando la matriz bidimensional fila por fila.
     *
     * @param trayectoria Colección inmutable de [PuntoTrayectoria] calculada por el motor físico.
     */
    override fun graficar(trayectoria: List<PuntoTrayectoria>) {
        if (trayectoria.isEmpty()) {
            escritorLinea("No hay puntos en la trayectoria para graficar.")
            return
        }

        escritorLinea("\n--- TRAYECTORIA DEL PROYECTIL (ASCII) ---")
        val lienzo = renderizar(trayectoria)
        for (fila in lienzo) {
            escritorLinea(fila)
        }
        if (tieneDesplazamientoNegativo(trayectoria)) {
            val minX = minOf(0.0, trayectoria.minOf { it.x })
            val maxX = maxOf(0.0, trayectoria.maxOf { it.x })
            escritorLinea("Eje X [Sur (-X) <-- $caracterEjeOrigen Origen (0.00 m) --> Norte (+X)]: [%.2f m a %.2f m]".format(minX, maxX))
        }
    }

    /**
     * Renderiza la colección de puntos en una lista de cadenas que representan las filas
     * de la matriz bidimensional, facilitando inspección y pruebas unitarias sin acoplamiento a E/S.
     *
     * Vista lateral observada desde el Este (derecha del proyectil lanzado hacia el Norte):
     * - El avance hacia el Norte (+X) se proyecta hacia la derecha.
     * - El retroceso hacia el Sur (-X, por viento de frente del Norte) se proyecta hacia la izquierda.
     * - Cuando existen desplazamientos negativos (X < 0.0), se traza una línea vertical recta ([caracterEjeOrigen])
     *   en la columna correspondiente al punto de origen (X = 0.0) y se grafican con [caracterProyectil]
     *   tanto la trayectoria como los rebotes hacia atrás.
     *
     * @param trayectoria Colección inmutable de puntos discretos en el espacio.
     * @return Lista inmutable de cadenas con tamaño igual a [filas] y longitud de cada cadena igual a [columnas].
     */
    fun renderizar(trayectoria: List<PuntoTrayectoria>): List<String> {
        val matriz = Array(filas) { CharArray(columnas) { caracterFondo } }

        if (trayectoria.isEmpty()) {
            return matriz.map { String(it) }
        }

        val hayNegativos = tieneDesplazamientoNegativo(trayectoria)
        val minX = if (hayNegativos) minOf(0.0, trayectoria.minOf { it.x }) else trayectoria.minOf { it.x }
        val maxX = if (hayNegativos) maxOf(0.0, trayectoria.maxOf { it.x }) else trayectoria.maxOf { it.x }
        val minY = minOf(0.0, trayectoria.minOf { it.y })
        val maxY = trayectoria.maxOf { it.y }

        if (hayNegativos) {
            val columnaOrigen = mapearCoordenadaX(0.0, minX, maxX)
            for (fila in 0 until filas) {
                matriz[fila][columnaOrigen] = caracterEjeOrigen
            }
        }

        for (punto in trayectoria) {
            val fila = mapearCoordenadaY(punto.y, minY, maxY)
            val columna = mapearCoordenadaX(punto.x, minX, maxX)
            matriz[fila][columna] = caracterProyectil
        }

        return matriz.map { String(it) }
    }

    /**
     * Determina si la trayectoria contiene puntos con desplazamiento negativo en el eje X (retroceso hacia el Sur).
     */
    internal fun tieneDesplazamientoNegativo(trayectoria: List<PuntoTrayectoria>): Boolean {
        return trayectoria.any { it.x < -EPSILON }
    }

    /**
     * Mapea una coordenada física continua X al índice discreto de columna dentro de la matriz.
     * Si el alcance en X es nulo o despreciable (el alcance máximo es 0, [maxX] <= [minX], o
     * la diferencia es menor o igual a [EPSILON]), todos los puntos se mapean a la columna 0
     * para representar estrictamente el espacio físico (Y vs X) como una línea vertical.
     *
     * @param x Coordenada física horizontal en metros.
     * @param minX Valor mínimo de X en la trayectoria.
     * @param maxX Valor máximo de X en la trayectoria.
     * @return Índice de columna comprendido en [0, columnas - 1].
     */
    internal fun mapearCoordenadaX(x: Double, minX: Double, maxX: Double): Int {
        if (maxX <= minX || (maxX - minX) <= EPSILON) {
            return 0
        }
        val proporcion = ((x - minX) / (maxX - minX)).coerceIn(0.0, 1.0)
        return (proporcion * (columnas - 1)).roundToInt().coerceIn(0, columnas - 1)
    }

    /**
     * Mapea una coordenada física continua Y al índice discreto de fila dentro de la matriz.
     * Invierte el eje vertical: la fila 0 representa la altura máxima ([maxY]) y la fila
     * [filas - 1] representa la altura mínima del suelo ([minY]).
     *
     * @param y Coordenada física vertical en metros.
     * @param minY Valor mínimo de Y en la trayectoria (referenciado al suelo).
     * @param maxY Valor máximo de Y en la trayectoria.
     * @return Índice de fila comprendido en [0, filas - 1].
     */
    internal fun mapearCoordenadaY(y: Double, minY: Double, maxY: Double): Int {
        if (maxY <= minY || (maxY - minY) <= EPSILON) {
            return filas - 1
        }
        val proporcion = ((y - minY) / (maxY - minY)).coerceIn(0.0, 1.0)
        val filaInvertida = ((1.0 - proporcion) * (filas - 1)).roundToInt()
        return filaInvertida.coerceIn(0, filas - 1)
    }

    companion object {
        val ESCRITOR_LINEA_DEFECTO: (String) -> Unit = { println(it) }
        const val DEFAULT_FILAS: Int = 15
        const val DEFAULT_COLUMNAS: Int = 60
        const val DEFAULT_CARACTER_PROYECTIL: Char = '*'
        const val DEFAULT_CARACTER_FONDO: Char = '.'
        const val DEFAULT_CARACTER_EJE_ORIGEN: Char = '|'
        const val EPSILON: Double = 1e-6
    }
}
