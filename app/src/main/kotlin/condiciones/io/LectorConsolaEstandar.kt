package condiciones.io

import java.util.Scanner

/**
 * Implementación predeterminada de [LectorConsola] que lee desde la entrada estándar (`System.`in``).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encapsula exclusivamente la lectura física desde
 *   un [Scanner] o función proveedora inyectada con resiliencia ante cierres de flujo.
 */
class LectorConsolaEstandar(
    private val scanner: Scanner = Scanner(System.`in`),
    private val proveedor: (() -> String?)? = null
) : LectorConsola {

    /**
     * Constructor secundario que acepta una función proveedora de líneas para pruebas unitarias.
     */
    constructor(proveedor: () -> String?) : this(scanner = Scanner(System.`in`), proveedor = proveedor)

    override fun leerLinea(): String? {
        return try {
            if (proveedor != null) proveedor.invoke() else if (scanner.hasNextLine()) scanner.nextLine() else null
        } catch (e: Exception) {
            null
        }
    }
}
