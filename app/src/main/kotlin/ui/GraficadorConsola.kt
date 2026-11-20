package ui

import fisicas.motor.PuntoTrayectoria

/**
 * Componente de interfaz de usuario por consola que implementa [Graficador] coordinando
 * la transformación geométrica a grilla matricial ([MapeadorGrillaTrayectoria]) y el
 * renderizado e impresión de texto en la terminal ([RenderizadorConsolaAscii]).
 *
 * Principios de Diseño SOLID y POO:
 * - Single Responsibility Principle (SRP): Actúa exclusivamente como coordinador entre
 *   el servicio de mapeo de coordenadas y el renderizador ASCII de consola.
 * - Alta Cohesión: Sus operaciones principales operan conjuntamente sobre sus dos colaboradores
 *   ([mapeador] y [renderizador]).
 * - Dependency Inversion Principle (DIP): Permite inyectar colaboradores personalizados o
 *   una función [escritorLinea], manteniendo desacoplamiento total respecto de `System.out`.
 * - Inmutabilidad y Thread-Safety: La clase es completamente inmutable y carece de estado mutable.
 *
 * @property mapeador Servicio encargado de transformar puntos físicos a coordenadas de la grilla.
 * @property renderizador Servicio encargado de renderizar e imprimir los caracteres en consola.
 */
class GraficadorConsola(
    private val mapeador: MapeadorGrillaTrayectoria,
    private val renderizador: RenderizadorConsolaAscii
) : Graficador {

    constructor(
        filas: Int = DEFAULT_FILAS,
        columnas: Int = DEFAULT_COLUMNAS,
        caracterProyectil: Char = DEFAULT_CARACTER_PROYECTIL,
        caracterFondo: Char = DEFAULT_CARACTER_FONDO,
        escritorLinea: (String) -> Unit = ESCRITOR_LINEA_DEFECTO,
        caracterEjeOrigen: Char = DEFAULT_CARACTER_EJE_ORIGEN
    ) : this(
        mapeador = MapeadorGrillaTrayectoria(filas = filas, columnas = columnas),
        renderizador = RenderizadorConsolaAscii(
            caracterProyectil = caracterProyectil,
            caracterFondo = caracterFondo,
            caracterEjeOrigen = caracterEjeOrigen,
            escritorLinea = escritorLinea
        )
    )

    fun obtenerFilas(): Int = mapeador.filas
    fun obtenerColumnas(): Int = mapeador.columnas
    fun obtenerCaracterProyectil(): Char = renderizador.caracterProyectil
    fun obtenerCaracterFondo(): Char = renderizador.caracterFondo
    fun obtenerCaracterEjeOrigen(): Char = renderizador.caracterEjeOrigen

    /**
     * Proyecta la trayectoria a la grilla matricial e imprime el resultado ASCII en la terminal.
     *
     * @param trayectoria Colección inmutable de [PuntoTrayectoria] calculada por el motor físico.
     */
    override fun graficar(trayectoria: List<PuntoTrayectoria>) {
        renderizador.imprimir(mapeador.mapear(trayectoria))
    }

    /**
     * Proyecta la trayectoria y renderiza las filas de caracteres del lienzo ASCII sin realizar E/S.
     *
     * @param trayectoria Colección inmutable de puntos discretos en el espacio.
     * @return Lista inmutable de cadenas con tamaño igual a las filas y ancho igual a las columnas.
     */
    fun renderizar(trayectoria: List<PuntoTrayectoria>): List<String> {
        return renderizador.renderizarLienzo(mapeador.mapear(trayectoria))
    }

    companion object {
        val ESCRITOR_LINEA_DEFECTO: (String) -> Unit = RenderizadorConsolaAscii.ESCRITOR_LINEA_DEFECTO
        const val DEFAULT_FILAS: Int = MapeadorGrillaTrayectoria.DEFAULT_FILAS
        const val DEFAULT_COLUMNAS: Int = MapeadorGrillaTrayectoria.DEFAULT_COLUMNAS
        const val DEFAULT_CARACTER_PROYECTIL: Char = RenderizadorConsolaAscii.DEFAULT_CARACTER_PROYECTIL
        const val DEFAULT_CARACTER_FONDO: Char = RenderizadorConsolaAscii.DEFAULT_CARACTER_FONDO
        const val DEFAULT_CARACTER_EJE_ORIGEN: Char = RenderizadorConsolaAscii.DEFAULT_CARACTER_EJE_ORIGEN
        const val EPSILON: Double = MapeadorGrillaTrayectoria.EPSILON
    }
}
