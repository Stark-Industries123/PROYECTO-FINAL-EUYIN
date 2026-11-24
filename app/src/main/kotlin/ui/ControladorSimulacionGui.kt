package ui

import config.ConfigSimulacion
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.ResultadoSimulacion

/**
 * Controlador de eventos de la interfaz gráfica encargado de coordinar las acciones del usuario,
 * la validación de parámetros, la invocación al motor físico y la presentación de resultados.
 */
class ControladorSimulacionGui(
    private val calculador: CalculadorTrayectoria,
    val validadorFormulario: ValidadorFormularioGui,
    val dialogoEstadisticas: DialogoEstadisticas,
    val gestorVistas: GestorVistasGui
) : AccionesJuego {

    private var ultimaConfiguracion: ConfigSimulacion? = null
    private var ultimoResultado: ResultadoSimulacion? = null

    /**
     * Inicializa las vistas, sincroniza el planeta inicial y posiciona la aplicación en el menú.
     */
    fun inicializar() {
        gestorVistas.registrarVistas(validadorFormulario.formulario, this)
        gestorVistas.panelTrayectoria.establecerAlFinalizarAnimacion { mostrarEstadisticasPostPartida() }
        gestorVistas.panelTrayectoria.establecerPlaneta(validadorFormulario.sincronizarPlanetaInicial())
        gestorVistas.mostrarVista(GestorVistasGui.VISTA_MENU_PRINCIPAL)
    }

    override fun alMostrarVista(nombreVista: String) {
        gestorVistas.mostrarVista(nombreVista)
    }

    override fun alIniciarJuego() {
        val config = validadorFormulario.construirConfiguracion { mostrarError(it) } ?: return
        gestorVistas.mostrarVista(GestorVistasGui.VISTA_JUEGO)
        ejecutarConConfig(config)
    }

    override fun alCambiarPlaneta(indice: Int) {
        val planeta = validadorFormulario.actualizarPlanetaPorIndice(indice)
        gestorVistas.panelTrayectoria.establecerPlaneta(planeta.obtenerNombre())
    }

    override fun alFinalizarSimulacion() {
        gestorVistas.panelTrayectoria.completarAnimacion()
    }

    /**
     * Valida la configuración cargada en el formulario y ejecuta la simulación física.
     */
    fun ejecutarSimulacion(): Boolean {
        val config = validadorFormulario.construirConfiguracion { mostrarError(it) } ?: return false
        ejecutarConConfig(config)
        return true
    }

    private fun ejecutarConConfig(config: ConfigSimulacion) {
        ultimaConfiguracion = config
        val nombrePlaneta = config.fuenteGravedad.obtenerNombre()
        gestorVistas.panelTrayectoria.establecerPlaneta(nombrePlaneta)
        val resultado = calculador.calcularTrayectoria(config)
        ultimoResultado = resultado
        gestorVistas.panelTrayectoria.graficar(resultado.trayectoria)
        val resumen = String.format(
            "Planeta: %s | Tiempo: %.2f s | Alcance: %.2f m | Altura máx: %.2f m | Rebotes: %d",
            nombrePlaneta,
            resultado.tiempoVuelo,
            resultado.distanciaMaxima,
            resultado.alturaMaxima,
            resultado.cantidadRebotes
        ).replace(',', '.')
        gestorVistas.actualizarResumen(resumen)
    }

    private fun mostrarEstadisticasPostPartida() {
        val config = ultimaConfiguracion ?: return
        val resultado = ultimoResultado ?: return
        dialogoEstadisticas.mostrarEstadisticas(config, resultado)
    }

    private fun mostrarError(mensaje: String) {
        gestorVistas.actualizarResumen(mensaje)
        dialogoEstadisticas.mostrarError(mensaje)
    }
}
