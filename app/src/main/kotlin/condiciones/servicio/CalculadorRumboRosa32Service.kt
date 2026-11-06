package condiciones.servicio

import condiciones.dominio.CuadranteRosa
import condiciones.dominio.PuntoRosaVientos

/**
 * Implementación del servicio de cálculo de rumbos basado en la rosa de los vientos tradicional
 * de 32 rumbos (cuartas).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable exclusivo del cálculo de proyección
 *   azimutal y consulta de rumbos, delegando la creación del catálogo a [CatalogoRosaVientos32].
 * - Alta Cohesión: Todos sus métodos operan sobre la colección inmutable [puntos].
 */
class CalculadorRumboRosa32Service(
    catalogo: CatalogoRosaVientos32 = CatalogoRosaVientos32()
) : CalculadorRumboService {

    private val puntos: List<PuntoRosaVientos> = catalogo.construirPuntos()

    override fun calcularRumbo(grados: Double): PuntoRosaVientos {
        require(grados in 0.0..360.0) {
            "El ángulo debe estar comprendido entre 0.0 y 360.0 grados (recibido: $grados)."
        }
        val indice = calcularIndiceRumbo(grados)
        return puntos[indice]
    }

    private fun calcularIndiceRumbo(grados: Double): Int {
        val total = puntos.size
        val normalizado = if (grados == 360.0) 0.0 else ((grados % 360.0) + 360.0) % 360.0
        val desplazamiento = normalizado + (360.0 / (total * 2.0))
        val amplitud = 360.0 / total
        return (desplazamiento / amplitud).toInt() % total
    }

    override fun obtenerPuntos(): List<PuntoRosaVientos> = puntos

    override fun obtenerPuntosPorCuadrante(): Map<CuadranteRosa, List<PuntoRosaVientos>> {
        return puntos.groupBy { it.cuadrante }
    }

    companion object {
        const val TOTAL_PUNTOS: Int = 32
    }
}
