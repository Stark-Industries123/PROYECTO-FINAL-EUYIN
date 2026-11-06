package ui

import fisicas.gravedad.FuenteGravedad

/**
 * Componente de dominio UI encargado de la validación y parseo seguro de entradas de consola.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Valida rangos numéricos de entrada, delegando
 *   la resolución del cuerpo celeste en [SelectorPlaneta].
 * - Alta Cohesión: Todos los métodos comparten el estado de validación de la instancia.
 */
class ValidadorEntrada(
    private val selectorPlaneta: SelectorPlanetaContract = SelectorPlaneta()
) : ValidadorEntradaContract {

    /**
     * Parsea una opción numérica de texto al cuerpo celeste correspondiente.
     */
    override fun parsearPlaneta(entrada: String?): FuenteGravedad? = selectorPlaneta.seleccionar(entrada)

    /**
     * Valida y parsea la magnitud de la velocidad inicial (> 0 m/s).
     */
    override fun parsearVelocidad(entrada: String?): ResultadoValidacion<Double> {
        return validarNumero(entrada, "20.0", "Error: La velocidad inicial debe ser estrictamente mayor a 0 (> 0).") { it > 0.0 }
    }

    /**
     * Valida y parsea el ángulo de elevación (0° a 90° inclusive).
     */
    override fun parsearAngulo(entrada: String?): ResultadoValidacion<Double> {
        return validarNumero(entrada, "45.0", "Error: El ángulo debe estar entre 0 y 90 grados inclusive.") { it in 0.0..90.0 }
    }

    /**
     * Valida y parsea la masa del proyectil (> 0 kg).
     */
    override fun parsearMasa(entrada: String?): ResultadoValidacion<Double> {
        return validarNumero(entrada, "1.0", "Error: La masa debe ser estrictamente mayor a 0 (> 0).") { it > 0.0 }
    }

    /**
     * Valida y parsea la velocidad del viento (>= 0 km/h).
     */
    override fun parsearViento(entrada: String?): ResultadoValidacion<Double> {
        return validarNumero(entrada, "15.0", "Error: La velocidad del viento debe ser mayor o igual a 0 (>= 0).") { it >= 0.0 }
    }

    /**
     * Valida y parsea la dirección azimutal del viento en grados (0° a 360°).
     */
    override fun parsearDireccion(entrada: String?): ResultadoValidacion<Double> {
        return validarNumero(entrada, "90.0", "Error: La dirección del viento debe estar entre 0 y 360 grados inclusive.") { it in 0.0..360.0 }
    }

    private fun validarNumero(entrada: String?, ejemplo: String, mensajeError: String, esValido: (Double) -> Boolean): ResultadoValidacion<Double> {
        val valor = entrada?.trim()?.toDoubleOrNull()
            ?: return ResultadoValidacion.Error("Entrada inválida. Debes ingresar un número válido (ej: $ejemplo).")
        return if (esValido(valor)) ResultadoValidacion.Exito(valor) else ResultadoValidacion.Error(mensajeError)
    }
}
