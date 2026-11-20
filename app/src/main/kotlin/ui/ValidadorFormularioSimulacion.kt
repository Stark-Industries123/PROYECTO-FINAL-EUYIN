package ui

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra

/**
 * Traduce y valida los datos crudos de [DatosFormularioSimulacion] en una instancia válida de
 * [ConfigSimulacion], aplicando las reglas del validador inyectado y el límite máximo de la GUI.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Concentra la validación del formulario y la resolución
 *   del planeta seleccionado sin acoplar el controlador ni la ventana a las reglas de parseo.
 * - Dependency Inversion Principle (DIP): Depende de [ValidadorEntradaContract] y [FuenteGravedad].
 */
class ValidadorFormularioSimulacion(
    private val validador: ValidadorEntradaContract = ValidadorEntrada(),
    private var planetaActual: FuenteGravedad = Tierra()
) {

    /**
     * Retorna la fuente de gravedad actualmente seleccionada.
     */
    fun obtenerPlanetaActual(): FuenteGravedad = planetaActual

    /**
     * Resuelve y actualiza la fuente de gravedad a partir de la opción seleccionada en la UI.
     */
    fun actualizarPlaneta(opcionPlaneta: String): FuenteGravedad {
        planetaActual = validador.parsearPlaneta(opcionPlaneta) ?: planetaActual
        return planetaActual
    }

    /**
     * Valida todos los campos de [datos] y construye [ConfigSimulacion], o notifica el error en [alError].
     */
    fun construirConfiguracion(
        datos: DatosFormularioSimulacion,
        alError: (String) -> Unit
    ): ConfigSimulacion? {
        val velocidad = validarVelocidad(datos.velocidadTexto, alError) ?: return null
        val angulo = validador.parsearAngulo(datos.anguloTexto).resolver(alError) ?: return null
        val masa = validador.parsearMasa(datos.masaTexto).resolver(alError) ?: return null
        val viento = validador.parsearViento(datos.vientoTexto).resolver(alError) ?: return null
        val planeta = actualizarPlaneta(datos.opcionPlanetaNormalizada())
        return ConfigSimulacion(
            velocidadInicial = velocidad,
            angulo = angulo,
            fuenteGravedad = planeta,
            masaKg = masa,
            velocidadVientoKmH = viento,
            direccionVientoGrados = datos.direccionNormalizada()
        )
    }

    private fun validarVelocidad(velocidadTexto: String, alError: (String) -> Unit): Double? {
        val velocidad = validador.parsearVelocidad(velocidadTexto).resolver(alError) ?: return null
        if (!velocidad.isFinite() || velocidad > VELOCIDAD_MAXIMA_MS) {
            alError("Error: La velocidad inicial no puede superar $VELOCIDAD_MAXIMA_MS m/s.")
            return null
        }
        return velocidad
    }

    companion object {
        const val VELOCIDAD_MAXIMA_MS: Double = 1000.0
    }
}
