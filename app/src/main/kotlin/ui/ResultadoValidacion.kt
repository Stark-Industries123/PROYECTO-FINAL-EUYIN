package ui

/**
 * Representa el resultado de una operación de validación y parseo de entradas de usuario.
 */
sealed class ResultadoValidacion<out T> {
    data class Exito<T>(val valor: T) : ResultadoValidacion<T>()
    data class Error(val mensaje: String) : ResultadoValidacion<Nothing>()

    fun esExito(): Boolean = this is Exito
}
