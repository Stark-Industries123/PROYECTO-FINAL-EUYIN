package fisicas.motor

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas unitarias de integración entre [CalculadorTrayectoria], [Simulador] y [ConfigSimulacion].
 */
class CalculadorTrayectoriaTest {

    @Test
    fun testCalculoTrayectoriaTierraConAbstracciones() {
        val gravedad: FuenteGravedad = Tierra()
        val calculador: CalculadorTrayectoria = Simulador(gravedad)
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 45.0, fuenteGravedad = gravedad)
        val resultado = calculador.calcularTrayectoria(config)
        assertTrue(resultado.tiempoVuelo > 0.0)
        assertTrue(resultado.distanciaMaxima > 0.0)
        assertTrue(resultado.alturaMaxima > 0.0)
        val primerPunto = resultado.trayectoria.first()
        assertEquals(0.0, primerPunto.tiempo, 0.001)
        assertEquals(0.0, primerPunto.x, 0.001)
        assertEquals(0.0, primerPunto.y, 0.001)
    }

    @Test
    fun testCalculoTrayectoriaAnguloCeroConAbstracciones() {
        val gravedad: FuenteGravedad = Tierra()
        val calculador: CalculadorTrayectoria = Simulador(gravedad)
        val config = ConfigSimulacion(velocidadInicial = 15.0, angulo = 0.0, fuenteGravedad = gravedad)
        val resultado = calculador.calcularTrayectoria(config)
        assertEquals(0.0, resultado.tiempoVuelo, 0.001)
        assertEquals(0.0, resultado.distanciaMaxima, 0.001)
        assertEquals(0.0, resultado.alturaMaxima, 0.001)
        assertTrue(resultado.trayectoria.isNotEmpty())
    }

    @Test
    fun testCalculoTrayectoriaConConfiguracionCompleta() {
        val gravedad: FuenteGravedad = Tierra()
        val calculador: CalculadorTrayectoria = Simulador(gravedad)
        val config = ConfigSimulacion(25.0, 45.0, gravedad, 2.5, 30.0, 90.0)
        val resultado = calculador.calcularTrayectoria(config)
        assertTrue(resultado.tiempoVuelo > 0.0)
        assertTrue(resultado.distanciaMaxima > 0.0)
        assertTrue(resultado.alturaMaxima > 0.0)
        assertTrue(resultado.desviacionLateralMaxima > 0.0)
    }
}
