package ui

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad

/**
 * Validador de entradas de la interfaz gráfica encargado de transformar los textos del
 * [FormularioParametros] en una [ConfigSimulacion] válida usando [ValidadorEntradaContract].
 */
class ValidadorFormularioGui(
    private val validador: ValidadorEntradaContract,
    private val formulario: FormularioParametros,
    private var fuenteGravedad: FuenteGravedad
) : ValidadorConfiguracionGui {

    override fun sincronizarPlanetaInicial(): String {
        val nombreActual = fuenteGravedad.obtenerNombre()
        formulario.seleccionarPlaneta(nombreActual)
        return nombreActual
    }

    override fun actualizarPlanetaPorIndice(indice: Int): String {
        val opcion = (indice + 1).toString()
        fuenteGravedad = validador.parsearPlaneta(opcion) ?: fuenteGravedad
        return fuenteGravedad.obtenerNombre()
    }

    override fun actualizarEntradas(valores: Map<String, String>, direccionVientoGrados: Double?) {
        if (direccionVientoGrados != null) {
            formulario.actualizarEntradas(valores, direccionVientoGrados)
        } else {
            formulario.actualizarEntradas(valores)
        }
    }

    override fun construirConfiguracion(alError: (String) -> Unit): ConfigSimulacion? {
        val datos = formulario.extraerDatosCrudos()
        val velocidad = validarVelocidad(datos[FormularioParametros.CLAVE_VELOCIDAD], alError) ?: return null
        val angulo = validador.parsearAngulo(datos[FormularioParametros.CLAVE_ANGULO]).resolver(alError) ?: return null
        val masa = validador.parsearMasa(datos[FormularioParametros.CLAVE_MASA]).resolver(alError) ?: return null
        val viento = validador.parsearViento(datos[FormularioParametros.CLAVE_VIENTO]).resolver(alError) ?: return null
        val direccion = datos[FormularioParametros.CLAVE_DIRECCION]?.toDoubleOrNull() ?: 0.0
        val planeta = validador.parsearPlaneta(datos[FormularioParametros.CLAVE_PLANETA]) ?: fuenteGravedad
        fuenteGravedad = planeta
        return ConfigSimulacion(
            velocidadInicial = velocidad,
            angulo = angulo,
            fuenteGravedad = planeta,
            masaKg = masa,
            velocidadVientoKmH = viento,
            direccionVientoGrados = direccion
        )
    }

    private fun validarVelocidad(texto: String?, alError: (String) -> Unit): Double? {
        val velocidad = validador.parsearVelocidad(texto).resolver(alError) ?: return null
        if (!velocidad.isFinite() || velocidad > VELOCIDAD_MAXIMA_MS) {
            alError("Error: La velocidad inicial no puede superar $VELOCIDAD_MAXIMA_MS m/s.")
            return null
        }
        return velocidad
    }

    companion object {
        private const val VELOCIDAD_MAXIMA_MS: Double = 1000.0
    }
}
