package ui

import java.awt.CardLayout
import javax.swing.JPanel

/**
 * Gestor de navegación entre pantallas del simulador Swing basado en [CardLayout].
 *
 * Responsabilidad única (SRP): ensamblar las vistas (Menú, Configuración, Juego), cambiar
 * la pantalla activa deteniendo animaciones cuando corresponde y actualizar el resumen del HUD.
 */
class GestorVistasGui(
    val panelTrayectoria: PanelTrayectoria
) {

    private val cardLayout = CardLayout()
    val contenedorVistas: JPanel = JPanel(cardLayout)
    private val fabrica = FabricaComponentesGui()
    private var vistaJuego: VistaJuego? = null

    /**
     * Construye y registra las tres vistas principales dentro del [CardLayout].
     */
    fun registrarVistas(formulario: FormularioParametros, acciones: AccionesJuego) {
        val vistaMenu = VistaMenuPrincipal(fabrica, acciones)
        val vistaConfig = VistaConfiguracion(fabrica, formulario, acciones)
        val juego = VistaJuego(fabrica, panelTrayectoria, acciones)
        vistaJuego = juego
        contenedorVistas.add(vistaMenu, VISTA_MENU_PRINCIPAL)
        contenedorVistas.add(vistaConfig, VISTA_CONFIGURACION)
        contenedorVistas.add(juego, VISTA_JUEGO)
    }

    /**
     * Activa la pantalla indicada por [nombreVista], deteniendo la animación si se abandona el juego.
     */
    fun mostrarVista(nombreVista: String) {
        if (nombreVista != VISTA_JUEGO) {
            panelTrayectoria.detenerAnimacion()
        }
        cardLayout.show(contenedorVistas, nombreVista)
    }

    /**
     * Actualiza el texto de resumen o error visible en la vista de juego.
     */
    fun actualizarResumen(texto: String) {
        vistaJuego?.actualizarResumen(texto)
    }

    /**
     * Retorna el texto de resumen actualmente mostrado en la vista de juego.
     */
    fun obtenerTextoResumen(): String = vistaJuego?.obtenerTextoResumen() ?: ""

    companion object {
        const val VISTA_MENU_PRINCIPAL: String = "MenuPrincipal"
        const val VISTA_CONFIGURACION: String = "Configuracion"
        const val VISTA_JUEGO: String = "Juego"
    }
}
