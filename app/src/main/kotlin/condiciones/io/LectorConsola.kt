package condiciones.io

/**
 * Abstracción de operaciones de lectura de entrada de usuario.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Define el contrato de recepción de líneas de texto y valores validados.
 * - Interface Segregation Principle (ISP): Contrato acotado y dedicado a la lectura de consola.
 */
interface LectorConsola {

    /**
     * Lee la siguiente línea introducida por el usuario, o `null` si se alcanza EOF.
     */
    fun leerLinea(): String?

    /**
     * Lee una línea de texto no vacía aplicando hasta [reintentos] intentos.
     */
    fun leerLineaValida(reintentos: Int = 3): String? {
        for (intento in 0 until reintentos) {
            val linea = leerLineaSegura(null) ?: return null
            val texto = linea.trim()
            if (texto.isNotEmpty()) {
                return texto
            }
        }
        return null
    }

    /**
     * Lee iterativamente un valor numérico [Double] validado mediante [validador].
     */
    fun leerDouble(
        validador: (Double) -> Boolean = { true },
        onEntradaInvalida: ((String) -> Unit)? = null
    ): Double? {
        while (true) {
            val entrada = leerLineaSegura(onEntradaInvalida) ?: return null
            val valorValido = procesarCandidatoDouble(entrada, validador, onEntradaInvalida)
            if (valorValido != null) {
                return valorValido
            }
        }
    }

    private fun leerLineaSegura(onEntradaInvalida: ((String) -> Unit)?): String? {
        return try {
            leerLinea()
        } catch (e: Exception) {
            val detalle = e.message ?: "fallo de flujo"
            onEntradaInvalida?.invoke("Error al leer entrada: $detalle")
            ""
        }
    }

    private fun procesarCandidatoDouble(
        entrada: String,
        validador: (Double) -> Boolean,
        onEntradaInvalida: ((String) -> Unit)?
    ): Double? {
        val valor = entrada.trim().toDoubleOrNull()
        if (valor == null) {
            onEntradaInvalida?.invoke("Error: Debe ingresar un valor numérico válido.")
            return null
        }
        if (!validador(valor)) {
            onEntradaInvalida?.invoke("Error: El valor ingresado ($valor) se encuentra fuera del rango permitido.")
            return null
        }
        return valor
    }
}
