package fisicas.motor

import config.ConfigSimulacion
arreglador-solid
import fisicas.gravedad.Tierra
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas unitarias especializadas en desviación lateral por viento y mecánica de rebotes de [Simulador].
 */
class SimuladorRebotesYVientoTest {

    private fun crearTierra() = Tierra()

    @Test
    fun testRequisitoC_Viento90EsteGeneraDesviacionEnCoordenadaZ() {
        val tierra = crearTierra()
        val simulador = Simulador(tierra)
        val conViento = simulador.calcularTrayectoria(ConfigSimulacion(25.0, 45.0, tierra, 1.0, 35.0, 90.0))
        val sinViento = simulador.calcularTrayectoria(ConfigSimulacion(25.0, 45.0, tierra, 1.0, 0.0, 0.0))
        assertTrue(sinViento.trayectoria.all { abs(it.z) < 1e-9 })
        assertEquals(0.0, sinViento.desviacionLateralMaxima, 1e-9)
        assertTrue(conViento.desviacionLateralMaxima > 0.5)
        assertTrue(conViento.trayectoria.any { abs(it.z) > 0.1 })
    }

    @Test
    fun testRequisitoD_RebotesReducenAlturaMaximaEnCadaPique() {
        val tierra = crearTierra()
        val simulador = Simulador(gravedadFoco = tierra, coeficienteRestitucion = 0.75)
        val resultado = simulador.calcularTrayectoria(ConfigSimulacion(20.0, 60.0, tierra, masaKg = 2.0))
        assertTrue(resultado.cantidadRebotes >= 2)
        val piques = resultado.alturasMaximasPorPique
        assertTrue(piques.size >= 3)
        for (i in 1 until piques.size) {
            assertTrue(piques[i] < piques[i - 1])

import fisicas.fuerzas.CalculadorArrastreAerodinamico
import fisicas.gravedad.Tierra
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt

/**
 * Pruebas unitarias para verificar el alcance negativo en X con parámetros extremos,
 * el ciclo continuo de rebotes bajo aceleración de viento ininterrumpida (ax),
 * y la exactitud matemática de [MetricasImpacto] al finalizar la simulación.
 */
class SimuladorRebotesYVientoTest {

    private val tierra = Tierra()
    private val delta = 1e-6

    @Test
    fun `viento fuerte del Norte con poca masa genera alcance y desplazamiento negativo en X`() {
        val calculador = CalculadorArrastreAerodinamico()
        val motor = MotorFisicaEuler2D(calculador)
        val simulador = Simulador(
            gravedadFoco = tierra,
            calculadorAerodinamico = calculador,
            motorFisica = motor
        )

        // Proyectil liviano (0.3 kg) enfrentando viento fuerte en contra (0.0° Norte, 90 km/h)
        val configExtremo = ConfigSimulacion(
            velocidadInicial = 15.0,
            angulo = 60.0,
            fuenteGravedad = tierra,
            masaKg = 0.3,
            velocidadVientoKmH = 90.0,
            direccionVientoGrados = 0.0
        )

        val resultado = simulador.calcularTrayectoria(configExtremo)

        assertTrue(resultado.tieneDesplazamientoNegativo())
        assertTrue(
            resultado.trayectoria.any { it.x < 0.0 },
            "La trayectoria debe contener coordenadas X negativas cuando el viento supera la inercia"
        )
        assertTrue(
            resultado.distanciaMaxima < 0.0,
            "El alcance final (distanciaMaxima) debe ser negativo (obtenido: ${resultado.distanciaMaxima})"
        )
        assertEquals(resultado.trayectoria.last().x, resultado.distanciaMaxima, delta)
        assertTrue(resultado.huboRebotes(), "El proyectil debe realizar rebotes hacia atrás antes de detenerse")
    }

    @Test
    fun `ciclo de rebotes continuo aplica aceleracion del viento ax ininterrumpidamente en cada pique`() {
        val simulador = Simulador(
            gravedadFoco = tierra,
            coeficienteRestitucion = 0.8
        )

        // Proyectil liviano con viento fuerte en contra que invierte su trayectoria y sigue retrocediendo en cada rebote
        val config = ConfigSimulacion(
            velocidadInicial = 12.0,
            angulo = 70.0,
            fuenteGravedad = tierra,
            masaKg = 0.25,
            velocidadVientoKmH = 85.0,
            direccionVientoGrados = 0.0
        )

        val resultado = simulador.calcularTrayectoria(config)

        // Identificamos los puntos donde acaba de ocurrir un rebote (y == 0.0 y vy > 0.0 tras t > 0)
        val puntosRebote = resultado.trayectoria.filter { it.tiempo > 0.0 && it.y == 0.0 && it.vy > 0.0 }
        assertTrue(puntosRebote.size >= 2, "Deben registrarse múltiples rebotes sucesivos")

        // En cada rebote sucesivo bajo viento en contra, la coordenada X debe desplazarse cada vez más hacia -X
        for (i in 1 until puntosRebote.size) {
            assertTrue(
                puntosRebote[i].x < puntosRebote[i - 1].x,
                "El rebote $i (x=${puntosRebote[i].x}) debe ocurrir más atrás en X que el rebote ${i - 1} (x=${puntosRebote[i - 1].x})"
            )
 main
        }
    }

    @Test
arreglador-solid
    fun testEfectoMasaEnDesviacionLateralPorViento() {
        val tierra = crearTierra()
        val simulador = Simulador(tierra)
        val resLiviano = simulador.calcularTrayectoria(ConfigSimulacion(25.0, 45.0, tierra, 0.2, 30.0, 90.0))
        val resPesado = simulador.calcularTrayectoria(ConfigSimulacion(25.0, 45.0, tierra, 5.0, 30.0, 90.0))
        assertTrue(resLiviano.desviacionLateralMaxima > resPesado.desviacionLateralMaxima)
    }

    @Test
    fun testRestitucionCeroNoRealizaRebotes() {
        val tierra = crearTierra()
        val simuladorSinRebote = Simulador(gravedadFoco = tierra, coeficienteRestitucion = 0.0)
        val resultado = simuladorSinRebote.calcularTrayectoria(ConfigSimulacion(20.0, 45.0, tierra))
        assertEquals(0, resultado.cantidadRebotes)
        assertEquals(1, resultado.alturasMaximasPorPique.size)
    fun `exactitud matematica de MetricasImpacto al finalizar simulacion sin rebotes`() {
        val simuladorSinRebote = Simulador(
            gravedadFoco = tierra,
            coeficienteRestitucion = 0.0
        )

        val config = ConfigSimulacion(
            velocidadInicial = 25.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 2.0,
            velocidadVientoKmH = 20.0,
            direccionVientoGrados = 0.0
        )

        val resultado = simuladorSinRebote.calcularTrayectoria(config)
        val metricas = resultado.metricasImpacto

        val rapidezEsperada = sqrt(metricas.vx * metricas.vx + metricas.vy * metricas.vy)
        val anguloEsperadoGrados = Math.toDegrees(atan(abs(metricas.vy) / metricas.vx))

        assertTrue(metricas.velocidadEscalarFinal > 0.0, "La velocidad escalar de impacto debe ser positiva")
        assertEquals(rapidezEsperada, metricas.velocidadEscalarFinal, delta)
        assertEquals(anguloEsperadoGrados, metricas.anguloLlegada, delta)
main
    }
}
