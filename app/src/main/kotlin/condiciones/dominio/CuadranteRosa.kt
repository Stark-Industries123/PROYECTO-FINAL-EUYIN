package condiciones.dominio

/**
 * Representa los cuatro cuadrantes de la rosa de los vientos para la delimitación
 * de los 32 rumbos o cuartas náuticas.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es modelar
 *   los cuatro sectores cuadrantales y su rango de grados asociado.
 * - Inmutabilidad (POO): Estructura enum inmutable y segura ante concurrencia.
 *
 * @property numero Número identificador del cuadrante (1 al 4).
 * @property descripcion Denominación descriptiva y lista canónica de rumbos.
 * @property rangoMinGrados Grado inicial del cuadrante (inclusive).
 * @property rangoMaxGrados Grado final del cuadrante (inclusive).
 */
enum class CuadranteRosa(
    val numero: Int,
    val descripcion: String,
    val rangoMinGrados: Double,
    val rangoMaxGrados: Double
) {
    NORTE_A_ESTE(
        numero = 1,
        descripcion = "Norte a Este (0°-90°: N, NpE, NNE, NEpN, NE, NEpE, ENE, EpN, E)",
        rangoMinGrados = 0.0,
        rangoMaxGrados = 90.0
    ),
    ESTE_A_SUR(
        numero = 2,
        descripcion = "Este a Sur (90°-180°: EpS, ESE, SEpE, SE, SEpS, SSE, SpE, S)",
        rangoMinGrados = 90.0,
        rangoMaxGrados = 180.0
    ),
    SUR_A_OESTE(
        numero = 3,
        descripcion = "Sur a Oeste (180°-270°: SpO, SSO, SOpS, SO, SOpO, OSO, OpS, O)",
        rangoMinGrados = 180.0,
        rangoMaxGrados = 270.0
    ),
    OESTE_A_NORTE(
        numero = 4,
        descripcion = "Oeste a Norte (270°-360°: OpN, ONO, NOpO, NO, NOpN, NNO, NpO, N)",
        rangoMinGrados = 270.0,
        rangoMaxGrados = 360.0
    );

    /**
     * Verifica si un ángulo en grados sexagesimales pertenece al rango angular del cuadrante.
     *
     * @param grados Ángulo en grados.
     * @return `true` si el ángulo está comprendido en el cuadrante, `false` en caso contrario.
     */
    fun contieneAngulo(grados: Double): Boolean {
        val normalizado = ((grados % 360.0) + 360.0) % 360.0
        return when (this) {
            NORTE_A_ESTE -> normalizado >= 0.0 && normalizado <= 90.0
            ESTE_A_SUR -> normalizado >= 90.0 && normalizado <= 180.0
            SUR_A_OESTE -> normalizado >= 180.0 && normalizado <= 270.0
            OESTE_A_NORTE -> (normalizado >= 270.0 && normalizado <= 360.0) || normalizado == 0.0
        }
    }
}
