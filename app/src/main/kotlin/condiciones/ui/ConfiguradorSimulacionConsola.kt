package condiciones.ui

import condiciones.io.EscritorConsola
import condiciones.io.EscritorConsolaEstandar
import condiciones.io.LectorConsola
import condiciones.servicio.EnsambladorCondicionesService
import config.ConfigSimulacion

/**
 * Coordinador interactivo encargado de recolectar por consola todos los parámetros
 * de disparo y ensamblar la estructura inmutable [ConfigSimulacion].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es coordinar la captura
 *   de parámetros de simulación y construir [ConfigSimulacion]. No ejecuta cálculos físicos
 *   ni formatea reportes de impacto.
 */
class ConfiguradorSimulacionConsola(
    private val solicitador: SolicitadorEntradaService = SolicitadorEntradaService(),
    private val ensamblador: EnsambladorCondicionesService = EnsambladorCondicionesService(),
    private val escritor: EscritorConsola = EscritorConsolaEstandar()
) {

    constructor(
        lector: LectorConsola,
        escritor: EscritorConsola
    ) : this(
        solicitador = SolicitadorEntradaService(lector, escritor),
        ensamblador = EnsambladorCondicionesService(),
        escritor = escritor
    )

    /**
     * Solicita interactivamente los parámetros en un flujo continuo por consola y retorna
     * un [ConfigSimulacion] completamente inicializado.
     */
    fun solicitarConfiguracion(): ConfigSimulacion {
        escritor.escribirLinea("=== CONFIGURACIÓN DE LA SIMULACIÓN DE TIRO PARABÓLICO ===")
        val planeta = solicitador.solicitarPlaneta()
        val velocidad = solicitador.solicitarVelocidadInicial()
        val angulo = solicitador.solicitarAnguloDisparo()
        val condiciones = ensamblador.ensamblar(
            direccionGrados = solicitador.solicitarDireccionVientoConGuia(),
            velocidadViento = solicitador.solicitarVelocidadViento(),
            pesoProyectil = solicitador.solicitarPesoProyectil()
        )
        return ConfigSimulacion(velocidad, angulo, planeta, condiciones)
    }
}
