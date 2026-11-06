package fisicas.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.math.sqrt

class WindConfigTest {

    private val delta = 1e-6

    @Test
    fun testVientoRumboNorteCeroGrados() {
        val viento = WindConfig(magnitud = 10.0, anguloGrados = 0.0)
        val vec = viento.aVector()
        assertEquals(0.0, vec.x, delta)
        assertEquals(10.0, vec.y, delta)
    }

    @Test
    fun testVientoRumboEsteNoventaGrados() {
        val viento = WindConfig(magnitud = 15.0, anguloGrados = 90.0)
        val vec = viento.aVector()
        assertEquals(15.0, vec.x, delta)
        assertEquals(0.0, vec.y, delta)
    }

    @Test
    fun testVientoRumboSurCientoOchentaGrados() {
        val viento = WindConfig(magnitud = 20.0, anguloGrados = 180.0)
        val vec = viento.aVector()
        assertEquals(0.0, vec.x, delta)
        assertEquals(-20.0, vec.y, delta)
    }

    @Test
    fun testVientoRumboOesteDoscientosSetentaGrados() {
        val viento = WindConfig(magnitud = 25.0, anguloGrados = 270.0)
        val vec = viento.aVector()
        assertEquals(-25.0, vec.x, delta)
        assertEquals(0.0, vec.y, delta)
    }

    @Test
    fun testVientoRumboTrescientosSesentaGrados() {
        val viento = WindConfig(magnitud = 12.0, anguloGrados = 360.0)
        val vec = viento.aVector()
        assertEquals(0.0, vec.x, delta)
        assertEquals(12.0, vec.y, delta)
    }

    @Test
    fun testVientoCuarentaYCincoGradosSimetrico() {
        val viento = WindConfig(magnitud = 10.0, anguloGrados = 45.0)
        val vec = viento.aVector()
        val esperado = 10.0 * (sqrt(2.0) / 2.0)
        assertEquals(esperado, vec.x, delta)
        assertEquals(esperado, vec.y, delta)
    }

    @Test
    fun testValidacionesParametrosInvalidos() {
        assertThrows<IllegalArgumentException> { WindConfig(magnitud = -5.0 + delta, anguloGrados = 90.0) }
        assertThrows<IllegalArgumentException> { WindConfig(magnitud = 10.0, anguloGrados = -1.0) }
        assertThrows<IllegalArgumentException> { WindConfig(magnitud = 10.0, anguloGrados = 360.1) }
    }
}
