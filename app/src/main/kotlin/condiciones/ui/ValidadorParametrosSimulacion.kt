package condiciones.ui

import fisicas.gravedad.FuenteGravedad
import ui.ValidadorEntrada
import ui.ValidadorEntradaContract

/**
 * Componente dedicado exclusivamente a evaluar las restricciones de dominio sobre los parámetros
 * numéricos ingresados para la simulación y el configurador de condiciones iniciales.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Separa las reglas de validación de rangos y mensajes
 *   de error respecto del flujo de lectura interactiva en [SolicitadorEntrada].
 */
class ValidadorParametrosSimulacion(
    private val validadorPlaneta: ValidadorEntradaContract = ValidadorEntrada(),
    private val prefijoError: String = "Error:"
) : ValidadorParametros {

    override fun parsearPlaneta(entrada: String?): FuenteGravedad? {
        return if (prefijoError.isNotEmpty()) validadorPlaneta.parsearPlaneta(entrada) else null
    }

    override fun validarVelocidadInicial(valor: Double): String? {
        return if (valor <= 0.0) {
            "$prefijoError La velocidad inicial debe ser estrictamente mayor a 0 (> 0)."
        } else {
            null
        }
    }

    override fun validarAnguloDisparo(valor: Double): String? {
        return if (valor < 0.0 || valor > 90.0) {
            "$prefijoError El ángulo debe estar entre 0 y 90 grados inclusive."
        } else {
            null
        }
    }

    override fun validarDireccionViento(valor: Double): String? {
        return if (valor < 0.0 || valor > 360.0) {
            "$prefijoError La dirección del viento debe estar entre 0.0 y 360.0 (ingresado: $valor)."
        } else {
            null
        }
    }

    override fun validarVelocidadViento(valor: Double): String? {
        return if (valor < 0.0) {
            "$prefijoError La velocidad del viento debe ser mayor o igual a 0.0 (ingresado: $valor)."
        } else {
            null
        }
    }

    override fun validarPesoProyectil(valor: Double): String? {
        return if (valor <= 0.0) {
            "$prefijoError El peso del proyectil debe ser estrictamente mayor a 0.0 kg (ingresado: $valor)."
        } else {
            null
        }
    }
}
