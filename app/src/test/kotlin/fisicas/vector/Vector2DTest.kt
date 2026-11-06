package fisicas.vector

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Vector2DTest {

    private val delta = 1e-6

    @Test
    fun testSumaYRestaDeVectores() {
        val v1 = Vector2D(3.0, 4.0)
        val v2 = Vector2D(1.5, -2.0)
        val suma = v1 + v2
        val resta = v1 - v2
        assertEquals(4.5, suma.x, delta)
        assertEquals(2.0, suma.y, delta)
        assertEquals(1.5, resta.x, delta)
        assertEquals(6.0, resta.y, delta)
    }

    @Test
    fun testMultiplicacionYDivisionPorEscalar() {
        val v = Vector2D(10.0, -20.0)
        val prod = v * 0.5
        val div = v / 2.0
        assertEquals(5.0, prod.x, delta)
        assertEquals(-10.0, prod.y, delta)
        assertEquals(5.0, div.x, delta)
        assertEquals(-10.0, div.y, delta)
        assertThrows<IllegalArgumentException> { v / 0.0 }
    }

    @Test
    fun testOpuestoYMagnitudEuclidea() {
        val v = Vector2D(3.0, -4.0)
        val opuesto = -v
        assertEquals(-3.0, opuesto.x, delta)
        assertEquals(4.0, opuesto.y, delta)
        assertEquals(5.0, v.magnitud(), delta)
        assertEquals(25.0, v.magnitudCuadrada(), delta)
    }

    @Test
    fun testNormalizarVectorYVectorCero() {
        val unitario = Vector2D(0.0, 10.0).normalizar()
        assertEquals(0.0, unitario.x, delta)
        assertEquals(1.0, unitario.y, delta)
        val cero = Vector2D(0.0, 0.0).normalizar()
        assertEquals(0.0, cero.x, delta)
        assertFalse(cero.x.isNaN())
    }

    @Test
    fun testProductoPuntoYDistancia() {
        val v1 = Vector2D(1.0, 2.0)
        val v2 = Vector2D(3.0, 4.0)
        assertEquals(11.0, v1.productoPunto(v2), delta)
        assertEquals(5.0, Vector2D(1.0, 1.0).distanciaA(Vector2D(4.0, 5.0)), delta)
    }
}
