package fisicas.motor

import fisicas.gravedad.FuenteGravedad
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

// Data class para empaquetar los resultados limpios
data class ResultadoSimulacion(
    val tiempoVuelo: Double,
    val distanciaMaxima: Double,
    val alturaMaxima: Double
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

        return ResultadoSimulacion(tiempoVuelo, distanciaMaxima, alturaMaxima)
    }
}