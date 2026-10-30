package condiciones.dominio

/**
 * Agregado del modelo de dominio que encapsula las condiciones iniciales completas
 * configuradas para el tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encapsula la relación entre proyectil y viento
 *   sin acoplarse con interfaces de usuario ni motores físicos concretos.
 * - Inmutabilidad (POO): Estructura inmutable y coherente.
 *
 * @property proyectil Modelo del proyectil configurado ([Proyectil]).
 * @property viento Modelo del viento configurado con su rumbo exacto ([Viento]).
 */
data class CondicionesIniciales(
    val proyectil: Proyectil,
    val viento: Viento
) {
    /**
     * Retorna una síntesis descriptiva de las condiciones iniciales.
     */
    fun descripcionResumen(): String {
        return "${proyectil.descripcionFormateada()} | ${viento.descripcionFormateada()}"
    }
}
