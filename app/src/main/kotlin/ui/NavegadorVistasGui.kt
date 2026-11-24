package ui

import fisicas.motor.PuntoTrayectoria
import javax.swing.JPanel

/**
 * Contrato de navegación entre pantallas y control visual del escenario de juego en Swing.
 *
 * Desacopla al controlador de eventos de los paneles concretos y del [java.awt.CardLayout] (DIP).
 */
interface NavegadorVistasGui {

    /**
     * Retorna el contenedor raíz de pantallas gestionado por el navegador.
     */
    fun obtenerContenedor(): JPanel

    /**
     * Activa la vista identificada por [nombreVista].
     */
    fun mostrarVista(nombreVista: String)

    /**
     * Actualiza el texto de resumen o error mostrado en el HUD de juego.
     */
    fun actualizarResumen(texto: String)

    /**
     * Retorna el mensaje actualmente visible en el HUD de juego.
     */
    fun obtenerTextoResumen(): String

    /**
     * Actualiza el planeta activo y, si se provee [trayectoria], inicia su animación en el lienzo.
     */
    fun mostrarEscenario(planeta: String, trayectoria: List<PuntoTrayectoria>? = null)

    /**
     * Completa de inmediato la animación en curso.
     */
    fun completarAnimacion()

    /**
     * Retorna la lista de puntos actualmente cargados en el escenario de juego.
     */
    fun obtenerPuntosGraficados(): List<PuntoTrayectoria>

    companion object {
        const val VISTA_MENU_PRINCIPAL: String = "MenuPrincipal"
        const val VISTA_CONFIGURACION: String = "Configuracion"
        const val VISTA_JUEGO: String = "Juego"
    }
}
