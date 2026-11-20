package ui

import fisicas.motor.PuntoTrayectoria

/**
 * Contrato de abstracción que expone las operaciones de presentación de la interfaz gráfica
 * hacia [SimulacionController].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Permite que [SimulacionController] coordine el flujo
 *   de la simulación y sea probado unitariamente sin depender de Swing ni de [VentanaSimulacion].
 */
interface VistaSimulacion {

    /**
     * Cambia la pantalla activa de la interfaz gráfica.
     */
    fun mostrarVista(nombreVista: String)

    /**
     * Captura los valores actuales ingresados por el usuario en el formulario de configuración.
     */
    fun leerDatosFormulario(): DatosFormularioSimulacion

    /**
     * Sincroniza la representación visual del planeta activo (selector y fondo de escenario).
     */
    fun aplicarPlanetaVisual(nombrePlaneta: String)

    /**
     * Inicia el renderizado animado de [trayectoria] y muestra [resumenHud] en la barra superior.
     */
    fun mostrarTrayectoria(trayectoria: List<PuntoTrayectoria>, resumenHud: String)

    /**
     * Completa de inmediato la animación en curso.
     */
    fun completarAnimacionActual()

    /**
     * Presenta un mensaje de error de validación al usuario.
     */
    fun mostrarError(mensaje: String)

    /**
     * Presenta el reporte de estadísticas finales tras completar la trayectoria.
     */
    fun mostrarEstadisticas(mensaje: String)
}
