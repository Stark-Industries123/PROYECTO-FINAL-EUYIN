package ui

/**
 * Contrato de acciones de usuario disparadas desde las vistas Swing del simulador.
 *
 * Desacopla las pantallas (Menú, Configuración, Juego) de la lógica de control de eventos
 * respetando DIP e ISP.
 */
interface AccionesJuego {

    /**
     * Valida la configuración actual, cambia a la vista de juego y ejecuta el lanzamiento.
     */
    fun alIniciarJuego()

    /**
     * Navega hacia la vista identificada por [nombreVista].
     */
    fun alMostrarVista(nombreVista: String)

    /**
     * Completa instantáneamente la animación en curso y abre las estadísticas finales.
     */
    fun alFinalizarSimulacion()

    /**
     * Notifica que el usuario seleccionó un nuevo planeta en el índice [indice].
     */
    fun alCambiarPlaneta(indice: Int)

    companion object {
        const val VISTA_MENU_PRINCIPAL: String = "MenuPrincipal"
        const val VISTA_CONFIGURACION: String = "Configuracion"
        const val VISTA_JUEGO: String = "Juego"
    }
}
