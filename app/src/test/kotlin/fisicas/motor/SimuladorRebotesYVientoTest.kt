package fisicas.motor

import config.ConfigSimulacion
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
        }
    }

    @Test
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
    }
}
