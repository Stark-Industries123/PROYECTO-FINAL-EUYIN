package ui

/**
 * Transporta de forma inmutable los valores crudos ingresados en el formulario de configuración
 * para ser validados y transformados en una configuración física por el controlador.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela los datos de entrada de la UI y su normalización
 *   básica, evitando listas largas de parámetros y desacoplando los componentes Swing del controlador.
 */
data class DatosFormularioSimulacion(
    val opcionPlaneta: String = "1",
    val velocidadTexto: String = "35.0",
    val anguloTexto: String = "45.0",
    val masaTexto: String = "1.0",
    val vientoTexto: String = "10.0",
    val direccionVientoGrados: Double = 0.0
) {

    /**
     * Verifica que los campos numéricos obligatorios contengan texto no vacío.
     */
    fun tieneCamposObligatorios(): Boolean {
        return velocidadTexto.isNotBlank() && anguloTexto.isNotBlank() && masaTexto.isNotBlank()
    }

    /**
     * Retorna el identificador de opción del planeta sin espacios y con respaldo a la opción por defecto.
     */
    fun opcionPlanetaNormalizada(): String = opcionPlaneta.trim().ifEmpty { "1" }

    /**
     * Retorna la dirección del viento normalizada al intervalo [0.0, 360.0).
     */
    fun direccionNormalizada(): Double = ((direccionVientoGrados % 360.0) + 360.0) % 360.0
}
