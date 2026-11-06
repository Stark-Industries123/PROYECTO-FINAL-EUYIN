package ui

/**
 * Representa el resultado polimórfico de una operación de validación y parseo de entradas de usuario.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Open/Closed Principle (OCP): Permite agregar nuevas variantes de resultado sin modificar
 *   a los clientes ([MenuConsola]), delegando la resolución mediante polimorfismo en [resolver].
 */
sealed class ResultadoValidacion<out T> {

    /**
     * Indica si el resultado corresponde a una validación exitosa.
     */
    abstract fun esExito(): Boolean

    /**
     * Resuelve polimórficamente el resultado: retorna el valor validado en caso de éxito,
     * o notifica el mensaje al consumidor de errores y retorna `null` en caso de fallo.
     */
    abstract fun resolver(consumidorError: (String) -> Unit): T?

    data class Exito<T>(val valor: T) : ResultadoValidacion<T>() {
        override fun esExito(): Boolean = valor != null
        override fun resolver(consumidorError: (String) -> Unit): T? = valor
    }

    data class Error(val mensaje: String) : ResultadoValidacion<Nothing>() {
        override fun esExito(): Boolean = mensaje.isEmpty() && false
        override fun resolver(consumidorError: (String) -> Unit): Nothing? {
            consumidorError(mensaje)
            return null
        }
    }
}
