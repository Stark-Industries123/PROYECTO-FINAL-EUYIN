package condiciones.ui

import condiciones.io.EscritorConsola
import condiciones.io.EscritorConsolaEstandar
import condiciones.io.LectorConsola
import condiciones.io.LectorConsolaEstandar
import fisicas.gravedad.FuenteGravedad

/**
 * Gestiona el ciclo interactivo de lectura, parseo numérico seguro y control de fin de flujo (EOF)
 * sobre los canales [LectorConsola] y [EscritorConsola].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Aísla la mecánica de reintentos de consola y control de E/S
 *   respecto de las reglas de negocio de cada parámetro.
 */
class LectorInteractivoConsola(
    private val lector: LectorConsola = LectorConsolaEstandar(),
    private val escritor: EscritorConsola = EscritorConsolaEstandar()
) {

    fun escribirLinea(texto: String = "") {
        escritor.escribirLinea(texto)
    }

    fun mostrarGuiaCardinalDefecto() {
        escritor.escribirLinea("Norte: 315° a 45°")
        escritor.escribirLinea("Este: 45° a 135°")
        escritor.escribirLinea("Sur: 135° a 225°")
        escritor.escribirLinea("Oeste: 225° a 315°")
    }

    fun solicitarPlaneta(parser: (String) -> FuenteGravedad?): FuenteGravedad {
        while (true) {
            mostrarMenuPlanetas()
            val entrada = leerEntradaSegura() ?: continue
            val planeta = parser(entrada)
            if (planeta != null) {
                return planeta
            }
            escritor.escribirLinea("Opción inválida. Por favor, selecciona un número del 1 al 4.")
        }
    }

    private fun mostrarMenuPlanetas() {
        escritor.escribirLinea("\nSelecciona el entorno gravitacional:")
        escritor.escribirLinea("1. Tierra")
        escritor.escribirLinea("2. Luna")
        escritor.escribirLinea("3. Mercurio")
        escritor.escribirLinea("4. Júpiter")
        escritor.escribir("Opción (1-4): ")
    }

    fun solicitarDoubleValido(mensaje: String, validacionAdicional: (Double) -> String?): Double {
        while (true) {
            escritor.escribir(mensaje)
            val entrada = leerEntradaSegura() ?: continue
            val valor = entrada.toDoubleOrNull()
            if (valor == null) {
                escritor.escribirLinea("Error: Debe ingresar un valor numérico válido.")
                continue
            }
            val error = validacionAdicional(valor)
            if (error == null) return valor
            escritor.escribirLinea(error)
        }
    }

    private fun leerEntradaSegura(): String? {
        val entrada = try {
            lector.leerLinea()?.trim()
        } catch (e: Exception) {
            val detalle = e.message ?: "fallo de lectura"
            escritor.escribirLinea("Error de lectura: $detalle. Intente nuevamente.")
            return null
        }
        if (entrada == null) {
            escritor.escribirLinea("Error: Fin de la entrada de datos (EOF). No se puede continuar.")
            throw IllegalStateException("Fin de flujo de entrada alcanzado.")
        }
        return entrada
    }
}
