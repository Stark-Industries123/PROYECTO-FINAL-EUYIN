package fisicas.motor

import fisicas.gravedad.FuenteGravedad
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

// Data class para empaquetar los resultados limpios (abierto a extensión con valor por defecto)
data class ResultadoSimulacion(
    val tiempoVuelo: Double,
    val distanciaMaxima: Double,
    val alturaMaxima: Double,
    val trayectoria: List<PuntoTrayectoria> = emptyList()
)

class Simulador(private val gravedadFoco: FuenteGravedad) {

    fun calcularTrayectoria(velocidadInicial: Double, anguloGrados: Double): ResultadoSimulacion {
        // Accedemos a la propiedad definida en tu interfaz
        val g = gravedadFoco.gravedad 
        
        // Convertir grados a radianes para usar las funciones trigonométricas
        val anguloRadianes = anguloGrados * PI / 180.0

        // Descomponer velocidad inicial
        val v0x = velocidadInicial * cos(anguloRadianes)
        val v0y = velocidadInicial * sin(anguloRadianes)

        // Fórmulas físicas del tiro parabólico
        val tiempoVuelo = (2 * v0y) / g
        val distanciaMaxima = v0x * tiempoVuelo
        val alturaMaxima = (v0y * v0y) / (2 * g)

        // Cálculo de los puntos de la trayectoria desde t = 0.0 hasta tiempoVuelo en pasos de 0.1 s
        val trayectoria = mutableListOf<PuntoTrayectoria>()
        var t = 0.0

        while (t <= tiempoVuelo + EPSILON) {
            val x = velocidadInicial * cos(anguloRadianes) * t
            val y = velocidadInicial * sin(anguloRadianes) * t - (0.5 * g * t * t)
            trayectoria.add(PuntoTrayectoria(tiempo = t, x = x, y = y))
            t += PASO_TIEMPO
        }

        return ResultadoSimulacion(tiempoVuelo, distanciaMaxima, alturaMaxima, trayectoria)
    }

    companion object {
        private const val PASO_TIEMPO = 0.1
        private const val EPSILON = 1e-9
    }
}