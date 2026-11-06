package condiciones.ui

import condiciones.dominio.CondicionesIniciales
import condiciones.dominio.PuntoRosaVientos
import condiciones.io.EscritorConsola
import condiciones.io.EscritorConsolaEstandar
import condiciones.servicio.CalculadorRumboRosa32Service
import condiciones.servicio.CalculadorRumboService

/**
 * Presentador de consola responsable del formateo y visualización de la rosa de los vientos
 * y del reporte final de condiciones iniciales del tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encargado exclusivamente de la representación
 *   textual de los datos para la interfaz de consola, delegando subtareas en métodos privados cortos.
 * - Alta Cohesión: Todos sus métodos operan sobre los colaboradores de salida y rumbo.
 */
class PresentadorRosaVientosService(
    private val escritor: EscritorConsola = EscritorConsolaEstandar(),
    private val calculadorRumbo: CalculadorRumboService = CalculadorRumboRosa32Service()
) : PresentadorRosaVientos {

    override fun mostrarGuiaGeneralViento() {
        escritor.escribirLinea("Norte: 315° a 45°")
        escritor.escribirLinea("Este: 45° a 135°")
        escritor.escribirLinea("Sur: 135° a 225°")
        escritor.escribirLinea("Oeste: 225° a 315°")
    }

    override fun mostrarRosaDeLosVientos() {
        mostrarEncabezadoRosa()
        val puntos = calculadorRumbo.obtenerPuntos()
        val porCuadrante = calculadorRumbo.obtenerPuntosPorCuadrante()
        for ((cuadrante, lista) in porCuadrante) {
            val titulo = "${cuadrante.numero}. ${cuadrante.descripcion}"
            val cierre = if (cuadrante.numero == 4) puntos.firstOrNull() else null
            mostrarGrupoCuadrante(titulo, lista, cierre)
        }
        escritor.escribirLinea(SEPARADOR)
        escritor.escribirLinea()
    }

    private fun mostrarEncabezadoRosa() {
        escritor.escribirLinea(SEPARADOR)
        escritor.escribirLinea("         ROSA DE LOS VIENTOS (32 RUMBOS / CUARTAS - 11.25° POR PUNTO)          ")
        escritor.escribirLinea(SEPARADOR)
        escritor.escribirLinea("La rosa de los vientos divide el círculo de 360° en 32 divisiones de 11.25° cada una.")
        escritor.escribirLinea("Cada punto cubre un rango de ±5.625° respecto de su ángulo central.")
        escritor.escribirLinea()
    }

    private fun mostrarGrupoCuadrante(
        titulo: String,
        puntos: List<PuntoRosaVientos>,
        cierreNorte: PuntoRosaVientos?
    ) {
        escritor.escribirLinea("▶ Cuadrante $titulo:")
        for (punto in puntos) {
            imprimirPunto(punto)
        }
        if (cierreNorte != null) {
            imprimirCierreNorte(cierreNorte)
        }
        escritor.escribirLinea()
    }

    private fun imprimirCierreNorte(cierreNorte: PuntoRosaVientos) {
        val linea = "   • %-5s (%-24s): %6.2f° [Rango: %s]".format(
            cierreNorte.codigo,
            "${cierreNorte.nombreCompleto} (360°)",
            360.00,
            cierreNorte.formatearRango()
        ).replace(',', '.')
        escritor.escribirLinea(linea)
    }

    private fun imprimirPunto(punto: PuntoRosaVientos) {
        escritor.escribirLinea("   • ${punto.descripcionDetallada()}")
    }

    override fun mostrarReporteFinal(condiciones: CondicionesIniciales) {
        mostrarEncabezadoReporte()
        mostrarSeccionProyectil(condiciones)
        mostrarSeccionVientoBasica(condiciones)
        mostrarSeccionVientoDetalle(condiciones)
        escritor.escribirLinea(SEPARADOR)
    }

    private fun mostrarEncabezadoReporte() {
        escritor.escribirLinea(SEPARADOR)
        escritor.escribirLinea("             REPORTE FINAL: CONDICIONES INICIALES DEL TIRO PARABÓLICO           ")
        escritor.escribirLinea(SEPARADOR)
    }

    private fun mostrarSeccionProyectil(condiciones: CondicionesIniciales) {
        val proyectil = condiciones.proyectil
        escritor.escribirLinea("1. PROYECTIL CONFIGURADO:")
        val lineaPeso = "   • Peso / Masa:             %.2f kg (%.1f gramos)".format(
            proyectil.pesoKg,
            proyectil.pesoEnGramos()
        ).replace(',', '.')
        escritor.escribirLinea(lineaPeso)
        escritor.escribirLinea()
    }

    private fun mostrarSeccionVientoBasica(condiciones: CondicionesIniciales) {
        val viento = condiciones.viento
        val rumbo = viento.rumbo
        escritor.escribirLinea("2. CONDICIONES DEL VIENTO (ROSA DE LOS VIENTOS DE 32 PUNTOS):")
        escritor.escribirLinea("   • Dirección ingresada:     %.2f°".format(viento.direccionGrados).replace(',', '.'))
        escritor.escribirLinea("   • Rumbo exacto calculado:  %s (%s)".format(rumbo.codigo, rumbo.nombreCompleto))
        escritor.escribirLinea("   • Cuadrante de origen:     %s".format(rumbo.cuadrante.descripcion))
        escritor.escribirLinea("   • Rumbo nominal del punto: %.2f°".format(rumbo.anguloNominal).replace(',', '.'))
    }

    private fun mostrarSeccionVientoDetalle(condiciones: CondicionesIniciales) {
        val viento = condiciones.viento
        val rumbo = viento.rumbo
        val desviacion = rumbo.calcularDesviacion(viento.direccionGrados)
        val totalPuntos = calculadorRumbo.obtenerPuntos().size.coerceAtLeast(1)
        val amplitud = 360.0 / totalPuntos
        val rango = "   • Rango de cobertura:      [%s] (amplitud: %.2f°)".format(rumbo.formatearRango(), amplitud).replace(',', '.')
        escritor.escribirLinea(rango)
        escritor.escribirLinea("   • Desviación angular:      %+.2f° con respecto al centro nominal".format(desviacion).replace(',', '.'))
        escritor.escribirLinea("   • Velocidad del viento:    %.2f km/h (%.2f m/s)".format(viento.velocidad, viento.velocidadEnMS()).replace(',', '.'))
        escritor.escribirLinea("   • Origen del viento:       Sopla DESDE el rumbo %s (%s)".format(rumbo.codigo, rumbo.nombreCompleto))
    }

    companion object {
        private const val SEPARADOR: String = "================================================================================"
    }
}
