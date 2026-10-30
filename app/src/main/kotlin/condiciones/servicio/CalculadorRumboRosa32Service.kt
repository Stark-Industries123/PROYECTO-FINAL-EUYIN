package condiciones.servicio

import condiciones.dominio.CuadranteRosa
import condiciones.dominio.PuntoRosaVientos

/**
 * Implementación del servicio de cálculo de rumbos basado en la rosa de los vientos tradicional
 * de 32 rumbos (cuartas).
 *
 * Fundamento Matemático:
 * - El círculo completo abarca 360.0°.
 * - La rosa de los vientos divide el círculo en exactamente 32 divisiones iguales.
 * - Amplitud angular de cada punto: 360.0° / 32 = 11.25° por punto.
 * - Cada punto `i` (0 <= i < 32) posee un ángulo central nominal de: `i * 11.25°`.
 * - Cada rumbo abarca una ventana de ±5.625° (11.25° / 2) alrededor de su ángulo nominal:
 *   [nominal - 5.625°, nominal + 5.625°).
 * - Para calcular el índice del rumbo de forma directa y cerrada:
 *   `indice = floor((anguloNormalizado + 5.625°) / 11.25°) mod 32`
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable exclusivo de la matemática de proyección
 *   azimutal y de suministrar la estructura de los 32 rumbos náuticos.
 * - Open/Closed Principle (OCP): Implementa [CalculadorRumboService] permitiendo sustitución polimórfica.
 * - Inmutabilidad (POO): La colección de puntos se genera una única vez y permanece inmutable.
 */
class CalculadorRumboRosa32Service : CalculadorRumboService {

    private val puntos: List<PuntoRosaVientos> = inicializar32Puntos()

    override fun calcularRumbo(grados: Double): PuntoRosaVientos {
        require(grados in 0.0..360.0) {
            "El ángulo debe estar comprendido entre 0.0 y 360.0 grados (recibido: $grados)."
        }

        // Si el usuario ingresa 360°, se normaliza a 0° (Norte)
        val normalizado = if (grados == 360.0) 0.0 else ((grados % 360.0) + 360.0) % 360.0

        // Aplicación de la fórmula matemática para los 32 puntos de 11.25°:
        val desplazamiento = normalizado + PuntoRosaVientos.SEMI_AMPLITUD_GRADOS
        val indiceCalculado = ((desplazamiento / PuntoRosaVientos.AMPLITUD_PUNTO_GRADOS).toInt()) % TOTAL_PUNTOS

        return puntos[indiceCalculado]
    }

    override fun obtenerPuntos(): List<PuntoRosaVientos> = puntos

    override fun obtenerPuntosPorCuadrante(): Map<CuadranteRosa, List<PuntoRosaVientos>> {
        return puntos.groupBy { it.cuadrante }
    }

    private fun inicializar32Puntos(): List<PuntoRosaVientos> {
        val definiciones = listOf(
            // Cuadrante 1: Norte a Este (0°-90°)
            Triple("N", "Norte", CuadranteRosa.NORTE_A_ESTE),
            Triple("NpE", "Norte por el Este", CuadranteRosa.NORTE_A_ESTE),
            Triple("NNE", "Nornoreste", CuadranteRosa.NORTE_A_ESTE),
            Triple("NEpN", "Noreste por el Norte", CuadranteRosa.NORTE_A_ESTE),
            Triple("NE", "Noreste", CuadranteRosa.NORTE_A_ESTE),
            Triple("NEpE", "Noreste por el Este", CuadranteRosa.NORTE_A_ESTE),
            Triple("ENE", "Estenoreste", CuadranteRosa.NORTE_A_ESTE),
            Triple("EpN", "Este por el Norte", CuadranteRosa.NORTE_A_ESTE),
            Triple("E", "Este", CuadranteRosa.NORTE_A_ESTE),

            // Cuadrante 2: Este a Sur (90°-180°)
            Triple("EpS", "Este por el Sur", CuadranteRosa.ESTE_A_SUR),
            Triple("ESE", "Estesureste", CuadranteRosa.ESTE_A_SUR),
            Triple("SEpE", "Sureste por el Este", CuadranteRosa.ESTE_A_SUR),
            Triple("SE", "Sureste", CuadranteRosa.ESTE_A_SUR),
            Triple("SEpS", "Sureste por el Sur", CuadranteRosa.ESTE_A_SUR),
            Triple("SSE", "Sursureste", CuadranteRosa.ESTE_A_SUR),
            Triple("SpE", "Sur por el Este", CuadranteRosa.ESTE_A_SUR),
            Triple("S", "Sur", CuadranteRosa.ESTE_A_SUR),

            // Cuadrante 3: Sur a Oeste (180°-270°)
            Triple("SpO", "Sur por el Oeste", CuadranteRosa.SUR_A_OESTE),
            Triple("SSO", "Sursuroeste", CuadranteRosa.SUR_A_OESTE),
            Triple("SOpS", "Suroeste por el Sur", CuadranteRosa.SUR_A_OESTE),
            Triple("SO", "Suroeste", CuadranteRosa.SUR_A_OESTE),
            Triple("SOpO", "Suroeste por el Oeste", CuadranteRosa.SUR_A_OESTE),
            Triple("OSO", "Oestesuroeste", CuadranteRosa.SUR_A_OESTE),
            Triple("OpS", "Oeste por el Sur", CuadranteRosa.SUR_A_OESTE),
            Triple("O", "Oeste", CuadranteRosa.SUR_A_OESTE),

            // Cuadrante 4: Oeste a Norte (270°-360°)
            Triple("OpN", "Oeste por el Norte", CuadranteRosa.OESTE_A_NORTE),
            Triple("ONO", "Oestenoroeste", CuadranteRosa.OESTE_A_NORTE),
            Triple("NOpO", "Noroeste por el Oeste", CuadranteRosa.OESTE_A_NORTE),
            Triple("NO", "Noroeste", CuadranteRosa.OESTE_A_NORTE),
            Triple("NOpN", "Noroeste por el Norte", CuadranteRosa.OESTE_A_NORTE),
            Triple("NNO", "Nornoroeste", CuadranteRosa.OESTE_A_NORTE),
            Triple("NpO", "Norte por el Oeste", CuadranteRosa.OESTE_A_NORTE)
        )

        return definiciones.mapIndexed { idx, (codigo, nombre, cuadrante) ->
            val nominal = idx * PuntoRosaVientos.AMPLITUD_PUNTO_GRADOS
            val min = (nominal - PuntoRosaVientos.SEMI_AMPLITUD_GRADOS + 360.0) % 360.0
            val max = (nominal + PuntoRosaVientos.SEMI_AMPLITUD_GRADOS) % 360.0
            PuntoRosaVientos(
                indice = idx,
                codigo = codigo,
                nombreCompleto = nombre,
                anguloNominal = nominal,
                rangoMinGrados = min,
                rangoMaxGrados = max,
                cuadrante = cuadrante
            )
        }
    }

    companion object {
        const val TOTAL_PUNTOS: Int = 32
    }
}
