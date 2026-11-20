package ui

import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.Simulador

/**
 * Controlador de la simulación gráfica encargado de coordinar eventos de botones, validación
 * de parámetros, ejecución del motor físico y actualización de [VistaSimulacion].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Centraliza la lógica de flujo y coordinación de la
 *   simulación separándola por completo de la construcción de paneles Swing.
 * - Dependency Inversion Principle (DIP): Depende de [VistaSimulacion] y [CalculadorTrayectoria].
 * - Bajo Acoplamiento (CBO) y Alta Cohesión: Todos sus métodos coordinan la sesión sobre [vista].
 */
class SimulacionController(
    private val vista: VistaSimulacion,
    private val calculador: CalculadorTrayectoria = Simulador(pasoTiempoMuestreo = 0.016),
    private val validadorFormulario: ValidadorFormularioSimulacion = ValidadorFormularioSimulacion(),
    private val formateador: FormateadorEstadisticasUI = FormateadorEstadisticasUI()
) {

    private var ultimoTextoEstadisticas: String? = null

    /**
     * Sincroniza la vista inicial con el planeta activo y presenta el menú principal.
     */
    fun inicializarVista() {
        vista.aplicarPlanetaVisual(validadorFormulario.obtenerPlanetaActual().obtenerNombre())
        vista.mostrarVista(VISTA_MENU_PRINCIPAL)
    }

    /**
     * Navega hacia la vista del menú principal.
     */
    fun alAbrirMenuPrincipal() {
        vista.mostrarVista(VISTA_MENU_PRINCIPAL)
    }

    /**
     * Navega hacia la vista de configuración de misión.
     */
    fun alAbrirConfiguracion() {
        vista.mostrarVista(VISTA_CONFIGURACION)
    }

    /**
     * Actualiza la gravedad seleccionada cuando cambia el planeta en la configuración.
     */
    fun alCambiarPlaneta(opcionPlaneta: String) {
        val planeta = validadorFormulario.actualizarPlaneta(opcionPlaneta)
        vista.aplicarPlanetaVisual(planeta.obtenerNombre())
    }

    /**
     * Valida la configuración actual, navega al escenario de juego y lanza la simulación física.
     */
    fun alIniciarJuego(): Boolean {
        return ejecutarSimulacion(cambiarAVistaJuego = true)
    }

    /**
     * Ejecuta la validación, calcula la trayectoria en el motor físico y actualiza la vista.
     */
    fun ejecutarSimulacion(cambiarAVistaJuego: Boolean = false): Boolean {
        val datos = vista.leerDatosFormulario()
        val config = validadorFormulario.construirConfiguracion(datos) { vista.mostrarError(it) }
            ?: return false
        if (cambiarAVistaJuego) {
            vista.mostrarVista(VISTA_JUEGO)
        }
        vista.aplicarPlanetaVisual(config.fuenteGravedad.obtenerNombre())
        val resultado = calculador.calcularTrayectoria(config)
        ultimoTextoEstadisticas = formateador.formatearEstadisticasFinales(config, resultado)
        val resumenHud = formateador.formatearResumenHud(config, resultado)
        vista.mostrarTrayectoria(resultado.trayectoria, resumenHud)
        return true
    }

    /**
     * Solicita a la vista saltar inmediatamente la animación en curso.
     */
    fun alFinalizarSimulacion() {
        vista.completarAnimacionActual()
    }

    /**
     * Notifica las estadísticas post-partida cuando concluye la animación de la trayectoria.
     */
    fun alCompletarAnimacion() {
        val estadisticas = ultimoTextoEstadisticas ?: return
        vista.mostrarEstadisticas(estadisticas)
    }

    companion object {
        const val VISTA_MENU_PRINCIPAL: String = "MenuPrincipal"
        const val VISTA_CONFIGURACION: String = "Configuracion"
        const val VISTA_JUEGO: String = "Juego"
    }
}
