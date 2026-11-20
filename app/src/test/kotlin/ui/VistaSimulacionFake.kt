package ui

import fisicas.motor.PuntoTrayectoria

class VistaSimulacionFake(
    private val datos: DatosFormularioSimulacion = DatosFormularioSimulacion()
) : VistaSimulacion {

    private val estado = mutableMapOf<String, Any?>()

    fun obtenerTexto(clave: String): String? = estado[clave] as? String

    fun obtenerPuntos(): List<PuntoTrayectoria> {
        @Suppress("UNCHECKED_CAST")
        return (estado["puntos"] as? List<PuntoTrayectoria>) ?: emptyList()
    }

    fun fueCompletada(): Boolean = estado["completada"] == true

    override fun mostrarVista(nombreVista: String) {
        estado["vista"] = nombreVista
    }

    override fun leerDatosFormulario(): DatosFormularioSimulacion = datos

    override fun aplicarPlanetaVisual(nombrePlaneta: String) {
        estado["planeta"] = nombrePlaneta
    }

    override fun mostrarTrayectoria(trayectoria: List<PuntoTrayectoria>, resumenHud: String) {
        estado["puntos"] = trayectoria
        estado["resumen"] = resumenHud
    }

    override fun completarAnimacionActual() {
        estado["completada"] = true
    }

    override fun mostrarError(mensaje: String) {
        estado["error"] = mensaje
    }

    override fun mostrarEstadisticas(mensaje: String) {
        estado["estadisticas"] = mensaje
    }
}
