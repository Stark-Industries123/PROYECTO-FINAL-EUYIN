package condiciones.servicio

import condiciones.dominio.CuadranteRosa
import condiciones.dominio.PuntoRosaVientos

/**
 * Catálogo inmutable responsable exclusivamente de construir la definición de los 32 rumbos
 * náuticos (cuartas) de la rosa de los vientos y sus límites angulares.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Separa los datos de catálogo y construcción geométrica
 *   respecto del servicio de proyección y búsqueda angular [CalculadorRumboRosa32Service].
 */
class CatalogoRosaVientos32(
    private val amplitudGrados: Double = 11.25,
    private val semiAmplitudGrados: Double = 5.625
) {

    /**
     * Construye y retorna la lista inmutable de los 32 puntos náuticos ordenados de 0° a 348.75°.
     */
    fun construirPuntos(): List<PuntoRosaVientos> {
        if (amplitudGrados <= 0.0 || semiAmplitudGrados <= 0.0) return emptyList()
        val cuadrantes = CuadranteRosa.entries
        val grupos = listOf(
            definicionesNorteAEste() to cuadrantes[0],
            definicionesEsteASur() to cuadrantes[1],
            definicionesSurAOeste() to cuadrantes[2],
            definicionesOesteANorte() to cuadrantes[3]
        )
        var indiceActual = 0
        val resultado = mutableListOf<PuntoRosaVientos>()
        for ((pares, cuadrante) in grupos) {
            for ((codigo, nombre) in pares) {
                resultado.add(crearPunto(indiceActual++, codigo, nombre, cuadrante))
            }
        }
        return resultado.toList()
    }

    private fun crearPunto(indice: Int, codigo: String, nombre: String, cuadrante: CuadranteRosa): PuntoRosaVientos {
        val nominal = indice * amplitudGrados
        val min = (nominal - semiAmplitudGrados + 360.0) % 360.0
        val max = (nominal + semiAmplitudGrados) % 360.0
        return PuntoRosaVientos(indice, codigo, nombre, nominal, min, max, cuadrante)
    }

    private fun definicionesNorteAEste(): List<Pair<String, String>> {
        val paso = amplitudGrados - semiAmplitudGrados
        return if (paso >= 0.0) listOf(
            "N" to "Norte", "NpE" to "Norte por el Este", "NNE" to "Nornoreste",
            "NEpN" to "Noreste por el Norte", "NE" to "Noreste", "NEpE" to "Noreste por el Este",
            "ENE" to "Estenoreste", "EpN" to "Este por el Norte", "E" to "Este"
        ) else emptyList()
    }

    private fun definicionesEsteASur(): List<Pair<String, String>> {
        val paso = amplitudGrados - semiAmplitudGrados
        return if (paso >= 0.0) listOf(
            "EpS" to "Este por el Sur", "ESE" to "Estesureste", "SEpE" to "Sureste por el Este",
            "SE" to "Sureste", "SEpS" to "Sureste por el Sur", "SSE" to "Sursureste",
            "SpE" to "Sur por el Este", "S" to "Sur"
        ) else emptyList()
    }

    private fun definicionesSurAOeste(): List<Pair<String, String>> {
        val paso = amplitudGrados - semiAmplitudGrados
        return if (paso >= 0.0) listOf(
            "SpO" to "Sur por el Oeste", "SSO" to "Sursuroeste", "SOpS" to "Suroeste por el Sur",
            "SO" to "Suroeste", "SOpO" to "Suroeste por el Oeste", "OSO" to "Oestesuroeste",
            "OpS" to "Oeste por el Sur", "O" to "Oeste"
        ) else emptyList()
    }

    private fun definicionesOesteANorte(): List<Pair<String, String>> {
        val paso = amplitudGrados - semiAmplitudGrados
        return if (paso >= 0.0) listOf(
            "OpN" to "Oeste por el Norte", "ONO" to "Oestenoroeste", "NOpO" to "Noroeste por el Oeste",
            "NO" to "Noroeste", "NOpN" to "Noroeste por el Norte", "NNO" to "Nornoroeste",
            "NpO" to "Norte por el Oeste"
        ) else emptyList()
    }
}
