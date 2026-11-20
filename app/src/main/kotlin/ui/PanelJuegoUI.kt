package ui

import fisicas.motor.PuntoTrayectoria
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JPanel

/**
 * Vista Swing de la pantalla de juego que integra la barra HUD superior de acciones y resumen
 * sobre el lienzo animado [PanelTrayectoria].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encapsula la composición visual del escenario de juego
 *   y su HUD superior, aislando a [VentanaSimulacion] de los detalles del lienzo animado.
 */
class PanelJuegoUI(
    private val panelTrayectoria: PanelTrayectoria = PanelTrayectoria(),
    private val estilo: EstiloUI = EstiloUI()
) : JPanel(BorderLayout()) {

    private val etiquetaResumen = estilo.crearEtiquetaHud("Listo para jugar.")

    init {
        panelTrayectoria.layout = BorderLayout()
        add(panelTrayectoria, BorderLayout.CENTER)
    }

    /**
     * Instala la barra HUD superior en el escenario conectando cada botón con su acción de control.
     */
    fun configurarAcciones(
        alMenu: () -> Unit,
        alConfig: () -> Unit,
        alRelanzar: () -> Unit,
        alFinalizar: () -> Unit
    ) {
        val barraSuperior = JPanel(FlowLayout(FlowLayout.LEFT, 12, 8)).apply {
            isOpaque = false
            add(estilo.crearBotonCompacto("Menú Principal", alMenu))
            add(estilo.crearBotonCompacto("Configuración", alConfig))
            add(estilo.crearBotonCompacto("Repetir Disparo", alRelanzar))
            add(estilo.crearBotonCompacto("Finalizar", alFinalizar))
            add(etiquetaResumen)
        }
        panelTrayectoria.add(barraSuperior, BorderLayout.NORTH)
    }

    /**
     * Registra la acción a ejecutar cuando la animación de trayectoria finalice.
     */
    fun establecerAlFinalizarAnimacion(callback: () -> Unit) {
        panelTrayectoria.establecerAlFinalizarAnimacion(callback)
    }

    /**
     * Actualiza el planeta visual del fondo en el lienzo de juego.
     */
    fun aplicarPlaneta(nombrePlaneta: String) {
        panelTrayectoria.establecerPlaneta(nombrePlaneta)
    }

    /**
     * Muestra la trayectoria animada y actualiza el texto de métricas en el HUD superior.
     */
    fun mostrarTrayectoria(trayectoria: List<PuntoTrayectoria>, resumen: String) {
        panelTrayectoria.graficar(trayectoria)
        etiquetaResumen.text = resumen
    }

    /**
     * Actualiza el mensaje de estado o error visible en el HUD superior.
     */
    fun mostrarMensajeResumen(mensaje: String) {
        etiquetaResumen.text = mensaje
    }

    /**
     * Detiene la animación si se encuentra en ejecución.
     */
    fun detenerAnimacion() {
        panelTrayectoria.detenerAnimacion()
    }

    /**
     * Completa instantáneamente la animación en curso.
     */
    fun completarAnimacion() {
        panelTrayectoria.completarAnimacion()
    }

    /**
     * Retorna el texto actualmente mostrado en el HUD de resumen.
     */
    fun obtenerResumen(): String = etiquetaResumen.text

    /**
     * Retorna la trayectoria actualmente cargada en el lienzo.
     */
    fun obtenerPuntos(): List<PuntoTrayectoria> = panelTrayectoria.obtenerPuntos()
}
