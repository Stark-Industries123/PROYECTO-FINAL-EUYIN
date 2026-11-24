package ui

import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.Simulador
import java.awt.Component

/**
 * Ensamblador encargado de componer el [EjecutorSimulacionGui] conectando el motor físico,
 * el validador de formulario y el diálogo modal de estadísticas.
 */
class EnsambladorMotorGui(
    private val calculador: CalculadorTrayectoria = Simulador(pasoTiempoMuestreo = 0.016),
    private val fabricaValidador: FabricaValidadorGui = FabricaValidadorGui()
) {

    /**
     * Construye un [EjecutorSimulacionGui] asociado a [componentePadre] y [formulario].
     */
    fun crearMotor(componentePadre: Component, formulario: FormularioParametros): EjecutorSimulacionGui {
        val validador = fabricaValidador.crearValidador(formulario)
        val dialogo = DialogoEstadisticas(componentePadre)
        return EjecutorSimulacionGui(calculador, validador, dialogo)
    }
}
