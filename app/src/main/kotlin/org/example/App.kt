package org.example

import condiciones.io.EscritorConsola
import condiciones.io.LectorConsola
import condiciones.ui.ConfiguradorSimulacionConsola
import fisicas.motor.ResultadoSimulacion
import fisicas.motor.Simulador
import ui.PresentadorResultadosSimulacion

/**
 * Punto de entrada principal y orquestador ejecutable de la aplicación de tiro parabólico.
 *
 * Cumplimiento estricto de POO y SOLID:
 * - Single Responsibility Principle (SRP): Coordina exclusivamente el flujo de alto nivel
 *   entre la captura de configuración ([ConfiguradorSimulacionConsola]), el motor físico ([Simulador])
 *   y la presentación de resultados ([PresentadorResultadosSimulacion]).
 */
class App(
    private val configurador: ConfiguradorSimulacionConsola = ConfiguradorSimulacionConsola(),
    private val presentador: PresentadorResultadosSimulacion = PresentadorResultadosSimulacion()
) {

    /**
     * Constructor secundario que permite inyectar [LectorConsola] y [EscritorConsola]
     * para pruebas unitarias y de integración desacopladas de la consola estándar.
     */
    constructor(
        lector: LectorConsola,
        escritor: EscritorConsola
    ) : this(
        configurador = ConfiguradorSimulacionConsola(lector, escritor),
        presentador = PresentadorResultadosSimulacion(escritor)
    )

    /**
     * Ejecuta el flujo interactivo unificado, simula la trayectoria y presenta el reporte y gráfico.
     */
    fun ejecutar(): ResultadoSimulacion {
        val config = configurador.solicitarConfiguracion()
        val resultados = Simulador(config.fuenteGravedad).calcularTrayectoria(config)
        presentador.presentarCompleto(config, resultados)
        return resultados
    }

    /**
     * Solicita interactivamente los parámetros de simulación delegando en [ConfiguradorSimulacionConsola].
     */
    fun solicitarConfiguracion() = configurador.solicitarConfiguracion()

    companion object {
        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            App().ejecutar()
        }
    }
}
