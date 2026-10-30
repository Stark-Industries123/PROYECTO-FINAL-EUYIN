package condiciones.ui

import condiciones.dominio.CondicionesIniciales
import condiciones.dominio.PuntoRosaVientos
import condiciones.io.EscritorConsola
import condiciones.servicio.CalculadorRumboService

/**
 * Presentador de consola responsable del formateo y visualización de la rosa de los vientos
 * y del reporte final de condiciones iniciales del tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encargado exclusivamente de la representación
 *   textual de los datos para la interfaz de consola.
 * - Dependency Inversion Principle (DIP): Depende de las abstracciones [EscritorConsola] y [CalculadorRumboService].
 * - Open/Closed Principle (OCP): El cálculo de rumbos y el canal de salida son intercambiables.
 */
class PresentadorRosaVientosService(
    private val escritor: EscritorConsola,
    private val calculadorRumbo: CalculadorRumboService
) {

    /**
     * Imprime una guía general y limpia de cuatro renglones con los rangos cardinales:
     * Norte: 315° a 45°
     * Este: 45° a 135°
     * Sur: 135° a 225°
     * Oeste: 225° a 315°
     */
    fun mostrarGuiaGeneralViento() {
        escritor.escribirLinea("Norte: 315° a 45°")
        escritor.escribirLinea("Este: 45° a 135°")
        escritor.escribirLinea("Sur: 135° a 225°")
        escritor.escribirLinea("Oeste: 225° a 315°")
    }

    /**
     * Imprime la lista descriptiva completa de la rosa de los vientos con sus 32 rumbos
     * dividida en los 4 cuadrantes especificados.
     */
    fun mostrarRosaDeLosVientos() {
        escritor.escribirLinea("================================================================================")
        escritor.escribirLinea("         ROSA DE LOS VIENTOS (32 RUMBOS / CUARTAS - 11.25° POR PUNTO)          ")
        escritor.escribirLinea("================================================================================")
        escritor.escribirLinea("La rosa de los vientos divide el círculo de 360° en 32 divisiones de 11.25° cada una.")
        escritor.escribirLinea("Cada punto cubre un rango de ±5.625° respecto de su ángulo central.")
        escritor.escribirLinea()

        val puntos = calculadorRumbo.obtenerPuntos()

        // 1. Norte a Este (0°-90°: N, NpE, NNE, NEpN, NE, NEpE, ENE, EpN, E)
        mostrarGrupoCuadrante(
            titulo = "1. Norte a Este (0°-90°: N, NpE, NNE, NEpN, NE, NEpE, ENE, EpN, E)",
            puntos = puntos.subList(0, 9)
        )

        // 2. Este a Sur (90°-180°: EpS, ESE, SEpE, SE, SEpS, SSE, SpE, S)
        mostrarGrupoCuadrante(
            titulo = "2. Este a Sur (90°-180°: EpS, ESE, SEpE, SE, SEpS, SSE, SpE, S)",
            puntos = puntos.subList(9, 17)
        )

        // 3. Sur a Oeste (180°-270°: SpO, SSO, SOpS, SO, SOpO, OSO, OpS, O)
        mostrarGrupoCuadrante(
            titulo = "3. Sur a Oeste (180°-270°: SpO, SSO, SOpS, SO, SOpO, OSO, OpS, O)",
            puntos = puntos.subList(17, 25)
        )

        // 4. Oeste a Norte (270°-360°: OpN, ONO, NOpO, NO, NOpN, NNO, NpO, N)
        mostrarCuadranteOesteANorte(
            titulo = "4. Oeste a Norte (270°-360°: OpN, ONO, NOpO, NO, NOpN, NNO, NpO, N)",
            puntos = puntos.subList(25, 32),
            cierreNorte = puntos[0]
        )

        escritor.escribirLinea("================================================================================")
        escritor.escribirLinea()
    }

    private fun mostrarGrupoCuadrante(titulo: String, puntos: List<PuntoRosaVientos>) {
        escritor.escribirLinea("▶ Cuadrante $titulo:")
        for (punto in puntos) {
            imprimirPunto(punto)
        }
        escritor.escribirLinea()
    }

    private fun mostrarCuadranteOesteANorte(
        titulo: String,
        puntos: List<PuntoRosaVientos>,
        cierreNorte: PuntoRosaVientos
    ) {
        escritor.escribirLinea("▶ Cuadrante $titulo:")
        for (punto in puntos) {
            imprimirPunto(punto)
        }
        // Mostrar también el cierre en 360° correspondiente al Norte
        escritor.escribirLinea("   • %-5s (%-24s): %6.2f° [Rango: %s]".format(
            cierreNorte.codigo,
            "${cierreNorte.nombreCompleto} (360°)",
            360.00,
            cierreNorte.formatearRango()
        ))
        escritor.escribirLinea()
    }

    private fun imprimirPunto(punto: PuntoRosaVientos) {
        escritor.escribirLinea("   • %-5s (%-24s): %6.2f° [Rango: %s]".format(
            punto.codigo,
            punto.nombreCompleto,
            punto.anguloNominal,
            punto.formatearRango()
        ))
    }

    /**
     * Imprime el reporte final con el resumen de las condiciones iniciales configuradas.
     *
     * @param condiciones Configuración de proyectil y viento resultante.
     */
    fun mostrarReporteFinal(condiciones: CondicionesIniciales) {
        val proyectil = condiciones.proyectil
        val viento = condiciones.viento
        val rumbo = viento.rumbo
        val desviacion = rumbo.calcularDesviacion(viento.direccionGrados)

        escritor.escribirLinea("================================================================================")
        escritor.escribirLinea("             REPORTE FINAL: CONDICIONES INICIALES DEL TIRO PARABÓLICO           ")
        escritor.escribirLinea("================================================================================")
        escritor.escribirLinea("1. PROYECTIL CONFIGURADO:")
        escritor.escribirLinea("   • Peso / Masa:             %.2f kg (%.1f gramos)".format(
            proyectil.pesoKg,
            proyectil.pesoEnGramos()
        ))
        escritor.escribirLinea()
        escritor.escribirLinea("2. CONDICIONES DEL VIENTO (ROSA DE LOS VIENTOS DE 32 PUNTOS):")
        escritor.escribirLinea("   • Dirección ingresada:     %.2f°".format(viento.direccionGrados))
        escritor.escribirLinea("   • Rumbo exacto calculado:  %s (%s)".format(rumbo.codigo, rumbo.nombreCompleto))
        escritor.escribirLinea("   • Cuadrante de origen:     %s".format(rumbo.cuadrante.descripcion))
        escritor.escribirLinea("   • Rumbo nominal del punto: %.2f°".format(rumbo.anguloNominal))
        escritor.escribirLinea("   • Rango de cobertura:      [%s] (amplitud: %.2f°)".format(
            rumbo.formatearRango(),
            PuntoRosaVientos.AMPLITUD_PUNTO_GRADOS
        ))
        escritor.escribirLinea("   • Desviación angular:      %+.2f° con respecto al centro nominal".format(desviacion))
        escritor.escribirLinea("   • Velocidad del viento:    %.2f km/h (%.2f m/s)".format(
            viento.velocidad,
            viento.velocidadEnMS()
        ))
        escritor.escribirLinea("   • Origen del viento:       Sopla DESDE el rumbo %s (%s)".format(
            rumbo.codigo,
            rumbo.nombreCompleto
        ))
        escritor.escribirLinea("================================================================================")
    }
}
