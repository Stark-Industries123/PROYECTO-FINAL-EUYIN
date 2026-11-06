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
arreglador-solid
    fun solicitarConfiguracion() = configurador.solicitarConfiguracion()
    fun solicitarConfiguracion(): ConfigSimulacion {
        escritor.escribirLinea("=== CONFIGURACIÓN DE LA SIMULACIÓN DE TIRO PARABÓLICO ===")
        val planeta = solicitador.solicitarPlaneta()
        val velocidad = solicitador.solicitarVelocidadInicial()
        val angulo = solicitador.solicitarAnguloDisparo()
        val direccionViento = solicitador.solicitarDireccionVientoConGuia()
        val velocidadViento = solicitador.solicitarVelocidadViento()
        val pesoProyectil = solicitador.solicitarPesoProyectil()

        // Cálculo exacto del rumbo náutico de 32 puntos (11.25° por punto)
        val rumbo = calculadorRumbo.calcularRumbo(direccionViento)
        val viento = Viento(
            direccionGrados = direccionViento,
            velocidad = velocidadViento,
            rumbo = rumbo
        )
        val proyectil = Proyectil(pesoKg = pesoProyectil)

        return ConfigSimulacion(
            velocidadInicial = velocidad,
            angulo = angulo,
            fuenteGravedad = planeta,
            proyectil = proyectil,
            viento = viento
        )
    }

    /**
     * Muestra el reporte final del impacto utilizando los valores dinámicos configurados.
     *
     * @param config Configuración física que incluye los modelos [Proyectil] y [Viento].
     * @param resultados Métricas calculadas por el motor físico.
     */
    fun mostrarResultados(config: ConfigSimulacion, resultados: ResultadoSimulacion) {
        escritor.escribirLinea("\n--- RESULTADOS DEL IMPACTO ---")
        escritor.escribirLinea("Planeta objetivo: ${config.fuenteGravedad.obtenerNombre()}")
        escritor.escribirLinea("Gravedad aplicada: ${config.fuenteGravedad.gravedad} m/s²")
        escritor.escribirLinea("Masa del proyectil: %.2f kg".format(config.masaKg))
        if (config.viento != null) {
            val rumbo = config.viento.rumbo
            escritor.escribirLinea("Viento: %.1f km/h (Sector: %s, Rumbo: %s - %s, %.1f°)".format(
                config.velocidadVientoKmH,
                config.sectorViento.descripcion,
                rumbo.codigo,
                rumbo.nombreCompleto,
                config.direccionVientoGrados
            ))
            escritor.escribirLinea("Origen del viento: Sopla DESDE el rumbo %s (%s)".format(
                rumbo.codigo,
                rumbo.nombreCompleto
            ))
        } else {
            escritor.escribirLinea("Viento: %.1f km/h (Sector: %s, %.1f°)".format(
                config.velocidadVientoKmH,
                config.sectorViento.descripcion,
                config.direccionVientoGrados
            ))
        }
        escritor.escribirLinea("Tiempo de vuelo total: %.2f segundos".format(resultados.tiempoVuelo))
        escritor.escribirLinea("Alcance máximo frontal (X): %.2f metros".format(resultados.distanciaMaxima))
        escritor.escribirLinea("Desviación lateral (Z): %.2f metros".format(resultados.desviacionLateralMaxima))
        escritor.escribirLinea("Altura máxima (Y): %.2f metros".format(resultados.alturaMaxima))
        escritor.escribirLinea("Cantidad de rebotes: ${resultados.cantidadRebotes}")
        escritor.escribirLinea("Velocidad escalar de impacto: %.2f m/s".format(resultados.metricasImpacto.velocidadEscalarFinal))
        escritor.escribirLinea("Ángulo de llegada al suelo: %.2f°".format(resultados.metricasImpacto.anguloLlegada))
    }
  main

    companion object {
        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            App().ejecutar()
        }
    }
}
