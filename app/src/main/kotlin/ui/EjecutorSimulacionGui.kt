package ui

import config.ConfigSimulacion
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.ResultadoSimulacion

/**
 * Servicio de aplicación para la interfaz Swing encargado de validar la configuración,
 * ejecutar el motor físico [CalculadorTrayectoria] y conservar el último resultado para
 * las estadísticas post-partida.
 *
 * Cumplimiento de SOLID:
 * - Dependency Inversion Principle (DIP): Depende únicamente de las abstracciones
 *   [CalculadorTrayectoria], [ValidadorConfiguracionGui] y [PresentadorEstadisticasGui].
 */
class EjecutorSimulacionGui(
    private val calculador: CalculadorTrayectoria,
    private val validador: ValidadorConfiguracionGui,
    private val dialogo: PresentadorEstadisticasGui
) : MotorSimulacionGui {

    private var ultimaConfiguracion: ConfigSimulacion? = null
    private var ultimoResultado: ResultadoSimulacion? = null

    override fun sincronizarPlanetaInicial(): String = validador.sincronizarPlanetaInicial()

    override fun actualizarPlanetaPorIndice(indice: Int): String = validador.actualizarPlanetaPorIndice(indice)

    override fun actualizarEntradas(valores: Map<String, String>, direccionVientoGrados: Double?) {
        validador.actualizarEntradas(valores, direccionVientoGrados)
    }

    override fun ejecutar(
        alResultado: (String, ResultadoSimulacion) -> Unit,
        alError: (String) -> Unit
    ): Boolean {
        val config = validador.construirConfiguracion { mensaje ->
            dialogo.mostrarError(mensaje)
            alError(mensaje)
        } ?: return false
        ultimaConfiguracion = config
        val resultado = calculador.calcularTrayectoria(config)
        ultimoResultado = resultado
        alResultado(config.fuenteGravedad.obtenerNombre(), resultado)
        return true
    }

    override fun mostrarUltimasEstadisticas() {
        val config = ultimaConfiguracion ?: return
        val resultado = ultimoResultado ?: return
        dialogo.mostrarEstadisticas(config, resultado)
    }

    override fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String {
        return dialogo.construirTextoEstadisticas(config, resultado)
    }
}
