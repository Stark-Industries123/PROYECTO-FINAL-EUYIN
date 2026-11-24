package ui

import javax.swing.JPanel

/**
 * Fábrica responsable de instanciar las pantallas Swing del simulador (Menú, Configuración y Juego)
 * compartiendo la misma [FabricaComponentesGui].
 */
class FabricaVistasGui(
    private val fabricaComponentes: FabricaComponentesGui = FabricaComponentesGui()
) {

    /**
     * Construye el mapa de vistas iniciales (Menú Principal y Configuración).
     */
    fun crearMenuYConfiguracion(
        formulario: FormularioParametros,
        acciones: AccionesJuego
    ): Map<String, JPanel> {
        return mapOf(
            AccionesJuego.VISTA_MENU_PRINCIPAL to VistaMenuPrincipal(fabricaComponentes, acciones),
            AccionesJuego.VISTA_CONFIGURACION to VistaConfiguracion(fabricaComponentes, formulario, acciones)
        )
    }

    /**
     * Construye la pantalla de juego conectada a las [acciones] del controlador.
     */
    fun crearVistaJuego(acciones: AccionesJuego): VistaJuego {
        return VistaJuego(fabricaComponentes, acciones)
    }
}
