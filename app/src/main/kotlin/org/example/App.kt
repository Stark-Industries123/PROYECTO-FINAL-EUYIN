package org.example

import condiciones.dominio.Proyectil
import condiciones.dominio.Viento
import condiciones.io.EscritorConsola
import condiciones.io.EscritorConsolaEstandar
import condiciones.io.LectorConsola
import condiciones.io.LectorConsolaEstandar
import condiciones.servicio.CalculadorRumboRosa32Service
import condiciones.servicio.CalculadorRumboService
import condiciones.ui.SolicitadorEntradaService
import config.ConfigSimulacion
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.ResultadoSimulacion
import fisicas.motor.Simulador
import ui.Graficador
import ui.GraficadorConsola
import ui.MenuConsola

/**
 * Punto de entrada principal y orquestador ejecutable de la aplicación de tiro parabólico.
 *
 * Integra de forma unificada la recolección interactiva de datos mediante [SolicitadorEntradaService],
 * el cálculo exacto del rumbo náutico de 32 rumbos (11.25° por punto) mediante [CalculadorRumboService],
 * la instanciación de los modelos de dominio ([Proyectil], [Viento]) y el cómputo cinemático dinámico
 * a través del motor físico ([CalculadorTrayectoria]).
 *
 * Cumplimiento estricto de POO y SOLID:
 * - 100% Orientado a Objetos: Toda la lógica de orquestación reside dentro de la clase [App].
 * - Single Responsibility Principle (SRP): Orquesta la interacción continua de consola,
 *   la instanciación de los modelos de dominio, la ejecución del motor físico y la visualización final.
 * - Open/Closed Principle (OCP): Abierto a extensión e inyección de servicios alternativos (lectores,
 *   escritores, simuladores, graficadores o calculadores de rumbos) sin alterar esta clase.
 * - Dependency Inversion Principle (DIP): Depende de contratos y abstracciones
 *   ([SolicitadorEntradaService], [CalculadorRumboService], [CalculadorTrayectoria], [Graficador], [EscritorConsola]).
 */
class App(
    private val solicitador: SolicitadorEntradaService = SOLICITADOR_DEFECTO,
    private val calculadorRumbo: CalculadorRumboService = CALCULADOR_DEFECTO,
    private val fabricaSimulador: (ConfigSimulacion) -> CalculadorTrayectoria = FABRICA_SIMULADOR_DEFECTO,
    private val graficador: Graficador = GRAFICADOR_DEFECTO,
    private val escritor: EscritorConsola = ESCRITOR_DEFECTO
) {

    /**
     * Constructor secundario que permite inyectar fácilmente [LectorConsola] y [EscritorConsola]
     * para pruebas unitarias y de integración desacopladas de la consola estándar.
     */
    constructor(
        lector: LectorConsola,
        escritor: EscritorConsola,
        calculadorRumbo: CalculadorRumboService = CALCULADOR_DEFECTO,
        fabricaSimulador: (ConfigSimulacion) -> CalculadorTrayectoria = FABRICA_SIMULADOR_DEFECTO,
        graficador: Graficador = GraficadorConsola(escritorLinea = { escritor.escribirLinea(it) })
    ) : this(
        solicitador = SolicitadorEntradaService(lector, escritor),
        calculadorRumbo = calculadorRumbo,
        fabricaSimulador = fabricaSimulador,
        graficador = graficador,
        escritor = escritor
    )

    /**
     * Constructor secundario de compatibilidad hacia atrás con constructores anteriores.
     */
    @Deprecated("Utilizar el constructor con SolicitadorEntradaService o Lector/Escritor")
    constructor(
        menu: MenuConsola,
        fabricaSimulador: (ConfigSimulacion) -> CalculadorTrayectoria = FABRICA_SIMULADOR_DEFECTO,
        graficador: Graficador = GRAFICADOR_DEFECTO
    ) : this(
        solicitador = SOLICITADOR_DEFECTO,
        calculadorRumbo = CALCULADOR_DEFECTO,
        fabricaSimulador = fabricaSimulador,
        graficador = graficador,
        escritor = ESCRITOR_DEFECTO
    )

    /**
     * Ejecuta el flujo interactivo unificado, simula la trayectoria y presenta el reporte y gráfico.
     *
     * @return El resultado inmutable de la simulación física calculada.
     */
    fun ejecutar(): ResultadoSimulacion {
        val config = solicitarConfiguracion()
        val simulador = fabricaSimulador(config)
        val resultados = simulador.calcularTrayectoria(config)

        mostrarResultados(config, resultados)
        graficador.graficar(resultados.trayectoria)
        return resultados
    }

    /**
     * Solicita interactivamente los parámetros en un flujo continuo y unificado por consola:
     * 1) Entorno gravitacional (Planeta)
     * 2) Velocidad inicial
     * 3) Ángulo de disparo
     * 4) Guía general limpia de 4 renglones y dirección del viento en grados (0-360°)
     * 5) Velocidad del viento (km/h)
     * 6) Peso del proyectil (kg)
     *
     * Luego calcula el rumbo exacto (32 puntos a 11.25° por punto) e instancia los modelos de dominio
     * [Viento] y [Proyectil], retornando un [ConfigSimulacion] completamente inicializado.
     *
     * @return [ConfigSimulacion] listo para la integración física.
     */
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
    }

    companion object {
        val LECTOR_DEFECTO: LectorConsola = LectorConsolaEstandar()
        val ESCRITOR_DEFECTO: EscritorConsola = EscritorConsolaEstandar()
        val CALCULADOR_DEFECTO: CalculadorRumboService = CalculadorRumboRosa32Service()
        val SOLICITADOR_DEFECTO: SolicitadorEntradaService = SolicitadorEntradaService(LECTOR_DEFECTO, ESCRITOR_DEFECTO)
        val MENU_DEFECTO: MenuConsola = MenuConsola()
        val FABRICA_SIMULADOR_DEFECTO: (ConfigSimulacion) -> CalculadorTrayectoria = { config -> Simulador(config.fuenteGravedad) }
        val GRAFICADOR_DEFECTO: Graficador = GraficadorConsola()

        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            App().ejecutar()
        }
    }
}
