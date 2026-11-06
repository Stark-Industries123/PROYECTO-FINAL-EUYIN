package ui

import fisicas.gravedad.FuenteGravedad

/**
 * Contrato de abstracción para el selector de cuerpos celestes ([FuenteGravedad]).
 */
interface SelectorPlanetaContract {

    /**
     * Parsea el texto ingresado y retorna la [FuenteGravedad] si corresponde a una opción válida.
     */
    fun seleccionar(entrada: String?): FuenteGravedad?
}
