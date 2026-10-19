package ui

import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Jupiter
import fisicas.gravedad.Luna
import fisicas.gravedad.Mercurio
import fisicas.gravedad.Tierra

/**
 * Representa el resultado de una operación de validación y parseo de entradas de usuario.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encapsula el resultado exitoso o fallido de una validación.
 * - Inmutabilidad (POO): Estructura inmutable con tipos algebraicos seguros.
 */
sealed class ResultadoValidacion<out T> {
    data class Exito<T>(val valor: T) : ResultadoValidacion<T>()
    data class Error(val mensaje: String) : ResultadoValidacion<Nothing>()
}

/**
 * Componente de dominio UI encargado de la validación y parseo seguro de entradas de consola.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única razón para cambiar es la modificación
 *   de las reglas de validación o conversión de datos de entrada. Desacopla las reglas de validación
 *   de la coordinación de entrada/salida (I/O).
 * - Baja Complejidad Ciclomática: Cada método está enfocado en una regla de negocio atómica
 *   con menos de 4 caminos condicionales.
 */
class ValidadorEntrada {

    /**
     * Parsea una opción numérica de texto al cuerpo celeste correspondiente.
     *
     * @param entrada Texto ingresado por el usuario.
     * @return [FuenteGravedad] si la opción es válida (1 a 4), o null si es inválida.
     */
    fun parsearPlaneta(entrada: String?): FuenteGravedad? {
        val opcion = entrada?.trim()?.toIntOrNull() ?: return null
        return mapearPlanetaPorOpcion(opcion)
    }

    private fun mapearPlanetaPorOpcion(opcion: Int): FuenteGravedad? {
        return when (opcion) {
            OPCION_TIERRA -> Tierra()
            OPCION_LUNA -> Luna()
            OPCION_MERCURIO -> Mercurio()
            OPCION_JUPITER -> Jupiter()
            else -> null
        }
    }

    /**
     * Valida y parsea la magnitud de la velocidad inicial (> 0 m/s).
     */
    fun parsearVelocidad(entrada: String?): ResultadoValidacion<Double> {
        val valor = entrada?.trim()?.toDoubleOrNull()
            ?: return ResultadoValidacion.Error("Entrada inválida. Debes ingresar un número válido (ej: 20.0).")

        if (valor <= 0.0) {
            return ResultadoValidacion.Error("Error: La velocidad inicial debe ser estrictamente mayor a 0 (> 0).")
        }
        return ResultadoValidacion.Exito(valor)
    }

    /**
     * Valida y parsea el ángulo de elevación (0° a 90° inclusive).
     */
    fun parsearAngulo(entrada: String?): ResultadoValidacion<Double> {
        val valor = entrada?.trim()?.toDoubleOrNull()
            ?: return ResultadoValidacion.Error("Entrada inválida. Debes ingresar un número válido (ej: 45.0).")

        if (valor < 0.0 || valor > 90.0) {
            return ResultadoValidacion.Error("Error: El ángulo debe estar entre 0 y 90 grados inclusive.")
        }
        return ResultadoValidacion.Exito(valor)
    }

    /**
     * Valida y parsea la masa del proyectil (> 0 kg).
     */
    fun parsearMasa(entrada: String?): ResultadoValidacion<Double> {
        val valor = entrada?.trim()?.toDoubleOrNull()
            ?: return ResultadoValidacion.Error("Entrada inválida. Debes ingresar un número válido (ej: 1.0).")

        if (valor <= 0.0) {
            return ResultadoValidacion.Error("Error: La masa debe ser estrictamente mayor a 0 (> 0).")
        }
        return ResultadoValidacion.Exito(valor)
    }

    /**
     * Valida y parsea la velocidad del viento (>= 0 km/h).
     */
    fun parsearViento(entrada: String?): ResultadoValidacion<Double> {
        val valor = entrada?.trim()?.toDoubleOrNull()
            ?: return ResultadoValidacion.Error("Entrada inválida. Debes ingresar un número válido (ej: 15.0).")

        if (valor < 0.0) {
            return ResultadoValidacion.Error("Error: La velocidad del viento debe ser mayor o igual a 0 (>= 0).")
        }
        return ResultadoValidacion.Exito(valor)
    }

    /**
     * Valida y parsea la dirección azimutal del viento en grados (0° a 360°).
     */
    fun parsearDireccion(entrada: String?): ResultadoValidacion<Double> {
        val valor = entrada?.trim()?.toDoubleOrNull()
            ?: return ResultadoValidacion.Error("Entrada inválida. Debes ingresar un número válido (ej: 90.0).")

        if (valor < 0.0 || valor > 360.0) {
            return ResultadoValidacion.Error("Error: La dirección del viento debe estar entre 0 y 360 grados inclusive.")
        }
        return ResultadoValidacion.Exito(valor)
    }

    companion object {
        private const val OPCION_TIERRA = 1
        private const val OPCION_LUNA = 2
        private const val OPCION_MERCURIO = 3
        private const val OPCION_JUPITER = 4
    }
}
