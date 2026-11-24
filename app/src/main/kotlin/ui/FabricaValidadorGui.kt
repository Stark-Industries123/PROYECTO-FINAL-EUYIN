package ui

import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra

/**
 * Fábrica responsable de construir el [ValidadorFormularioGui] con su fuente de gravedad
 * y validador de entradas por defecto o inyectados.
 */
class FabricaValidadorGui(
    private val fuenteGravedad: FuenteGravedad = Tierra(),
    private val validador: ValidadorEntradaContract = ValidadorEntrada()
) {

    /**
     * Crea un [ValidadorFormularioGui] conectado al [formulario] especificado.
     */
    fun crearValidador(formulario: FormularioParametros): ValidadorFormularioGui {
        return ValidadorFormularioGui(validador, formulario, fuenteGravedad)
    }
}
