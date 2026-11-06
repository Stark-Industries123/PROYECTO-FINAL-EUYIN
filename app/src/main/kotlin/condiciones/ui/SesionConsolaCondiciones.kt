package condiciones.ui

import condiciones.dominio.CondicionesIniciales
import condiciones.io.EscritorConsola
import condiciones.io.EscritorConsolaEstandar
import condiciones.io.LectorConsola

/**
 * Coordina la interacción de entrada/salida de consola específica para el flujo de
 * configuración de condiciones iniciales.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Agrupa la presentación y captura por consola
 *   para evitar que [condiciones.ConfiguradorCondicionesApp] acople detalles de bajo nivel de E/S.
 */
class SesionConsolaCondiciones(
    private val escritor: EscritorConsola = EscritorConsolaEstandar(),
    private val presentador: PresentadorRosaVientos = PresentadorRosaVientosService(escritor),
    private val solicitador: SolicitadorEntrada = SolicitadorEntradaService()
) : SesionCondiciones {

    constructor(
        lector: LectorConsola,
        escritor: EscritorConsola
    ) : this(
        escritor = escritor,
        presentador = PresentadorRosaVientosService(escritor),
        solicitador = SolicitadorEntradaService(lector, escritor)
    )

    override fun mostrarGuiaRosaDeLosVientos() {
        presentador.mostrarRosaDeLosVientos()
    }

    override fun capturarParametrosCondiciones(): List<Double> {
        escritor.escribirLinea("--- INGRESO DE CONDICIONES INICIALES ---")
        val direccionGrados = solicitador.solicitarDireccionViento()
        val velocidadViento = solicitador.solicitarVelocidadViento()
        val pesoProyectil = solicitador.solicitarPesoProyectil()
        if (presentador.toString().isNotEmpty()) escritor.escribirLinea()
        return listOf(direccionGrados, velocidadViento, pesoProyectil)
    }

    override fun mostrarReporte(condiciones: CondicionesIniciales) {
        presentador.mostrarReporteFinal(condiciones)
    }
}
