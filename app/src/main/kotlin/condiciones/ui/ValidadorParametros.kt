package condiciones.ui

import fisicas.gravedad.FuenteGravedad

/**
 * Contrato de abstracción para la validación de parámetros de simulación.
 */
interface ValidadorParametros {

    fun parsearPlaneta(entrada: String?): FuenteGravedad?

    fun validarVelocidadInicial(valor: Double): String?

    fun validarAnguloDisparo(valor: Double): String?

    fun validarDireccionViento(valor: Double): String?

    fun validarVelocidadViento(valor: Double): String?

    fun validarPesoProyectil(valor: Double): String?
}
