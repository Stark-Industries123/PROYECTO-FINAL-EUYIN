package fisicas.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.math.sqrt

class WindConfigTest {

    private val delta = 1e-6

    @Test
    fun `viento con rumbo Norte 0 grados apunta hacia arriba (+Y)`() {
        val viento = WindConfig(magnitud = 10.0, anguloGrados = 0.0)
        val vec = viento.aVector()

        assertEquals(0.0, vec.x, delta)
        assertEquals(10.0, vec.y, delta)
    }

    @Test
    fun `viento con rumbo Este 90 grados apunta hacia la derecha (+X)`() {
        val viento = WindConfig(magnitud = 15.0, anguloGrados = 90.0)
        val vec = viento.aVector()

        assertEquals(15.0, vec.x, delta)
        assertEquals(0.0, vec.y, delta)
    }

    @Test
    fun `viento con rumbo Sur 180 grados apunta hacia abajo (-Y)`() {
        val viento = WindConfig(magnitud = 20.0, anguloGrados = 180.0)
        val vec = viento.aVector()

        assertEquals(0.0, vec.x, delta)
        assertEquals(-20.0, vec.y, delta)
    }

    @Test
    fun `viento con rumbo Oeste 270 grados apunta hacia la izquierda (-X)`() {
        val viento = WindConfig(magnitud = 25.0, anguloGrados = 270.0)
        val vec = viento.aVector()

        assertEquals(-25.0, vec.x, delta)
        assertEquals(0.0, vec.y, delta)
    }

    @Test
    fun `viento con rumbo 360 grados coincide con Norte 0 grados`() {
        val viento = WindConfig(magnitud = 12.0, anguloGrados = 360.0)
        val vec = viento.aVector()

        assertEquals(0.0, vec.x, delta)
        assertEquals(12.0, vec.y, delta)
    }

    @Test
    fun `viento a 45 grados Noreste divide componentes simetricamente`() {
        val viento = WindConfig(magnitud = 10.0, anguloGrados = 45.0)
        val vec = viento.aVector()

        val esperado = 10.0 * (sqrt(2.0) / 2.0)
        assertEquals(esperado, vec.x, delta)
        assertEquals(esperado, vec.y, delta)
    }

    @Test
    fun `magnitud negativa lanza IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            WindConfig(magnitud = -5.0, anguloGrados = 90.0)
        }
    }

    @Test
    fun `angulo menor a 0 o mayor a 360 lanza IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            WindConfig(magnitud = 10.0, anguloGrados = -1.0)
        }
        assertThrows<IllegalArgumentException> {
            WindConfig(magnitud = 10.0, anguloGrados = 360.1)
        }
    }
}
