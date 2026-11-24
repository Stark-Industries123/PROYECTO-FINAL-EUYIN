package ui

import java.awt.Component

/**
 * Ensamblador de composición raíz para la ventana Swing del simulador.
 *
 * Responsabilidad única (SRP): cablear el motor de aplicación GUI, el gestor de vistas,
 * las pantallas y el [ControladorSimulacionGui] para entregar una fachada [ControladorVentanaGui]
 * lista para ser utilizada por [VentanaSimulacion].
 */
class EnsambladorVentanaGui(
    private val ensambladorMotor: EnsambladorMotorGui = EnsambladorMotorGui(),
    private val fabricaVistas: FabricaVistasGui = FabricaVistasGui()
) {

    /**
     * Compone e inicializa toda la jerarquía del controlador de la ventana Swing.
     */
    fun ensamblar(componentePadre: Component): ControladorVentanaGui {
        val formulario = FormularioParametros()
        val motor = ensambladorMotor.crearMotor(componentePadre, formulario)
        val gestor = GestorVistasGui()
        val controlador = ControladorSimulacionGui(motor, gestor)
        val pantallas = fabricaVistas.crearMenuYConfiguracion(formulario, controlador)
        val vistaJuego = fabricaVistas.crearVistaJuego(controlador)
        gestor.registrarPantallas(pantallas, vistaJuego) { controlador.alFinalizarAnimacion() }
        controlador.inicializar()
        return controlador
    }
}
