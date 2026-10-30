package fisicas.motor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt

/**
 * Pruebas unitarias aisladas para verificar la exactitud matemática de [MetricasImpacto].
 */
class MetricasImpactoTest {

    private val delta = 1e-6

    @Test
    fun `calcula con exactitud matematica la velocidad escalar final y el angulo de llegada`() {
        // Triángulo 3-4-5: vx = 3.0, vy = -4.0 -> v_final = sqrt(9 + 16) = 5.0
        val metricas = MetricasImpacto(vx = 3.0, vy = -4.0)

        val velocidadEsperada = sqrt(3.0 * 3.0 + (-4.0) * (-4.0))
        val anguloRadEsperado = atan(abs(-4.0) / 3.0)
        val anguloGradosEsperado = Math.toDegrees(anguloRadEsperado)

        assertEquals(5.0, metricas.velocidadEscalarFinal, delta)
        assertEquals(velocidadEsperada, metricas.calcularVelocidadEscalar(), delta)
        assertEquals(anguloRadEsperado, metricas.anguloLlegadaRadianes, delta)
        assertEquals(anguloGradosEsperado, metricas.anguloLlegadaGrados, delta)
        assertEquals(anguloGradosEsperado, metricas.anguloLlegada, delta)
        assertFalse(metricas.tieneRetrocesoHorizontal())
    }

    @Test
    fun `componentes iguales en magnitud producen angulo de llegada de 45 grados`() {
        val metricas = MetricasImpacto(vx = 12.5, vy = -12.5)

        assertEquals(sqrt(2.0) * 12.5, metricas.velocidadEscalarFinal, delta)
        assertEquals(PI / 4.0, metricas.anguloLlegadaRadianes, delta)
        assertEquals(45.0, metricas.anguloLlegada, delta)
    }

    @Test
    fun `detecta retroceso horizontal cuando vx es negativo y construye desde PuntoTrayectoria`() {
        val puntoFinal = PuntoTrayectoria(tiempo = 2.0, x = -5.0, y = 0.0, vx = -8.0, vy = -6.0)
        val metricas = MetricasImpacto.desdePunto(puntoFinal)

        assertEquals(10.0, metricas.velocidadEscalarFinal, delta)
        assertEquals(atan(6.0 / -8.0), metricas.anguloLlegadaRadianes, delta)
        assertTrue(metricas.tieneRetrocesoHorizontal())
    }

    @Test
    fun `impacto nulo en el origen retorna velocidad y angulo cero sin NaN`() {
        val nulo = MetricasImpacto.IMPACTO_NULO

        assertEquals(0.0, nulo.velocidadEscalarFinal, delta)
        assertEquals(0.0, nulo.anguloLlegadaRadianes, delta)
        assertEquals(0.0, nulo.anguloLlegada, delta)
    }
}
