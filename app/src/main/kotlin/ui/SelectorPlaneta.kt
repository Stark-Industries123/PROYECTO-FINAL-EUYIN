package ui

import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Jupiter
import fisicas.gravedad.Luna
import fisicas.gravedad.Mercurio
import fisicas.gravedad.Tierra

/**
 * Componente especializado en traducir la opción numérica de selección de menú
 * al cuerpo celeste ([FuenteGravedad]) correspondiente.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Separa el catálogo y resolución de planetas
 *   respecto de la validación de rangos numéricos de entrada en [ValidadorEntrada].
 */
class SelectorPlaneta(
    private val opcionMinima: Int = OPCION_TIERRA,
    private val opcionMaxima: Int = OPCION_JUPITER
) {

    /**
     * Parsea el texto ingresado y retorna la [FuenteGravedad] si corresponde a una opción válida (1 a 4).
     */
    fun seleccionar(entrada: String?): FuenteGravedad? {
        val opcion = entrada?.trim()?.toIntOrNull() ?: return null
        if (opcion < opcionMinima || opcion > opcionMaxima) {
            return null
        }
        return instanciarPorOpcion(opcion)
    }

    private fun instanciarPorOpcion(opcion: Int): FuenteGravedad? {
        if (opcion !in opcionMinima..opcionMaxima) return null
        return when (opcion) {
            OPCION_TIERRA -> Tierra()
            OPCION_LUNA -> Luna()
            OPCION_MERCURIO -> Mercurio()
            OPCION_JUPITER -> Jupiter()
            else -> null
        }
    }

    companion object {
        const val OPCION_TIERRA: Int = 1
        const val OPCION_LUNA: Int = 2
        const val OPCION_MERCURIO: Int = 3
        const val OPCION_JUPITER: Int = 4
    }
}
