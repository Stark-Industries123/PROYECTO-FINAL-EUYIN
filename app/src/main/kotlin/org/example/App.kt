package org.example

import condiciones.io.EscritorConsola
import condiciones.io.LectorConsola
import condiciones.ui.ConfiguradorSimulacion
import condiciones.ui.ConfiguradorSimulacionConsola
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.ResultadoSimulacion
import fisicas.motor.Simulador
import ui.PresentadorResultados
import ui.PresentadorResultadosSimulacion

/**
 * Punto de entrada principal y orquestador ejecutable de la aplicación de tiro parabólico.
 *
 * Cumplimiento estricto de POO y SOLID:
 * - Single Responsibility Principle (SRP): Coordina exclusivamente el flujo de alto nivel
 *   entre la captura de configuración ([ConfiguradorSimulacion]), el motor físico ([CalculadorTrayectoria])
 *   y la presentación de resultados ([PresentadorResultados]).
 * - Dependency Inversion Principle (DIP): Depende de abstracciones inyectadas por constructor.
 */
class App(
    private val configurador: ConfiguradorSimulacion = ConfiguradorSimulacionConsola(),
    private val presentador: PresentadorResultados = PresentadorResultadosSimulacion(),
    private val calculador: CalculadorTrayectoria = Simulador()
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
        presentador = PresentadorResultadosSimulacion(escritor),
        calculador = Simulador()
    )

    /**
     * Ejecuta el flujo interactivo unificado, simula la trayectoria y presenta el reporte y gráfico.
     */
    fun ejecutar(): ResultadoSimulacion {
        val config = configurador.solicitarConfiguracion()
        val resultados = calculador.calcularTrayectoria(config)
        presentador.presentarCompleto(config, resultados)
        return resultados
    }

    /**
     * Solicita interactivamente los parámetros de simulación delegando en [ConfiguradorSimulacion].
     */
    fun solicitarConfiguracion() = configurador.solicitarConfiguracion()

    companion object {
        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            App().ejecutar()
        }
    }
}
