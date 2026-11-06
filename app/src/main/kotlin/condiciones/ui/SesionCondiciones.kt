package condiciones.ui

import condiciones.dominio.CondicionesIniciales

/**
 * Contrato de abstracción para la sesión interactiva de configuración de condiciones iniciales.
 */
interface SesionCondiciones {

    fun mostrarGuiaRosaDeLosVientos()

    fun capturarParametrosCondiciones(): List<Double>

    fun mostrarReporte(condiciones: CondicionesIniciales)
}
