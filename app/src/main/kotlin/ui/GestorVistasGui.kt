package ui

import fisicas.motor.PuntoTrayectoria
import java.awt.CardLayout
import javax.swing.JPanel

/**
 * Gestor de navegación entre pantallas del simulador Swing basado en [CardLayout].
 *
 * Responsabilidad única (SRP): administrar el contenedor de pantallas, cambiar la vista activa
 * deteniendo animaciones al salir del juego y delegar las actualizaciones del escenario a [VistaJuego].
 */
class GestorVistasGui : NavegadorVistasGui {

    private val contenedorVistas: JPanel = JPanel(CardLayout())
    private var vistaJuego: VistaJuego? = null

    /**
     * Registra las pantallas principales y la vista de juego dentro del [CardLayout].
     */
    fun registrarPantallas(
        pantallas: Map<String, JPanel>,
        juego: VistaJuego,
        alFinalizarAnimacion: () -> Unit
    ) {
        pantallas.forEach { (nombre, panel) -> contenedorVistas.add(panel, nombre) }
        vistaJuego = juego
        juego.establecerAlFinalizarAnimacion(alFinalizarAnimacion)
        contenedorVistas.add(juego, NavegadorVistasGui.VISTA_JUEGO)
    }

    override fun obtenerContenedor(): JPanel = contenedorVistas

    override fun mostrarVista(nombreVista: String) {
        if (nombreVista != NavegadorVistasGui.VISTA_JUEGO) {
            vistaJuego?.detenerAnimacion()
        }
        (contenedorVistas.layout as CardLayout).show(contenedorVistas, nombreVista)
    }

    override fun actualizarResumen(texto: String) {
        vistaJuego?.actualizarResumen(texto)
    }

    override fun obtenerTextoResumen(): String = vistaJuego?.obtenerTextoResumen() ?: ""

    override fun mostrarEscenario(planeta: String, trayectoria: List<PuntoTrayectoria>?) {
        vistaJuego?.mostrarEscenario(planeta, trayectoria)
    }

    override fun completarAnimacion() {
        vistaJuego?.completarAnimacion()
    }

    override fun obtenerPuntosGraficados(): List<PuntoTrayectoria> = vistaJuego?.obtenerPuntos() ?: emptyList()

    companion object {
        const val VISTA_MENU_PRINCIPAL: String = NavegadorVistasGui.VISTA_MENU_PRINCIPAL
        const val VISTA_CONFIGURACION: String = NavegadorVistasGui.VISTA_CONFIGURACION
        const val VISTA_JUEGO: String = NavegadorVistasGui.VISTA_JUEGO
    }
}
