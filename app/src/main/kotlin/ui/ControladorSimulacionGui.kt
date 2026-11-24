package ui

import config.ConfigSimulacion
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.ResultadoSimulacion
import javax.swing.JPanel

/**
 * Controlador de eventos de la interfaz gráfica encargado de coordinar las acciones del usuario
 * entre la fachada de simulación [MotorSimulacionGui] y el navegador [NavegadorVistasGui].
 *
 * Cumplimiento de SOLID:
 * - Dependency Inversion Principle (DIP): Depende exclusivamente de abstracciones inyectadas.
 */
class ControladorSimulacionGui(
    private val motor: MotorSimulacionGui,
    private val navegador: NavegadorVistasGui
) : ControladorVentanaGui {

    /**
     * Sincroniza el planeta inicial en el escenario y posiciona la aplicación en el menú principal.
     */
    fun inicializar() {
        navegador.mostrarEscenario(motor.sincronizarPlanetaInicial())
        navegador.mostrarVista(NavegadorVistasGui.VISTA_MENU_PRINCIPAL)
    }

    /**
     * Callback disparado cuando finaliza la animación del proyectil en el lienzo de juego.
     */
    fun alFinalizarAnimacion() {
        motor.mostrarUltimasEstadisticas()
    }

    override fun obtenerContenedor(): JPanel = navegador.obtenerContenedor()

    override fun alMostrarVista(nombreVista: String) {
        navegador.mostrarVista(nombreVista)
    }

    override fun alIniciarJuego() {
        ejecutarConTransicion(cambiarAVistaJuego = true)
    }

    override fun alCambiarPlaneta(indice: Int) {
        navegador.mostrarEscenario(motor.actualizarPlanetaPorIndice(indice))
    }

    override fun alFinalizarSimulacion() {
        navegador.completarAnimacion()
    }

    override fun actualizarEntradas(valores: Map<String, String>, direccionVientoGrados: Double?) {
        motor.actualizarEntradas(valores, direccionVientoGrados)
    }

    override fun obtenerTextoResumen(): String = navegador.obtenerTextoResumen()

    override fun obtenerPuntosGraficados(): List<PuntoTrayectoria> = navegador.obtenerPuntosGraficados()

    override fun ejecutarSimulacion(): Boolean = ejecutarConTransicion(cambiarAVistaJuego = false)

    override fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String {
        return motor.construirTextoEstadisticas(config, resultado)
    }

    private fun ejecutarConTransicion(cambiarAVistaJuego: Boolean): Boolean {
        return motor.ejecutar(
            alResultado = { nombrePlaneta, resultado ->
                if (cambiarAVistaJuego) {
                    navegador.mostrarVista(NavegadorVistasGui.VISTA_JUEGO)
                }
                navegador.mostrarEscenario(nombrePlaneta, resultado.trayectoria)
                navegador.actualizarResumen(formatearResumen(nombrePlaneta, resultado))
            },
            alError = { mensaje -> navegador.actualizarResumen(mensaje) }
        )
    }

    private fun formatearResumen(nombrePlaneta: String, resultado: ResultadoSimulacion): String {
        return String.format(
            "Planeta: %s | Tiempo: %.2f s | Alcance: %.2f m | Altura máx: %.2f m | Rebotes: %d",
            nombrePlaneta,
            resultado.tiempoVuelo,
            resultado.distanciaMaxima,
            resultado.alturaMaxima,
            resultado.cantidadRebotes
        ).replace(',', '.')
    }
}
