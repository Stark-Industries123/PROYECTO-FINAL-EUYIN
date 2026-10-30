package condiciones.io

import java.util.NoSuchElementException
import java.util.Scanner

/**
 * Abstracción de operaciones de lectura de entrada de usuario.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Interface Segregation Principle (ISP): Contrato acotado y dedicado a la recepción de líneas de texto y datos validados.
 * - Dependency Inversion Principle (DIP): Desacopla la lógica de aplicación de `System.`in``, `Scanner` o `readlnOrNull()`.
 */
interface LectorConsola {

    /**
     * Lee la siguiente línea introducida por el usuario.
     *
     * @return Cadena leída o `null` si se alcanza el fin del flujo (EOF).
     */
    fun leerLinea(): String?

    /**
     * Lee una línea de texto válida utilizando un bucle de reintento ante entradas en blanco o excepciones.
     *
     * @param reintentos Cantidad máxima de reintentos antes de devolver `null` (por defecto 3).
     * @return Cadena leída no vacía o `null` si se superan los reintentos o finaliza el flujo (EOF).
     */
    fun leerLineaValida(reintentos: Int = 3): String? {
        var intentos = 0
        while (intentos < reintentos) {
            val linea = try {
                leerLinea()
            } catch (e: Exception) {
                null
            } ?: return null

            val texto = linea.trim()
            if (texto.isNotEmpty()) {
                return texto
            }
            intentos++
        }
        return null
    }

    /**
     * Lee iterativamente un valor numérico [Double] ejecutando un bucle con captura de excepciones.
     *
     * @param validador Predicado opcional para verificar rangos o restricciones de negocio.
     * @param onEntradaInvalida Acción ejecutada ante formato no numérico o fallo de validación.
     * @return Valor numérico validado o `null` si el flujo se cerró (EOF).
     */
    fun leerDouble(
        validador: (Double) -> Boolean = { true },
        onEntradaInvalida: ((String) -> Unit)? = null
    ): Double? {
        while (true) {
            val entrada = try {
                leerLinea()
            } catch (e: Exception) {
                onEntradaInvalida?.invoke("Error al leer entrada: ${e.message ?: "Entrada inválida"}")
                continue
            } ?: return null

            val valor = try {
                entrada.trim().toDouble()
            } catch (e: NumberFormatException) {
                onEntradaInvalida?.invoke("Error: Debe ingresar un valor numérico válido.")
                continue
            }

            if (!validador(valor)) {
                onEntradaInvalida?.invoke("Error: El valor ingresado ($valor) se encuentra fuera del rango permitido.")
                continue
            }

            return valor
        }
    }
}

/**
 * Implementación predeterminada de [LectorConsola] que lee desde la entrada estándar de consola (`System.`in``).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Capturar datos desde la consola estándar de forma interactiva
 *   garantizando la resiliencia ante excepciones y cierres de flujo.
 * - Dependency Inversion Principle (DIP): Implementa [LectorConsola] permitiendo sustitución en tests mediante
 *   inyección de [Scanner] o funciones lambda.
 *
 * Características:
 * - Emplea [Scanner] conectado a [System.`in`] con verificación previa vía [Scanner.hasNextLine] para no bloquear
 *   indefinidamente el hilo de ejecución ni lanzar excepciones inesperadas.
 * - Soporta alternativamente [readlnOrNull] con manejo controlado de excepciones ([NoSuchElementException], [IllegalStateException]).
 * - Proporciona bucles seguros ante entradas no válidas y captura completa de excepciones.
 *
 * @property scanner Instancia de [Scanner] sobre la consola del sistema.
 * @property proveedor Función lectora alternativa (útil para pruebas unitarias desacopladas de stdin).
 */
class LectorConsolaEstandar(
    private val scanner: Scanner = Scanner(System.`in`),
    private val proveedor: (() -> String?)? = null
) : LectorConsola {

    /**
     * Constructor secundario que acepta una función proveedora de líneas (por ejemplo [readlnOrNull] o lambdas de test).
     */
    constructor(proveedor: () -> String?) : this(scanner = Scanner(System.`in`), proveedor = proveedor)

    override fun leerLinea(): String? {
        if (proveedor != null) {
            return try {
                proveedor.invoke()
            } catch (e: NoSuchElementException) {
                null
            } catch (e: IllegalStateException) {
                null
            } catch (e: Exception) {
                null
            }
        }

        return try {
            if (scanner.hasNextLine()) {
                scanner.nextLine()
            } else {
                null
            }
        } catch (e: NoSuchElementException) {
            null
        } catch (e: IllegalStateException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        /**
         * Proveedor funcional por defecto que utiliza [readlnOrNull] protegiendo contra excepciones de fin de flujo.
         */
        val PROVEEDOR_DEFECTO: () -> String? = {
            try {
                readlnOrNull()
            } catch (e: NoSuchElementException) {
                null
            } catch (e: IllegalStateException) {
                null
            } catch (e: Exception) {
                null
            }
        }
    }
}
