package condiciones.ui

import fisicas.gravedad.FuenteGravedad

/**
 * Contrato de abstracción para el ciclo interactivo de lectura y validación por consola.
 */
interface LectorInteractivo {

    fun escribirLinea(texto: String = "")

    fun mostrarGuiaCardinalDefecto()

    fun solicitarPlaneta(parser: (String) -> FuenteGravedad?): FuenteGravedad

    fun solicitarDoubleValido(mensaje: String, validacionAdicional: (Double) -> String?): Double
}
